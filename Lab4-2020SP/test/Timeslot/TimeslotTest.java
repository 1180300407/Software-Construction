package Timeslot;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class TimeslotTest {

	@Test
	public void gettest() {
		Timeslot timeslot=new Timeslot("2020-04-07 10:08", "2020-05-08 15:17");
		assertEquals("2020-04-07 10:08", timeslot.getStarttime());
		assertEquals("2020-05-08 15:17", timeslot.getEndtime());
	}
	
	@Test
	public void hashCodeTest() {
		Set<Timeslot> timeslots=new HashSet<Timeslot>();
		Timeslot timeslot=new Timeslot("2020-04-07 10:08", "2020-05-08 15:17");
		Timeslot timeslot2=new Timeslot("2020-04-07 10:08", "2020-05-08 15:17");
		assertEquals(timeslot.hashCode(), timeslot2.hashCode());
		timeslots.add(timeslot);
		assertFalse(timeslots.add(timeslot2));
	}
	
	@Test
	public void equalsTest() {
		Timeslot timeslot=new Timeslot("2020-04-07 10:08", "2020-05-08 15:17");
		Timeslot timeslot2=new Timeslot("2020-04-07 10:08", "2020-05-08 15:17");
		assertEquals(timeslot,timeslot2);
		Timeslot timeslot3=new Timeslot("2020-05-07 10:08", "2020-05-08 15:17");
		assertNotEquals(timeslot,timeslot3);
		Timeslot timeslot4=new Timeslot("2020-04-07 10:08", "2020-04-08 15:17");
		assertNotEquals(timeslot,timeslot4);
		String others="2020-04-07 10:08"+"2020-05-08 15:17";
		assertNotEquals(timeslot,others);
	}
}
