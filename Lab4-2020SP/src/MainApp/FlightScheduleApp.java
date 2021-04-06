package MainApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import Board.FlightBoard;
import Exceptions.IllegalPlaneContentException;
import Exceptions.InconsistentStartOrEndException;
import Exceptions.IncorrectElementDependencyException;
import Exceptions.LocationNotFoundException;
import Exceptions.PlanEntryNotCreateException;
import Exceptions.PlanEntryOccupyLocationException;
import Exceptions.PlanEntryOccupyResourceException;
import Exceptions.ResourceConflictException;
import Exceptions.ResourceNotFoundException;
import Exceptions.SameLabelException;
import Exceptions.UnGrammaticalWordException;
import Location.Location;
import LogFile.MyFormatter;
import Resources.Plane;
import Schedule.FlightSchedule;
import Timeslot.Timeslot;
import common.PlanningEntry;
import compositeinterface.FlightEntry;

public class FlightScheduleApp {
	private static Logger myLogger=Logger.getLogger("FlightScheduleAppLog");
	private final static int timebound=1;//��������ʶ��ʱ��鿴��־ʱ����ѯ��ʱ�䷶Χ
	
	public void menu() {
		System.out.println("-------�������ϵͳ-------");
		System.out.println("**** 1.���ӹ���ķɻ�  ****");
		System.out.println("**** 2.ɾ������ķɻ�  ****");
		System.out.println("**** 3.���ӹ���Ļ���  ****");
		System.out.println("**** 4.ɾ������Ļ���  ****");
		System.out.println("**** 5.����һ���º���  ****");
		System.out.println("**** 6.Ϊ�������ɻ�  ****");
		System.out.println("**** 7.ָ������������****");
		System.out.println("**** 8.ָ��������н���****");
		System.out.println("**** 9.չʾ���������   ****");
		System.out.println("**** 10.�鿴����״̬     ****");
		System.out.println("**** 11.��ǰȡ������     ****");	
		System.out.println("**** 12.���ɻ������ͻ***");
		System.out.println("**** 13.չʾ�ɻ����ల��***");
		System.out.println("**** 14.ͨ���ļ���������***");
		System.out.println("**** 15.��ѯ��־            ****");
		System.out.println("**** 16.�˳�ϵͳ	      ****");
	}
	
	public static void main(String[] args) throws IOException {
		//��־��¼
		myLogger.setLevel(Level.INFO);
		//д���ļ�
		FileHandler handler=new FileHandler("src/LogFile/FlightScheduleAppLog.log");
		handler.setFormatter(new MyFormatter());//���ù̶���ʽ
		handler.setLevel(Level.INFO);
		myLogger.addHandler(handler);
		myLogger.setUseParentHandlers(false);
		
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		FlightScheduleApp fsApp=new FlightScheduleApp();
		fsApp.menu();
		String input=bf.readLine();
		FlightSchedule fs=new FlightSchedule();
		myLogger.info("����FlightScheduleApp");
		if(input==null) {
			myLogger.severe("�������쳣");
			throw new IOException();	
		}
		while(!input.equals("16")) {
			switch (input) {
			case "1":{//���ӷɻ�
				System.out.println("����������ɻ��ı�š����ͺš���λ��������");
				input=bf.readLine();
				myLogger.info("��ӷɻ�");
				if(input==null) {
					myLogger.severe("�������쳣");
					throw new IOException();	
				}
				String[] information=input.split(" ");
				while(information.length!=4) {
					System.out.println("���벻����!����������!");//�쳣����
					input=bf.readLine();
					if(input==null) {
						myLogger.severe("�������쳣");
						throw new IOException();	
					}
					information=input.split(" ");
				}
				int seats=Integer.parseInt(information[2]);
				double age=Double.parseDouble(information[3]);
				Plane plane=new Plane(information[0], information[1], seats, age);
				try {
					fs.addPlane(plane);
				} catch (IllegalPlaneContentException e) {
					myLogger.severe(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
				}
				break;
			}
			case "2":{//ɾ���ɻ�
				System.out.println("�������ɾ���ɻ��ı��:");
				input=bf.readLine();
				myLogger.info("ɾ���ɻ�");
				try {
					fs.deletePlane(input);
				} catch (ResourceNotFoundException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"ɾ��ʧ��!");
				} catch (PlanEntryOccupyResourceException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"ɾ��ʧ��!");
				}
				break;
			}
			case "3":{//���ӻ���
				System.out.println("��������������ľ��ȡ�γ�ȡ�����");
				input=bf.readLine();
				myLogger.info("���ӻ���");
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
				Location location=new Location(information[0], information[1], information[2], true);
				fs.addLocation(location);//���λ��
				break;
			}
			case "4":{//ɾ������
				System.out.println("�������ɾ������������:");
				input=bf.readLine();
				myLogger.info("ɾ������");
				try {
					fs.deleteLocation(input);
				} catch (LocationNotFoundException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"ɾ��ʧ��!");
				} catch (PlanEntryOccupyLocationException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"ɾ��ʧ��!");
				}//��������ɾ��
				break;
			}
			case "5":{//��������
				System.out.println("������Ҫ�½��ĺ�������:");
				String flightname=bf.readLine();
				System.out.println("�����뺽����ɵĻ�������:");
				String startlocation=bf.readLine();
				System.out.println("�����뺽�ཱུ��Ļ�������:");
				String endlocation=bf.readLine();
				System.out.println("�����뺽��Ԥ�����ʱ��(yyyy-MM-dd HH:mm):");
				String start=bf.readLine();
				System.out.println("�����뺽��Ԥ�ƽ���ʱ��(yyyy-MM-dd HH:mm):");
				String end=bf.readLine();
				Timeslot timeslot=new Timeslot(start, end);
				myLogger.info("��������");
				try {
					fs.createFlight(flightname, startlocation, endlocation, timeslot);
				} catch (InconsistentStartOrEndException e) {
					myLogger.severe(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"�޷�����!");
				} catch (LocationNotFoundException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"�޷�����!");
				} catch (SameLabelException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
				}
				break;
			}
			case "6":{//����ɻ�
				System.out.println("������Ҫ����ĺ�������:");
				String flightname=bf.readLine();
				System.out.println("������Ϊ����Ҫ����ķɻ����:");
				String planeID=bf.readLine();
				System.out.println("������Ҫ����ɻ��ĺ����Ԥ�����ʱ��:");
				String departuretime=bf.readLine();
				System.out.println("������Ҫ����ɻ��ĺ����Ԥ�ƽ���ʱ��:");
				String arrivaltime=bf.readLine();
				Timeslot timeslot=new Timeslot(departuretime, arrivaltime);
				List<Timeslot> timeslots=new ArrayList<Timeslot>();
				timeslots.add(timeslot);
				myLogger.info("Ϊ�������ɻ�");
				try {
					fs.allocatePlane(flightname, planeID,timeslots);
				} catch (ResourceNotFoundException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"������ѡ��ɻ����з���!");
				} catch (ResourceConflictException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"������ѡ��ɻ����з���!");
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
				}
				break;
			}
			case "7":{//�������
				System.out.println("������Ҫ��ɵĺ�������:");
				String flightname=bf.readLine();
				System.out.println("������Ҫ��ɵĺ����Ԥ�����ʱ��:");
				String departuretime=bf.readLine();
				System.out.println("������Ҫ��ɵĺ����Ԥ�ƽ���ʱ��:");
				String arrivaltime=bf.readLine();
				Timeslot timeslot=new Timeslot(departuretime, arrivaltime);
				List<Timeslot> timeslots=new ArrayList<Timeslot>();
				timeslots.add(timeslot);
				myLogger.info("�������");
				try {
					fs.departure(flightname,timeslots);
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
				}
				break;
			}
			case "8":{//���ཱུ��
				System.out.println("������Ҫ����ĺ�������:");
				String flightname=bf.readLine();
				System.out.println("������ϣ������ĺ����Ԥ�����ʱ��:");
				String departuretime=bf.readLine();
				System.out.println("������ϣ������ĺ����Ԥ�ƽ���ʱ��:");
				String arrivaltime=bf.readLine();
				Timeslot timeslot=new Timeslot(departuretime, arrivaltime);
				List<Timeslot> timeslots=new ArrayList<Timeslot>();
				timeslots.add(timeslot);
				myLogger.info("���ཱུ��");
				try {
					fs.endFlight(flightname,timeslots);
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
				}
				break;
			}
			case "9":{//Board���ӻ�
				System.out.println("������Ҫչʾ����Ļ�������:");
				String locationname=bf.readLine();
				Location location=fs.getLocationbyName(locationname);
				Calendar calendar=Calendar.getInstance();
				FlightBoard fb=new FlightBoard(location, fs.getFlights(),calendar);
				myLogger.info("���ӻ�չʾ��Ϣ��");
				try {
					fb.visualize();
				} catch (ParseException e) {
					myLogger.severe(e.getMessage());
					e.printStackTrace();
				}
				break;
			}
			case "10":{//�鿴����״̬
				System.out.println("������Ҫ�鿴״̬�ĺ�������:");
				String flightname=bf.readLine();
				System.out.println("������ϣ���鿴״̬�ĺ����Ԥ�����ʱ��:");
				String departuretime=bf.readLine();
				System.out.println("������ϣ���鿴״̬�ĺ����Ԥ�ƽ���ʱ��:");
				String arrivaltime=bf.readLine();
				Timeslot timeslot=new Timeslot(departuretime, arrivaltime);
				List<Timeslot> timeslots=new ArrayList<Timeslot>();
				timeslots.add(timeslot);
				String state;
				myLogger.info("�鿴����״̬");
				try {
					state = fs.getFlightState(flightname,timeslots);
					System.out.println("Ŀǰ����״̬Ϊ:"+state);
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
				}
				break;
			}
			case "11":{//��ǰȡ������
				System.out.println("��������ǰȡ���ĺ�������:");
				String flightname=bf.readLine();
				System.out.println("������ϣ��ȡ���ĺ����Ԥ�����ʱ��:");
				String departuretime=bf.readLine();
				System.out.println("������ϣ��ȡ���ĺ����Ԥ�ƽ���ʱ��:");
				String arrivaltime=bf.readLine();
				Timeslot timeslot=new Timeslot(departuretime, arrivaltime);
				List<Timeslot> timeslots=new ArrayList<Timeslot>();
				timeslots.add(timeslot);
				myLogger.info("ȡ������");
				try {
					fs.cancelFlight(flightname,timeslots);
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
				}
				break;
			}
			case "12":{//���ɻ������ͻ
				PlanningEntryAPIs peAPI=new PlanningEntryAPIsFirstImpl();
				//PlanningEntryAPIs peAPI=new PlanningEntryAPIsSecondImpl();//����ģʽ��client������ѡ��һ��ʵ�ַ�ʽ
				boolean flag=peAPI.checkResourceExclusiveConflict(fs.getFlights());
				myLogger.info("���ɻ������ͻ");
				if(flag) {
					System.out.println("��ǰ���༯���д��ڷɻ������ͻ!");
				}
				else {
					System.out.println("��ǰ���༯�ϲ����ڷɻ������ͻ!");
				}
				break;
			}
			case "13":{//չʾռ��ָ���ɻ��ĺ���
				System.out.println("������Ҫ�鿴����ռ�õķɻ����:");
				String planeId=bf.readLine();
				PlanningEntryAPIs peAPI=new PlanningEntryAPIsFirstImpl();
				List<FlightEntry<Plane>> fEntries=fs.getFlightssofassignPlane(planeId);
				myLogger.info("����ռ��ָ���ɻ��ĺ���");
				if(fEntries==null) {
					System.out.println("�÷ɻ�δ���亽��!");
					break;
				}
				else {
					System.out.println("ռ�ø÷ɻ��ĺ�����:");
					for(FlightEntry<Plane> fe:fEntries) {
						System.out.println(fe.getName()+" Ŀǰ����״̬Ϊ:"+fe.getStateName());
					}
					System.out.println("�Ƿ���Ҫ�鿴ָ�������ǰ�򺽰�?(Y/N)");
					input=bf.readLine();
					if(input==null) {
						myLogger.severe("�������쳣");
						throw new IOException();	
					}
					if(input.equals("Y")) {
						System.out.println("������Ҫ�鿴��ָ������:");
						input=bf.readLine();
						System.out.println("������Ҫ�鿴�ĺ����Ԥ�����ʱ��:");
						String departuretime=bf.readLine();
						System.out.println("������Ҫ�鿴�ĺ����Ԥ�ƽ���ʱ��:");
						String arrivaltime=bf.readLine();
						Timeslot timeslot=new Timeslot(departuretime, arrivaltime);
						List<Timeslot> timeslots=new ArrayList<Timeslot>();
						timeslots.add(timeslot);
						FlightEntry<Plane> flightEntry=fs.getFlightbyName(input,timeslots);
						while(flightEntry==null) {
							System.out.println("��ѯʧ��!�������������ѡȡ!");
							System.out.println("������Ҫ�鿴��ָ������:");
							input=bf.readLine();
							System.out.println("������Ҫ�鿴�ĺ����Ԥ�����ʱ��:");
							departuretime=bf.readLine();
							System.out.println("������Ҫ�鿴�ĺ����Ԥ�ƽ���ʱ��:");
							arrivaltime=bf.readLine();
							timeslot=new Timeslot(departuretime, arrivaltime);
							timeslots=new ArrayList<Timeslot>();
							timeslots.add(timeslot);
							flightEntry=fs.getFlightbyName(input,timeslots);
						}
						Plane plane=fs.getPlanebyID(planeId);
						myLogger.info("�鿴ָ�������ǰ�򺽰�");
						try {
							PlanningEntry<Plane> fEntry=peAPI.findPreEntryPerResource(plane,flightEntry, fs.getFlights());
							if(fEntry==null){
								System.out.println("�ú�����ǰ�򺽰�!");
							}
							else
								System.out.println("��ǰ�򺽰�Ϊ:"+fEntry.getName());
						} catch (ParseException e) {
							myLogger.severe(e.getMessage());
							e.printStackTrace();
						}
					}
				}
				break;
			}
			case "14":{
				System.out.println("������Ҫ��ȡ���ļ�·��:");
				input=bf.readLine();
				myLogger.info("��ȡ�ļ���������");
				if(input==null) {
					myLogger.severe("�������쳣");
					throw new IOException();	
				}
				try {
					fs.createFlightByFile(input);
					System.out.println("���ഴ���ɹ�!");
				} catch (FileNotFoundException e) {
					myLogger.severe(e.getMessage());
					System.out.println(e.getMessage()+"������ѡ���ļ����ж�ȡ!");
				} catch (LocationNotFoundException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"������ѡ���ļ����ж�ȡ!");
				} catch (IncorrectElementDependencyException e) {
					myLogger.severe(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"������ѡ���ļ����ж�ȡ!");
				} catch (SameLabelException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"������ѡ���ļ����ж�ȡ!");
				} catch (UnGrammaticalWordException e) {
					myLogger.severe(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"������ѡ���ļ����ж�ȡ!");
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"������ѡ���ļ����ж�ȡ!");
				} catch (ResourceNotFoundException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"������ѡ���ļ����ж�ȡ!");
				} catch (ResourceConflictException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"������ѡ���ļ����ж�ȡ!");
				}
				break;
			}
			case "15":{
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
			
			if(!input.equals("16")) {//ÿ���һ�ι���,���´�ӡ�˵����û�ѡȡ
				fsApp.menu();
				input=bf.readLine();
			}
			
			if(input==null) {
				myLogger.severe("�������쳣");
				throw new IOException();	
			}
		}
		myLogger.info("�˳�FlightScheduleApp");
		handler.close();
	}
	
	private static void queryLog(String condition) throws IOException, ParseException {
		String time,classname,functionname,message,level;
		boolean timeflag=condition.equals("time");
		boolean actionflag=condition.equals("action");
		boolean exceptionflag=condition.equals("exception");
		if(!timeflag&&!actionflag&&!exceptionflag)//�Ƿ���ѯ��ʽ
			return;
		File file=new File("src/LogFile/FlightScheduleAppLog.log");
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
						if(level.equals("INFO")&&message.equals("��ӷɻ�"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "2":{
						if(level.equals("INFO")&&message.equals("ɾ���ɻ�"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "3":{
						if(level.equals("INFO")&&message.equals("���ӻ���"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "4":{
						if(level.equals("INFO")&&message.equals("ɾ������"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}	
					case "5":{
						if(level.equals("INFO")&&message.equals("��������"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "6":{
						if(level.equals("INFO")&&message.equals("Ϊ�������ɻ�"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "7":{
						if(level.equals("INFO")&&message.equals("�������"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "8":{
						if(level.equals("INFO")&&message.equals("���ཱུ��"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "9":{
						if(level.equals("INFO")&&message.equals("���ӻ�չʾ��Ϣ��"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "10":{
						if(level.equals("INFO")&&message.equals("�鿴����״̬"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "11":{
						if(level.equals("INFO")&&message.equals("ȡ������"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "12":{
						if(level.equals("INFO")&&message.equals("���ɻ������ͻ"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "13":{
						if(level.equals("INFO")&&(message.equals("����ռ��ָ���ɻ��ĺ���")||message.equals("�鿴ָ�������ǰ�򺽰�")))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "14":{
						if(level.equals("INFO")&&message.equals("��ȡ�ļ���������"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "15":{
						if(level.equals("INFO")&&message.equals("�鿴��־"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "16":{
						if(level.equals("INFO")&&message.equals("�˳�FlightScheduleApp"))
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
