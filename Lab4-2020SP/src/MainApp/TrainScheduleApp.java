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
	private final static int timebound=1;//常数，标识按时间查看日志时所查询的时间范围
	
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
		System.out.println("**** 15.查询日志	      ****");
		System.out.println("**** 16.退出系统	      ****");
	}
	
	public static void main(String[] args) throws IOException {
		//日志记录
		myLogger.setLevel(Level.INFO);
		//写入文件
		FileHandler handler=new FileHandler("src/LogFile/TrainScheduleAppLog.log");
		handler.setFormatter(new MyFormatter());//采用固定格式
		handler.setLevel(Level.INFO);
		myLogger.addHandler(handler);
		myLogger.setUseParentHandlers(false);
		
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		TrainScheduleApp tsApp=new TrainScheduleApp();
		tsApp.menu();
		String input=bf.readLine();
		TrainSchedule ts=new TrainSchedule();
		myLogger.info("进入TrainScheduleApp");
		if(input==null) {
			myLogger.severe("空输入异常");
			throw new IOException();	
		}
		while(!input.equals("16")) {
			switch (input) {
			case "1":{//增加车厢
				System.out.println("请依次输入车厢的编号、类型、定员数、出厂年份");
				input=bf.readLine();
				myLogger.info("添加车厢");
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
				Carriage carriage=new Carriage(information[0], information[1], seats, information[3]);
				ts.addCarriage(carriage);
				break;
			}
			case "2":{//删除车厢
				System.out.println("请输入待删除车厢的编号:");
				input=bf.readLine();
				myLogger.info("删除车厢");
				try {
					ts.deleteCarriage(input);
				} catch (ResourceNotFoundException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"删除失败!");
				} catch (PlanEntryOccupyResourceException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"删除失败!");
				}
				break;
			}
			case "3":{//增加站点
				System.out.println("请依次输入高铁站点的经度、纬度、名称");
				input=bf.readLine();
				myLogger.info("增加高铁站点");
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
				ts.addLocation(location);//添加位置
				break;
			}
			case "4":{//删除站点
				System.out.println("请输入待删除高铁站点的名称:");
				input=bf.readLine();
				myLogger.info("删除高铁站点");
				try {
					ts.deleteLocation(input);
				} catch (LocationNotFoundException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"删除失败!");
				} catch (PlanEntryOccupyLocationException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"删除失败!");
				}//根据名称删除
				break;
			}
			case "5":{//创建高铁车次
				System.out.println("请输入要新建的高铁车次名称:");
				String trainname=bf.readLine();
				System.out.println("请依次输入车次经过的站点名称(每个位置间以#分隔):");
				input=bf.readLine();
				myLogger.info("创建高铁车次");
				if(input==null) {
					myLogger.severe("空输入异常");
					throw new IOException();	
				}
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
				
				try {
					ts.createTrain(trainname, locationnames, timeslots);
				} catch (LocationNotFoundException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"无法创建!");
				} catch (SameLabelException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"不能创建重名车次!");
				}
				break;
			}
			case "6":{//分配车厢
				System.out.println("请输入要分配车厢的车次名称:");
				String trainname=bf.readLine();
				System.out.println("请依次输入为车次分配的车厢编号:");
				String ID=bf.readLine();
				myLogger.info("为列车分配车厢");
				if(ID==null) {
					myLogger.severe("空输入异常");
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
					System.out.println(e.getErrorMessage()+"请重新选择车厢进行分配!");
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
				} catch (ResourceConflictException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"请重新选择车厢进行分配!");
				}
				break;
			}
			case "7":{//列车启动
				System.out.println("请输入要启动的车次名称:");
				String trainname=bf.readLine();
				myLogger.info("启动列车");
				try {
					ts.startTrain(trainname);
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
				}
				break;
			}
			case "8":{//列车停车
				System.out.println("请输入要停车的列车名称:");
				String trainname=bf.readLine();
				myLogger.info("列车停车");
				try {
					ts.endTrain(trainname);
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
				}
				break;
			}
			case "9":{//Board可视化
				System.out.println("请输入要展示车次的站点名称:");
				String locationname=bf.readLine();
				Location location=ts.getLocationbyName(locationname);
				Calendar calendar=Calendar.getInstance();
				TrainBoard tb=new TrainBoard(location, ts.getTrains(),calendar);
				myLogger.info("可视化展示信息板");
				try {
					tb.visualize();
				} catch (ParseException e) {
					myLogger.warning(e.getMessage());
					e.printStackTrace();
				}
				break;
			}
			case "10":{//查看列车状态
				System.out.println("请输入要查看状态的车次名称:");
				String trainname=bf.readLine();
				myLogger.info("查看列车状态");
				try {
					String state=ts.getTrainState(trainname);
					System.out.println("目前列车状态为:"+state);
				} catch (PlanEntryNotCreateException e) {
					System.out.println(e.getErrorMessage());
					myLogger.warning(e.getErrorMessage());
				}
				
				break;
			}
			case "11":{//提前取消列车
				System.out.println("请输入提前取消的车次名称:");
				String trainname=bf.readLine();
				myLogger.info("取消列车");
				try {
					ts.cancelTrain(trainname);
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
				}
				break;
			}
			case "12":{//检测车厢分配冲突
				PlanningEntryAPIs peAPI=new PlanningEntryAPIsFirstImpl();
				//PlanningEntryAPIs peAPI=new PlanningEntryAPIsSecondImpl();//策略模式，client可自由选择一种实现方式
				boolean flag=peAPI.checkResourceExclusiveConflict(ts.getTrains());
				myLogger.info("检测车厢分配冲突");
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
				myLogger.info("展示占用指定车厢的列车");
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
					if(input==null) {
						myLogger.severe("空输入异常");
						throw new IOException();	
					}
					if(input.equals("Y")) {
						myLogger.info("查看指定车次的前序车次");
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
							myLogger.severe(e.getMessage());
							e.printStackTrace();
						}
					}
				}
				break;
			}
			case "14":{
				System.out.println("请输入要阻塞的车次名称:");
				String trainname=bf.readLine();
				myLogger.info("阻塞指定车次");
				try {
					ts.blockTrain(trainname);
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
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
				tsApp.menu();
				input=bf.readLine();
			}
			
			if(input==null) {
				myLogger.severe("空输入异常");
				throw new IOException();	
			}
		}
		myLogger.info("退出TrainScheduleApp");
		handler.close();
	}
	
	private static void queryLog(String condition) throws IOException, ParseException {
		String time,classname,functionname,message,level;
		boolean timeflag=condition.equals("time");
		boolean actionflag=condition.equals("action");
		boolean exceptionflag=condition.equals("exception");
		if(!timeflag&&!actionflag&&!exceptionflag)//非法查询方式
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
			System.out.println("请输入您想要查看的操作类型(用功能菜单中每个操作前面的序号替代)");
			BufferedReader bf2 = new BufferedReader(new InputStreamReader(System.in));
			inputString=bf2.readLine();
		}
		
		try {
			String line=null;
			SimpleDateFormat dateFormat=new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",Locale.UK);
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
						if(level.equals("INFO")&&message.equals("添加车厢"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "2":{
						if(level.equals("INFO")&&message.equals("删除车厢"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "3":{
						if(level.equals("INFO")&&message.equals("增加高铁站点"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "4":{
						if(level.equals("INFO")&&message.equals("删除高铁站点"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}	
					case "5":{
						if(level.equals("INFO")&&message.equals("创建高铁车次"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "6":{
						if(level.equals("INFO")&&message.equals("为列车分配车厢"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "7":{
						if(level.equals("INFO")&&message.equals("启动列车"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "8":{
						if(level.equals("INFO")&&message.equals("列车停车"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "9":{
						if(level.equals("INFO")&&message.equals("可视化展示信息板"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "10":{
						if(level.equals("INFO")&&message.equals("查看列车状态"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "11":{
						if(level.equals("INFO")&&message.equals("取消列车"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "12":{
						if(level.equals("INFO")&&message.equals("检测车厢分配冲突"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "13":{
						if(level.equals("INFO")&&(message.equals("展示占用指定车厢的列车")||message.equals("查看指定车次的前序车次")))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "14":{
						if(level.equals("INFO")&&message.equals("阻塞指定车次"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "15":{
						if(level.equals("INFO")&&message.equals("查看日志"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "16":{
						if(level.equals("INFO")&&message.equals("退出TrainScheduleApp"))
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
