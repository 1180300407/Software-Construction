package Schedule;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Location.Location;
import Resources.Carriage;
import Timeslot.Timeslot;
import compositeinterface.TrainEntry;

public class TrainScheduleTest {
	TrainSchedule ts;
	Carriage carriage=new Carriage("1", "type", 5, "manufactureyear");
	Carriage carriage2=new Carriage("10", "type", 15, "manufactureyear");
	Location location=new Location("130E","45S" , "test", true);
	Location location2=new Location("10E","47n" , "tet", true);
	
	@Before
	public void prepare() {
		ts=new TrainSchedule();
	}
	
	/*为了避免get函数与该函数之间的联系，先将属性改为public进行测试
	//测试策略：单次添加车厢；重复添加车厢
	//			添加一个车厢；添加多个车厢
	@Test
	public void addCarriagetest() {
		ts.addCarriage(carriage);
		assertEquals(1, ts.carriages.size());
		assertTrue(ts.carriages.contains(carriage));
		ts.addCarriage(carriage);
		assertEquals(1, ts.carriages.size());
		ts.addCarriage(carriage2);
		assertEquals(2, ts.carriages.size());
		assertTrue(ts.carriages.contains(carriage2));
	}
	
	//为了避免get函数与该函数之间的联系，先将属性改为public进行函数的测试
	//测试策略：单次添加位置；重复添加位置
	//			添加一个位置；添加多个位置
	public void addLocationTest() {
		ts.addLocation(location);
		assertEquals(1, ts.locations.size());
		assertEquals(1, ts.locationnames.size());
		assertTrue(ts.locations.contains(location));
		assertTrue(ts.locationnames.contains(location.getName()));
		ts.addLocation(location);
		assertEquals(1, ts.locations.size());
		assertEquals(1, ts.locationnames.size());
		ts.addLocation(location2);
		assertEquals(2, ts.locations.size());
		assertEquals(2, ts.locationnames.size());
		assertTrue(ts.locations.contains(location2));
		assertTrue(ts.locationnames.contains(location2.getName()));
	}
	
	//为了避免get函数与该函数之间的联系，先将属性改为public进行函数的测试
	//测试策略:位置尚未纳入管理；位置已经纳入管理
	//		   列车之前从未创建过；列车已经创建
	//		  站点个数与时间个数不匹配；站点个数与时间个数匹配
	public void createTrainTest() {
		ts.addLocation(location);
		List<String> locations=new ArrayList<>();
		locations.add(location.getName());
		locations.add(location2.getName());
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		timeslots.add(timeslot);
		ts.createTrain("T120", locations, timeslots);
		assertEquals(0, ts.trains.size());
		ts.addLocation(location2);
		ts.createTrain("T120", locations, timeslots);
		assertEquals(1, ts.trains.size());
		ts.createTrain("T120", locations, timeslots);
		assertEquals(1, ts.trains.size());
		Location location3=new Location("10W","47S" , "e6", true);
		locations.add(location3.getName());
		ts.addLocation(location3);
		ts.createTrain("TR12", locations, timeslots);
		assertEquals(1, ts.trains.size());
	}*/
	
	//测试策略同前createTrains,将直接用属性获取的地方改为用get函数获取
	@Test
	public void getTrainsTest() {
		ts.addLocation(location);
		List<String> locations=new ArrayList<>();
		locations.add(location.getName());
		locations.add(location2.getName());
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		timeslots.add(timeslot);
		ts.createTrain("T120", locations, timeslots);
		assertEquals(0, ts.getTrains().size());
		ts.addLocation(location2);
		ts.createTrain("T120", locations, timeslots);
		assertEquals(1, ts.getTrains().size());
		ts.createTrain("T120", locations, timeslots);
		assertEquals(1, ts.getTrains().size());
		Location location3=new Location("10W","47S" , "e6", true);
		locations.add(location3.getName());
		ts.addLocation(location3);
		ts.createTrain("TR12", locations, timeslots);
		assertEquals(1, ts.getTrains().size());
	}
	
	//测试策略同前addCarriage,将直接用属性获取的地方改为用get函数获取
	@Test
	public void getCarriagesTest() {
		ts.addCarriage(carriage);
		assertEquals(1, ts.getCarriages().size());
		assertTrue(ts.getCarriages().contains(carriage));
		ts.addCarriage(carriage);
		assertEquals(1, ts.getCarriages().size());
		ts.addCarriage(carriage2);
		assertEquals(2, ts.getCarriages().size());
		assertTrue(ts.getCarriages().contains(carriage2));
	}
	
	//测试策略同前addLocation,将直接用属性获取的地方改为用get函数获取
	@Test
	public void getLocatonsTest() {
		ts.addLocation(location);
		assertEquals(1, ts.getLocations().size());
		assertEquals(1, ts.getLocationNames().size());
		assertTrue(ts.getLocations().contains(location));
		assertTrue(ts.getLocationNames().contains(location.getName()));
		ts.addLocation(location);
		assertEquals(1, ts.getLocations().size());
		assertEquals(1, ts.getLocationNames().size());
		ts.addLocation(location2);
		assertEquals(2, ts.getLocations().size());
		assertTrue(ts.getLocations().contains(location2));
		assertTrue(ts.getLocationNames().contains(location2.getName()));
	}
	
	//测试策略：车厢已纳入管理；车厢未纳入管理
	//			车厢已被分配；车厢未被分配
	@Test
	public void deleteCarriageTest() {
		ts.addCarriage(carriage);
		ts.deleteCarriage("10");
		assertEquals(1, ts.getCarriages().size());
		ts.addCarriage(carriage2);
		assertEquals(2, ts.getCarriages().size());
		ts.deleteCarriage("10");
		assertEquals(1, ts.getCarriages().size());
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		ts.addLocation(location);
		ts.addLocation(location2);
		List<String> locations=new ArrayList<>();
		locations.add(location.getName());
		locations.add(location2.getName());
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		timeslots.add(timeslot);
		ts.createTrain("G1238", locations, timeslots);
		List<String> ids=new ArrayList<String>();
		ids.add(carriage.getId());
		ts.allocateCarriage("G1238",ids );
		ts.deleteCarriage("1");
		assertEquals(1, ts.getCarriages().size());
	}
	
	//测试策略：位置已纳入管理；位置未纳入管理
	//			位置被占用；位置未被占用
	@Test
	public void deleteLocationTest() {
		ts.addLocation(location);
		ts.deleteLocation("tet");
		assertEquals(1, ts.getLocations().size());
		ts.addLocation(location2);
		assertEquals(2, ts.getLocations().size());
		ts.deleteLocation("tet");
		assertEquals(1, ts.getLocations().size());
		ts.addLocation(location2);
		List<String> locations=new ArrayList<>();
		locations.add(location.getName());
		locations.add(location2.getName());
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		timeslots.add(timeslot);
		ts.createTrain("G1238", locations, timeslots);
		ts.deleteLocation("tet");
		assertEquals(2, ts.getLocations().size());
	}
	
	//测试策略：车次已创建；车次未创建
	@Test
	public void getTrainStateTest() {
		ts.addLocation(location);
		ts.addLocation(location2);
		List<String> locations=new ArrayList<>();
		locations.add(location.getName());
		locations.add(location2.getName());
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		timeslots.add(timeslot);
		ts.createTrain("G1238", locations, timeslots);
		assertEquals("null", ts.getTrainState(""));
		assertEquals("Waiting", ts.getTrainState("G1238"));
	}
	
	//测试策略：车次已创建；车次未创建
	//			车厢已纳入管理；车厢未纳入管理
	//			单次分配；重复分配
	@Test
	public void allocateCarriageTest() {
		ts.addLocation(location);
		ts.addLocation(location2);
		List<String> locations=new ArrayList<>();
		locations.add(location.getName());
		locations.add(location2.getName());
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		timeslots.add(timeslot);
		ts.createTrain("G1238", locations, timeslots);
		List<String> carriages=new ArrayList<>();
		carriages.add(carriage.getId());
		ts.allocateCarriage("G1238", carriages);
		assertEquals("Waiting", ts.getTrainState("G1238"));//车厢未纳入管理
		ts.addCarriage(carriage);
		ts.allocateCarriage("G1238", carriages);
		assertEquals("Allocated", ts.getTrainState("G1238"));//单次分配
		ts.allocateCarriage("G1238", carriages);
		assertEquals("Allocated", ts.getTrainState("G1238"));//重复分配
		assertNotEquals("Allocated", ts.getTrainState("G0"));//列车未创建
	}
	
	//测试策略:列车已创建；列车未创建
	//		   列车已分配车厢；列车未分配车厢
	@Test
	public void startTrainTest() {
		ts.addLocation(location);
		ts.addLocation(location2);
		List<String> locations=new ArrayList<>();
		locations.add(location.getName());
		locations.add(location2.getName());
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		timeslots.add(timeslot);
		ts.createTrain("G1238", locations, timeslots);
		ts.startTrain("F4");
		assertEquals("Waiting", ts.getTrainState("G1238"));//列车未启动
		ts.startTrain("G1238");
		assertEquals("Waiting", ts.getTrainState("G1238"));//列车未分配
		List<String> carriages=new ArrayList<>();
		carriages.add(carriage.getId());
		ts.addCarriage(carriage);
		ts.allocateCarriage("G1238", carriages);
		ts.startTrain("G1238");
		assertEquals("Running", ts.getTrainState("G1238"));//列车已分配
	}
	
	//测试策略:列车已创建；列车未创建
	//			列车未启动；列车已启动
	@Test
	public void cancelTrainTest() {
		ts.addLocation(location);
		ts.addLocation(location2);
		List<String> locations=new ArrayList<>();
		locations.add(location.getName());
		locations.add(location2.getName());
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		timeslots.add(timeslot);
		ts.createTrain("G1238", locations, timeslots);
		ts.cancelTrain("F4");//列车未创建
		assertEquals("null", ts.getTrainState("F4"));
		assertEquals("Waiting", ts.getTrainState("G1238"));
		ts.cancelTrain("G1238");
		assertEquals("Cancelled", ts.getTrainState("G1238"));//列车未启动
		List<String> carriages=new ArrayList<>();
		carriages.add(carriage.getId());
		ts.addCarriage(carriage);
		Timeslot timeslot2=new Timeslot("2020-04-25 10:14", "2020-04-27 14:15");
		timeslots.remove(timeslot);
		timeslots.add(timeslot2);
		ts.createTrain("G147", locations, timeslots);
		ts.allocateCarriage("G147", carriages);
		ts.startTrain("G147");
		ts.cancelTrain("G147");
		assertEquals("Running", ts.getTrainState("G147"));//列车已启动
	}
	
	//测试策略:列车已创建；列车未创建
	//			列车未启动；列车已启动
	@Test
	public void endTrainTest() {
		ts.addLocation(location);
		ts.addLocation(location2);
		List<String> locations=new ArrayList<>();
		locations.add(location.getName());
		locations.add(location2.getName());
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		timeslots.add(timeslot);
		ts.createTrain("G1238", locations, timeslots);
		ts.endTrain("F4");//列车未创建
		assertEquals("null", ts.getTrainState("F4"));
		assertNotEquals("Ended", ts.getTrainState("G1238"));
		ts.endTrain("G1238");//列车未启动
		assertNotEquals("Ended", ts.getTrainState("G1238"));
		List<String> carriages=new ArrayList<>();
		carriages.add(carriage.getId());
		ts.addCarriage(carriage);
		ts.allocateCarriage("G1238", carriages);
		ts.startTrain("G1238");
		ts.endTrain("G1238");
		assertEquals("Ended", ts.getTrainState("G1238"));//列车已启动
	}
	
	//测试策略:列车未创建；列车已创建；
	//		     列车状态为Running;列车状态不是Running
	@Test
	public void blockTrainTest() throws ParseException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		Timeslot timeslot2=new Timeslot("2020-04-07 17:14" ,"2020-04-08 14:15");
		Timeslot timeslot3=new Timeslot("2020-04-09 17:14" ,"2020-04-10 14:15");
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
		timeslots.add(timeslot2);
		timeslots.add(timeslot3);
		ts.addLocation(location);
		ts.addLocation(location2);
		List<String> locations=new ArrayList<>();
		locations.add(location.getName());
		locations.add(location2.getName());
		Location location3=new Location("58E","45S" , "qes", true);
		Location location4=new Location("36E","45S" , "mk", true);
		ts.addLocation(location3);
		ts.addLocation(location4);
		locations.add(location4.getName());
		locations.add(location3.getName());
		ts.createTrain("G1238", locations, timeslots);
		ts.blockTrain("qw");//列车未创建
		assertNotEquals("Blocked", ts.getTrainState("G1238"));
		ts.blockTrain("G1238");
		assertNotEquals("Blocked", ts.getTrainState("G1238"));//还未启动，阻塞应该失败
		List<String> carriages=new ArrayList<>();
		carriages.add(carriage.getId());
		ts.addCarriage(carriage);
		ts.allocateCarriage("G1238", carriages);
		ts.startTrain("G1238");
		ts.blockTrain("G1238");
		assertEquals("Blocked", ts.getTrainState("G1238"));//启动后应该阻塞成功
	}
	
	@Test
	public void getTrainsofassignCarriageTest() {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
		ts.addLocation(location);
		ts.addLocation(location2);
		List<String> locations=new ArrayList<>();
		locations.add(location.getName());
		locations.add(location2.getName());
		ts.addCarriage(carriage);
		ts.addCarriage(carriage2);
		ts.createTrain("G124", locations, timeslots);
		List<String> carriages=new ArrayList<>();
		carriages.add(carriage.getId());
		ts.allocateCarriage("G124", carriages);
		ts.createTrain("T12", locations, timeslots);
		carriages.remove(0);
		carriages.add(carriage2.getId());
		ts.allocateCarriage("T12", carriages);
		List<TrainEntry<Carriage>> tes=ts.getTrainsofassignCarriage("1");
		assertEquals(1, tes.size());
		assertEquals("G124", tes.get(0).getName());
		tes=ts.getTrainsofassignCarriage("10");
		assertEquals(1, tes.size());
		assertEquals("T12", tes.get(0).getName());
	}
	
	@Test
	public void getTrainbyNameTest() {
		ts.addCarriage(carriage);
		ts.addLocation(location2);
		ts.addLocation(location);
		List<String> locationnames=new ArrayList<String>();
		locationnames.add(location.getName());
		locationnames.add(location2.getName());
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
		ts.createTrain("G74", locationnames, timeslots);
		assertEquals("G74", ts.getTrainbyName("G74").getName());
		assertEquals(2, ts.getTrainbyName("G74").getLocation().size());
		assertEquals(1, ts.getTrainbyName("G74").getTime().size());
	}
	
	@Test
	public void getCarriagebyIDTest() {
		ts.addCarriage(carriage);
		ts.addCarriage(carriage2);
		assertEquals("1", ts.getCarriagebyID("1").getId());
		assertEquals("10", ts.getCarriagebyID("10").getId());
		assertEquals("type", ts.getCarriagebyID("1").getType());
		assertEquals("type", ts.getCarriagebyID("10").getType());
	}
	
	@Test
	public void getLocationbyNameTest() {
		ts.addLocation(location);
		ts.addLocation(location2);
		assertEquals("test", ts.getLocationbyName("test").getName());
		assertEquals("tet", ts.getLocationbyName("tet").getName());
		assertEquals("130E", ts.getLocationbyName("test").getLongitude());
		assertEquals("47n", ts.getLocationbyName("tet").getLatitude());
	}
}
