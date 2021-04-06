package Board;

import static org.junit.Assert.*;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import org.junit.Test;

import Location.Location;
import Resources.Teacher;
import Timeslot.Timeslot;
import compositeinterface.CourseEntry;

/**
 * 测试CourseBoard中的private辅助函数
 */

public class CourseBoardTest {
	//测试CourseBoard中的函数，需要将其中private属性改为public
	/*
	//测试策略:既有是当天的课程，又有不是当天的课程
	@Test
	public void setRequestCoursestest() throws ParseException {
		Teacher teacher=new Teacher("130283xxx", "name", true, "professtionalTitle");
		List<Teacher> teachers=new ArrayList<Teacher>();
		teachers.add(teacher);
		Location location=new Location("130E","45S" , "test", true);
		CourseEntry<Teacher> course1=new CourseEntry<Teacher>("rr");
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-05 12:15");
		Timeslot timeslot2=new Timeslot("2020-04-05 01:14", "2020-04-05 02:15");
		Timeslot timeslot3=new Timeslot("2020-04-07 10:14", "2020-04-07 14:15");//不是当天的课程
		List<Timeslot> timeslots1=new ArrayList<Timeslot>();
		timeslots1.add(timeslot);
		List<Timeslot> timeslots2=new ArrayList<Timeslot>();
		timeslots2.add(timeslot2);
		List<Timeslot> timeslots3=new ArrayList<Timeslot>();
		timeslots3.add(timeslot3);
		List<Location> onelocation=new ArrayList<Location>();
		onelocation.add(location);
		course1.setLocation(onelocation);
		course1.setTime(timeslots1);
		course1.allocateResource(teachers);
		CourseEntry<Teacher> course2=new CourseEntry<Teacher>("aa");
		CourseEntry<Teacher> course3=new CourseEntry<Teacher>("pp");
		course2.setLocation(onelocation);
		course2.setTime(timeslots2);
		course2.allocateResource(teachers);
		course3.setLocation(onelocation);
		course3.setTime(timeslots3);
		course3.allocateResource(teachers);
		List<CourseEntry<Teacher>> ces=new ArrayList<CourseEntry<Teacher>>();
		ces.add(course1);
		ces.add(course2);
		ces.add(course3);//课程中既有是当天的课程，又有不是当天的课程，看其是否正确划分并保存
		Calendar calendar=Calendar.getInstance();
		calendar.set(2020, 4-1, 5);
		CourseBoard cb=new CourseBoard(location, ces,calendar);
		cb.setRequestCourses(calendar);
		assertEquals(2, cb.courses.size());//应该只保存两个当天课程
	}
	
	//测试策略:初始顺序相反，观察是否排序成功
	@Test
	public void sortCoursesTest() throws ParseException {
		Teacher teacher=new Teacher("130283xxx", "name", true, "professtionalTitle");
		List<Teacher> teachers=new ArrayList<Teacher>();
		teachers.add(teacher);
		Location location=new Location("130E","45S" , "test", true);
		CourseEntry<Teacher> course1=new CourseEntry<Teacher>("rr");
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-05 12:15");
		Timeslot timeslot2=new Timeslot("2020-04-05 01:14", "2020-04-05 02:15");
		List<Timeslot> timeslots1=new ArrayList<Timeslot>();
		timeslots1.add(timeslot);
		List<Timeslot> timeslots2=new ArrayList<Timeslot>();
		timeslots2.add(timeslot2);
		List<Location> onelocation=new ArrayList<Location>();
		onelocation.add(location);
		course1.setLocation(onelocation);
		course1.setTime(timeslots1);
		course1.allocateResource(teachers);
		CourseEntry<Teacher> course2=new CourseEntry<Teacher>("aa");
		course2.setLocation(onelocation);
		course2.setTime(timeslots2);
		course2.allocateResource(teachers);
		List<CourseEntry<Teacher>> ces=new ArrayList<CourseEntry<Teacher>>();
		ces.add(course1);
		ces.add(course2);
		Calendar calendar=Calendar.getInstance();
		calendar.set(2020, 4-1, 5);
		CourseBoard cb=new CourseBoard(location, ces,calendar);
		cb.setRequestCourses(calendar);
		cb.sortCourses();
		assertEquals(course2.getName(), cb.courses.get(0).getName());
	}*/
	
	@Test
	public void iteratorTest() throws ParseException{
		Teacher teacher=new Teacher("130283xxx", "name", true, "professtionalTitle");
		List<Teacher> teachers=new ArrayList<Teacher>();
		teachers.add(teacher);
		Location location=new Location("130E","45S" , "test", true);
		CourseEntry<Teacher> course1=new CourseEntry<Teacher>("rr");
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-05 12:15");
		Timeslot timeslot2=new Timeslot("2020-04-05 01:14", "2020-04-05 02:15");
		List<Timeslot> timeslots1=new ArrayList<Timeslot>();
		timeslots1.add(timeslot);
		List<Timeslot> timeslots2=new ArrayList<Timeslot>();
		timeslots2.add(timeslot2);
		List<Location> onelocation=new ArrayList<Location>();
		onelocation.add(location);
		course1.setLocation(onelocation);
		course1.setTime(timeslots1);
		course1.allocateResource(teachers);
		CourseEntry<Teacher> course2=new CourseEntry<Teacher>("aa");
		course2.setLocation(onelocation);
		course2.setTime(timeslots2);
		course2.allocateResource(teachers);
		List<CourseEntry<Teacher>> ces=new ArrayList<CourseEntry<Teacher>>();
		ces.add(course1);
		ces.add(course2);
		Calendar calendar=Calendar.getInstance();
		calendar.set(2020, 4-1, 5);
		CourseBoard cb=new CourseBoard(location, ces,calendar);
		List<CourseEntry<Teacher>> cEntries=new ArrayList<>();
		for(Iterator<CourseEntry<Teacher>> iterator=cb.iterator();iterator.hasNext();) {
			cEntries.add(iterator.next());
		}
		
		assertEquals(2, cEntries.size());
	}
}
