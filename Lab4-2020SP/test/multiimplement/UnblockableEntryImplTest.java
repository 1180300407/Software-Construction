package multiimplement;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import Timeslot.Timeslot;

public class UnblockableEntryImplTest {
	//测试策略:设置时间一次；设置时间n次(n>=2)
	//			单个时间对；n个时间对（n>=2）
	//为了解开set函数与get函数之间的耦合,将类中的timeslot属性设置为public进行测试
	/*
	@Test
	public void setTimetest() {
		Timeslot timeslot=new Timeslot("2005-04-17 10:47", "2005-04-18 14:54");
		Timeslot timeslot2=new Timeslot("2020-07-17 00:47", "2020-07-18 14:50");
		UnBlockableEntryImpl ube=new UnBlockableEntryImpl();
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot2);
		timeslots.add(timeslot);
		ube.setTime(timeslots);
		assertEquals(0, ube.timeslot.size());
		timeslots.remove(timeslot2);
		ube.setTime(timeslots);
		assertEquals(1, ube.timeslot.size());
		assertTrue(ube.timeslot.contains(timeslot));
		timeslots.remove(0);
		timeslots.add(timeslot2);
		ube.setTime(timeslots);
		assertEquals(1, ube.timeslot.size());
		assertTrue(ube.timeslot.contains(timeslot));
	}*/

	//测试策略:设置时间一次；设置时间n次(n>=2)
	//			单个时间对；n个时间对（n>=2）
	//			
	@Test
	public void getTimeTest() {
		Timeslot timeslot=new Timeslot("2005-04-17 10:47", "2005-04-18 14:54");
		Timeslot timeslot2=new Timeslot("2020-07-17 00:47", "2020-07-18 14:50");
		UnBlockableEntryImpl ube=new UnBlockableEntryImpl();
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot2);
		timeslots.add(timeslot);
		ube.setTime(timeslots);
		assertEquals(0, ube.getTime().size());
		timeslots.remove(timeslot2);
		ube.setTime(timeslots);
		assertEquals(1, ube.getTime().size());
		assertTrue(ube.getTime().contains(timeslot));
		timeslots.remove(0);
		timeslots.add(timeslot2);
		ube.setTime(timeslots);
		assertEquals(1, ube.getTime().size());
		assertTrue(ube.getTime().contains(timeslot));
	}
}
