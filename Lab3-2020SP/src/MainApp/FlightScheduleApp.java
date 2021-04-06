package MainApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import API.*;
import Board.FlightBoard;
import Location.Location;
import Resources.Plane;
import Schedule.FlightSchedule;
import Timeslot.Timeslot;
import common.PlanningEntry;
import compositeinterface.FlightEntry;

public class FlightScheduleApp {
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
		System.out.println("**** 15.�Ժ����������  ****");
		System.out.println("**** 16.�˳�ϵͳ	      ****");
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		FlightScheduleApp fsApp=new FlightScheduleApp();
		fsApp.menu();
		String input=bf.readLine();
		FlightSchedule fs=new FlightSchedule();
		while(!input.equals("15")) {
			switch (input) {
			case "1":{//���ӷɻ�
				System.out.println("����������ɻ��ı�š����ͺš���λ��������");
				input=bf.readLine();
				String[] information=input.split(" ");
				while(information.length!=4) {
					System.out.println("���벻����!����������!");//�쳣����
					input=bf.readLine();
					information=input.split(" ");
				}
				int seats=Integer.parseInt(information[2]);
				double age=Double.parseDouble(information[3]);
				Plane plane=new Plane(information[0], information[1], seats, age);
				fs.addPlane(plane);
				break;
			}
			case "2":{//ɾ���ɻ�
				System.out.println("�������ɾ���ɻ��ı��:");
				input=bf.readLine();
				fs.deletePlane(input);
				break;
			}
			case "3":{//���ӻ���
				System.out.println("��������������ľ��ȡ�γ�ȡ�����");
				input=bf.readLine();
				String[] information=input.split(" ");
				while(information.length!=3) {
					System.out.println("���벻����!����������!");//�쳣����
					input=bf.readLine();
					information=input.split(" ");
					
				}
				Location location=new Location(information[0], information[1], information[2], false);
				fs.addLocation(location);//���λ��
				break;
			}
			case "4":{//ɾ������
				System.out.println("�������ɾ������������:");
				input=bf.readLine();
				fs.deleteLocation(input);//��������ɾ��
				break;
			}
			case "5":{//��������
				System.out.println("������Ҫ�½��ĺ�������:");
				String flightname=bf.readLine();
				System.out.println("���������뺽�ྭ���Ļ�������(�Կո���):");
				input=bf.readLine();
				String[] locations=input.split(" ");
				List<String> locationnames=new ArrayList<String>();
				for(int i=0;i<locations.length;i++) {
					locationnames.add(locations[i]);
				}
				List<Timeslot> timeslots=new ArrayList<Timeslot>();
				if(locations.length==2) {//û���м�վ��
					System.out.println("�����뺽��Ԥ�����ʱ��(yyyy-MM-dd HH:mm):");
					String start=bf.readLine();
					System.out.println("�����뺽��Ԥ�ƽ���ʱ��(yyyy-MM-dd HH:mm):");
					String end=bf.readLine();
					Timeslot timeslot=new Timeslot(start, end);
					timeslots.add(timeslot);
				}
				else if(locations.length==3) {//��һ���м�վ��
					System.out.println("�����뺽��Ԥ�����ʱ��(yyyy-MM-dd HH:mm):");
					String start=bf.readLine();
					System.out.println("�����뺽��Ԥ�ƾ�ͣʱ��(yyyy-MM-dd HH:mm):");
					String middlestart=bf.readLine();
					System.out.println("�����뺽�����м��Ԥ�Ƴ���ʱ��(yyyy-MM-dd HH:mm):");
					String middleend=bf.readLine();
					System.out.println("�����뺽��Ԥ�ƽ���ʱ��(yyyy-MM-dd HH:mm):");
					String end=bf.readLine();
					Timeslot timeslot=new Timeslot(start, middlestart);
					Timeslot timeslot2=new Timeslot(middleend, end);
					timeslots.add(timeslot);
					timeslots.add(timeslot2);
				}
				else {//������Ҫ��
					System.out.println("����λ�ø���������Ҫ��!����ʧ��!");
					break;
				}
				fs.createFlight(flightname, locationnames, timeslots);
				break;
			}
			case "6":{//����ɻ�
				System.out.println("������Ҫ����ĺ�������:");
				String flightname=bf.readLine();
				System.out.println("������Ϊ����Ҫ����ķɻ����:");
				String planeID=bf.readLine();
				System.out.println("������Ҫ����ɻ��ĺ����Ԥ�����ʱ��(���Ǿ�ͣ��):");
				String departuretime=bf.readLine();
				System.out.println("������Ҫ����ɻ��ĺ����Ԥ�ƽ���ʱ��(���Ǿ�ͣ��):");
				String arrivaltime=bf.readLine();
				Timeslot timeslot=new Timeslot(departuretime, arrivaltime);
				List<Timeslot> timeslots=new ArrayList<Timeslot>();
				timeslots.add(timeslot);
				fs.allocatePlane(flightname, planeID,timeslots);
				break;
			}
			case "7":{//�������
				System.out.println("������Ҫ��ɵĺ�������:");
				String flightname=bf.readLine();
				System.out.println("������Ҫ��ɵĺ����Ԥ�����ʱ��(���Ǿ�ͣ��):");
				String departuretime=bf.readLine();
				System.out.println("������Ҫ��ɵĺ����Ԥ�ƽ���ʱ��(���Ǿ�ͣ��):");
				String arrivaltime=bf.readLine();
				Timeslot timeslot=new Timeslot(departuretime, arrivaltime);
				List<Timeslot> timeslots=new ArrayList<Timeslot>();
				timeslots.add(timeslot);
				fs.departure(flightname,timeslots);
				break;
			}
			case "8":{//���ཱུ��
				System.out.println("������Ҫ����ĺ�������:");
				String flightname=bf.readLine();
				System.out.println("������Ҫ����ĺ����Ԥ�����ʱ��(���Ǿ�ͣ��):");
				String departuretime=bf.readLine();
				System.out.println("������Ҫ����ĺ����Ԥ�ƽ���ʱ��(���Ǿ�ͣ��):");
				String arrivaltime=bf.readLine();
				Timeslot timeslot=new Timeslot(departuretime, arrivaltime);
				List<Timeslot> timeslots=new ArrayList<Timeslot>();
				timeslots.add(timeslot);
				fs.endFlight(flightname,timeslots);
				break;
			}
			case "9":{//Board���ӻ�
				System.out.println("������Ҫչʾ����Ļ�������:");
				String locationname=bf.readLine();
				Location location=fs.getLocationbyName(locationname);
				Calendar calendar=Calendar.getInstance();
				FlightBoard fb=new FlightBoard(location, fs.getFlights(),calendar);
				try {
					fb.visualize();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				break;
			}
			case "10":{//�鿴����״̬
				System.out.println("������Ҫ�鿴״̬�ĺ�������:");
				String flightname=bf.readLine();
				System.out.println("������Ҫ�鿴״̬�ĺ����Ԥ�����ʱ��(���Ǿ�ͣ��):");
				String departuretime=bf.readLine();
				System.out.println("������Ҫ�鿴״̬�ĺ����Ԥ�ƽ���ʱ��(���Ǿ�ͣ��):");
				String arrivaltime=bf.readLine();
				Timeslot timeslot=new Timeslot(departuretime, arrivaltime);
				List<Timeslot> timeslots=new ArrayList<Timeslot>();
				timeslots.add(timeslot);
				String state=fs.getFlightState(flightname,timeslots);
				if(state==null) {
					System.out.println("�ú�����δ����!");
					break;
				}
				System.out.println("Ŀǰ����״̬Ϊ:"+state);
				break;
			}
			case "11":{//��ǰȡ������
				System.out.println("��������ǰȡ���ĺ�������:");
				String flightname=bf.readLine();
				System.out.println("������Ҫȡ���ĺ����Ԥ�����ʱ��(���Ǿ�ͣ��):");
				String departuretime=bf.readLine();
				System.out.println("������Ҫȡ���ĺ����Ԥ�ƽ���ʱ��(���Ǿ�ͣ��):");
				String arrivaltime=bf.readLine();
				Timeslot timeslot=new Timeslot(departuretime, arrivaltime);
				List<Timeslot> timeslots=new ArrayList<Timeslot>();
				timeslots.add(timeslot);
				fs.cancelFlight(flightname,timeslots);
				break;
			}
			case "12":{//���ɻ������ͻ
				PlanningEntryAPIs peAPI=new PlanningEntryAPIsFirstImpl();
				//PlanningEntryAPIs peAPI=new PlanningEntryAPIsSecondImpl();//����ģʽ��client������ѡ��һ��ʵ�ַ�ʽ
				boolean flag=peAPI.checkResourceExclusiveConflict(fs.getFlights());
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
					if(input.equals("Y")) {
						System.out.println("������Ҫ�鿴��ָ������:");
						input=bf.readLine();
						System.out.println("������Ҫ�鿴�ĺ����Ԥ�����ʱ��(���Ǿ�ͣ��):");
						String departuretime=bf.readLine();
						System.out.println("������Ҫ�鿴�ĺ����Ԥ�ƽ���ʱ��(���Ǿ�ͣ��):");
						String arrivaltime=bf.readLine();
						Timeslot timeslot=new Timeslot(departuretime, arrivaltime);
						List<Timeslot> timeslots=new ArrayList<Timeslot>();
						timeslots.add(timeslot);
						FlightEntry<Plane> flightEntry=fs.getFlightbyName(input,timeslots);
						while(flightEntry==null) {
							System.out.println("��ѯʧ��!�������������ѡȡ!");
							System.out.println("������Ҫ�鿴��ָ������:");
							input=bf.readLine();
							System.out.println("������Ҫ�鿴�ĺ����Ԥ�����ʱ��(���Ǿ�ͣ��):");
							departuretime=bf.readLine();
							System.out.println("������Ҫ�鿴�ĺ����Ԥ�ƽ���ʱ��(���Ǿ�ͣ��):");
							arrivaltime=bf.readLine();
							timeslot=new Timeslot(departuretime, arrivaltime);
							timeslots=new ArrayList<Timeslot>();
							timeslots.add(timeslot);
							flightEntry=fs.getFlightbyName(input,timeslots);
						}
						Plane plane=fs.getPlanebyID(planeId);
						try {
							PlanningEntry<Plane> fEntry=peAPI.findPreEntryPerResource(plane,flightEntry, fs.getFlights());
							if(fEntry==null){
								System.out.println("�ú�����ǰ�򺽰�!");
							}
							else
								System.out.println("��ǰ�򺽰�Ϊ:"+fEntry.getName());
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}
				break;
			}
			case "14":{
				System.out.println("������Ҫ��ȡ���ļ�·��:");
				input=bf.readLine();
				boolean flag=fs.createFlightByFile(input);
				if(flag) {
					System.out.println("�����ɹ�!");
				}
				else {
					System.out.println("����������:");
				}
				break;
			}
			case "15":{
				System.out.println("������Ҫ�����ĺ�������:");
				input=bf.readLine();
				System.out.println("������Ҫ�����ĺ����Ԥ�����ʱ��(���Ǿ�ͣ��):");
				String departuretime=bf.readLine();
				System.out.println("������Ҫ�����ĺ����Ԥ�ƽ���ʱ��(���Ǿ�ͣ��):");
				String arrivaltime=bf.readLine();
				Timeslot timeslot=new Timeslot(departuretime, arrivaltime);
				List<Timeslot> timeslots=new ArrayList<Timeslot>();
				timeslots.add(timeslot);
				fs.blockFlight(input,timeslots);
			}
			default:{
				System.out.println("�������,������ѡ����!");
				break;
			}
			
			}
			
			if(!input.equals("16")) {//ÿ���һ�ι���,���´�ӡ�˵����û�ѡȡ
				fsApp.menu();
				input=bf.readLine();
			}
		}
	}
}
