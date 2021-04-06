package API;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import Resources.Teacher;
import Timeslot.Timeslot;
import compositeinterface.CourseEntry;

public class PlanningEntryAPIsFirstImplTest extends PlanningEntryAPIsTest{
	@Override
	public PlanningEntryAPIs getAPI() {
		return new PlanningEntryAPIsFirstImpl();
	}
	
	//*测试其中的private辅助方法，测试时将其改为public
	@Test
	public void findFirstPreEntrytest() throws ParseException {
		PlanningEntryAPIsFirstImpl pApIsFirstImpl=new PlanningEntryAPIsFirstImpl();
		CourseEntry<Teacher> ce=new CourseEntry<Teacher>("soft");
		Teacher teacher=new Teacher("130283xxx", "name", true, "professtionalTitle");
		List<Teacher> teachers=new ArrayList<Teacher>();
		teachers.add(teacher);
		Timeslot timeslot=new Timeslot("2020-05-07 10:08", "2020-05-08 15:17");
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
		ce.setTime(timeslots);
		ce.allocateResource(teachers);
		Timeslot timeslot2=new Timeslot("2020-05-06 10:08", "2020-05-06 15:17");
		Timeslot timeslot3=new Timeslot("2020-05-05 10:08", "2020-05-05 15:17");//两个都是ce前序计划项
		List<Timeslot> timeslots2=new ArrayList<Timeslot>();
		List<Timeslot> timeslots3=new ArrayList<Timeslot>();
		timeslots2.add(timeslot2);
		timeslots3.add(timeslot3);
		CourseEntry<Teacher> ce2=new CourseEntry<Teacher>("sos");
		ce2.setTime(timeslots2);
		ce2.allocateResource(teachers);
		List<CourseEntry<Teacher>> ces=new ArrayList<CourseEntry<Teacher>>();
		ces.add(ce2);//ce2先加入list，下标为0
		CourseEntry<Teacher> ce3=new CourseEntry<Teacher>("sosr");
		ce3.setTime(timeslots3);
		ce3.allocateResource(teachers);
		ces.add(ce3);
		assertEquals(0, pApIsFirstImpl.findFirstPreEntry(teacher, ce, ces));
		Timeslot timeslot4=new Timeslot("2020-05-09 10:08", "2020-05-09 15:17");
		List<Timeslot> timeslots4=new ArrayList<Timeslot>();
		timeslots4.add(timeslot4);
		CourseEntry<Teacher> ce4=new CourseEntry<Teacher>("sttt");
		ce4.setTime(timeslots4);
		ce4.allocateResource(teachers);//更改list中顺序，其第0个元素更改为不是前序计划项
		ces.removeAll(ces);
		ces.add(ce4);
		ces.add(ce3);
		ces.add(ce2);
		assertEquals(1, pApIsFirstImpl.findFirstPreEntry(teacher, ce, ces));
	}

}
