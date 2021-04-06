package multiimplement;

import static org.junit.Assert.*;

import org.junit.Test;

import Location.Location;

public class TwoLocationEntryImplTest {

	//测试策略:设置位置一次；设置位置n次(n>=2)
	//为了解开set函数与get函数之间的耦合,将类中的location属性设置为public进行测试
	/*
	@Test
	public void setLocationstest() {
		Location location1=new Location("157W", "30N", "name", true);
		Location location2=new Location("50E", "47S", "nam", false);
		Location location3=new Location("24E", "47S", "5", true);
		TwoLocationEntryImpl tle=new TwoLocationEntryImpl();
		tle.setLocations(location1, location2);
		assertEquals(location1, tle.start);
		assertEquals(location2, tle.end);
		tle.setLocations(location3, location3);
		assertEquals(location1, tle.start);
		assertEquals(location2, tle.end);
	}*/
	
	////测试策略:设置位置一次；设置位置n次(n>=2)
	@Test
	public void getTest() {
		Location location1=new Location("157W", "30N", "name", true);
		Location location2=new Location("50E", "47S", "nam", false);
		Location location3=new Location("24E", "47S", "5", true);
		TwoLocationEntryImpl tle=new TwoLocationEntryImpl();
		tle.setLocations(location1, location2);
		assertEquals(location1, tle.getStart());
		assertEquals(location2, tle.getEnd());
		tle.setLocations(location3, location3);
		assertEquals(location1, tle.getStart());
		assertEquals(location2, tle.getEnd());
	}
}
