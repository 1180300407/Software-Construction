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
	private final static int timebound=1;//常数，标识按时间查看日志时所查询的时间范围
	
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
		System.out.println("**** 15.查询日志            ****");
		System.out.println("**** 16.退出系统	      ****");
	}
	
	public static void main(String[] args) throws IOException {
		//日志记录
		myLogger.setLevel(Level.INFO);
		//写入文件
		FileHandler handler=new FileHandler("src/LogFile/FlightScheduleAppLog.log");
		handler.setFormatter(new MyFormatter());//采用固定格式
		handler.setLevel(Level.INFO);
		myLogger.addHandler(handler);
		myLogger.setUseParentHandlers(false);
		
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		FlightScheduleApp fsApp=new FlightScheduleApp();
		fsApp.menu();
		String input=bf.readLine();
		FlightSchedule fs=new FlightSchedule();
		myLogger.info("进入FlightScheduleApp");
		if(input==null) {
			myLogger.severe("空输入异常");
			throw new IOException();	
		}
		while(!input.equals("16")) {
			switch (input) {
			case "1":{//增加飞机
				System.out.println("请依次输入飞机的编号、机型号、座位数、机龄");
				input=bf.readLine();
				myLogger.info("添加飞机");
				if(input==null) {
					myLogger.severe("空输入异常");
					throw new IOException();	
				}
				String[] information=input.split(" ");
				while(information.length!=4) {
					System.out.println("输入不完善!请重新输入!");//异常处理
					input=bf.readLine();
					if(input==null) {
						myLogger.severe("空输入异常");
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
			case "2":{//删除飞机
				System.out.println("请输入待删除飞机的编号:");
				input=bf.readLine();
				myLogger.info("删除飞机");
				try {
					fs.deletePlane(input);
				} catch (ResourceNotFoundException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"删除失败!");
				} catch (PlanEntryOccupyResourceException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"删除失败!");
				}
				break;
			}
			case "3":{//增加机场
				System.out.println("请依次输入机场的经度、纬度、名称");
				input=bf.readLine();
				myLogger.info("增加机场");
				if(input==null) {
					myLogger.severe("空输入异常");
					throw new IOException();	
				}
				String[] information=input.split(" ");
				while(information.length!=3) {
					System.out.println("输入不完善!请重新输入!");//异常处理
					input=bf.readLine();
					if(input==null) {
						myLogger.severe("空输入异常");
						throw new IOException();	
					}
					information=input.split(" ");
				}
				Location location=new Location(information[0], information[1], information[2], true);
				fs.addLocation(location);//添加位置
				break;
			}
			case "4":{//删除机场
				System.out.println("请输入待删除机场的名称:");
				input=bf.readLine();
				myLogger.info("删除机场");
				try {
					fs.deleteLocation(input);
				} catch (LocationNotFoundException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"删除失败!");
				} catch (PlanEntryOccupyLocationException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"删除失败!");
				}//根据名称删除
				break;
			}
			case "5":{//创建航班
				System.out.println("请输入要新建的航班名称:");
				String flightname=bf.readLine();
				System.out.println("请输入航班起飞的机场名称:");
				String startlocation=bf.readLine();
				System.out.println("请输入航班降落的机场名称:");
				String endlocation=bf.readLine();
				System.out.println("请输入航班预计起飞时间(yyyy-MM-dd HH:mm):");
				String start=bf.readLine();
				System.out.println("请输入航班预计降落时间(yyyy-MM-dd HH:mm):");
				String end=bf.readLine();
				Timeslot timeslot=new Timeslot(start, end);
				myLogger.info("创建航班");
				try {
					fs.createFlight(flightname, startlocation, endlocation, timeslot);
				} catch (InconsistentStartOrEndException e) {
					myLogger.severe(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"无法创建!");
				} catch (LocationNotFoundException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"无法创建!");
				} catch (SameLabelException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
				}
				break;
			}
			case "6":{//分配飞机
				System.out.println("请输入要分配的航班名称:");
				String flightname=bf.readLine();
				System.out.println("请输入为航班要分配的飞机编号:");
				String planeID=bf.readLine();
				System.out.println("请输入要分配飞机的航班的预计起飞时间:");
				String departuretime=bf.readLine();
				System.out.println("请输入要分配飞机的航班的预计降落时间:");
				String arrivaltime=bf.readLine();
				Timeslot timeslot=new Timeslot(departuretime, arrivaltime);
				List<Timeslot> timeslots=new ArrayList<Timeslot>();
				timeslots.add(timeslot);
				myLogger.info("为航班分配飞机");
				try {
					fs.allocatePlane(flightname, planeID,timeslots);
				} catch (ResourceNotFoundException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"请重新选择飞机进行分配!");
				} catch (ResourceConflictException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"请重新选择飞机进行分配!");
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
				}
				break;
			}
			case "7":{//航班起飞
				System.out.println("请输入要起飞的航班名称:");
				String flightname=bf.readLine();
				System.out.println("请输入要起飞的航班的预计起飞时间:");
				String departuretime=bf.readLine();
				System.out.println("请输入要起飞的航班的预计降落时间:");
				String arrivaltime=bf.readLine();
				Timeslot timeslot=new Timeslot(departuretime, arrivaltime);
				List<Timeslot> timeslots=new ArrayList<Timeslot>();
				timeslots.add(timeslot);
				myLogger.info("航班起飞");
				try {
					fs.departure(flightname,timeslots);
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
				}
				break;
			}
			case "8":{//航班降落
				System.out.println("请输入要降落的航班名称:");
				String flightname=bf.readLine();
				System.out.println("请输入希望降落的航班的预计起飞时间:");
				String departuretime=bf.readLine();
				System.out.println("请输入希望降落的航班的预计降落时间:");
				String arrivaltime=bf.readLine();
				Timeslot timeslot=new Timeslot(departuretime, arrivaltime);
				List<Timeslot> timeslots=new ArrayList<Timeslot>();
				timeslots.add(timeslot);
				myLogger.info("航班降落");
				try {
					fs.endFlight(flightname,timeslots);
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
				}
				break;
			}
			case "9":{//Board可视化
				System.out.println("请输入要展示航班的机场名称:");
				String locationname=bf.readLine();
				Location location=fs.getLocationbyName(locationname);
				Calendar calendar=Calendar.getInstance();
				FlightBoard fb=new FlightBoard(location, fs.getFlights(),calendar);
				myLogger.info("可视化展示信息板");
				try {
					fb.visualize();
				} catch (ParseException e) {
					myLogger.severe(e.getMessage());
					e.printStackTrace();
				}
				break;
			}
			case "10":{//查看航班状态
				System.out.println("请输入要查看状态的航班名称:");
				String flightname=bf.readLine();
				System.out.println("请输入希望查看状态的航班的预计起飞时间:");
				String departuretime=bf.readLine();
				System.out.println("请输入希望查看状态的航班的预计降落时间:");
				String arrivaltime=bf.readLine();
				Timeslot timeslot=new Timeslot(departuretime, arrivaltime);
				List<Timeslot> timeslots=new ArrayList<Timeslot>();
				timeslots.add(timeslot);
				String state;
				myLogger.info("查看航班状态");
				try {
					state = fs.getFlightState(flightname,timeslots);
					System.out.println("目前航班状态为:"+state);
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
				}
				break;
			}
			case "11":{//提前取消航班
				System.out.println("请输入提前取消的航班名称:");
				String flightname=bf.readLine();
				System.out.println("请输入希望取消的航班的预计起飞时间:");
				String departuretime=bf.readLine();
				System.out.println("请输入希望取消的航班的预计降落时间:");
				String arrivaltime=bf.readLine();
				Timeslot timeslot=new Timeslot(departuretime, arrivaltime);
				List<Timeslot> timeslots=new ArrayList<Timeslot>();
				timeslots.add(timeslot);
				myLogger.info("取消航班");
				try {
					fs.cancelFlight(flightname,timeslots);
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
				}
				break;
			}
			case "12":{//检测飞机分配冲突
				PlanningEntryAPIs peAPI=new PlanningEntryAPIsFirstImpl();
				//PlanningEntryAPIs peAPI=new PlanningEntryAPIsSecondImpl();//策略模式，client可自由选择一种实现方式
				boolean flag=peAPI.checkResourceExclusiveConflict(fs.getFlights());
				myLogger.info("检测飞机分配冲突");
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
				myLogger.info("查找占用指定飞机的航班");
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
					if(input==null) {
						myLogger.severe("空输入异常");
						throw new IOException();	
					}
					if(input.equals("Y")) {
						System.out.println("请输入要查看的指定航班:");
						input=bf.readLine();
						System.out.println("请输入要查看的航班的预计起飞时间:");
						String departuretime=bf.readLine();
						System.out.println("请输入要查看的航班的预计降落时间:");
						String arrivaltime=bf.readLine();
						Timeslot timeslot=new Timeslot(departuretime, arrivaltime);
						List<Timeslot> timeslots=new ArrayList<Timeslot>();
						timeslots.add(timeslot);
						FlightEntry<Plane> flightEntry=fs.getFlightbyName(input,timeslots);
						while(flightEntry==null) {
							System.out.println("查询失败!请从以上输入中选取!");
							System.out.println("请输入要查看的指定航班:");
							input=bf.readLine();
							System.out.println("请输入要查看的航班的预计起飞时间:");
							departuretime=bf.readLine();
							System.out.println("请输入要查看的航班的预计降落时间:");
							arrivaltime=bf.readLine();
							timeslot=new Timeslot(departuretime, arrivaltime);
							timeslots=new ArrayList<Timeslot>();
							timeslots.add(timeslot);
							flightEntry=fs.getFlightbyName(input,timeslots);
						}
						Plane plane=fs.getPlanebyID(planeId);
						myLogger.info("查看指定航班的前序航班");
						try {
							PlanningEntry<Plane> fEntry=peAPI.findPreEntryPerResource(plane,flightEntry, fs.getFlights());
							if(fEntry==null){
								System.out.println("该航班无前序航班!");
							}
							else
								System.out.println("其前序航班为:"+fEntry.getName());
						} catch (ParseException e) {
							myLogger.severe(e.getMessage());
							e.printStackTrace();
						}
					}
				}
				break;
			}
			case "14":{
				System.out.println("请输入要读取的文件路径:");
				input=bf.readLine();
				myLogger.info("读取文件创建航班");
				if(input==null) {
					myLogger.severe("空输入异常");
					throw new IOException();	
				}
				try {
					fs.createFlightByFile(input);
					System.out.println("航班创建成功!");
				} catch (FileNotFoundException e) {
					myLogger.severe(e.getMessage());
					System.out.println(e.getMessage()+"请重新选择文件进行读取!");
				} catch (LocationNotFoundException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"请重新选择文件进行读取!");
				} catch (IncorrectElementDependencyException e) {
					myLogger.severe(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"请重新选择文件进行读取!");
				} catch (SameLabelException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"请重新选择文件进行读取!");
				} catch (UnGrammaticalWordException e) {
					myLogger.severe(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"请重新选择文件进行读取!");
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"请重新选择文件进行读取!");
				} catch (ResourceNotFoundException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"请重新选择文件进行读取!");
				} catch (ResourceConflictException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"请重新选择文件进行读取!");
				}
				break;
			}
			case "15":{
				System.out.println("请输入查询方式(time/action/exception):");
				System.out.printf("time:查找%d小时内的所有日志记录\n", timebound);
				System.out.println("action:按类型查找所有符合的日志记录");
				System.out.println("exception:查看所有异常的日志记录");
				input=bf.readLine();
				myLogger.info("查看日志");
				if(input==null) {
					myLogger.severe("空输入异常");
					throw new IOException();	
				}
				try {
					queryLog(input);
				}  catch (ParseException e) {
					myLogger.severe("检测出日志格式异常");
					e.printStackTrace();
				}
				break;
			}
			default:{
				System.out.println("输入错误,请重新选择功能!");
				break;
			}
			
			}
			if(input==null) {
				myLogger.severe("空输入异常");
				throw new IOException();	
			}
			
			if(!input.equals("16")) {//每完成一次功能,重新打印菜单供用户选取
				fsApp.menu();
				input=bf.readLine();
			}
			
			if(input==null) {
				myLogger.severe("空输入异常");
				throw new IOException();	
			}
		}
		myLogger.info("退出FlightScheduleApp");
		handler.close();
	}
	
	private static void queryLog(String condition) throws IOException, ParseException {
		String time,classname,functionname,message,level;
		boolean timeflag=condition.equals("time");
		boolean actionflag=condition.equals("action");
		boolean exceptionflag=condition.equals("exception");
		if(!timeflag&&!actionflag&&!exceptionflag)//非法查询方式
			return;
		File file=new File("src/LogFile/FlightScheduleAppLog.log");
		if(!file.exists()||!file.isFile()) {
			throw new FileNotFoundException();
		}
		InputStreamReader read = new InputStreamReader(new FileInputStream(file));
		BufferedReader bufferedReader = new BufferedReader(read);
		
		String inputString=null;
		if(actionflag) {
			System.out.println("请输入您想要查看的操作类型(用功能菜单中每个操作前面的序号替代)");
			BufferedReader bf2 = new BufferedReader(new InputStreamReader(System.in));
			inputString=bf2.readLine();
		}
		
		try {	
			String line=null;
			SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",Locale.UK);
			Pattern pattern=Pattern.compile("<(.*?)> <(.*?)> <(.*?)> <(.*?)>: <(.*?)>");
			while((line=bufferedReader.readLine())!=null) {
				Matcher matcher=pattern.matcher(line);
				if(!matcher.find()) {//日志出错
					throw new IOException();
				}
				time=matcher.group(1);//正则表达式读取五部分
				classname=matcher.group(2);
				functionname=matcher.group(3);
				level=matcher.group(4);
				message=matcher.group(5);
				Date date2=dateFormat.parse(time);
				if(timeflag) {//按时间过滤
					Date date=new Date();
					long between = (date2.getTime()-date.getTime())/1000;//计算当前时间与该事件对应的时间的差
					between=between/(60*60*1000);//转化成小时
					if(between>timebound)//只显示timebound以内的事件
						continue;
					System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
				}
				else if(actionflag) {
					if(inputString==null) {
						myLogger.severe("空输入异常");
						throw new IOException();	
					}
					switch (inputString) {//根据操作类型对应查询
					case "1":{
						if(level.equals("INFO")&&message.equals("添加飞机"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "2":{
						if(level.equals("INFO")&&message.equals("删除飞机"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "3":{
						if(level.equals("INFO")&&message.equals("增加机场"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "4":{
						if(level.equals("INFO")&&message.equals("删除机场"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}	
					case "5":{
						if(level.equals("INFO")&&message.equals("创建航班"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "6":{
						if(level.equals("INFO")&&message.equals("为航班分配飞机"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "7":{
						if(level.equals("INFO")&&message.equals("航班起飞"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "8":{
						if(level.equals("INFO")&&message.equals("航班降落"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "9":{
						if(level.equals("INFO")&&message.equals("可视化展示信息板"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "10":{
						if(level.equals("INFO")&&message.equals("查看航班状态"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "11":{
						if(level.equals("INFO")&&message.equals("取消航班"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "12":{
						if(level.equals("INFO")&&message.equals("检测飞机分配冲突"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "13":{
						if(level.equals("INFO")&&(message.equals("查找占用指定飞机的航班")||message.equals("查看指定航班的前序航班")))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "14":{
						if(level.equals("INFO")&&message.equals("读取文件创建航班"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "15":{
						if(level.equals("INFO")&&message.equals("查看日志"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "16":{
						if(level.equals("INFO")&&message.equals("退出FlightScheduleApp"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					default:
						break;
					}
				}
				else {
					if(level.equals("WARNING")||level.equals("SEVERE"))//查询所有异常
						System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
				}
			}
		}finally {//清理资源
			bufferedReader.close();
		}
			
	}
}
