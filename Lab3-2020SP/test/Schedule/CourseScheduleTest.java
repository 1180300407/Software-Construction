package Schedule;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Location.Location;
import Resources.Teacher;
import Timeslot.Timeslot;
import compositeinterface.CourseEntry;

public class CourseScheduleTest {
	CourseSchedule cs;
	Teacher teacher=new Teacher("130283xxx", "name", true, "professtionalTitle");
	Location location=new Location("130E","45S" , "test", true);
	
	@Before
	public void prepare() {
		cs=new CourseSchedule();
	}
	/*为了避免get函数与该函数之间的联系，先将属性改为public进行测试
	//测试策略：单次添加教师；重复添加教师
	//			添加一个教师	；添加多个教师
	@Test
	public void addTeachertest() {
		cs.addTeacher(teacher);
		assertEquals(1,cs.teachers.size());
		assertTrue(cs.teachers.contains(teacher));
		Teacher teacher2=new Teacher("130283", "n", true, "professtionalTitle");
		cs.addTeacher(teacher2);
		assertEquals(2,cs.teachers.size());
		assertTrue(cs.teachers.contains(teacher2));
		cs.addTeacher(teacher2);
		assertEquals(2,cs.teachers.size());
	}

	//为了避免get函数与该函数之间的联系，先将属性改为public进行函数的测试
	//测试策略：单次添加位置；重复添加位置
	//			添加一个位置；添加多个位置
	@Test
	public void addLocationTest() {
		cs.addLocation(location);
		Location location2=new Location("13E","4S" , "tes", true);
		assertEquals(1, cs.locations.size());
		assertTrue(cs.locations.contains(location));
		cs.addLocation(location2);
		assertTrue(cs.locations.contains(location2));
		assertEquals(2, cs.locations.size());
		cs.addLocation(location2);
		assertEquals(2, cs.locations.size());
	}*/
	
	@Test
	public void getTeachersTest() {
		cs.addTeacher(teacher);
		Teacher teacher2=new Teacher("130283", "n", true, "professtionalTitle");
		assertEquals(1, cs.getTeachers().size());
		assertTrue(cs.getTeachers().contains(teacher));
		cs.addTeacher(teacher2);
		assertTrue(cs.getTeachers().contains(teacher2));
		assertEquals(2, cs.getTeachers().size());
	}
	
	@Test
	public void getLocationsTest() {
		cs.addLocation(location);
		assertEquals(1, cs.getLocations().size());
		assertTrue(cs.getLocations().contains(location));
		Location location2=new Location("13E","4S" , "tes", true);
		cs.addLocation(location2);
		assertEquals(2, cs.getLocations().size());
		assertTrue(cs.getLocations().contains(location2));
	}
	
	/*为了避免get函数与该函数之间的联系，先将属性改为public进行函数的测试
	//测试策略:位置尚未纳入管理；位置已经纳入管理
	//		   课程之前从未创建过；课程已经创建
	@Test
	public void createCourseTest() {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		cs.createCourse("Software", "test", timeslot);
		assertEquals(0, cs.courses.size());
		cs.addLocation(location);
		cs.createCourse("Software","test", timeslot);
		assertEquals(1, cs.courses.size());
		cs.createCourse("Software","test", timeslot);
		assertEquals(1, cs.courses.size());
	}*/
	
	//测试策略:位置尚未纳入管理；位置已经纳入管理
	//		   课程之前从未创建过；课程已经创建
	@Test
	public void getCoursesTest() {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		cs.createCourse("Software","test", timeslot);
		assertEquals(0, cs.getCourses().size());
		cs.addLocation(location);
		cs.createCourse("Software","test", timeslot);
		assertEquals(1, cs.getCourses().size());
		cs.createCourse("Software","test", timeslot);
		assertEquals(1, cs.getCourses().size());
	}
	
	//测试策略：指定课程已创建；指定课程未创建
	@Test
	public void getCourseStateTest() {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		cs.addLocation(location);
		cs.createCourse("Software","test", timeslot);
		assertEquals("null", cs.getCourseState(""));
		assertEquals("Waiting", cs.getCourseState("Software"));
	}
	
	//测试策略:删除的教师还未创建；删除的教师已创建
	//			教师已分配课程；教师未分配课程
	@Test
	public void deleteTeacherTest() {
		cs.addTeacher(teacher);
		Teacher teacher2=new Teacher("130283", "n", true, "professtionalTitle");
		cs.deleteTeacher("130283");
		assertEquals(1, cs.getTeachers().size());
		cs.addTeacher(teacher2);
		cs.deleteTeacher("130283");
		assertEquals(1,cs.getTeachers().size());
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		cs.addLocation(location);
		cs.createCourse("Software","test", timeslot);
		List<String> teacherList=new ArrayList<String>();
		teacherList.add("130283xxx");
		cs.allocateTeacher("Software", teacherList);
		cs.deleteTeacher("130283xxx");
		assertEquals(1,cs.getTeachers().size());
	}
	
	//测试策略:删除的位置还未创建；删除的位置已创建
	//			位置被占用；位置未被占用
	@Test
	public void deleteLocationTest() {
		cs.addLocation(location);
		cs.deleteLocation("tet");
		assertEquals(1, cs.getLocations().size());
		cs.deleteLocation("test");
		assertEquals(0, cs.getLocations().size());
		cs.addLocation(location);
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		cs.createCourse("ddd", location.getName(), timeslot);
		cs.deleteLocation("test");
		assertEquals(1, cs.getLocations().size());
	}
	
	//测试策略：分配的教师已经纳入管理；分配的教师还未纳入管理
	//			课程未创建；课程已创建
	@Test
	public void allocateTeacherTest() {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		cs.addLocation(location);
		cs.createCourse("Software","test", timeslot);
		List<String> teacherList=new ArrayList<String>();
		teacherList.add("130283xxx");
		cs.allocateTeacher("Software", teacherList);
		assertNotEquals("Allocated", cs.getCourseState("Software"));
		cs.addTeacher(teacher);
		cs.allocateTeacher("NULL", teacherList);
		assertNotEquals("Allocated", cs.getCourseState("Software"));
		assertNotEquals("Allocated", cs.getCourseState("NULL"));
		cs.allocateTeacher("Software", teacherList);
		assertEquals("Allocated", cs.getCourseState("Software"));
	}
	
	//测试策略：课程已创建；课程未创建
	//			课程已分配教师；课程未分配教师
	@Test
	public void startCourseTest() {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		cs.addLocation(location);
		cs.addTeacher(teacher);
		cs.createCourse("Software", "test", timeslot);
		Timeslot timeslot2=new Timeslot("2020-04-01 10:14", "2020-04-03 14:15");
		cs.createCourse("Math", "test", timeslot2);
		cs.startCourse("null");
		assertNotEquals("Running", cs.getCourseState("Math"));
		assertNotEquals("Running", cs.getCourseState("Software"));
		List<String> teacherList=new ArrayList<String>();
		teacherList.add("130283xxx");
		cs.allocateTeacher("Software", teacherList);
		cs.startCourse("Software");
		assertNotEquals("Running", cs.getCourseState("Math"));
		assertEquals("Running", cs.getCourseState("Software"));
	}
	
	//测试策略：课程已创建；课程未创建
	//			课程未上课；课程已上课
	@Test
	public void cancelCourseTest() {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		cs.addLocation(location);
		cs.addTeacher(teacher);
		cs.createCourse("Software", "test", timeslot);
		cs.cancelCourse("null");
		assertNotEquals("Cancelled", cs.getCourseState("Software"));
		Timeslot timeslot2=new Timeslot("2020-04-01 10:14", "2020-04-03 14:15");
		cs.createCourse("Math", "test", timeslot2);
		List<String> teacherList=new ArrayList<String>();
		teacherList.add("130283xxx");
		cs.allocateTeacher("Math", teacherList);
		cs.startCourse("Math");
		cs.cancelCourse("Software");
		cs.cancelCourse("Math");
		assertNotEquals("Cancelled", cs.getCourseState("Math"));
		assertEquals("Cancelled", cs.getCourseState("Software"));
	}
	
	//测试策略：课程已创建；课程未创建
	//			课程未上课；课程已上课
	@Test
	public void endCourseTest() {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		cs.addLocation(location);
		cs.addTeacher(teacher);
		cs.createCourse("Software", "test", timeslot);
		cs.endCourse("null");
		assertNotEquals("Ended", cs.getCourseState("Software"));
		Timeslot timeslot2=new Timeslot("2020-04-01 10:14", "2020-04-03 14:15");
		cs.createCourse("Math", "test", timeslot2);
		List<String> teacherList=new ArrayList<String>();
		teacherList.add("130283xxx");
		cs.allocateTeacher("Math", teacherList);
		cs.startCourse("Math");
		cs.endCourse("Software");
		cs.endCourse("Math");
		assertNotEquals("Ended", cs.getCourseState("Software"));
		assertEquals("Ended", cs.getCourseState("Math"));
	}
	
	//测试策略：课程未创建；课程已创建
	//			位置未纳入管理；位置已纳入管理
	public void changeLocationTest() {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		cs.addTeacher(teacher);
		cs.addLocation(location);
		cs.createCourse("Software", "test", timeslot);
		Location location2=new Location("13E","4S" , "tes", true);
		cs.addLocation(location2);
		Location location3=new Location("10W","40N" , "ths", true);
		cs.changeLocation("s","tes");
		assertEquals(location, cs.getCourses().get(0).getLocation());
		cs.changeLocation("Software", "ths");
		assertEquals(location, cs.getCourses().get(0).getLocation());
		cs.addLocation(location3);
		cs.changeLocation("Software", "ths");
		assertEquals(location3, cs.getCourses().get(0).getLocation());
	}
	
	@Test
	public void getCoursesofassignTeachertest() {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-05 14:15");
		cs.addTeacher(teacher);
		Teacher teacher2=new Teacher("130x", "r", true, "professtionalTitle");
		cs.addTeacher(teacher2);
		cs.addLocation(location);
		Timeslot timeslot2=new Timeslot("2020-04-04 10:14", "2020-04-04 14:15");
		cs.createCourse("w", "test", timeslot2);
		cs.createCourse("t", "test", timeslot);
		List<String> teacherList1=new ArrayList<String>();
		List<String> teacherList2=new ArrayList<String>();
		teacherList1.add("130283xxx");
		teacherList2.add("130x");
		cs.allocateTeacher("w", teacherList2);
		cs.allocateTeacher("t", teacherList1);
		List<CourseEntry<Teacher>> ces=cs.getCoursesofassignTeacher("130x");
		assertEquals(1, ces.size());
		assertEquals("w", ces.get(0).getName());
		ces=cs.getCoursesofassignTeacher("130283xxx");
		assertEquals(1, ces.size());
		assertEquals("t", ces.get(0).getName());
	}
	
	@Test
	public void getCoursebyNameTest() {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-05 14:15");
		cs.addTeacher(teacher);
		cs.addLocation(location);
		cs.createCourse("soft", "test", timeslot);
		assertEquals(1, cs.getCoursebyName("soft").getLocation().size());
		assertEquals(1, cs.getCoursebyName("soft").getTime().size());
		assertEquals(timeslot, cs.getCoursebyName("soft").getTime().get(0));
		assertEquals(location, cs.getCoursebyName("soft").getLocation().get(0));
	}
	
	@Test
	public void getLocationbyNameTest() {
		cs.addLocation(location);
		assertEquals("test", cs.getLocationbyName("test").getName());
		assertEquals("130E", cs.getLocationbyName("test").getLongitude());
	}
	
	@Test
	public void getTeacherbyIDTest() {
		cs.addTeacher(teacher);
		assertEquals("130283xxx", cs.getTeacherbyID("130283xxx").getId());
		assertEquals("name", cs.getTeacherbyID("130283xxx").getName());
	}
}
