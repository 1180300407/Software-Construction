package Schedule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import API.PlanningEntryAPIs;
import API.PlanningEntryAPIsFirstImpl;
import Location.Location;
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
	//Abstraction function:
	//	AF(courses,teachers,locations)=一个对课程courses，所有教师teachers，教室locations进行管理的系统
	//Representation invariant:
	//	true
	//Safety from rep exposure:
	//	成员变量全是private的，对外返回时全部转为不可变类型，不存在表示泄露
	
	/**
	 * 增加一个可供管理的教师，若教师已存在不会重复添加
	 * @param teacher 待添加的教师
	 */
	public void addTeacher(Teacher teacher) {
		if(!teachers.contains(teacher))
			teachers.add(teacher); //Teacher为不可变类，不存在表示泄露
	}
	
	/**
	 * 删除具有指定ID的教师，若教师不存在并不会进行删除操作,教师若已分配课程则无法删除
	 * @param ID 待删除的教师的ID
	 */
	public void deleteTeacher(String ID) {
		int index=-1;
		for(Teacher teacher:teachers) {
			if(teacher.getId().equals(ID)) {
				index=teachers.indexOf(teacher);
				break;
			}
		}
		if(index==-1) {
			System.out.println("目前管理的教师中不存在该ID的教师!");
			return;
		}
		
		boolean flag=false;
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getResource().contains(teachers.get(index))&&(ce.getStateName().equals("Allocated")||ce.getStateName().equals("Running"))) {
				flag=true;
				break;
			}
		}
		if(!flag) {
			teachers.remove(index);
			return;
		}
		
		System.out.println("该教师已分配课程!删除失败!");
	}
	
	/**
	 * 增加一个可供管理的教室，若教室已存在不会重复添加
	 * @param location 待添加的教室
	 */
	public void addLocation(Location location) {
		if(!locations.contains(location))
			locations.add(location);
	}
	
	/**
	 * 删除具有指定名称的教室，若教室不存在并不会进行删除操作,若该位置已被分配给某课程则无法删除
	 * @param locationname 待删除的教室名称
	 */
	public void deleteLocation(String locationname) {
		int index=-1;
		for(Location location:locations) {
			if(location.getName().equals(locationname)) {
				index=locations.indexOf(location);
				break;
			}
		}
		if(index==-1) {
			System.out.println("目前管理的位置中不存在该位置!删除失败!");
			return;
		}
		
		boolean flag=false;
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getLocation().contains(locations.get(index))&&!ce.getStateName().equals("Cancelled")&&!ce.getStateName().equals("Ended")) {
				flag=true;
				break;
			}
		}
		if(!flag) {
			locations.remove(index);
			return;
		}
		System.out.println("目前该位置已被分配给某课程!删除失败!");
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
	 * @return 创建成功返回true，否则返回false
	 */
	public boolean createCourse(String name,String location,Timeslot timeslot) {
		int flag=-1;
		for(Location location2:locations) {//得到该名称位置
			if(location2.getName().equals(location)) {
				flag=locations.indexOf(location2);
				break;
			}
		}
		if(flag==-1) {
			System.out.println("分配的位置尚未纳入管理!");
			return false;
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
				System.out.println("该课程已存在，不能重复创建");
				return false;
			}
		}
		courses.add(newcourse);
		PlanningEntryAPIs api=new PlanningEntryAPIsFirstImpl();
		boolean flag2=api.checkLocationConflict(courses);
		if(flag2) {
			System.out.println("请注意,新建课程与其他课程有位置冲突!请考虑是否取消!");
		}
		return true;
	}
	
	/**
	 * 为课程分配教师，教师应包含在管理的所有教师之中
	 * @param coursename 指定的课程名称
	 * @param teacher 分配的教师的身份证号
	 */
	public void allocateTeacher(String coursename,List<String> teacherIDs) {
		List<Teacher> assignteachers=new ArrayList<Teacher>();
		for(String teacherID:teacherIDs) {
			Teacher teacher=new Teacher(teacherID, "name", true, "professtionalTitle");
			if(!teachers.contains(teacher)) {
				System.out.println("指定教师未纳入管理!");
				return;
			}
			int index=teachers.indexOf(teacher);
			assignteachers.add(teachers.get(index));
		}
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getName().equals(coursename)) {
				ce.allocateResource(assignteachers);
				PlanningEntryAPIs api=new PlanningEntryAPIsFirstImpl();
				boolean flag2=api.checkResourceExclusiveConflict(courses);
				if(flag2) {
					System.out.println("请注意,新建课程与其他课程有教师分配冲突!请考虑是否取消!");
				}
				return;
			}
		}
		
		System.out.println("指定课程还未创建，无法分配教师!");
	}
	
	/**
	 * 指定某一课程上课，课程应存在
	 * @param coursename 指定课程名
	 */
	public void startCourse(String coursename) {//是否需要考虑时间???
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getName().equals(coursename)) {
				ce.start();
				return;
			}
		}
		
		System.out.println("指定课程不存在，无法上课!");
	}
	
	/**
	 * 课前取消课程，课程应存在
	 * @param coursename 指定课程名称
	 */
	public void cancelCourse(String coursename) {
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getName().equals(coursename)) {
				ce.cancel();
				return;
			}
		}
		
		System.out.println("指定课程不存在，无法取消!");
	}
	
	/**
	 * 输出目前课程的状态，课程应已经创建过
	 * @param coursename 要查看的课程
	 */
	public String getCourseState(String coursename) {
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getName().equals(coursename)) { 
				return ce.getStateName();
			}
		}
		
		return "null";
	}
	
	/**
	 * 指定某课程下课，课程应存在
	 * @param coursename 指定课程名
	 */
	public void endCourse(String coursename) {
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getName().equals(coursename)) {
				ce.end();
				return;
			}
		}
		
		System.out.println("指定课程不存在!");
	}
	
	/**
	 * 更换课程地点,地点应该是已经被纳入管理的，课程应该已经创建，未结束
	 * @param coursename 指定课程名称
	 * @param newlocation 新的教室
	 */
	public void changeLocation(String coursename,String newlocation) {
		int flag=-1;
		for(Location location2:locations) {//得到该名称位置
			if(location2.getName().equals(newlocation)) {
				flag=locations.indexOf(location2);
				break;
			}
		}
		if(flag==-1) {
			System.out.println("更改的位置尚未纳入管理!");
			return;
		}
		Location location=locations.get(flag);
		for(CourseEntry<Teacher> ce:courses) {
			if(ce.getName().equals(coursename)&&!ce.getStateName().equals("Ended")&&!ce.getStateName().equals("Cancelled")) {//上课前、上课中都可更换教室
				List<Location> newLocation=new ArrayList<Location>();
				newLocation.add(location);
				ce.setLocation(newLocation);
				return;
			}
		}
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
	 * @return 以locationName为名称的位置,不存在则返回null                            
	 */
	public Location getLocationbyName(String locationName) {
		for(Location location:locations) {
			if(location.getName().contentEquals(locationName))
				return location;
		}
		
		return null;
	}
}
