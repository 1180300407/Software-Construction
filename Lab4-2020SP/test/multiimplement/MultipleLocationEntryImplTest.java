package multiimplement;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Location.Location;

public class MultipleLocationEntryImplTest {
	Location location1=new Location("157W", "30N", "name", true);
	Location location2=new Location("50E", "47S", "nam", false);
	Location location3=new Location("24E", "47S", "5", true);
	List<Location> locs=new ArrayList<Location>();
	MultipleLocationEntryImpl mle;
	
	@Before
	public void prepare() {
		locs.add(location1);
		locs.add(location2);
		locs.add(location3);
		mle=new MultipleLocationEntryImpl();
	}
	
	//为了解开set函数与get函数之间的耦合,将类中的locations和setbefore属性设置为public进行测试
	//测试策略:locations：设置一次是否成功;设置多次考察是否只能设置一次
	/*
	@Test
	public void setLocationstest1() {
		mle.setLocations(locs);
		assertEquals(locs, mle.locations);
		List<Location> l=new ArrayList<Location>();
		l.add(location1);
		l.add(location2);
		mle.setLocations(l);
		assertNotEquals(l, mle.locations);
	}*/
	
	
	@Test
	public void getLocationTest() {
		assertFalse(mle.getsetbefore());
		mle.setLocations(locs);
		assertEquals(locs, mle.getLocation());
		assertTrue(mle.getsetbefore());
	}
	
	//测试策略:位置个数等于2；位置个数大于2
	@Test
	public void setLocationsTest2() {
		List<Location> l=new ArrayList<Location>();
		l.add(location1);
		l.add(location2);
		mle.setLocations(l);
		assertEquals(l, mle.getLocation());
		assertTrue(mle.getsetbefore());
		
		MultipleLocationEntryImpl mle2=new MultipleLocationEntryImpl();
		mle2.setLocations(locs);
		assertEquals(locs, mle2.getLocation());
	}
}
