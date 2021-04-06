package Schedule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import API.PlanningEntryAPIs;
import API.PlanningEntryAPIsFirstImpl;
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
import Timeslot.Timeslot;
import compositeinterface.CourseEntry;
import compositeinterface.CoursePlanningEntry;

/**
 * 课表管理，可对多个教师、教室、课程进行管理，可变类
 * @author 123
 *
 */

public class CourseSchedule {
	private List<CourseEntry<Teacher>> courses=new ArrayList<>();
	private List<Teacher> teachers=new ArrayList<Teacher>();
	private List<Location> locations=new ArrayList<Location>();
	private static Logger myLogger=Logger.getLogger("CourseScheduleLog");
	//Abstraction function:
	//	AF(courses,teachers,locations)=一个对课程courses，所有教师teachers，教室locations进行管理的系统
	//Representation invariant:
	//	管理的课程、教师、教室中都不应含有重复元素
	//Safety from rep exposure:
	//	成员变量全是private的，对外返回时全部转为不可变类型，不存在表示泄露
	
	private void checkRep() {
		Set<CourseEntry<Teacher>> courseSet=new HashSet<>(courses);
		Set<Teacher> teacherSet=new HashSet<Teacher>(teachers);
		Set<Location> locationSet=new HashSet<Location>(locations);
		assert locationSet.size()==locations.size();
		assert teacherSet.size()==teachers.size();
		assert courseSet.size()==courses.size();
		//日志记录
		myLogger.setLevel(Level.INFO);
		myLogger.setUseParentHandlers(false);
		//写入文件
		FileHandler handler;
		try {
			handler = new FileHandler("src/LogFile/CourseScheduleLog.log");
			handler.setFormatter(new MyFormatter());//采用固定格式
			handler.setLevel(Level.INFO);
			myLogger.addHandler(handler);
			myLogger.info("进行不变量检查");
			handler.close();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 增加一个可供管理的教师，若教师已存在不会重复添加
	 * @param teacher 待添加的教师
	 */
	public void addTeacher(Teacher teacher) {
		if(!teachers.contains(teacher))
			teachers.add(teacher); //Teacher为不可变类，不存在表示泄露
		checkRep();
	}
	
	/**
	 * 删除具有指定ID的教师，删除的教师应该已经纳入管理之中,且未分配课程
	 * @param ID 待删除的教师的ID
	 * @throws ResourceNotFoundException 要删除的教师不存在 
	 * @throws PlanEntryOccupyResourceException 要删除的教师已分配课程
	 */
	public void deleteTeacher(String ID) throws ResourceNotFoundException, PlanEntryOccupyResourceException {
		int index=-1;
		for(Teacher teacher:teachers) {
			if(teacher.getId().equals(ID)) {
				index=teachers.indexOf(teacher);
				break;
			}
		}
		if(index==-1) {
			throw new ResourceNotFoundException(ID);
		}
		
		boolean flag=false;
		CourseEntry<Teacher> courseEntry=null;
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getResource().contains(teachers.get(index))&&(ce.getStateName().equals("Allocated")||ce.getStateName().equals("Running"))) {
				flag=true;
				courseEntry=ce;
				break;
			}
		}
		if(!flag) {
			teachers.remove(index);
			return;
		}
		else
		 throw new PlanEntryOccupyResourceException(courseEntry.getName());
	}
	
	/**
	 * 增加一个可供管理的教室，若教室已存在不会重复添加
	 * @param location 待添加的教室
	 */
	public void addLocation(Location location) {
		if(!locations.contains(location))
			locations.add(location);
		checkRep();
	}
	
	/**
	 * 删除具有指定名称的教室，教室应该已创建,且未被分配给任何课程
	 * @param locationname 待删除的教室名称
	 * @throws LocationNotFoundException 要删除的教室不存在
	 * @throws PlanEntryOccupyLocationException 教室被占用
	 */
	public void deleteLocation(String locationname) throws LocationNotFoundException, PlanEntryOccupyLocationException {
		int index=-1;
		for(Location location:locations) {
			if(location.getName().equals(locationname)) {
				index=locations.indexOf(location);
				break;
			}
		}
		if(index==-1) {
			throw new LocationNotFoundException(locationname);
		}
		
		boolean flag=false;
		CourseEntry<Teacher> courseEntry=null;
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getLocation().contains(locations.get(index))&&!ce.getStateName().equals("Cancelled")&&!ce.getStateName().equals("Ended")) {
				flag=true;
				courseEntry=ce;
				break;
			}
		}
		if(!flag) {
			locations.remove(index);
			return;
		}
		else 
		 throw new PlanEntryOccupyLocationException(courseEntry.getName());
	}
	
	/**
	 * @return 得到所有管理下的课程
	 */
	public List<CourseEntry<Teacher>> getCourses() {
		return Collections.unmodifiableList(courses);
	}

	/**
	 * @return 得到所有管理下的教师
	 */
	public List<Teacher> getTeachers() {
		return Collections.unmodifiableList(teachers);
	}

	/**
	 * @return 得到所有管理下的教室
	 */
	public List<Location> getLocations() {
		return Collections.unmodifiableList(locations);
	}

	/**
	 * 增加一个课程对其进行管理，不允许重名课程出现
	 * @param name 新增课程名
	 * @param location 课程占用的教室名称
	 * @param timeslot 课程的上课时间和下课时间构成的时间对
	 * @return 如果创建成功返回true，否则返回false
	 * @throws LocationNotFoundException 教室未创建
	 * @throws SameLabelException 重名课程
	 * @throws LocationConflictException 创建课程会产生位置冲突
	 */
	public boolean createCourse(String name,String location,Timeslot timeslot) throws LocationNotFoundException, SameLabelException, LocationConflictException {
		int flag=-1;
		for(Location location2:locations) {//得到该名称位置
			if(location2.getName().equals(location)) {
				flag=locations.indexOf(location2);
				break;
			}
		}
		if(flag==-1) {
			throw new LocationNotFoundException(location);
		}
		CourseEntry<Teacher> newcourse=CoursePlanningEntry.CreateCourse(name);
		List<Timeslot> onetimeslot=new ArrayList<Timeslot>();
		onetimeslot.add(timeslot);
		newcourse.setTime(onetimeslot);
		List<Location> oneLocation=new ArrayList<Location>();
		oneLocation.add(locations.get(flag));
		newcourse.setLocation(oneLocation);
		for(CourseEntry<Teacher> cpe:courses) {
			if(cpe.getName().contentEquals(name)) {
				throw new SameLabelException(name);
			}
		}
		CourseEntry<Teacher> courseEntry=new CourseEntry<Teacher>("");
		courseEntry.setLocation(oneLocation);
		List<CourseEntry<Teacher>> courseEntries=new ArrayList<>();
		courseEntries.add(courseEntry);
		courseEntries.addAll(courses);
		PlanningEntryAPIs api=new PlanningEntryAPIsFirstImpl();
		boolean flag2=api.checkLocationConflict(courseEntries);
		if(flag2) {
			throw new LocationConflictException();
		}
		courses.add(newcourse);
		checkRep();
		return true;
	}
	
	/**
	 * 为课程分配教师，教师应包含在管理的所有教师之中
	 * @param coursename 指定的课程名称
	 * @param teacher 分配的教师的身份证号
	 * @throws ResourceNotFoundException 教师还未纳入管理
	 * @throws PlanEntryNotCreateException 课程尚未创建
	 * @throws ResourceConflictException 分配教师会产生冲突
	 */
	public void allocateTeacher(String coursename,String teacherID) throws ResourceNotFoundException, PlanEntryNotCreateException, ResourceConflictException {
		int index=-1;
		for(Teacher teacher:teachers) {
			if(teacher.getId().equals(teacherID))
			{
				index=teachers.indexOf(teacher);
			}
		}
		if(index==-1) {
			throw new ResourceNotFoundException(teacherID);
		}
		Teacher teacher=teachers.get(index);
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getName().equals(coursename)) {
				List<Teacher> oneTeacher=new ArrayList<Teacher>();
				oneTeacher.add(teacher);
				CourseEntry<Teacher> courseEntry=new CourseEntry<Teacher>("null");//新建一个课程，为其分配与搜索得到的课程一样的属性，观察是否会产生冲突
				courseEntry.setLocation(ce.getLocation());
				courseEntry.setTime(ce.getTime());
				courseEntry.allocateResource(oneTeacher);
				List<CourseEntry<Teacher>> courseEntries=new ArrayList<>();//不加入courses中，只是一个替代品，检测是否会发生冲突，避免真正分配带来的冲突
				courseEntries.add(courseEntry);
				courseEntries.addAll(courses);
				PlanningEntryAPIs api=new PlanningEntryAPIsFirstImpl();
				boolean flag2=api.checkResourceExclusiveConflict(courseEntries);
				if(flag2) {
					throw new ResourceConflictException(teacherID);
				}
				ce.allocateResource(oneTeacher);
				return;
			}
		}
		
		throw new PlanEntryNotCreateException(coursename);
	}
	
	/**
	 * 指定某一课程上课，课程应存在
	 * @param coursename 指定课程名
	 * @throws PlanEntryNotCreateException 课程尚未创建
	 */
	public void startCourse(String coursename) throws PlanEntryNotCreateException {
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getName().equals(coursename)) {
				ce.start();
				return;
			}
		}
		
		throw new PlanEntryNotCreateException(coursename);
	}
	
	/**
	 * 课前取消课程，课程应存在
	 * @param coursename 指定课程名称
	 * @throws PlanEntryNotCreateException 课程尚未创建
	 */
	public void cancelCourse(String coursename) throws PlanEntryNotCreateException {
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getName().equals(coursename)) {
				ce.cancel();
				return;
			}
		}
		
		throw new PlanEntryNotCreateException(coursename);
	}
	
	/**
	 * 输出目前课程的状态，课程应已经创建过
	 * @param coursename 要查看的课程
	 * @return 课程当前状态
	 * @throws PlanEntryNotCreateException 课程未创建
	 */
	public String getCourseState(String coursename) throws PlanEntryNotCreateException {
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getName().equals(coursename)) { 
				return ce.getStateName();
			}
		}
		
		throw new PlanEntryNotCreateException(coursename);
	}
	
	/**
	 * 指定某课程下课，课程应存在
	 * @param coursename 指定课程名
	 * @throws PlanEntryNotCreateException 课程尚未创建
	 */
	public void endCourse(String coursename) throws PlanEntryNotCreateException {
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getName().equals(coursename)) {
				ce.end();
				return;
			}
		}
		
		throw new PlanEntryNotCreateException(coursename);
	}
	
	/**
	 * 更换课程地点,地点应该是已经被纳入管理的，课程应该已经创建，未结束
	 * @param coursename 指定课程名称
	 * @param newlocation 新的教室
	 * @throws LocationNotFoundException 位置尚未创建
	 * @throws PlanEntryNotCreateException 课程尚未创建
	 * @throws PlanEntryStateNotMatchException 课程已取消或结束，无法更改位置s
	 */
	public void changeLocation(String coursename,String newlocation) throws LocationNotFoundException, PlanEntryNotCreateException, PlanEntryStateNotMatchException {
		int flag=-1;
		for(Location location2:locations) {//得到该名称位置
			if(location2.getName().equals(newlocation)) {
				flag=locations.indexOf(location2);
				break;
			}
		}
		if(flag==-1) {
			throw new LocationNotFoundException(newlocation);
		}
		Location location=locations.get(flag);
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getName().equals(coursename)) {
				if(!ce.getStateName().equals("Ended")&&!ce.getStateName().equals("Cancelled")) {//上课前、上课中都可更换教室
					List<Location> newLocation=new ArrayList<Location>();
					newLocation.add(location);
					ce.setLocation(newLocation);
					return;
				}
				throw new PlanEntryStateNotMatchException(coursename);
			}
		}
		throw new PlanEntryNotCreateException(coursename);
	}
	
	/**
	 * 获得指定教师教授的所有课程
	 * @param teacherID 指定教师的ID 
	 * @return 如果有指定教师教授的课程,返回这样的所有课程；否则返回null
	 */
	public List<CourseEntry<Teacher>> getCoursesofassignTeacher(String teacherID){
		int index=-1;
		for(Teacher teacher:teachers) {//找到指定教师
			if(teacher.getId().equals(teacherID)) {
				index=teachers.indexOf(teacher);
			}
		}
		if(index==-1) {
			System.out.println("目前管理的教师中不存在该ID的教师!");
			return null;
		}
		Teacher assignteacher=teachers.get(index);
		List<CourseEntry<Teacher>> ces=new ArrayList<CourseEntry<Teacher>>();
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getResource().contains(assignteacher))
				ces.add(ce);
		}
		if(ces.isEmpty())
			return null;
		return ces;
	}
	
	/**
	 * 通过课程名称得到课程
	 * @param coursename 课程名称
	 * @return 以coursename为名称的课程,未创建则返回null
	 */
	public CourseEntry<Teacher> getCoursebyName(String coursename){
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getName().contentEquals(coursename)) {
				return ce;
			}
		}
		return null;
	}
	
	/**
	 * 通过教师身份证号得到指定教师
	 * @param teacherID 教师身份证号
	 * @return 以teacherID为身份证号的指定教师,未创建则返回null
	 */
	public Teacher getTeacherbyID(String teacherID) {
		for(Teacher teacher:teachers) {
			if(teacher.getId().equals(teacherID))
				return teacher;
		}
		return null;
	}
	
	/**
	 * 通过位置名称得到该位置
	 * @param locationName 位置名称
	 * @return 以locationName为名称的位置                            
	 */
	public Location getLocationbyName(String locationName) {
		for(Location location:locations) {
			if(location.getName().contentEquals(locationName))
				return location;
		}
		
		return null;
	}
}
