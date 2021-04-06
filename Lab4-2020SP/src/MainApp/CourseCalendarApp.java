package MainApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import API.*;
import Board.CourseBoard;
import Exceptions.LocationConflictException;
import Exceptions.LocationNotFoundException;
import Exceptions.PlanEntryNotCreateException;
import Exceptions.PlanEntryOccupyLocationException;
import Exceptions.PlanEntryOccupyResourceException;
import Exceptions.PlanEntryStateNotMatchException;
import Exceptions.ResourceConflictException;
import Exceptions.ResourceNotFoundException;
import Exceptions.SameLabelException;
import Location.Location;
import LogFile.MyFormatter;
import Resources.Teacher;
import Schedule.CourseSchedule;
import Timeslot.Timeslot;
import common.PlanningEntry;
import compositeinterface.CourseEntry;

public class CourseCalendarApp {
	private static Logger myLogger=Logger.getLogger("CourseCalendarAppLog");
	private final static int timebound=1;//��������ʶ��ʱ��鿴��־ʱ����ѯ��ʱ�䷶Χ
	
	public void menu() {//����Ŀ¼
		System.out.println("-------�γ̹���ϵͳ-------");
		System.out.println("**** 1.���ӹ���Ľ�ʦ  ****");
		System.out.println("**** 2.ɾ������Ľ�ʦ  ****");
		System.out.println("**** 3.���ӹ���Ľ���  ****");
		System.out.println("**** 4.ɾ������Ľ���  ****");
		System.out.println("**** 5.����һ���¿γ�  ****");
		System.out.println("**** 6.Ϊ�γ̷����ʦ  ****");
		System.out.println("**** 7.���Ŀγ̽���      ****");
		System.out.println("**** 8.��ʦָ���γ��Ͽ�****");
		System.out.println("**** 9.��ʦָ���γ��¿�****");
		System.out.println("**** 10.�鿴�γ�״̬    ****");
		System.out.println("**** 11.��ǰȡ���γ�    ****");
		System.out.println("**** 12.չʾ���ҿγ̱� ****");
		System.out.println("**** 13.������ռ�ó�ͻ***");
		System.out.println("**** 14.����ʦ�����ͻ***");
		System.out.println("**** 15.չʾ��ʦ�γ̰���***");
		System.out.println("**** 16.��ѯ��־           ****");
		System.out.println("**** 17.�˳�ϵͳ           ****");
	}
	
	public static void main(String[] args) throws IOException {
		//��־��¼
		myLogger.setLevel(Level.INFO);
		//д���ļ�
		FileHandler handler=new FileHandler("src/LogFile/CourseCalendarAppLog.log");
		handler.setFormatter(new MyFormatter());//���ù̶���ʽ
		handler.setLevel(Level.INFO);
		myLogger.addHandler(handler);
		myLogger.setUseParentHandlers(false);
		
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		CourseCalendarApp ccApp=new CourseCalendarApp();
		ccApp.menu();
		String input=bf.readLine();
		CourseSchedule cs=new CourseSchedule();
		myLogger.info("����CourseCalendarApp");
		if(input==null) {
			myLogger.severe("�������쳣");
			throw new IOException();	
		}
		while(!input.equals("17")) {
			switch (input) {
			case "1":{//���ӽ�ʦ
				System.out.println("�����������ʦ�����֤�š��������Ա�(M/F)��ְ��");
				input=bf.readLine();
				myLogger.info("��ӽ�ʦ");
				if(input==null) {
					myLogger.severe("�������쳣");
					throw new IOException();	
				}
				String[] information=input.split(" ");
				while(information.length!=4||(!information[2].equals("F")&&!information[2].equals("M"))) {
					if(information.length!=4)
						System.out.println("���벻����!����������!");//�쳣����
					else
						System.out.println("�Ա���������!����������!");//�쳣����
					input=bf.readLine();
					if(input==null) {
						myLogger.severe("�������쳣");
						throw new IOException();	
					}
					information=input.split(" ");
				}
				if(information[2].equals("M")) {//������ʦ
					Teacher teacher=new Teacher(information[0],information[1] , true, information[3]);
					cs.addTeacher(teacher);
				}
				else {
					Teacher teacher=new Teacher(information[0],information[1] , false, information[3]);
					cs.addTeacher(teacher);
				}	
				break;
			}
			case "2":{//ɾ����ʦ
				System.out.println("�������ɾ����ʦ�����֤��:");
				input=bf.readLine();
				myLogger.info("ɾ����ʦ");
				try {
					cs.deleteTeacher(input);
				} catch (ResourceNotFoundException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"ɾ��ʧ��!");
				} catch (PlanEntryOccupyResourceException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"ɾ��ʧ��!");
				}
				break;
			}
			case "3":{//���ӽ���
				System.out.println("������������ҵľ��ȡ�γ�ȡ�����");
				input=bf.readLine();
				myLogger.info("���ӽ���");
				if(input==null) {
					myLogger.severe("�������쳣");
					throw new IOException();	
				}
				String[] information=input.split(" ");
				while(information.length!=3) {
					System.out.println("���벻����!����������!");//�쳣����
					input=bf.readLine();
					if(input==null) {
						myLogger.severe("�������쳣");
						throw new IOException();	
					}
					information=input.split(" ");			
				}
				Location location=new Location(information[0], information[1], information[2], false);
				cs.addLocation(location);//���λ��
				break;
			}
			case "4":{//ɾ������
				System.out.println("�������ɾ�����ҵ�����:");
				input=bf.readLine();
				myLogger.info("ɾ������");
				try {
					cs.deleteLocation(input);
				} catch (LocationNotFoundException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"ɾ��ʧ��!");
				} catch (PlanEntryOccupyLocationException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"ɾ��ʧ��!");
				}
				break;
			}
			case "5":{//�����γ�
				System.out.println("������Ҫ�����Ŀγ�����:");
				String coursename=bf.readLine();
				System.out.println("������γ�Ҫռ�õĽ�������:");
				String location=bf.readLine();
				System.out.println("������γ�Ԥ���Ͽ�ʱ��(yyyy-MM-dd HH:mm):");
				String start=bf.readLine();
				System.out.println("������γ�Ԥ���¿�ʱ��(yyyy-MM-dd HH:mm):");
				String end=bf.readLine();
				Timeslot timeslot=new Timeslot(start, end);
				myLogger.info("�����γ�");
				try {
					cs.createCourse(coursename, location, timeslot);
				} catch (LocationNotFoundException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"�޷�����!");
				} catch (SameLabelException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"���ܴ��������γ�!");
				} catch (LocationConflictException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"������ѡ���ص�!");
				}
				break;
			}
			case "6":{//Ϊ�γ̷����ʦ
				System.out.println("������Ҫ����γ�����:");
				String coursename=bf.readLine();
				System.out.println("������Ϊ�γ�Ҫ�����ʦ���֤��:");
				String teacher=bf.readLine();
				myLogger.info("Ϊ�γ̷����ʦ");
				try {
					cs.allocateTeacher(coursename, teacher);
				} catch (ResourceNotFoundException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"������ѡ���ʦ���з���!");
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
				} catch (ResourceConflictException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"������ѡ���ʦ���з���!");
				}
				break;
			}
			case "7":{//�γ̸���λ��
				System.out.println("������Ҫ���Ľ��ҵĿγ�����:");
				String coursename=bf.readLine();
				System.out.println("������Ҫ���ĵ���λ��:");
				String location=bf.readLine();
				myLogger.info("Ϊ�γ̸��Ľ���");
				try {
					cs.changeLocation(coursename, location);
				} catch (LocationNotFoundException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"������ѡ�����!");
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"������ѡ��γ�!");
				} catch (PlanEntryStateNotMatchException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"�ÿγ����������λ��!");
				}
				break;
			}
			case "8":{//�Ͽ�
				System.out.println("������Ҫ�ϿεĿγ�����:");
				String coursename=bf.readLine();
				myLogger.info("�Ͽ�");
				try {
					cs.startCourse(coursename);
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
				}
				break;
			}
			case "9":{//�¿�
				System.out.println("������Ҫ�¿εĿγ�����:");
				String coursename=bf.readLine();
				myLogger.info("�¿�");
				try {
					cs.endCourse(coursename);
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
				}
				break;
			}
			case "10":{//�鿴�γ�״̬
				System.out.println("������Ҫ�鿴״̬�Ŀγ�����:");
				String coursename=bf.readLine();
				myLogger.info("�鿴�γ�״̬");
				try {
					String state=cs.getCourseState(coursename);
					System.out.println("Ŀǰ�γ�״̬Ϊ:"+state);
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
				}
				break;
			}
			case "11":{//ȡ���γ�
				System.out.println("��������ǰȡ���Ŀγ�����:");
				String coursename=bf.readLine();
				myLogger.info("ȡ���γ�");
				try {
					cs.cancelCourse(coursename);
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
				}
				break;
			}
			case "12":{//Board���ӻ�
				System.out.println("������Ҫչʾ�γ̵Ľ�������:");
				String locationname=bf.readLine();
				Location location=cs.getLocationbyName(locationname);
				Calendar calendar=Calendar.getInstance();
				CourseBoard cb=new CourseBoard(location, cs.getCourses(),calendar);
				myLogger.info("���ӻ�չʾ��Ϣ��");
				try {
					cb.visualize();
				} catch (ParseException e) {
					myLogger.severe(e.getMessage());
					e.printStackTrace();
				}
				break;
			}
			case "13":{//���λ�ó�ͻ
				PlanningEntryAPIs peAPI=new PlanningEntryAPIsFirstImpl();
				//PlanningEntryAPIs peAPI=new PlanningEntryAPIsSecondImpl();//����ģʽ��client������ѡ��һ��ʵ�ַ�ʽ
				boolean flag=peAPI.checkLocationConflict(cs.getCourses());
				myLogger.info("���λ�ó�ͻ");
				if(flag) {
					System.out.println("��ǰ�ƻ������λ�ó�ͻ!");
				}
				else {
					System.out.println("��ǰ�ƻ������λ�ó�ͻ!");
				}
				break;
			}
			case "14":{//����ʦ�����ͻ
				PlanningEntryAPIs peAPI=new PlanningEntryAPIsFirstImpl();
				//PlanningEntryAPIs peAPI=new PlanningEntryAPIsSecondImpl();//����ģʽ��client������ѡ��һ��ʵ�ַ�ʽ
				boolean flag=peAPI.checkResourceExclusiveConflict(cs.getCourses());
				myLogger.info("����ʦ�����ͻ");
				if(flag) {
					System.out.println("��ǰ�γ̼����д��ڽ�ʦ�����ͻ!");
				}
				else {
					System.out.println("��ǰ�γ̼����в����ڽ�ʦ�����ͻ!");
				}
				break;
			}
			case "15":{//�鿴��ʦ�γ̰���
				System.out.println("������Ҫ�鿴�γ̰��ŵĽ�ʦ���֤��:");
				String teacherId=bf.readLine();
				PlanningEntryAPIs peAPI=new PlanningEntryAPIsFirstImpl();
				List<CourseEntry<Teacher>> ces=cs.getCoursesofassignTeacher(teacherId);
				myLogger.info("�鿴��ʦ�γ̰���");
				if(ces==null) {
					System.out.println("�ý�ʦδ����γ�!");
					break;
				}
				else {
					System.out.println("�ý�ʦ����Ŀγ���:");
					for(CourseEntry<Teacher> ce:ces) {
						System.out.println(ce.getName()+" Ŀǰ�γ�״̬Ϊ:"+ce.getStateName());
					}
					System.out.println("�Ƿ���Ҫ�鿴ָ���γ̵�ǰ��γ�?(Y/N)");
					input=bf.readLine();
					myLogger.info("�鿴ָ���γ̵�ǰ��γ�");
					if(input==null) {
						myLogger.severe("�������쳣");
						throw new IOException();	
					}
					if(input.equals("Y")) {
						System.out.println("������Ҫ�鿴��ָ���γ�:");
						input=bf.readLine();
						CourseEntry<Teacher> courseEntry=cs.getCoursebyName(input);
						while(courseEntry==null) {
							System.out.println("��ѯʧ��!�������������ѡȡ!");
							input=bf.readLine();
							courseEntry=cs.getCoursebyName(input);
						}
						Teacher teacher=cs.getTeacherbyID(teacherId);
						try {
							PlanningEntry<Teacher> cEntry=peAPI.findPreEntryPerResource(teacher, courseEntry, cs.getCourses());
							if(cEntry==null)
								System.out.println("�ÿγ���ǰ��γ�!");
							else
								System.out.println("��ǰ��γ�Ϊ:"+cEntry.getName());
						} catch (ParseException e) {
							myLogger.severe(e.getMessage());
							e.printStackTrace();
						}
					}
				}
				break;
			}
			case "16":{//��ѯ��־
				System.out.println("�������ѯ��ʽ(time/action/exception):");
				System.out.printf("time:����%dСʱ�ڵ�������־��¼\n", timebound);
				System.out.println("action:�����Ͳ������з��ϵ���־��¼");
				System.out.println("exception:�鿴�����쳣����־��¼");
				input=bf.readLine();
				myLogger.info("�鿴��־");
				if(input==null) {
					myLogger.severe("�������쳣");
					throw new IOException();	
				}
				try {
					queryLog(input);
				}  catch (ParseException e) {
					myLogger.severe("������־��ʽ�쳣");
					e.printStackTrace();
				}
				break;
			}
			default:{
				System.out.println("�������,������ѡ����!");
				break;
			}
			
			}
			if(input==null) {
				myLogger.severe("�������쳣");
				throw new IOException();	
			}
			if(!input.equals("17")) {//ÿ���һ�ι���,���´�ӡ�˵����û�ѡȡ
				ccApp.menu();
				input=bf.readLine();
			}
			if(input==null) {
				myLogger.severe("�������쳣");
				throw new IOException();	
			}
		}
		myLogger.info("�˳�CourseCalendarApp");
		handler.close();
	}
	
	private static void queryLog(String condition) throws IOException, ParseException {
		String time,classname,functionname,message,level;
		boolean timeflag=condition.equals("time");
		boolean actionflag=condition.equals("action");
		boolean exceptionflag=condition.equals("exception");
		if(!timeflag&&!actionflag&&!exceptionflag)//�Ƿ���ѯ��ʽ
			return;
		File file=new File("src/LogFile/CourseCalendarAppLog.log");
		if(!file.exists()||!file.isFile()) {
			throw new FileNotFoundException();
		}
		InputStreamReader read = new InputStreamReader(new FileInputStream(file));
		BufferedReader bufferedReader = new BufferedReader(read);
		
		String inputString=null;
		if(actionflag) {
			System.out.println("����������Ҫ�鿴�Ĳ�������(�ù��ܲ˵���ÿ������ǰ���������)");
			BufferedReader bf2 = new BufferedReader(new InputStreamReader(System.in));
			inputString=bf2.readLine();
		}
		
		try {
			String line=null;
			SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",Locale.UK);
			Pattern pattern=Pattern.compile("<(.*?)> <(.*?)> <(.*?)> <(.*?)>: <(.*?)>");
			while((line=bufferedReader.readLine())!=null) {
				Matcher matcher=pattern.matcher(line);
				if(!matcher.find()) {//��־����
					throw new IOException();
				}
				time=matcher.group(1);//������ʽ��ȡ�岿��
				classname=matcher.group(2);
				functionname=matcher.group(3);
				level=matcher.group(4);
				message=matcher.group(5);
				Date date2=dateFormat.parse(time);
				if(timeflag) {//��ʱ�����
					Date date=new Date();
					long between = (date2.getTime()-date.getTime())/1000;//���㵱ǰʱ������¼���Ӧ��ʱ��Ĳ�
					between=between/(60*60*1000);//ת����Сʱ
					if(between>timebound)//ֻ��ʾtimebound���ڵ��¼�
						continue;
					System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
				}
				else if(actionflag) {
					if(inputString==null) {
						myLogger.severe("�������쳣");
						throw new IOException();	
					}
					switch (inputString) {//���ݲ������Ͷ�Ӧ��ѯ
					case "1":{
						if(level.equals("INFO")&&message.equals("��ӽ�ʦ"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "2":{
						if(level.equals("INFO")&&message.equals("ɾ����ʦ"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "3":{
						if(level.equals("INFO")&&message.equals("���ӽ���"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "4":{
						if(level.equals("INFO")&&message.equals("ɾ������"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}	
					case "5":{
						if(level.equals("INFO")&&message.equals("�����γ�"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "6":{
						if(level.equals("INFO")&&message.equals("Ϊ�γ̷����ʦ"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "7":{
						if(level.equals("INFO")&&message.equals("Ϊ�γ̸��Ľ���"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "8":{
						if(level.equals("INFO")&&message.equals("�Ͽ�"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "9":{
						if(level.equals("INFO")&&message.equals("�¿�"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "10":{
						if(level.equals("INFO")&&message.equals("�鿴�γ�״̬"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "11":{
						if(level.equals("INFO")&&message.equals("ȡ���γ�"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "12":{
						if(level.equals("INFO")&&message.equals("���ӻ�չʾ��Ϣ��"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "13":{
						if(level.equals("INFO")&&message.equals("���λ�ó�ͻ"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "14":{
						if(level.equals("INFO")&&message.equals("����ʦ�����ͻ"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "15":{
						if(level.equals("INFO")&&(message.equals("�鿴��ʦ�γ̰���")||(message.equals("�鿴ָ���γ̵�ǰ��γ�"))))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "16":{
						if(level.equals("INFO")&&message.equals("�鿴��־"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "17":{
						if(level.equals("INFO")&&message.equals("�˳�CourseCalendarApp"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					default:
						break;
					}
				}
				else {
					if(level.equals("WARNING")||level.equals("SEVERE"))//��ѯ�����쳣
						System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
				}
			}
		}finally {//������Դ
			bufferedReader.close();
		}
			
	}
}
	
