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
import Board.TrainBoard;
import Exceptions.LocationNotFoundException;
import Exceptions.PlanEntryNotCreateException;
import Exceptions.PlanEntryOccupyLocationException;
import Exceptions.PlanEntryOccupyResourceException;
import Exceptions.ResourceConflictException;
import Exceptions.ResourceNotFoundException;
import Exceptions.SameLabelException;
import Location.Location;
import LogFile.MyFormatter;
import Resources.Carriage;
import Schedule.TrainSchedule;
import Timeslot.Timeslot;
import common.PlanningEntry;
import compositeinterface.TrainEntry;

public class TrainScheduleApp {
	private static Logger myLogger=Logger.getLogger("TrainScheduleAppLog");
	private final static int timebound=1;//��������ʶ��ʱ��鿴��־ʱ����ѯ��ʱ�䷶Χ
	
	public void menu() {
		System.out.println("-------��������ϵͳ-------");
		System.out.println("**** 1.���ӹ���ĳ���  ****");
		System.out.println("**** 2.ɾ������ĳ���  ****");
		System.out.println("**** 3.���ӹ����վ��  ****");
		System.out.println("**** 4.ɾ�������վ��  ****");
		System.out.println("**** 5.����һ���³���  ****");
		System.out.println("**** 6.Ϊ���η��䳵��  ****");
		System.out.println("**** 7.ָ�����ν�������****");
		System.out.println("**** 8.ָ�����ν���ͣ��****");
		System.out.println("**** 9.չʾվ�㳵�α�   ****");
		System.out.println("**** 10.�鿴�г�״̬     ****");
		System.out.println("**** 11.��ǰȡ������     ****");	
		System.out.println("**** 12.��⳵������ͻ***");
		System.out.println("**** 13.չʾ���ᳵ�ΰ���***");
		System.out.println("**** 14.�Գ��ν�������  ****");
		System.out.println("**** 15.��ѯ��־	      ****");
		System.out.println("**** 16.�˳�ϵͳ	      ****");
	}
	
	public static void main(String[] args) throws IOException {
		//��־��¼
		myLogger.setLevel(Level.INFO);
		//д���ļ�
		FileHandler handler=new FileHandler("src/LogFile/TrainScheduleAppLog.log");
		handler.setFormatter(new MyFormatter());//���ù̶���ʽ
		handler.setLevel(Level.INFO);
		myLogger.addHandler(handler);
		myLogger.setUseParentHandlers(false);
		
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		TrainScheduleApp tsApp=new TrainScheduleApp();
		tsApp.menu();
		String input=bf.readLine();
		TrainSchedule ts=new TrainSchedule();
		myLogger.info("����TrainScheduleApp");
		if(input==null) {
			myLogger.severe("�������쳣");
			throw new IOException();	
		}
		while(!input.equals("16")) {
			switch (input) {
			case "1":{//���ӳ���
				System.out.println("���������복��ı�š����͡���Ա�����������");
				input=bf.readLine();
				myLogger.info("��ӳ���");
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
				Carriage carriage=new Carriage(information[0], information[1], seats, information[3]);
				ts.addCarriage(carriage);
				break;
			}
			case "2":{//ɾ������
				System.out.println("�������ɾ������ı��:");
				input=bf.readLine();
				myLogger.info("ɾ������");
				try {
					ts.deleteCarriage(input);
				} catch (ResourceNotFoundException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"ɾ��ʧ��!");
				} catch (PlanEntryOccupyResourceException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"ɾ��ʧ��!");
				}
				break;
			}
			case "3":{//����վ��
				System.out.println("�������������վ��ľ��ȡ�γ�ȡ�����");
				input=bf.readLine();
				myLogger.info("���Ӹ���վ��");
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
				ts.addLocation(location);//���λ��
				break;
			}
			case "4":{//ɾ��վ��
				System.out.println("�������ɾ������վ�������:");
				input=bf.readLine();
				myLogger.info("ɾ������վ��");
				try {
					ts.deleteLocation(input);
				} catch (LocationNotFoundException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"ɾ��ʧ��!");
				} catch (PlanEntryOccupyLocationException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"ɾ��ʧ��!");
				}//��������ɾ��
				break;
			}
			case "5":{//������������
				System.out.println("������Ҫ�½��ĸ�����������:");
				String trainname=bf.readLine();
				System.out.println("���������복�ξ�����վ������(ÿ��λ�ü���#�ָ�):");
				input=bf.readLine();
				myLogger.info("������������");
				if(input==null) {
					myLogger.severe("�������쳣");
					throw new IOException();	
				}
				String[] locations=input.split("#");
				List<String> locationnames=new ArrayList<String>();
				for(int i=0;i<locations.length;i++) {
					locationnames.add(locations[i]);
				}
				System.out.println("�����������Ӧ��Ԥ�ƾ�ͣʱ��(yyyy-MM-dd HH:mm)(ÿ��ʱ��㰴һ�λس�����ȷ��):");
				List<Timeslot> timeslots=new ArrayList<Timeslot>();
				String start=null;
				String end=null;
				for(int i=0;i<2*(locations.length-1);i++) {
					input=bf.readLine();
					if(i%2==0) {
						start=input;
					}
					else {
						end=input;
						Timeslot timeslot=new Timeslot(start, end);
						timeslots.add(timeslot);
					}
					
				}
				
				try {
					ts.createTrain(trainname, locationnames, timeslots);
				} catch (LocationNotFoundException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"�޷�����!");
				} catch (SameLabelException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"���ܴ�����������!");
				}
				break;
			}
			case "6":{//���䳵��
				System.out.println("������Ҫ���䳵��ĳ�������:");
				String trainname=bf.readLine();
				System.out.println("����������Ϊ���η���ĳ�����:");
				String ID=bf.readLine();
				myLogger.info("Ϊ�г����䳵��");
				if(ID==null) {
					myLogger.severe("�������쳣");
					throw new IOException();	
				}
				String[] IDs=ID.split(" ");
				List<String> carriageIDs=new ArrayList<String>();
				for(int i=0;i<IDs.length;i++) {
					carriageIDs.add(IDs[i]);
				}
				try {
					ts.allocateCarriage(trainname, carriageIDs);
				} catch (ResourceNotFoundException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"������ѡ������з���!");
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
				} catch (ResourceConflictException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"������ѡ������з���!");
				}
				break;
			}
			case "7":{//�г�����
				System.out.println("������Ҫ�����ĳ�������:");
				String trainname=bf.readLine();
				myLogger.info("�����г�");
				try {
					ts.startTrain(trainname);
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
				}
				break;
			}
			case "8":{//�г�ͣ��
				System.out.println("������Ҫͣ�����г�����:");
				String trainname=bf.readLine();
				myLogger.info("�г�ͣ��");
				try {
					ts.endTrain(trainname);
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
				}
				break;
			}
			case "9":{//Board���ӻ�
				System.out.println("������Ҫչʾ���ε�վ������:");
				String locationname=bf.readLine();
				Location location=ts.getLocationbyName(locationname);
				Calendar calendar=Calendar.getInstance();
				TrainBoard tb=new TrainBoard(location, ts.getTrains(),calendar);
				myLogger.info("���ӻ�չʾ��Ϣ��");
				try {
					tb.visualize();
				} catch (ParseException e) {
					myLogger.warning(e.getMessage());
					e.printStackTrace();
				}
				break;
			}
			case "10":{//�鿴�г�״̬
				System.out.println("������Ҫ�鿴״̬�ĳ�������:");
				String trainname=bf.readLine();
				myLogger.info("�鿴�г�״̬");
				try {
					String state=ts.getTrainState(trainname);
					System.out.println("Ŀǰ�г�״̬Ϊ:"+state);
				} catch (PlanEntryNotCreateException e) {
					System.out.println(e.getErrorMessage());
					myLogger.warning(e.getErrorMessage());
				}
				
				break;
			}
			case "11":{//��ǰȡ���г�
				System.out.println("��������ǰȡ���ĳ�������:");
				String trainname=bf.readLine();
				myLogger.info("ȡ���г�");
				try {
					ts.cancelTrain(trainname);
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
				}
				break;
			}
			case "12":{//��⳵������ͻ
				PlanningEntryAPIs peAPI=new PlanningEntryAPIsFirstImpl();
				//PlanningEntryAPIs peAPI=new PlanningEntryAPIsSecondImpl();//����ģʽ��client������ѡ��һ��ʵ�ַ�ʽ
				boolean flag=peAPI.checkResourceExclusiveConflict(ts.getTrains());
				myLogger.info("��⳵������ͻ");
				if(flag) {
					System.out.println("��ǰ�г������д��ڳ�������ͻ!");
				}
				else {
					System.out.println("��ǰ�г����ϲ����ڳ�������ͻ!");
				}
				break;
			}
			case "13":{//չʾռ��ָ��������г�
				System.out.println("������Ҫ�鿴�г�ռ�õĳ�����:");
				String CarriageId=bf.readLine();
				PlanningEntryAPIs peAPI=new PlanningEntryAPIsFirstImpl();
				List<TrainEntry<Carriage>> tEntries=ts.getTrainsofassignCarriage(CarriageId);
				myLogger.info("չʾռ��ָ��������г�");
				if(tEntries==null) {
					System.out.println("�ó���δ���䳵��!");
					break;
				}
				else {
					System.out.println("ռ�øó�����г���:");
					for(TrainEntry<Carriage> te:tEntries) {
						System.out.println(te.getName()+" Ŀǰ�г�״̬Ϊ:"+te.getStateName());
					}
					System.out.println("�Ƿ���Ҫ�鿴ָ�����ε�ǰ�򳵴�?(Y/N)");
					input=bf.readLine();
					if(input==null) {
						myLogger.severe("�������쳣");
						throw new IOException();	
					}
					if(input.equals("Y")) {
						myLogger.info("�鿴ָ�����ε�ǰ�򳵴�");
						System.out.println("������Ҫ�鿴��ָ������:");
						input=bf.readLine();
						TrainEntry<Carriage> tEntry=ts.getTrainbyName(input);
						while(tEntry==null) {
							System.out.println("��ѯʧ��!�������������ѡȡ!");
							input=bf.readLine();
							tEntry=ts.getTrainbyName(input);
						}
						Carriage carriage=ts.getCarriagebyID(CarriageId);
						try {
							PlanningEntry<Carriage> planningEntry=peAPI.findPreEntryPerResource(carriage, tEntry, ts.getTrains());
							if(planningEntry==null)
								System.out.println("�ó�����ǰ�򳵴�!");
							else
								System.out.println("��ǰ�򳵴�Ϊ:"+planningEntry.getName());
						} catch (ParseException e) {
							myLogger.severe(e.getMessage());
							e.printStackTrace();
						}
					}
				}
				break;
			}
			case "14":{
				System.out.println("������Ҫ�����ĳ�������:");
				String trainname=bf.readLine();
				myLogger.info("����ָ������");
				try {
					ts.blockTrain(trainname);
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
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
				tsApp.menu();
				input=bf.readLine();
			}
			
			if(input==null) {
				myLogger.severe("�������쳣");
				throw new IOException();	
			}
		}
		myLogger.info("�˳�TrainScheduleApp");
		handler.close();
	}
	
	private static void queryLog(String condition) throws IOException, ParseException {
		String time,classname,functionname,message,level;
		boolean timeflag=condition.equals("time");
		boolean actionflag=condition.equals("action");
		boolean exceptionflag=condition.equals("exception");
		if(!timeflag&&!actionflag&&!exceptionflag)//�Ƿ���ѯ��ʽ
			return;
		File file=new File("src/LogFile/TrainScheduleAppLog.log");
		if(!file.exists()||!file.isFile()) {
			throw new FileNotFoundException();
		}
		InputStreamReader read;
		read = new InputStreamReader(new FileInputStream(file));
		BufferedReader bufferedReader = new BufferedReader(read);
		
		String inputString=null;
		if(actionflag) {
			System.out.println("����������Ҫ�鿴�Ĳ�������(�ù��ܲ˵���ÿ������ǰ���������)");
			BufferedReader bf2 = new BufferedReader(new InputStreamReader(System.in));
			inputString=bf2.readLine();
		}
		
		try {
			String line=null;
			SimpleDateFormat dateFormat=new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",Locale.UK);
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
						if(level.equals("INFO")&&message.equals("��ӳ���"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "2":{
						if(level.equals("INFO")&&message.equals("ɾ������"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "3":{
						if(level.equals("INFO")&&message.equals("���Ӹ���վ��"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "4":{
						if(level.equals("INFO")&&message.equals("ɾ������վ��"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}	
					case "5":{
						if(level.equals("INFO")&&message.equals("������������"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "6":{
						if(level.equals("INFO")&&message.equals("Ϊ�г����䳵��"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "7":{
						if(level.equals("INFO")&&message.equals("�����г�"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "8":{
						if(level.equals("INFO")&&message.equals("�г�ͣ��"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "9":{
						if(level.equals("INFO")&&message.equals("���ӻ�չʾ��Ϣ��"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "10":{
						if(level.equals("INFO")&&message.equals("�鿴�г�״̬"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "11":{
						if(level.equals("INFO")&&message.equals("ȡ���г�"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "12":{
						if(level.equals("INFO")&&message.equals("��⳵������ͻ"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "13":{
						if(level.equals("INFO")&&(message.equals("չʾռ��ָ��������г�")||message.equals("�鿴ָ�����ε�ǰ�򳵴�")))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "14":{
						if(level.equals("INFO")&&message.equals("����ָ������"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "15":{
						if(level.equals("INFO")&&message.equals("�鿴��־"))
							System.out.println("ʱ��:"+date2+" ������:"+classname+" ������:"+functionname+" ��־����:"+level+" �����Ϣ:"+message);
						break;
					}
					case "16":{
						if(level.equals("INFO")&&message.equals("�˳�TrainScheduleApp"))
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
