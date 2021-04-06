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
		System.out.println("-------航班管理系统-------");
		System.out.println("**** 1.增加管理的飞机  ****");
		System.out.println("**** 2.删除管理的飞机  ****");
		System.out.println("**** 3.增加管理的机场  ****");
		System.out.println("**** 4.删除管理的机场  ****");
		System.out.println("**** 5.增加一个新航班  ****");
		System.out.println("**** 6.为航班分配飞机  ****");
		System.out.println("**** 7.指定航班进行起飞****");
		System.out.println("**** 8.指定航班进行降落****");
		System.out.println("**** 9.展示机场航班表   ****");
		System.out.println("**** 10.查看航班状态     ****");
		System.out.println("**** 11.提前取消航班     ****");	
		System.out.println("**** 12.检测飞机分配冲突***");
		System.out.println("**** 13.展示飞机航班安排***");
		System.out.println("**** 14.通过文件创建航班***");
		System.out.println("**** 15.对航班进行阻塞  ****");
		System.out.println("**** 16.退出系统	      ****");
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		FlightScheduleApp fsApp=new FlightScheduleApp();
		fsApp.menu();
		String input=bf.readLine();
		FlightSchedule fs=new FlightSchedule();
		while(!input.equals("15")) {
			switch (input) {
			case "1":{//增加飞机
				System.out.println("请依次输入飞机的编号、机型号、座位数、机龄");
				input=bf.readLine();
				String[] information=input.split(" ");
				while(information.length!=4) {
					System.out.println("输入不完善!请重新输入!");//异常处理
					input=bf.readLine();
					information=input.split(" ");
				}
				int seats=Integer.parseInt(information[2]);
				double age=Double.parseDouble(information[3]);
				Plane plane=new Plane(information[0], information[1], seats, age);
				fs.addPlane(plane);
				break;
			}
			case "2":{//删除飞机
				System.out.println("请输入待删除飞机的编号:");
				input=bf.readLine();
				fs.deletePlane(input);
				break;
			}
			case "3":{//增加机场
				System.out.println("请依次输入机场的经度、纬度、名称");
				input=bf.readLine();
				String[] information=input.split(" ");
				while(information.length!=3) {
					System.out.println("输入不完善!请重新输入!");//异常处理
					input=bf.readLine();
					information=input.split(" ");
					
				}
				Location location=new Location(information[0], information[1], information[2], false);
				fs.addLocation(location);//添加位置
				break;
			}
			case "4":{//删除机场
				System.out.println("请输入待删除机场的名称:");
				input=bf.readLine();
				fs.deleteLocation(input);//根据名称删除
				break;
			}
			case "5":{//创建航班
				System.out.println("请输入要新建的航班名称:");
				String flightname=bf.readLine();
				System.out.println("请依次输入航班经过的机场名称(以空格间隔):");
				input=bf.readLine();
				String[] locations=input.split(" ");
				List<String> locationnames=new ArrayList<String>();
				for(int i=0;i<locations.length;i++) {
					locationnames.add(locations[i]);
				}
				List<Timeslot> timeslots=new ArrayList<Timeslot>();
				if(locations.length==2) {//没有中间站点
					System.out.println("请输入航班预计起飞时间(yyyy-MM-dd HH:mm):");
					String start=bf.readLine();
					System.out.println("请输入航班预计降落时间(yyyy-MM-dd HH:mm):");
					String end=bf.readLine();
					Timeslot timeslot=new Timeslot(start, end);
					timeslots.add(timeslot);
				}
				else if(locations.length==3) {//有一个中间站点
					System.out.println("请输入航班预计起飞时间(yyyy-MM-dd HH:mm):");
					String start=bf.readLine();
					System.out.println("请输入航班预计经停时间(yyyy-MM-dd HH:mm):");
					String middlestart=bf.readLine();
					System.out.println("请输入航班在中间点预计出发时间(yyyy-MM-dd HH:mm):");
					String middleend=bf.readLine();
					System.out.println("请输入航班预计降落时间(yyyy-MM-dd HH:mm):");
					String end=bf.readLine();
					Timeslot timeslot=new Timeslot(start, middlestart);
					Timeslot timeslot2=new Timeslot(middleend, end);
					timeslots.add(timeslot);
					timeslots.add(timeslot2);
				}
				else {//不符合要求
					System.out.println("航班位置个数不满足要求!创建失败!");
					break;
				}
				fs.createFlight(flightname, locationnames, timeslots);
				break;
			}
			case "6":{//分配飞机
				System.out.println("请输入要分配的航班名称:");
				String flightname=bf.readLine();
				System.out.println("请输入为航班要分配的飞机编号:");
				String planeID=bf.readLine();
				System.out.println("请输入要分配飞机的航班的预计起飞时间(不是经停点):");
				String departuretime=bf.readLine();
				System.out.println("请输入要分配飞机的航班的预计降落时间(不是经停点):");
				String arrivaltime=bf.readLine();
				Timeslot timeslot=new Timeslot(departuretime, arrivaltime);
				List<Timeslot> timeslots=new ArrayList<Timeslot>();
				timeslots.add(timeslot);
				fs.allocatePlane(flightname, planeID,timeslots);
				break;
			}
			case "7":{//航班起飞
				System.out.println("请输入要起飞的航班名称:");
				String flightname=bf.readLine();
				System.out.println("请输入要起飞的航班的预计起飞时间(不是经停点):");
				String departuretime=bf.readLine();
				System.out.println("请输入要起飞的航班的预计降落时间(不是经停点):");
				String arrivaltime=bf.readLine();
				Timeslot timeslot=new Timeslot(departuretime, arrivaltime);
				List<Timeslot> timeslots=new ArrayList<Timeslot>();
				timeslots.add(timeslot);
				fs.departure(flightname,timeslots);
				break;
			}
			case "8":{//航班降落
				System.out.println("请输入要降落的航班名称:");
				String flightname=bf.readLine();
				System.out.println("请输入要降落的航班的预计起飞时间(不是经停点):");
				String departuretime=bf.readLine();
				System.out.println("请输入要降落的航班的预计降落时间(不是经停点):");
				String arrivaltime=bf.readLine();
				Timeslot timeslot=new Timeslot(departuretime, arrivaltime);
				List<Timeslot> timeslots=new ArrayList<Timeslot>();
				timeslots.add(timeslot);
				fs.endFlight(flightname,timeslots);
				break;
			}
			case "9":{//Board可视化
				System.out.println("请输入要展示航班的机场名称:");
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
			case "10":{//查看航班状态
				System.out.println("请输入要查看状态的航班名称:");
				String flightname=bf.readLine();
				System.out.println("请输入要查看状态的航班的预计起飞时间(不是经停点):");
				String departuretime=bf.readLine();
				System.out.println("请输入要查看状态的航班的预计降落时间(不是经停点):");
				String arrivaltime=bf.readLine();
				Timeslot timeslot=new Timeslot(departuretime, arrivaltime);
				List<Timeslot> timeslots=new ArrayList<Timeslot>();
				timeslots.add(timeslot);
				String state=fs.getFlightState(flightname,timeslots);
				if(state==null) {
					System.out.println("该航班尚未创建!");
					break;
				}
				System.out.println("目前航班状态为:"+state);
				break;
			}
			case "11":{//提前取消航班
				System.out.println("请输入提前取消的航班名称:");
				String flightname=bf.readLine();
				System.out.println("请输入要取消的航班的预计起飞时间(不是经停点):");
				String departuretime=bf.readLine();
				System.out.println("请输入要取消的航班的预计降落时间(不是经停点):");
				String arrivaltime=bf.readLine();
				Timeslot timeslot=new Timeslot(departuretime, arrivaltime);
				List<Timeslot> timeslots=new ArrayList<Timeslot>();
				timeslots.add(timeslot);
				fs.cancelFlight(flightname,timeslots);
				break;
			}
			case "12":{//检测飞机分配冲突
				PlanningEntryAPIs peAPI=new PlanningEntryAPIsFirstImpl();
				//PlanningEntryAPIs peAPI=new PlanningEntryAPIsSecondImpl();//策略模式，client可自由选择一种实现方式
				boolean flag=peAPI.checkResourceExclusiveConflict(fs.getFlights());
				if(flag) {
					System.out.println("当前航班集合中存在飞机分配冲突!");
				}
				else {
					System.out.println("当前航班集合不存在飞机分配冲突!");
				}
				break;
			}
			case "13":{//展示占用指定飞机的航班
				System.out.println("请输入要查看航班占用的飞机编号:");
				String planeId=bf.readLine();
				PlanningEntryAPIs peAPI=new PlanningEntryAPIsFirstImpl();
				List<FlightEntry<Plane>> fEntries=fs.getFlightssofassignPlane(planeId);
				if(fEntries==null) {
					System.out.println("该飞机未分配航班!");
					break;
				}
				else {
					System.out.println("占用该飞机的航班有:");
					for(FlightEntry<Plane> fe:fEntries) {
						System.out.println(fe.getName()+" 目前航班状态为:"+fe.getStateName());
					}
					System.out.println("是否需要查看指定航班的前序航班?(Y/N)");
					input=bf.readLine();
					if(input.equals("Y")) {
						System.out.println("请输入要查看的指定航班:");
						input=bf.readLine();
						System.out.println("请输入要查看的航班的预计起飞时间(不是经停点):");
						String departuretime=bf.readLine();
						System.out.println("请输入要查看的航班的预计降落时间(不是经停点):");
						String arrivaltime=bf.readLine();
						Timeslot timeslot=new Timeslot(departuretime, arrivaltime);
						List<Timeslot> timeslots=new ArrayList<Timeslot>();
						timeslots.add(timeslot);
						FlightEntry<Plane> flightEntry=fs.getFlightbyName(input,timeslots);
						while(flightEntry==null) {
							System.out.println("查询失败!请从以上输入中选取!");
							System.out.println("请输入要查看的指定航班:");
							input=bf.readLine();
							System.out.println("请输入要查看的航班的预计起飞时间(不是经停点):");
							departuretime=bf.readLine();
							System.out.println("请输入要查看的航班的预计降落时间(不是经停点):");
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
								System.out.println("该航班无前序航班!");
							}
							else
								System.out.println("其前序航班为:"+fEntry.getName());
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				}
				break;
			}
			case "14":{
				System.out.println("请输入要读取的文件路径:");
				input=bf.readLine();
				boolean flag=fs.createFlightByFile(input);
				if(flag) {
					System.out.println("创建成功!");
				}
				else {
					System.out.println("请重新输入:");
				}
				break;
			}
			case "15":{
				System.out.println("请输入要阻塞的航班名称:");
				input=bf.readLine();
				System.out.println("请输入要阻塞的航班的预计起飞时间(不是经停点):");
				String departuretime=bf.readLine();
				System.out.println("请输入要阻塞的航班的预计降落时间(不是经停点):");
				String arrivaltime=bf.readLine();
				Timeslot timeslot=new Timeslot(departuretime, arrivaltime);
				List<Timeslot> timeslots=new ArrayList<Timeslot>();
				timeslots.add(timeslot);
				fs.blockFlight(input,timeslots);
			}
			default:{
				System.out.println("输入错误,请重新选择功能!");
				break;
			}
			
			}
			
			if(!input.equals("16")) {//每完成一次功能,重新打印菜单供用户选取
				fsApp.menu();
				input=bf.readLine();
			}
		}
	}
}
