package MainApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import API.*;
import Board.CourseBoard;
import Location.Location;
import Resources.Teacher;
import Schedule.CourseSchedule;
import Timeslot.Timeslot;
import common.PlanningEntry;
import compositeinterface.CourseEntry;

public class CourseCalendarApp {
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
		System.out.println("**** 16.退出系统           ****");
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		CourseCalendarApp ccApp=new CourseCalendarApp();
		ccApp.menu();
		String input=bf.readLine();
		CourseSchedule cs=new CourseSchedule();
		while(!input.equals("16")) {
			switch (input) {
			case "1":{//增加教师
				System.out.println("请依次输入教师的身份证号、姓名、性别(M/F)、职称");
				input=bf.readLine();
				String[] information=input.split(" ");
				while(information.length!=4||(!information[2].equals("F")&&!information[2].equals("M"))) {
					if(information.length!=4)
						System.out.println("输入不完善!请重新输入!");//异常处理
					else
						System.out.println("性别输入有误!请重新输入!");//异常处理
					input=bf.readLine();
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
				cs.deleteTeacher(input);
				break;
			}
			case "3":{//增加教室
				System.out.println("请依次输入教室的经度、纬度、名称");
				input=bf.readLine();
				String[] information=input.split(" ");
				while(information.length!=3) {
					System.out.println("输入不完善!请重新输入!");//异常处理
					input=bf.readLine();
					information=input.split(" ");
					
				}
				Location location=new Location(information[0], information[1], information[2], false);
				cs.addLocation(location);//添加位置
				break;
			}
			case "4":{//删除教室
				System.out.println("请输入待删除教室的名称:");
				input=bf.readLine();
				cs.deleteLocation(input);
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
				cs.createCourse(coursename, location, timeslot);
				break;
			}
			case "6":{//为课程分配教师
				System.out.println("请输入要分配课程名称:");
				String coursename=bf.readLine();
				System.out.println("请依次输入所有要分配教师的身份证号(用空格分隔):");
				input=bf.readLine();
				String[] teacher=input.split(" ");
				List<String> teacherIDs=new ArrayList<String>();
				for(int i=0;i<teacher.length;i++) {
					teacherIDs.add(teacher[i]);
				}
				cs.allocateTeacher(coursename, teacherIDs);
				break;
			}
			case "7":{//课程更改位置
				System.out.println("请输入要更改教室的课程名称:");
				String coursename=bf.readLine();
				System.out.println("请输入要更改的新位置:");
				String location=bf.readLine();
				cs.changeLocation(coursename, location);
				break;
			}
			case "8":{//上课
				System.out.println("请输入要上课的课程名称:");
				String coursename=bf.readLine();
				cs.startCourse(coursename);
				break;
			}
			case "9":{//下课
				System.out.println("请输入要下课的课程名称:");
				String coursename=bf.readLine();
				cs.endCourse(coursename);
				break;
			}
			case "10":{//查看课程状态
				System.out.println("请输入要查看状态的课程名称:");
				String coursename=bf.readLine();
				System.out.println("目前课程状态为:"+cs.getCourseState(coursename));
				break;
			}
			case "11":{//取消课程
				System.out.println("请输入提前取消的课程名称:");
				String coursename=bf.readLine();
				cs.cancelCourse(coursename);
				break;
			}
			case "12":{//Board可视化
				System.out.println("请输入要展示课程的教室名称:");
				String locationname=bf.readLine();
				Location location=cs.getLocationbyName(locationname);
				Calendar calendar=Calendar.getInstance();
				CourseBoard cb=new CourseBoard(location, cs.getCourses(),calendar);
				try {
					cb.visualize();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				break;
			}
			case "13":{//检测位置冲突
				PlanningEntryAPIs peAPI=new PlanningEntryAPIsFirstImpl();
				//PlanningEntryAPIs peAPI=new PlanningEntryAPIsSecondImpl();//策略模式，client可自由选择一种实现方式
				boolean flag=peAPI.checkLocationConflict(cs.getCourses());
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
							e.printStackTrace();
						}
					}
				}
				break;
			}
			default:{
				System.out.println("输入错误,请重新选择功能!");
				break;
			}
			
			}
			
			if(!input.equals("16")) {//每完成一次功能,重新打印菜单供用户选取
				ccApp.menu();
				input=bf.readLine();
			}
		}
	}
}
	
