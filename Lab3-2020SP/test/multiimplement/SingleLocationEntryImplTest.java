package multiimplement;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import Location.Location;

public class SingleLocationEntryImplTest {

	//测试策略:设置位置一次；设置位置n次(n>=2)
	//			位置个数为1；位置个数>1
	//为了解开set函数与get函数之间的耦合,将类中的location属性设置为public进行测试
	/*
	@Test
	public void setLocationtest() {
		Location location=new Location("157W", "30N", "name", true);
		Location location2=new Location("24E", "47S", "5", true);
		SingleLocationEntryImpl sle=new SingleLocationEntryImpl();
		List<Location> locations=new ArrayList<Location>();
		locations.add(location);
		locations.add(location2);
		sle.setLocation(locations);
		assertEquals(0, sle.location.size());
		locations.remove(location2);
		sle.setLocation(locations);
		assertEquals(1, sle.location.size());
		assertTrue(sle.location.contains(location));
		locations.remove(location);
		locations.add(location2);
		sle.setLocation(locations);
		assertEquals(1, sle.location.size());
		assertTrue(sle.location.contains(location2));
	}*/
	
	//测试策略:设置位置一次；设置位置n次(n>=2)
	//		   位置个数为1；位置个数>1
	@Test
	public void getLocationTest() {
		Location location=new Location("157W", "30N", "name", true);
		List<Location> locations=new ArrayList<Location>();
		Location location2=new Location("24E", "47S", "5", true);
		locations.add(location2);
		locations.add(location);
		SingleLocationEntryImpl sle=new SingleLocationEntryImpl();
		sle.setLocation(locations);
		assertEquals(0, sle.getLocation().size());
		locations.remove(location2);
		sle.setLocation(locations);
		assertEquals(1, sle.getLocation().size());
		assertTrue(sle.getLocation().contains(location));
		locations.remove(0);
		locations.add(location2);
		sle.setLocation(locations);
		assertEquals(1, sle.getLocation().size());
		assertTrue(sle.getLocation().contains(location2));
	}
}
