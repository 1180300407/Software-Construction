package Schedule;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	//测试策略:位置尚未纳入管理；位置已经纳入管理
	//		   课程之前从未创建过；课程已经创建
	//异常情况：位置尚未纳入管理
	@Test
	public void createCourseTest() throws LocationNotFoundException, SameLabelException, LocationConflictException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		expectedEx.expect(LocationNotFoundException.class);
		cs.createCourse("Software", "test", timeslot);
	}
	
	//*为了避免get函数与该函数之间的联系，先将属性改为public进行函数的测试
	/*位置已经纳入管理、课程之前从未创建过
	@Test
	public void createCourseTest2() throws LocationNotFoundException, SameLabelException, LocationConflictException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		cs.addLocation(location);
		cs.createCourse("Software","test", timeslot);
		assertEquals(1, cs.courses.size());
	}
	
	//异常情况：课程已经创建
	@Test
	public void createCourseTest3() throws LocationNotFoundException, SameLabelException, LocationConflictException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		cs.addLocation(location);
		cs.createCourse("Software","test", timeslot);
		assertEquals(1, cs.courses.size());
		expectedEx.expect(SameLabelException.class);
		cs.createCourse("Software","test", timeslot);
	}*/
	
	@Test
	public void getCoursesTest() throws LocationNotFoundException, SameLabelException, LocationConflictException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		cs.addLocation(location);
		cs.createCourse("Software","test", timeslot);
		assertEquals(1, cs.getCourses().size());
	}
	
	//测试策略：指定课程已创建；指定课程未创建
	//指定课程已创建
	@Test
	public void getCourseStateTest() throws LocationNotFoundException, SameLabelException, LocationConflictException, PlanEntryNotCreateException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		cs.addLocation(location);
		cs.createCourse("Software","test", timeslot);
		assertEquals("Waiting", cs.getCourseState("Software"));
	}
	
	//指定课程未创建
	@Test
	public void getCourseStateTest2() throws LocationNotFoundException, SameLabelException, LocationConflictException, PlanEntryNotCreateException {
		expectedEx.expect(PlanEntryNotCreateException.class);
		cs.getCourseState("null");
	}
	
	//测试策略:删除的教师还未创建；删除的教师已创建
	//			教师已分配课程；教师未分配课程
	//异常情况：删除的教师还未创建
	@Test
	public void deleteTeacherTest() throws ResourceNotFoundException, PlanEntryOccupyResourceException, LocationNotFoundException, SameLabelException, LocationConflictException, PlanEntryNotCreateException, ResourceConflictException {
		Teacher teacher2=new Teacher("130283", "n", true, "professtionalTitle");
		expectedEx.expect(ResourceNotFoundException.class);
		cs.deleteTeacher(teacher2.getId());
	}
	
	//异常情况：教师已分配课程
	@Test
	public void deleteTeacherTest2() throws ResourceNotFoundException, PlanEntryOccupyResourceException, LocationNotFoundException, SameLabelException, LocationConflictException, PlanEntryNotCreateException, ResourceConflictException {
		cs.addTeacher(teacher);
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		cs.addLocation(location);
		cs.createCourse("Software","test", timeslot);
		cs.allocateTeacher("Software", "130283xxx");
		expectedEx.expect(PlanEntryOccupyResourceException.class);
		cs.deleteTeacher("130283xxx");
	}
	
	//删除的教师已创建且教师未分配课程
	@Test
	public void deleteTeacherTest3() throws ResourceNotFoundException, PlanEntryOccupyResourceException, LocationNotFoundException, SameLabelException, LocationConflictException, PlanEntryNotCreateException, ResourceConflictException {
		Teacher teacher2=new Teacher("130283", "n", true, "professtionalTitle");
		cs.addTeacher(teacher2);
		assertEquals(1,cs.getTeachers().size());
		cs.deleteTeacher("130283");
		assertEquals(0,cs.getTeachers().size());
	}
	
	//测试策略:删除的位置还未创建；删除的位置已创建
	//			位置被占用；位置未被占用
	//异常情况：位置还未创建
	@Test
	public void deleteLocationTest() throws LocationNotFoundException, PlanEntryOccupyLocationException, SameLabelException, LocationConflictException {
		expectedEx.expect(LocationNotFoundException.class);
		cs.deleteLocation("tet");
	}
	
	//异常情况：位置被占用
	@Test
	public void deleteLocationTest2() throws LocationNotFoundException, PlanEntryOccupyLocationException, SameLabelException, LocationConflictException {
		cs.addLocation(location);
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		cs.createCourse("ddd", location.getName(), timeslot);
		expectedEx.expect(PlanEntryOccupyLocationException.class);
		cs.deleteLocation("test");
	}
	
	//删除的位置已创建且位置未被占用
	@Test
	public void deleteLocationTest3() throws LocationNotFoundException, PlanEntryOccupyLocationException, SameLabelException, LocationConflictException {
		cs.addLocation(location);
		assertEquals(1, cs.getLocations().size());
		cs.deleteLocation("test");
		assertEquals(0, cs.getLocations().size());
	}
	
	//测试策略：分配的教师已经纳入管理；分配的教师还未纳入管理
	//			课程未创建；课程已创建
	//异常情况：课程未创建
	@Test
	public void allocateTeacherTest() throws LocationNotFoundException, SameLabelException, LocationConflictException, ResourceNotFoundException, PlanEntryNotCreateException, ResourceConflictException {
		expectedEx.expect(ResourceNotFoundException.class);
		cs.allocateTeacher("Software", "130283xxx");
	}
	
	//课程已创建、分配的教师已经纳入管理；
	@Test
	public void allocateTeacherTest2() throws LocationNotFoundException, SameLabelException, LocationConflictException, ResourceNotFoundException, PlanEntryNotCreateException, ResourceConflictException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		cs.addLocation(location);
		cs.createCourse("Software","test", timeslot);
		cs.addTeacher(teacher);
		cs.allocateTeacher("Software", "130283xxx");
		assertEquals("Allocated", cs.getCourseState("Software"));
	}
	
	//异常情况：分配的教师还未纳入管理
	@Test
	public void allocateTeacherTest3() throws LocationNotFoundException, SameLabelException, LocationConflictException, ResourceNotFoundException, PlanEntryNotCreateException, ResourceConflictException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		cs.addLocation(location);
		cs.createCourse("Software","test", timeslot);
		expectedEx.expect(ResourceNotFoundException.class);
		cs.allocateTeacher("Software", "130283xxx");
	}
	
	//测试策略：课程已创建；课程未创建
	//			课程已分配教师；课程未分配教师
	//异常情况：课程未创建
	@Test
	public void startCourseTest() throws LocationNotFoundException, SameLabelException, LocationConflictException, ResourceNotFoundException, PlanEntryNotCreateException, ResourceConflictException {
		expectedEx.expect(PlanEntryNotCreateException.class);
		cs.startCourse("null");
	}
	
	//课程已创建、课程已分配教师；课程已创建、课程未分配教师
	@Test
	public void startCourseTest2() throws LocationNotFoundException, SameLabelException, LocationConflictException, ResourceNotFoundException, PlanEntryNotCreateException, ResourceConflictException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		cs.addLocation(location);
		cs.addTeacher(teacher);
		cs.createCourse("Software", "test", timeslot);
		Timeslot timeslot2=new Timeslot("2020-04-01 10:14", "2020-04-03 14:15");
		cs.createCourse("Math", "test", timeslot2);
		cs.allocateTeacher("Software", "130283xxx");
		cs.startCourse("Software");
		cs.startCourse("Math");
		assertNotEquals("Running", cs.getCourseState("Math"));
		assertEquals("Running", cs.getCourseState("Software"));
	}
	
	//测试策略：课程已创建；课程未创建
	//			课程未上课；课程已上课
	//异常情况：课程未创建
	@Test
	public void cancelCourseTest() throws LocationNotFoundException, SameLabelException, LocationConflictException, PlanEntryNotCreateException, ResourceNotFoundException, ResourceConflictException {
		expectedEx.expect(PlanEntryNotCreateException.class);
		cs.cancelCourse("null");
	}
	
	//课程已创建、课程未上课；课程已创建、课程已上课
	@Test
	public void cancelCourseTest2() throws LocationNotFoundException, SameLabelException, LocationConflictException, PlanEntryNotCreateException, ResourceNotFoundException, ResourceConflictException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		cs.addLocation(location);
		cs.addTeacher(teacher);
		cs.createCourse("Software", "test", timeslot);
		Timeslot timeslot2=new Timeslot("2020-04-01 10:14", "2020-04-03 14:15");
		cs.createCourse("Math", "test", timeslot2);
		cs.allocateTeacher("Math", "130283xxx");
		cs.startCourse("Math");
		cs.cancelCourse("Software");
		cs.cancelCourse("Math");
		assertNotEquals("Cancelled", cs.getCourseState("Math"));
		assertEquals("Cancelled", cs.getCourseState("Software"));
	}
	
	//测试策略：课程已创建；课程未创建
	//			课程未上课；课程已上课
	//异常情况：课程未创建
	@Test
	public void endCourseTest() throws LocationNotFoundException, SameLabelException, LocationConflictException, PlanEntryNotCreateException, ResourceNotFoundException, ResourceConflictException {
		expectedEx.expect(PlanEntryNotCreateException.class);
		cs.endCourse("null");
	}
	
	//课程已创建、课程未上课；课程已创建、课程已上课
	@Test
	public void endCourseTest2() throws LocationNotFoundException, SameLabelException, LocationConflictException, PlanEntryNotCreateException, ResourceNotFoundException, ResourceConflictException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		cs.addLocation(location);
		cs.addTeacher(teacher);
		cs.createCourse("Software", "test", timeslot);
		Timeslot timeslot2=new Timeslot("2020-04-01 10:14", "2020-04-03 14:15");
		cs.createCourse("Math", "test", timeslot2);
		cs.allocateTeacher("Math", "130283xxx");
		cs.startCourse("Math");
		cs.endCourse("Software");
		cs.endCourse("Math");
		assertNotEquals("Ended", cs.getCourseState("Software"));
		assertEquals("Ended", cs.getCourseState("Math"));
	}
	
	//测试策略：课程未创建；课程已创建
	//			位置未纳入管理；位置已纳入管理
	//			课程结束或取消；课程开始前/中
	//异常情况：位置未纳入管理
	@Test
	public void changeLocationTest() throws LocationNotFoundException, SameLabelException, LocationConflictException, PlanEntryNotCreateException, PlanEntryStateNotMatchException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		cs.addTeacher(teacher);
		cs.addLocation(location);
		cs.createCourse("Software", "test", timeslot);
		Location location3=new Location("10W","40N" , "ths", true);
		expectedEx.expect(LocationNotFoundException.class);
		cs.changeLocation("Software", location3.getName());
	}
	
	//异常情况：课程未创建
	@Test
	public void changeLocationTest2() throws LocationNotFoundException, SameLabelException, LocationConflictException, PlanEntryNotCreateException, PlanEntryStateNotMatchException {
		Location location2=new Location("13E","4S" , "tes", true);
		cs.addLocation(location2);
		expectedEx.expect(PlanEntryNotCreateException.class);
		cs.changeLocation("s","tes");
	}
	
	//课程已创建、位置已纳入管理、课程未开始
	@Test
	public void changeLocationTest3() throws LocationNotFoundException, SameLabelException, LocationConflictException, PlanEntryNotCreateException, PlanEntryStateNotMatchException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		cs.addTeacher(teacher);
		cs.addLocation(location);
		cs.createCourse("Software", "test", timeslot);
		Location location3=new Location("10W","40N" , "ths", true);
		cs.addLocation(location3);
		cs.changeLocation("Software", "ths");
		assertEquals(location3, cs.getCourses().get(0).getLocation().get(0));
	}
	
	//异常情况:课程结束或取消
	@Test
	public void changeLocationTest4() throws LocationNotFoundException, SameLabelException, LocationConflictException, PlanEntryNotCreateException, PlanEntryStateNotMatchException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		cs.addTeacher(teacher);
		cs.addLocation(location);
		cs.createCourse("Software", "test", timeslot);
		Location location3=new Location("10W","40N" , "ths", true);
		cs.addLocation(location3);
		cs.cancelCourse("Software");
		expectedEx.expect(PlanEntryStateNotMatchException.class);
		cs.changeLocation("Software", "ths");
	}
	
	@Test
	public void getCoursesofassignTeachertest() throws LocationNotFoundException, SameLabelException, LocationConflictException, ResourceNotFoundException, PlanEntryNotCreateException, ResourceConflictException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-05 14:15");
		cs.addTeacher(teacher);
		Teacher teacher2=new Teacher("130x", "r", true, "professtionalTitle");
		cs.addTeacher(teacher2);
		cs.addLocation(location);
		Timeslot timeslot2=new Timeslot("2020-04-04 10:14", "2020-04-04 14:15");
		cs.createCourse("w", "test", timeslot2);
		cs.createCourse("t", "test", timeslot);
		cs.allocateTeacher("w", "130x");
		cs.allocateTeacher("t", "130283xxx");
		List<CourseEntry<Teacher>> ces=cs.getCoursesofassignTeacher("130x");
		assertEquals(1, ces.size());
		assertEquals("w", ces.get(0).getName());
		ces=cs.getCoursesofassignTeacher("130283xxx");
		assertEquals(1, ces.size());
		assertEquals("t", ces.get(0).getName());
	}
	
	@Test
	public void getCoursebyNameTest() throws LocationNotFoundException, SameLabelException, LocationConflictException {
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
