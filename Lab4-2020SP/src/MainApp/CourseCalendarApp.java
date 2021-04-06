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
	private final static int timebound=1;//常数，标识按时间查看日志时所查询的时间范围
	
	public void menu() {//功能目录
		System.out.println("-------课程管理系统-------");
		System.out.println("**** 1.增加管理的教师  ****");
		System.out.println("**** 2.删除管理的教师  ****");
		System.out.println("**** 3.增加管理的教室  ****");
		System.out.println("**** 4.删除管理的教室  ****");
		System.out.println("**** 5.增加一门新课程  ****");
		System.out.println("**** 6.为课程分配教师  ****");
		System.out.println("**** 7.更改课程教室      ****");
		System.out.println("**** 8.教师指定课程上课****");
		System.out.println("**** 9.教师指定课程下课****");
		System.out.println("**** 10.查看课程状态    ****");
		System.out.println("**** 11.提前取消课程    ****");
		System.out.println("**** 12.展示教室课程表 ****");
		System.out.println("**** 13.检测教室占用冲突***");
		System.out.println("**** 14.检测教师分配冲突***");
		System.out.println("**** 15.展示教师课程安排***");
		System.out.println("**** 16.查询日志           ****");
		System.out.println("**** 17.退出系统           ****");
	}
	
	public static void main(String[] args) throws IOException {
		//日志记录
		myLogger.setLevel(Level.INFO);
		//写入文件
		FileHandler handler=new FileHandler("src/LogFile/CourseCalendarAppLog.log");
		handler.setFormatter(new MyFormatter());//采用固定格式
		handler.setLevel(Level.INFO);
		myLogger.addHandler(handler);
		myLogger.setUseParentHandlers(false);
		
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		CourseCalendarApp ccApp=new CourseCalendarApp();
		ccApp.menu();
		String input=bf.readLine();
		CourseSchedule cs=new CourseSchedule();
		myLogger.info("进入CourseCalendarApp");
		if(input==null) {
			myLogger.severe("空输入异常");
			throw new IOException();	
		}
		while(!input.equals("17")) {
			switch (input) {
			case "1":{//增加教师
				System.out.println("请依次输入教师的身份证号、姓名、性别(M/F)、职称");
				input=bf.readLine();
				myLogger.info("添加教师");
				if(input==null) {
					myLogger.severe("空输入异常");
					throw new IOException();	
				}
				String[] information=input.split(" ");
				while(information.length!=4||(!information[2].equals("F")&&!information[2].equals("M"))) {
					if(information.length!=4)
						System.out.println("输入不完善!请重新输入!");//异常处理
					else
						System.out.println("性别输入有误!请重新输入!");//异常处理
					input=bf.readLine();
					if(input==null) {
						myLogger.severe("空输入异常");
						throw new IOException();	
					}
					information=input.split(" ");
				}
				if(information[2].equals("M")) {//新增教师
					Teacher teacher=new Teacher(information[0],information[1] , true, information[3]);
					cs.addTeacher(teacher);
				}
				else {
					Teacher teacher=new Teacher(information[0],information[1] , false, information[3]);
					cs.addTeacher(teacher);
				}	
				break;
			}
			case "2":{//删除教师
				System.out.println("请输入待删除教师的身份证号:");
				input=bf.readLine();
				myLogger.info("删除教师");
				try {
					cs.deleteTeacher(input);
				} catch (ResourceNotFoundException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"删除失败!");
				} catch (PlanEntryOccupyResourceException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"删除失败!");
				}
				break;
			}
			case "3":{//增加教室
				System.out.println("请依次输入教室的经度、纬度、名称");
				input=bf.readLine();
				myLogger.info("增加教室");
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
				Location location=new Location(information[0], information[1], information[2], false);
				cs.addLocation(location);//添加位置
				break;
			}
			case "4":{//删除教室
				System.out.println("请输入待删除教室的名称:");
				input=bf.readLine();
				myLogger.info("删除教室");
				try {
					cs.deleteLocation(input);
				} catch (LocationNotFoundException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"删除失败!");
				} catch (PlanEntryOccupyLocationException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"删除失败!");
				}
				break;
			}
			case "5":{//创建课程
				System.out.println("请输入要创建的课程名称:");
				String coursename=bf.readLine();
				System.out.println("请输入课程要占用的教室名称:");
				String location=bf.readLine();
				System.out.println("请输入课程预计上课时间(yyyy-MM-dd HH:mm):");
				String start=bf.readLine();
				System.out.println("请输入课程预计下课时间(yyyy-MM-dd HH:mm):");
				String end=bf.readLine();
				Timeslot timeslot=new Timeslot(start, end);
				myLogger.info("创建课程");
				try {
					cs.createCourse(coursename, location, timeslot);
				} catch (LocationNotFoundException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"无法创建!");
				} catch (SameLabelException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"不能创建重名课程!");
				} catch (LocationConflictException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"请重新选定地点!");
				}
				break;
			}
			case "6":{//为课程分配教师
				System.out.println("请输入要分配课程名称:");
				String coursename=bf.readLine();
				System.out.println("请输入为课程要分配教师身份证号:");
				String teacher=bf.readLine();
				myLogger.info("为课程分配教师");
				try {
					cs.allocateTeacher(coursename, teacher);
				} catch (ResourceNotFoundException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"请重新选择教师进行分配!");
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
				} catch (ResourceConflictException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"请重新选择教师进行分配!");
				}
				break;
			}
			case "7":{//课程更改位置
				System.out.println("请输入要更改教室的课程名称:");
				String coursename=bf.readLine();
				System.out.println("请输入要更改的新位置:");
				String location=bf.readLine();
				myLogger.info("为课程更改教室");
				try {
					cs.changeLocation(coursename, location);
				} catch (LocationNotFoundException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"请重新选择教室!");
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"请重新选择课程!");
				} catch (PlanEntryStateNotMatchException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage()+"该课程已无需更改位置!");
				}
				break;
			}
			case "8":{//上课
				System.out.println("请输入要上课的课程名称:");
				String coursename=bf.readLine();
				myLogger.info("上课");
				try {
					cs.startCourse(coursename);
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
				}
				break;
			}
			case "9":{//下课
				System.out.println("请输入要下课的课程名称:");
				String coursename=bf.readLine();
				myLogger.info("下课");
				try {
					cs.endCourse(coursename);
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
				}
				break;
			}
			case "10":{//查看课程状态
				System.out.println("请输入要查看状态的课程名称:");
				String coursename=bf.readLine();
				myLogger.info("查看课程状态");
				try {
					String state=cs.getCourseState(coursename);
					System.out.println("目前课程状态为:"+state);
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
				}
				break;
			}
			case "11":{//取消课程
				System.out.println("请输入提前取消的课程名称:");
				String coursename=bf.readLine();
				myLogger.info("取消课程");
				try {
					cs.cancelCourse(coursename);
				} catch (PlanEntryNotCreateException e) {
					myLogger.warning(e.getErrorMessage());
					System.out.println(e.getErrorMessage());
				}
				break;
			}
			case "12":{//Board可视化
				System.out.println("请输入要展示课程的教室名称:");
				String locationname=bf.readLine();
				Location location=cs.getLocationbyName(locationname);
				Calendar calendar=Calendar.getInstance();
				CourseBoard cb=new CourseBoard(location, cs.getCourses(),calendar);
				myLogger.info("可视化展示信息板");
				try {
					cb.visualize();
				} catch (ParseException e) {
					myLogger.severe(e.getMessage());
					e.printStackTrace();
				}
				break;
			}
			case "13":{//检测位置冲突
				PlanningEntryAPIs peAPI=new PlanningEntryAPIsFirstImpl();
				//PlanningEntryAPIs peAPI=new PlanningEntryAPIsSecondImpl();//策略模式，client可自由选择一种实现方式
				boolean flag=peAPI.checkLocationConflict(cs.getCourses());
				myLogger.info("检测位置冲突");
				if(flag) {
					System.out.println("当前计划项存在位置冲突!");
				}
				else {
					System.out.println("当前计划项不存在位置冲突!");
				}
				break;
			}
			case "14":{//检测教师分配冲突
				PlanningEntryAPIs peAPI=new PlanningEntryAPIsFirstImpl();
				//PlanningEntryAPIs peAPI=new PlanningEntryAPIsSecondImpl();//策略模式，client可自由选择一种实现方式
				boolean flag=peAPI.checkResourceExclusiveConflict(cs.getCourses());
				myLogger.info("检测教师分配冲突");
				if(flag) {
					System.out.println("当前课程集合中存在教师分配冲突!");
				}
				else {
					System.out.println("当前课程集合中不存在教师分配冲突!");
				}
				break;
			}
			case "15":{//查看教师课程安排
				System.out.println("请输入要查看课程安排的教师身份证号:");
				String teacherId=bf.readLine();
				PlanningEntryAPIs peAPI=new PlanningEntryAPIsFirstImpl();
				List<CourseEntry<Teacher>> ces=cs.getCoursesofassignTeacher(teacherId);
				myLogger.info("查看教师课程安排");
				if(ces==null) {
					System.out.println("该教师未分配课程!");
					break;
				}
				else {
					System.out.println("该教师分配的课程有:");
					for(CourseEntry<Teacher> ce:ces) {
						System.out.println(ce.getName()+" 目前课程状态为:"+ce.getStateName());
					}
					System.out.println("是否需要查看指定课程的前序课程?(Y/N)");
					input=bf.readLine();
					myLogger.info("查看指定课程的前序课程");
					if(input==null) {
						myLogger.severe("空输入异常");
						throw new IOException();	
					}
					if(input.equals("Y")) {
						System.out.println("请输入要查看的指定课程:");
						input=bf.readLine();
						CourseEntry<Teacher> courseEntry=cs.getCoursebyName(input);
						while(courseEntry==null) {
							System.out.println("查询失败!请从以上输入中选取!");
							input=bf.readLine();
							courseEntry=cs.getCoursebyName(input);
						}
						Teacher teacher=cs.getTeacherbyID(teacherId);
						try {
							PlanningEntry<Teacher> cEntry=peAPI.findPreEntryPerResource(teacher, courseEntry, cs.getCourses());
							if(cEntry==null)
								System.out.println("该课程无前序课程!");
							else
								System.out.println("其前序课程为:"+cEntry.getName());
						} catch (ParseException e) {
							myLogger.severe(e.getMessage());
							e.printStackTrace();
						}
					}
				}
				break;
			}
			case "16":{//查询日志
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
			if(!input.equals("17")) {//每完成一次功能,重新打印菜单供用户选取
				ccApp.menu();
				input=bf.readLine();
			}
			if(input==null) {
				myLogger.severe("空输入异常");
				throw new IOException();	
			}
		}
		myLogger.info("退出CourseCalendarApp");
		handler.close();
	}
	
	private static void queryLog(String condition) throws IOException, ParseException {
		String time,classname,functionname,message,level;
		boolean timeflag=condition.equals("time");
		boolean actionflag=condition.equals("action");
		boolean exceptionflag=condition.equals("exception");
		if(!timeflag&&!actionflag&&!exceptionflag)//非法查询方式
			return;
		File file=new File("src/LogFile/CourseCalendarAppLog.log");
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
						if(level.equals("INFO")&&message.equals("添加教师"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "2":{
						if(level.equals("INFO")&&message.equals("删除教师"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "3":{
						if(level.equals("INFO")&&message.equals("增加教室"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "4":{
						if(level.equals("INFO")&&message.equals("删除教室"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}	
					case "5":{
						if(level.equals("INFO")&&message.equals("创建课程"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "6":{
						if(level.equals("INFO")&&message.equals("为课程分配教师"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "7":{
						if(level.equals("INFO")&&message.equals("为课程更改教室"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "8":{
						if(level.equals("INFO")&&message.equals("上课"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "9":{
						if(level.equals("INFO")&&message.equals("下课"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "10":{
						if(level.equals("INFO")&&message.equals("查看课程状态"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "11":{
						if(level.equals("INFO")&&message.equals("取消课程"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "12":{
						if(level.equals("INFO")&&message.equals("可视化展示信息板"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "13":{
						if(level.equals("INFO")&&message.equals("检测位置冲突"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "14":{
						if(level.equals("INFO")&&message.equals("检测教师分配冲突"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "15":{
						if(level.equals("INFO")&&(message.equals("查看教师课程安排")||(message.equals("查看指定课程的前序课程"))))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "16":{
						if(level.equals("INFO")&&message.equals("查看日志"))
							System.out.println("时间:"+date2+" 类名称:"+classname+" 方法名:"+functionname+" 日志级别:"+level+" 相关信息:"+message);
						break;
					}
					case "17":{
						if(level.equals("INFO")&&message.equals("退出CourseCalendarApp"))
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
	
