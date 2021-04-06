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
	/*Ϊ�˱���get������ú���֮�����ϵ���Ƚ����Ը�Ϊpublic���в���
	//���Բ��ԣ�������ӽ�ʦ���ظ���ӽ�ʦ
	//			���һ����ʦ	����Ӷ����ʦ
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

	//Ϊ�˱���get������ú���֮�����ϵ���Ƚ����Ը�Ϊpublic���к����Ĳ���
	//���Բ��ԣ��������λ�ã��ظ����λ��
	//			���һ��λ�ã���Ӷ��λ��
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
	
	//���Բ���:λ����δ�������λ���Ѿ��������
	//		   �γ�֮ǰ��δ���������γ��Ѿ�����
	//�쳣�����λ����δ�������
	@Test
	public void createCourseTest() throws LocationNotFoundException, SameLabelException, LocationConflictException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		expectedEx.expect(LocationNotFoundException.class);
		cs.createCourse("Software", "test", timeslot);
	}
	
	//*Ϊ�˱���get������ú���֮�����ϵ���Ƚ����Ը�Ϊpublic���к����Ĳ���
	/*λ���Ѿ���������γ�֮ǰ��δ������
	@Test
	public void createCourseTest2() throws LocationNotFoundException, SameLabelException, LocationConflictException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		cs.addLocation(location);
		cs.createCourse("Software","test", timeslot);
		assertEquals(1, cs.courses.size());
	}
	
	//�쳣������γ��Ѿ�����
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
	
	//���Բ��ԣ�ָ���γ��Ѵ�����ָ���γ�δ����
	//ָ���γ��Ѵ���
	@Test
	public void getCourseStateTest() throws LocationNotFoundException, SameLabelException, LocationConflictException, PlanEntryNotCreateException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		cs.addLocation(location);
		cs.createCourse("Software","test", timeslot);
		assertEquals("Waiting", cs.getCourseState("Software"));
	}
	
	//ָ���γ�δ����
	@Test
	public void getCourseStateTest2() throws LocationNotFoundException, SameLabelException, LocationConflictException, PlanEntryNotCreateException {
		expectedEx.expect(PlanEntryNotCreateException.class);
		cs.getCourseState("null");
	}
	
	//���Բ���:ɾ���Ľ�ʦ��δ������ɾ���Ľ�ʦ�Ѵ���
	//			��ʦ�ѷ���γ̣���ʦδ����γ�
	//�쳣�����ɾ���Ľ�ʦ��δ����
	@Test
	public void deleteTeacherTest() throws ResourceNotFoundException, PlanEntryOccupyResourceException, LocationNotFoundException, SameLabelException, LocationConflictException, PlanEntryNotCreateException, ResourceConflictException {
		Teacher teacher2=new Teacher("130283", "n", true, "professtionalTitle");
		expectedEx.expect(ResourceNotFoundException.class);
		cs.deleteTeacher(teacher2.getId());
	}
	
	//�쳣�������ʦ�ѷ���γ�
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
	
	//ɾ���Ľ�ʦ�Ѵ����ҽ�ʦδ����γ�
	@Test
	public void deleteTeacherTest3() throws ResourceNotFoundException, PlanEntryOccupyResourceException, LocationNotFoundException, SameLabelException, LocationConflictException, PlanEntryNotCreateException, ResourceConflictException {
		Teacher teacher2=new Teacher("130283", "n", true, "professtionalTitle");
		cs.addTeacher(teacher2);
		assertEquals(1,cs.getTeachers().size());
		cs.deleteTeacher("130283");
		assertEquals(0,cs.getTeachers().size());
	}
	
	//���Բ���:ɾ����λ�û�δ������ɾ����λ���Ѵ���
	//			λ�ñ�ռ�ã�λ��δ��ռ��
	//�쳣�����λ�û�δ����
	@Test
	public void deleteLocationTest() throws LocationNotFoundException, PlanEntryOccupyLocationException, SameLabelException, LocationConflictException {
		expectedEx.expect(LocationNotFoundException.class);
		cs.deleteLocation("tet");
	}
	
	//�쳣�����λ�ñ�ռ��
	@Test
	public void deleteLocationTest2() throws LocationNotFoundException, PlanEntryOccupyLocationException, SameLabelException, LocationConflictException {
		cs.addLocation(location);
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		cs.createCourse("ddd", location.getName(), timeslot);
		expectedEx.expect(PlanEntryOccupyLocationException.class);
		cs.deleteLocation("test");
	}
	
	//ɾ����λ���Ѵ�����λ��δ��ռ��
	@Test
	public void deleteLocationTest3() throws LocationNotFoundException, PlanEntryOccupyLocationException, SameLabelException, LocationConflictException {
		cs.addLocation(location);
		assertEquals(1, cs.getLocations().size());
		cs.deleteLocation("test");
		assertEquals(0, cs.getLocations().size());
	}
	
	//���Բ��ԣ�����Ľ�ʦ�Ѿ������������Ľ�ʦ��δ�������
	//			�γ�δ�������γ��Ѵ���
	//�쳣������γ�δ����
	@Test
	public void allocateTeacherTest() throws LocationNotFoundException, SameLabelException, LocationConflictException, ResourceNotFoundException, PlanEntryNotCreateException, ResourceConflictException {
		expectedEx.expect(ResourceNotFoundException.class);
		cs.allocateTeacher("Software", "130283xxx");
	}
	
	//�γ��Ѵ���������Ľ�ʦ�Ѿ��������
	@Test
	public void allocateTeacherTest2() throws LocationNotFoundException, SameLabelException, LocationConflictException, ResourceNotFoundException, PlanEntryNotCreateException, ResourceConflictException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		cs.addLocation(location);
		cs.createCourse("Software","test", timeslot);
		cs.addTeacher(teacher);
		cs.allocateTeacher("Software", "130283xxx");
		assertEquals("Allocated", cs.getCourseState("Software"));
	}
	
	//�쳣���������Ľ�ʦ��δ�������
	@Test
	public void allocateTeacherTest3() throws LocationNotFoundException, SameLabelException, LocationConflictException, ResourceNotFoundException, PlanEntryNotCreateException, ResourceConflictException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		cs.addLocation(location);
		cs.createCourse("Software","test", timeslot);
		expectedEx.expect(ResourceNotFoundException.class);
		cs.allocateTeacher("Software", "130283xxx");
	}
	
	//���Բ��ԣ��γ��Ѵ������γ�δ����
	//			�γ��ѷ����ʦ���γ�δ�����ʦ
	//�쳣������γ�δ����
	@Test
	public void startCourseTest() throws LocationNotFoundException, SameLabelException, LocationConflictException, ResourceNotFoundException, PlanEntryNotCreateException, ResourceConflictException {
		expectedEx.expect(PlanEntryNotCreateException.class);
		cs.startCourse("null");
	}
	
	//�γ��Ѵ������γ��ѷ����ʦ���γ��Ѵ������γ�δ�����ʦ
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
	
	//���Բ��ԣ��γ��Ѵ������γ�δ����
	//			�γ�δ�ϿΣ��γ����Ͽ�
	//�쳣������γ�δ����
	@Test
	public void cancelCourseTest() throws LocationNotFoundException, SameLabelException, LocationConflictException, PlanEntryNotCreateException, ResourceNotFoundException, ResourceConflictException {
		expectedEx.expect(PlanEntryNotCreateException.class);
		cs.cancelCourse("null");
	}
	
	//�γ��Ѵ������γ�δ�ϿΣ��γ��Ѵ������γ����Ͽ�
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
	
	//���Բ��ԣ��γ��Ѵ������γ�δ����
	//			�γ�δ�ϿΣ��γ����Ͽ�
	//�쳣������γ�δ����
	@Test
	public void endCourseTest() throws LocationNotFoundException, SameLabelException, LocationConflictException, PlanEntryNotCreateException, ResourceNotFoundException, ResourceConflictException {
		expectedEx.expect(PlanEntryNotCreateException.class);
		cs.endCourse("null");
	}
	
	//�γ��Ѵ������γ�δ�ϿΣ��γ��Ѵ������γ����Ͽ�
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
	
	//���Բ��ԣ��γ�δ�������γ��Ѵ���
	//			λ��δ�������λ�����������
	//			�γ̽�����ȡ�����γ̿�ʼǰ/��
	//�쳣�����λ��δ�������
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
	
	//�쳣������γ�δ����
	@Test
	public void changeLocationTest2() throws LocationNotFoundException, SameLabelException, LocationConflictException, PlanEntryNotCreateException, PlanEntryStateNotMatchException {
		Location location2=new Location("13E","4S" , "tes", true);
		cs.addLocation(location2);
		expectedEx.expect(PlanEntryNotCreateException.class);
		cs.changeLocation("s","tes");
	}
	
	//�γ��Ѵ�����λ������������γ�δ��ʼ
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
	
	//�쳣���:�γ̽�����ȡ��
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
