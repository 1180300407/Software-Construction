package API;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import Location.Location;
import Resources.Teacher;
import Timeslot.Timeslot;
import compositeinterface.CourseEntry;

public abstract class PlanningEntryAPIsTest {

	public abstract PlanningEntryAPIs getAPI();
	
	/*在验证checkLocationConflict前先验证其中checkTimeConflict方法正确性,因为是private的，测试时改为public
	//测试策略:时间有冲突能否检测；时间没冲突能否确认
	@Test
	public void checkTimeConflictTest() throws ParseException {
		PlanningEntryAPIs peAPI=getAPI();
		Timeslot timeslot=new Timeslot("2020-05-07 10:08", "2020-05-08 15:17");
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
		Timeslot timeslot2=new Timeslot("2020-05-09 10:08", "2020-05-10 15:17");
		timeslots.add(timeslot2);
		assertFalse(peAPI.checkTimeConflict(timeslots));
		Timeslot timeslot3=new Timeslot("2020-05-07 12:08", "2020-05-08 15:17");
		timeslots.add(timeslot3);
		assertTrue(peAPI.checkTimeConflict(timeslots));
	}*/
	
	//测试策略:位置有冲突能否检测；位置没冲突能否确认
	@Test
	public void checkLocationConflicttest() {
		PlanningEntryAPIs peAPI=getAPI();
		CourseEntry<Teacher> ce=new CourseEntry<Teacher>("soft");
		Teacher teacher=new Teacher("130283xxx", "name", true, "professtionalTitle");
		List<Teacher> teachers=new ArrayList<Teacher>();
		teachers.add(teacher);
		Location location=new Location("130E","45S" , "test", false);
		List<Location> locations=new ArrayList<Location>();
		locations.add(location);
		ce.setLocation(locations);
		Timeslot timeslot=new Timeslot("2020-05-07 10:08", "2020-05-08 15:17");
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
		ce.setTime(timeslots);
		ce.allocateResource(teachers);
		Timeslot timeslot2=new Timeslot("2020-05-09 10:08", "2020-05-10 15:17");
		Timeslot timeslot3=new Timeslot("2020-05-07 11:08", "2020-05-10 15:17");
		List<Timeslot> timeslots2=new ArrayList<Timeslot>();
		List<Timeslot> timeslots3=new ArrayList<Timeslot>();
		timeslots2.add(timeslot2);
		timeslots3.add(timeslot3);
		CourseEntry<Teacher> ce2=new CourseEntry<Teacher>("sos");
		ce2.setLocation(locations);
		ce2.setTime(timeslots2);
		List<CourseEntry<Teacher>> ces=new ArrayList<CourseEntry<Teacher>>();
		ces.add(ce);
		ces.add(ce2);
		assertFalse(peAPI.checkLocationConflict(ces));//无冲突情况
		CourseEntry<Teacher> ce3=new CourseEntry<Teacher>("sosr");
		ce3.setLocation(locations);
		ce3.setTime(timeslots3);
		ces.add(ce3);
		assertTrue(peAPI.checkLocationConflict(ces));//有冲突情况
	}
	
	@Test
	public void findPreEntryPerResourceTest() throws ParseException {
		PlanningEntryAPIs peAPI=getAPI();
		CourseEntry<Teacher> ce=new CourseEntry<Teacher>("soft");
		Teacher teacher=new Teacher("130283xxx", "name", true, "professtionalTitle");
		List<Teacher> teachers=new ArrayList<Teacher>();
		teachers.add(teacher);
		Timeslot timeslot=new Timeslot("2020-05-07 10:08", "2020-05-08 15:17");
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
		ce.setTime(timeslots);
		ce.allocateResource(teachers);
		Timeslot timeslot2=new Timeslot("2020-05-09 10:08", "2020-05-10 15:17");
		Timeslot timeslot3=new Timeslot("2020-05-05 10:08", "2020-05-06 15:17");//ce的前序计划项
		List<Timeslot> timeslots2=new ArrayList<Timeslot>();
		List<Timeslot> timeslots3=new ArrayList<Timeslot>();
		timeslots2.add(timeslot2);
		timeslots3.add(timeslot3);
		CourseEntry<Teacher> ce2=new CourseEntry<Teacher>("sos");
		ce2.setTime(timeslots2);
		ce2.allocateResource(teachers);
		List<CourseEntry<Teacher>> ces=new ArrayList<CourseEntry<Teacher>>();
		ces.add(ce2);
		CourseEntry<Teacher> ce3=new CourseEntry<Teacher>("sosr");
		ce3.setTime(timeslots3);
		ce3.allocateResource(teachers);
		ces.add(ce3);
		assertEquals(ce3, peAPI.findPreEntryPerResource(teacher, ce, ces));
	}
}
