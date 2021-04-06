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
		System.out.println("-------高铁管理系统-------");
		System.out.println("**** 1.增加管理的车厢  ****");
		System.out.println("**** 2.删除管理的车厢  ****");
		System.out.println("**** 3.增加管理的站点  ****");
		System.out.println("**** 4.删除管理的站点  ****");
		System.out.println("**** 5.增加一个新车次  ****");
		System.out.println("**** 6.为车次分配车厢  ****");
		System.out.println("**** 7.指定车次进行启动****");
		System.out.println("**** 8.指定车次进行停车****");
		System.out.println("**** 9.展示站点车次表   ****");
		System.out.println("**** 10.查看列车状态     ****");
		System.out.println("**** 11.提前取消车次     ****");	
		System.out.println("**** 12.检测车厢分配冲突***");
		System.out.println("**** 13.展示车厢车次安排***");
		System.out.println("**** 14.对车次进行阻塞  ****");
		System.out.println("**** 15.退出系统	      ****");
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		TrainScheduleApp tsApp=new TrainScheduleApp();
		tsApp.menu();
		String input=bf.readLine();
		TrainSchedule ts=new TrainSchedule();
		while(!input.equals("15")) {
			switch (input) {
			case "1":{//增加车厢
				System.out.println("请依次输入车厢的编号、类型、定员数、出厂年份");
				input=bf.readLine();
				String[] information=input.split(" ");
				while(information.length!=4) {
					System.out.println("输入不完善!请重新输入!");//异常处理
					input=bf.readLine();
					information=input.split(" ");
				}
				int seats=Integer.parseInt(information[2]);
				Carriage carriage=new Carriage(information[0], information[1], seats, information[3]);
				ts.addCarriage(carriage);
				break;
			}
			case "2":{//删除车厢
				System.out.println("请输入待删除车厢的编号:");
				input=bf.readLine();
				ts.deleteCarriage(input);
				break;
			}
			case "3":{//增加站点
				System.out.println("请依次输入高铁站点的经度、纬度、名称");
				input=bf.readLine();
				String[] information=input.split(" ");
				while(information.length!=3) {
					System.out.println("输入不完善!请重新输入!");//异常处理
					input=bf.readLine();
					information=input.split(" ");
					
				}
				Location location=new Location(information[0], information[1], information[2], false);
				ts.addLocation(location);//添加位置
				break;
			}
			case "4":{//删除站点
				System.out.println("请输入待删除高铁站点的名称:");
				input=bf.readLine();
				ts.deleteLocation(input);//根据名称删除
				break;
			}
			case "5":{//创建高铁车次
				System.out.println("请输入要新建的高铁车次名称:");
				String trainname=bf.readLine();
				System.out.println("请依次输入车次经过的站点名称(每个位置间以#分隔):");
				input=bf.readLine();
				String[] locations=input.split("#");
				List<String> locationnames=new ArrayList<String>();
				for(int i=0;i<locations.length;i++) {
					locationnames.add(locations[i]);
				}
				
				System.out.println("请依次输入对应的预计经停时间(yyyy-MM-dd HH:mm)(每个时间点按一次回车进行确认):");
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
			case "6":{//分配车厢
				System.out.println("请输入要分配车厢的车次名称:");
				String trainname=bf.readLine();
				System.out.println("请依次输入为车次分配的车厢编号:");
				String ID=bf.readLine();
				String[] IDs=ID.split(" ");
				List<String> carriageIDs=new ArrayList<String>();
				for(int i=0;i<IDs.length;i++) {
					carriageIDs.add(IDs[i]);
				}
				
				ts.allocateCarriage(trainname, carriageIDs);
				break;
			}
			case "7":{//列车启动
				System.out.println("请输入要启动的车次名称:");
				String trainname=bf.readLine();
				ts.startTrain(trainname);
				break;
			}
			case "8":{//列车停车
				System.out.println("请输入要停车的列车名称:");
				String trainname=bf.readLine();
				ts.endTrain(trainname);
				break;
			}
			case "9":{//Board可视化
				System.out.println("请输入要展示车次的站点名称:");
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
			case "10":{//查看列车状态
				System.out.println("请输入要查看状态的车次名称:");
				String trainname=bf.readLine();
				System.out.println("目前列车状态为:"+ts.getTrainState(trainname));
				break;
			}
			case "11":{//提前取消列车
				System.out.println("请输入提前取消的车次名称:");
				String trainname=bf.readLine();
				ts.cancelTrain(trainname);
				break;
			}
			case "12":{//检测车厢分配冲突
				PlanningEntryAPIs peAPI=new PlanningEntryAPIsFirstImpl();
				//PlanningEntryAPIs peAPI=new PlanningEntryAPIsSecondImpl();//策略模式，client可自由选择一种实现方式
				boolean flag=peAPI.checkResourceExclusiveConflict(ts.getTrains());
				if(flag) {
					System.out.println("当前列车集合中存在车厢分配冲突!");
				}
				else {
					System.out.println("当前列车集合不存在车厢分配冲突!");
				}
				break;
			}
			case "13":{//展示占用指定车厢的列车
				System.out.println("请输入要查看列车占用的车厢编号:");
				String CarriageId=bf.readLine();
				PlanningEntryAPIs peAPI=new PlanningEntryAPIsFirstImpl();
				List<TrainEntry<Carriage>> tEntries=ts.getTrainsofassignCarriage(CarriageId);
				if(tEntries==null) {
					System.out.println("该车厢未分配车次!");
					break;
				}
				else {
					System.out.println("占用该车厢的列车有:");
					for(TrainEntry<Carriage> te:tEntries) {
						System.out.println(te.getName()+" 目前列车状态为:"+te.getStateName());
					}
					System.out.println("是否需要查看指定车次的前序车次?(Y/N)");
					input=bf.readLine();
					if(input.equals("Y")) {
						System.out.println("请输入要查看的指定车次:");
						input=bf.readLine();
						TrainEntry<Carriage> tEntry=ts.getTrainbyName(input);
						while(tEntry==null) {
							System.out.println("查询失败!请从以上输入中选取!");
							input=bf.readLine();
							tEntry=ts.getTrainbyName(input);
						}
						Carriage carriage=ts.getCarriagebyID(CarriageId);
						try {
							PlanningEntry<Carriage> planningEntry=peAPI.findPreEntryPerResource(carriage, tEntry, ts.getTrains());
							if(planningEntry==null)
								System.out.println("该车次无前序车次!");
							else
								System.out.println("其前序车次为:"+planningEntry.getName());
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}
				break;
			}
			case "14":{
				System.out.println("请输入要阻塞的车次名称:");
				String trainname=bf.readLine();
				ts.blockTrain(trainname);
				break;
			}
			default:{
				System.out.println("输入错误,请重新选择功能!");
				break;
			}
			
			}
			
			if(!input.equals("15")) {//每完成一次功能,重新打印菜单供用户选取
				tsApp.menu();
				input=bf.readLine();
			}
		}
	}
}
