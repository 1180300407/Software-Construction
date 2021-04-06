package Schedule;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Location.Location;
import Resources.Plane;
import Timeslot.Timeslot;
import compositeinterface.FlightEntry;

public class FlightScheduleTest {
	FlightSchedule fs;
	Plane plane=new Plane("N3025", "type", 50, 2.5);
	Location start=new Location("130E","45S" , "test", true);
	Location end=new Location("10E","47n" , "tet", true);
	
	@Before
	public void prepare() {
		fs=new FlightSchedule();
	}
	/*为了避免get函数与该函数之间的联系，先将属性改为public进行测试
	//测试策略：单次添加飞机；重复添加飞机
	//			添加一个飞机；添加多个飞机
	@Test
	public void addPlanetest() {
		fs.addPlane(plane);
		assertEquals(1,fs.planes.size());
		assertTrue(fs.planes.contains(plane));
		Plane plane2=new Plane("N3074", "type", 50, 2.5);
		fs.addPlane(plane2);
		assertEquals(2,fs.planes.size());
		assertTrue(fs.planes.contains(plane2));
		fs.addPlane(plane);
		assertEquals(2,fs.planes.size());
	}

	//为了避免get函数与该函数之间的联系，先将属性改为public进行函数的测试
	//测试策略：单次添加位置；重复添加位置
	//			添加一个位置；添加多个位置
	@Test
	public void addLocationTest() {
		fs.addLocation(start);
		assertEquals(1, fs.locations.size());
		assertTrue(fs.locations.contains(start));
		fs.addLocation(end);
		assertTrue(fs.locations.contains(end));
		assertEquals(2, fs.locations.size());
		fs.addLocation(end);
		assertEquals(2, fs.locations.size());
	}*/
	
	@Test
	public void getPlanesTest() {
		fs.addPlane(plane);
		Plane plane2=new Plane("N3074", "type", 50, 2.5);
		assertEquals(1, fs.getPlanes().size());
		assertTrue(fs.getPlanes().contains(plane));
		fs.addPlane(plane2);
		assertTrue(fs.getPlanes().contains(plane2));
		assertEquals(2, fs.getPlanes().size());
	}
	
	@Test
	public void getLocationsTest() {
		fs.addLocation(start);
		assertEquals(1, fs.getLocations().size());
		assertTrue(fs.getLocations().contains(start));
		fs.addLocation(end);
		assertEquals(2, fs.getLocations().size());
		assertTrue(fs.getLocations().contains(end));
	}
	
	/*为了避免get函数与该函数之间的联系，先将属性改为public进行函数的测试
	//测试策略:位置尚未纳入管理；位置已经纳入管理
	//		   航班之前从未创建过；航班已经创建但位置改变；航班已创建,有相同日期但时间点改变;
	//		  航班已创建，完全重复地二次创建;航班已创建，日期不同时间点不同；航班已创建，日期不同时间点相同(这种情形应该是成立的)；
	@Test
	public void createFlightTest() {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
		List<String> strings=new ArrayList<String>();
		strings.add("test");
		strings.add("tet");
		fs.createFlight("NR7412", strings, timeslots);
		assertEquals(0, fs.flights.size());
		fs.addLocation(start);
		fs.addLocation(end);
		fs.createFlight("NR7412", strings, timeslots);
		assertEquals(1, fs.flights.size());
		fs.createFlight("NR7412", strings, timeslots);
		assertEquals(1, fs.flights.size());
		Timeslot timeslot2=new Timeslot("2020-04-06 10:14", "2020-04-07 14:15");
		List<Timeslot> timeslots2=new ArrayList<Timeslot>();
		timeslots2.add(timeslot2);
		fs.createFlight("NR7412", strings, timeslots2);
		assertEquals(1, fs.flights.size());
		fs.createFlight("NR7412",strings, timeslots2);
		assertEquals(1, fs.flights.size());
		Timeslot timeslot3=new Timeslot("2020-04-03 12:14", "2020-04-04 14:15");
		List<Timeslot> timeslots3=new ArrayList<Timeslot>();
		timeslots3.add(timeslot3);
		fs.createFlight("NR7412", strings, timeslots3);
		assertEquals(1, fs.flights.size());
		Timeslot timeslot4=new Timeslot("2020-04-03 10:14", "2020-04-04 14:15");
		List<Timeslot> timeslots4=new ArrayList<Timeslot>();
		timeslots4.add(timeslot4);
		fs.createFlight("NR7412", strings, timeslots4);
		assertEquals(2, fs.flights.size());
	}*/
	
	//测试策略:位置尚未纳入管理；位置已经纳入管理
	//		   航班之前从未创建过；航班已经创建
	@Test
	public void getFlightsTest() throws ParseException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
		List<String> strings=new ArrayList<String>();
		strings.add("test");
		strings.add("tet");
		fs.createFlight("NR7412",strings, timeslots);
		assertEquals(0, fs.getFlights().size());
		fs.addLocation(start);
		fs.addLocation(end);
		fs.createFlight("NR7412", strings, timeslots);
		assertEquals(1, fs.getFlights().size());
		fs.createFlight("NR7412", strings, timeslots);
		assertEquals(1, fs.getFlights().size());
	}
	
	//测试策略：指定航班已创建；指定航班未创建
	@Test
	public void getFlightStateTest() {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
		List<String> strings=new ArrayList<String>();
		strings.add("test");
		strings.add("tet");
		fs.addLocation(end);
		fs.addLocation(start);
		fs.createFlight("NR7412", strings, timeslots);
		assertEquals("null", fs.getFlightState("",timeslots));
		assertEquals("Waiting", fs.getFlightState("NR7412",timeslots));
	}
	
	//测试策略:删除的飞机还未创建；删除的飞机已创建
	//	   删除的飞机被占用；删除的飞机未被占用
	@Test
	public void deletePlaneTest() {
		fs.addPlane(plane);
		Plane plane2=new Plane("N3074", "type", 50, 2.5);
		fs.deletePlane("N3074");
		assertEquals(1, fs.getPlanes().size());
		fs.addPlane(plane2);
		fs.deletePlane("N3074");
		assertEquals(1, fs.getPlanes().size());
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		fs.addLocation(start);
		fs.addLocation(end);
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
		List<String> strings=new ArrayList<String>();
		strings.add("test");
		strings.add("tet");
		fs.createFlight("NQ7851", strings, timeslots);
		fs.allocatePlane("NQ7851", "N3025",timeslots);
		fs.deletePlane("N3025");
		assertEquals(1, fs.getPlanes().size());
	}
	
	//测试策略:删除的位置还未创建；删除的位置已创建
	//		   删除的位置被占用；删除的位置未被占用
	@Test
	public void deleteLocationTest() {
		fs.addLocation(start);
		fs.deleteLocation("tet");
		assertEquals(1, fs.getLocations().size());
		fs.addLocation(end);
		assertEquals(2, fs.getLocations().size());
		fs.deleteLocation("tet");
		assertEquals(1, fs.getLocations().size());
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
		List<String> strings=new ArrayList<String>();
		strings.add("test");
		strings.add("tet");
		fs.addLocation(end);
		fs.createFlight("NQ7851", strings, timeslots);
		fs.deleteLocation("tet");
		assertEquals(2, fs.getLocations().size());
	}
	
	//测试策略：分配的飞机已经纳入管理；分配的飞机还未纳入管理
	//			航班未创建；航班已创建
	@Test
	public void allocatePlaneTest() {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		fs.addLocation(start);
		fs.addLocation(end);
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
		List<String> strings=new ArrayList<String>();
		strings.add("test");
		strings.add("tet");
		fs.createFlight("NR7412", strings, timeslots);
		fs.allocatePlane("NR7412", "N3025",timeslots);
		assertNotEquals("Allocated", fs.getFlightState("NR7412",timeslots));
		fs.addPlane(plane);
		fs.allocatePlane("NULL", "N3025",timeslots);
		assertNotEquals("Allocated", fs.getFlightState("NR7412",timeslots));
		assertNotEquals("Allocated", fs.getFlightState("NULL",timeslots));
		fs.allocatePlane("NR7412", "N3025",timeslots);
		assertEquals("Allocated", fs.getFlightState("NR7412",timeslots));
	}
	
	//测试策略：航班已创建；航班未创建
	//			航班已分配飞机；航班未分配飞机
	@Test
	public void depatureTest() {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		fs.addLocation(start);
		fs.addLocation(end);
		fs.addPlane(plane);
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
		List<String> strings=new ArrayList<String>();
		strings.add("test");
		strings.add("tet");
		fs.createFlight("NR7412", strings, timeslots);
		Timeslot timeslot2=new Timeslot("2020-04-01 10:14", "2020-04-03 14:15");
		List<Timeslot> timeslots2=new ArrayList<Timeslot>();
		timeslots2.add(timeslot2);
		fs.createFlight("NR7417", strings, timeslots2);
		fs.departure("null",timeslots);
		assertNotEquals("Running", fs.getFlightState("NR7412",timeslots));
		assertNotEquals("Running", fs.getFlightState("NR7417",timeslots2));
		fs.allocatePlane("NR7417", "N3025",timeslots2);
		fs.departure("NR7412",timeslots);
		assertNotEquals("Running", fs.getFlightState("NR7412",timeslots));
		fs.allocatePlane("NR7417", "N3025",timeslots2);
		fs.departure("NR7417",timeslots2);
		assertEquals("Running", fs.getFlightState("NR7417",timeslots2));
	}
	
	//测试策略：航班已创建；航班未创建
	//			航班未起飞；航班已起飞
	@Test
	public void cancelFlightTest() {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		fs.addLocation(start);
		fs.addLocation(end);
		fs.addPlane(plane);
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
		List<String> strings=new ArrayList<String>();
		strings.add("test");
		strings.add("tet");
		fs.createFlight("NR7412", strings, timeslots);
		fs.cancelFlight("null",timeslots);
		assertNotEquals("Cancelled", fs.getFlightState("NR7412",timeslots));
		Timeslot timeslot2=new Timeslot("2020-04-01 10:14", "2020-04-03 14:15");
		List<Timeslot> timeslots2=new ArrayList<Timeslot>();
		timeslots2.add(timeslot2);
		fs.createFlight("NR7417", strings, timeslots2);
		fs.allocatePlane("NR7412", "N3025",timeslots);
		fs.departure("NR7412",timeslots);
		fs.cancelFlight("NR7412",timeslots);
		fs.cancelFlight("NR7417",timeslots2);
		assertNotEquals("Cancelled", fs.getFlightState("NR7412",timeslots));
		assertEquals("Cancelled", fs.getFlightState("NR7417",timeslots2));
	}
	
	//测试策略：航班已创建；航班未创建
	//			航班未起飞；航班已起飞
	@Test
	public void endFlightTest() {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		fs.addLocation(start);
		fs.addLocation(end);
		fs.addPlane(plane);
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
		List<String> strings=new ArrayList<String>();
		strings.add("test");
		strings.add("tet");
		fs.createFlight("NR7412", strings, timeslots);
		fs.endFlight("null",timeslots);
		assertNotEquals("Ended", fs.getFlightState("NR7412",timeslots));
		Timeslot timeslot2=new Timeslot("2020-04-01 10:14", "2020-04-03 14:15");
		List<Timeslot> timeslots2=new ArrayList<Timeslot>();
		timeslots2.add(timeslot2);
		fs.createFlight("NR7417", strings, timeslots2);
		fs.allocatePlane("NR7412", "N3025",timeslots);
		fs.departure("NR7412",timeslots);
		fs.endFlight("NR7412",timeslots);
		fs.endFlight("NR7417",timeslots2);
		assertNotEquals("Ended", fs.getFlightState("NR7417",timeslots2));
		assertEquals("Ended", fs.getFlightState("NR7412",timeslots));
	}
	
	@Test
	public void getFlightsofassignPlanetest() {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-05 14:15");
		fs.addPlane(plane);
		Plane plane2=new Plane("N3125", "type", 50, 2.5);
		fs.addPlane(plane2);
		fs.addLocation(start);
		fs.addLocation(end);
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
		List<String> strings=new ArrayList<String>();
		strings.add("test");
		strings.add("tet");
		Timeslot timeslot2=new Timeslot("2020-04-04 10:14", "2020-04-04 14:15");
		List<Timeslot> timeslots2=new ArrayList<Timeslot>();
		timeslots2.add(timeslot2);
		fs.createFlight("NA5874", strings, timeslots2);
		fs.createFlight("WR7412", strings, timeslots);
		fs.allocatePlane("NA5874", "N3125",timeslots2);
		fs.allocatePlane("WR7412", "N3025",timeslots);
		List<FlightEntry<Plane>> fes=fs.getFlightssofassignPlane("N3025");
		assertEquals(1, fes.size());
		assertEquals("WR7412", fes.get(0).getName());
		fes=fs.getFlightssofassignPlane("N3125");
		assertEquals(1, fes.size());
		assertEquals("NA5874", fes.get(0).getName());
	}
	
	
	@Test
	public void getPlanebyIDTest() {
		fs.addPlane(plane);
		assertEquals("N3025", fs.getPlanebyID("N3025").getId());
		assertEquals("type", fs.getPlanebyID("N3025").getType());
	}
	
	@Test
	public void getLocationbyNameTest() {
		fs.addLocation(start);
		fs.addLocation(end);
		assertEquals("test", fs.getLocationbyName("test").getName());
		assertEquals("tet", fs.getLocationbyName("tet").getName());
		assertEquals("10E", fs.getLocationbyName("tet").getLongitude());
		assertEquals("45S", fs.getLocationbyName("test").getLatitude());
	}
	
	@Test
	public void getFlightbyNameTest() {
		fs.addLocation(start);
		fs.addLocation(end);
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-05 14:15");
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
		List<String> strings=new ArrayList<String>();
		strings.add("test");
		strings.add("tet");
		fs.createFlight("NH7412", strings, timeslots);
		assertEquals("NH7412", fs.getFlightbyName("NH7412",timeslots).getName());
		assertEquals(2, fs.getFlightbyName("NH7412",timeslots).getLocation().size());
		assertEquals(1, fs.getFlightbyName("NH7412",timeslots).getTime().size());
	}
	
	//测试策略:两个航班名称完全相同；两个航班名称如CA0001和CA01是否能正确判别为相同
	//			字符部分不相同的航班名称；数字部分值不同的航班名称
	@Test
	public void ifTwoSameFlightNameTest() {
		String name1="equals";
		String name2="equals";//完全相同
		assertTrue(fs.ifTwoSameFlightName(name1, name2));
		String name3="FA0007";
		String name4="FG0007";//字符部分不同
		assertFalse(fs.ifTwoSameFlightName(name3, name4));
		String name5="FA07";//数字部分值相同
		assertTrue(fs.ifTwoSameFlightName(name5, name3));
		String name6="FG0004";//数字部分值不同
		assertFalse(fs.ifTwoSameFlightName(name6, name4));
		}
}
