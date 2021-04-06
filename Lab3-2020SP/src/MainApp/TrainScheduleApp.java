package MainApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import API.*;
import Board.TrainBoard;
import Location.Location;
import Resources.Carriage;
import Schedule.TrainSchedule;
import Timeslot.Timeslot;
import common.PlanningEntry;
import compositeinterface.TrainEntry;

public class TrainScheduleApp {
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
		System.out.println("**** 15.�˳�ϵͳ	      ****");
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		TrainScheduleApp tsApp=new TrainScheduleApp();
		tsApp.menu();
		String input=bf.readLine();
		TrainSchedule ts=new TrainSchedule();
		while(!input.equals("15")) {
			switch (input) {
			case "1":{//���ӳ���
				System.out.println("���������복��ı�š����͡���Ա�����������");
				input=bf.readLine();
				String[] information=input.split(" ");
				while(information.length!=4) {
					System.out.println("���벻����!����������!");//�쳣����
					input=bf.readLine();
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
				ts.deleteCarriage(input);
				break;
			}
			case "3":{//����վ��
				System.out.println("�������������վ��ľ��ȡ�γ�ȡ�����");
				input=bf.readLine();
				String[] information=input.split(" ");
				while(information.length!=3) {
					System.out.println("���벻����!����������!");//�쳣����
					input=bf.readLine();
					information=input.split(" ");
					
				}
				Location location=new Location(information[0], information[1], information[2], false);
				ts.addLocation(location);//���λ��
				break;
			}
			case "4":{//ɾ��վ��
				System.out.println("�������ɾ������վ�������:");
				input=bf.readLine();
				ts.deleteLocation(input);//��������ɾ��
				break;
			}
			case "5":{//������������
				System.out.println("������Ҫ�½��ĸ�����������:");
				String trainname=bf.readLine();
				System.out.println("���������복�ξ�����վ������(ÿ��λ�ü���#�ָ�):");
				input=bf.readLine();
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
				ts.createTrain(trainname, locationnames, timeslots);
				break;
			}
			case "6":{//���䳵��
				System.out.println("������Ҫ���䳵��ĳ�������:");
				String trainname=bf.readLine();
				System.out.println("����������Ϊ���η���ĳ�����:");
				String ID=bf.readLine();
				String[] IDs=ID.split(" ");
				List<String> carriageIDs=new ArrayList<String>();
				for(int i=0;i<IDs.length;i++) {
					carriageIDs.add(IDs[i]);
				}
				
				ts.allocateCarriage(trainname, carriageIDs);
				break;
			}
			case "7":{//�г�����
				System.out.println("������Ҫ�����ĳ�������:");
				String trainname=bf.readLine();
				ts.startTrain(trainname);
				break;
			}
			case "8":{//�г�ͣ��
				System.out.println("������Ҫͣ�����г�����:");
				String trainname=bf.readLine();
				ts.endTrain(trainname);
				break;
			}
			case "9":{//Board���ӻ�
				System.out.println("������Ҫչʾ���ε�վ������:");
				String locationname=bf.readLine();
				Location location=ts.getLocationbyName(locationname);
				Calendar calendar=Calendar.getInstance();
				TrainBoard tb=new TrainBoard(location, ts.getTrains(),calendar);
				try {
					tb.visualize();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				break;
			}
			case "10":{//�鿴�г�״̬
				System.out.println("������Ҫ�鿴״̬�ĳ�������:");
				String trainname=bf.readLine();
				System.out.println("Ŀǰ�г�״̬Ϊ:"+ts.getTrainState(trainname));
				break;
			}
			case "11":{//��ǰȡ���г�
				System.out.println("��������ǰȡ���ĳ�������:");
				String trainname=bf.readLine();
				ts.cancelTrain(trainname);
				break;
			}
			case "12":{//��⳵������ͻ
				PlanningEntryAPIs peAPI=new PlanningEntryAPIsFirstImpl();
				//PlanningEntryAPIs peAPI=new PlanningEntryAPIsSecondImpl();//����ģʽ��client������ѡ��һ��ʵ�ַ�ʽ
				boolean flag=peAPI.checkResourceExclusiveConflict(ts.getTrains());
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
					if(input.equals("Y")) {
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
							e.printStackTrace();
						}
					}
				}
				break;
			}
			case "14":{
				System.out.println("������Ҫ�����ĳ�������:");
				String trainname=bf.readLine();
				ts.blockTrain(trainname);
				break;
			}
			default:{
				System.out.println("�������,������ѡ����!");
				break;
			}
			
			}
			
			if(!input.equals("15")) {//ÿ���һ�ι���,���´�ӡ�˵����û�ѡȡ
				tsApp.menu();
				input=bf.readLine();
			}
		}
	}
}
