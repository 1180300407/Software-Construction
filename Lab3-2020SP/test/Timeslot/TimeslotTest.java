package Timeslot;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeslotTest {

	@Test
	public void gettest() {
		Timeslot timeslot=new Timeslot("2020-04-07 10:08", "2020-05-08 15:17");
		assertEquals("2020-04-07 10:08", timeslot.getStarttime());
		assertEquals("2020-05-08 15:17", timeslot.getEndtime());
	}
}
