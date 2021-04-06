package multiimplement;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import Timeslot.Timeslot;

public class UnblockableEntryImplTest {
	//���Բ���:����ʱ��һ�Σ�����ʱ��n��(n>=2)
	//			����ʱ��ԣ�n��ʱ��ԣ�n>=2��
	//Ϊ�˽⿪set������get����֮������,�����е�timeslot��������Ϊpublic���в���
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

	//���Բ���:����ʱ��һ�Σ�����ʱ��n��(n>=2)
	//			����ʱ��ԣ�n��ʱ��ԣ�n>=2��
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
