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
	/*Ϊ�˱���get������ú���֮�����ϵ���Ƚ����Ը�Ϊpublic���в���
	//���Բ��ԣ�������ӷɻ����ظ���ӷɻ�
	//			���һ���ɻ�����Ӷ���ɻ�
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

	//Ϊ�˱���get������ú���֮�����ϵ���Ƚ����Ը�Ϊpublic���к����Ĳ���
	//���Բ��ԣ��������λ�ã��ظ����λ��
	//			���һ��λ�ã���Ӷ��λ��
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
	
	/*Ϊ�˱���get������ú���֮�����ϵ���Ƚ����Ը�Ϊpublic���к����Ĳ���
	//���Բ���:λ����δ�������λ���Ѿ��������
	//		   ����֮ǰ��δ�������������Ѿ�������λ�øı䣻�����Ѵ���,����ͬ���ڵ�ʱ���ı�;
	//		  �����Ѵ�������ȫ�ظ��ض��δ���;�����Ѵ��������ڲ�ͬʱ��㲻ͬ�������Ѵ��������ڲ�ͬʱ�����ͬ(��������Ӧ���ǳ�����)��
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
	
	//���Բ���:λ����δ�������λ���Ѿ��������
	//		   ����֮ǰ��δ�������������Ѿ�����
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
	
	//���Բ��ԣ�ָ�������Ѵ�����ָ������δ����
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
	
	//���Բ���:ɾ���ķɻ���δ������ɾ���ķɻ��Ѵ���
	//	   ɾ���ķɻ���ռ�ã�ɾ���ķɻ�δ��ռ��
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
	
	//���Բ���:ɾ����λ�û�δ������ɾ����λ���Ѵ���
	//		   ɾ����λ�ñ�ռ�ã�ɾ����λ��δ��ռ��
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
	
	//���Բ��ԣ�����ķɻ��Ѿ������������ķɻ���δ�������
	//			����δ�����������Ѵ���
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
	
	//���Բ��ԣ������Ѵ���������δ����
	//			�����ѷ���ɻ�������δ����ɻ�
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
	
	//���Բ��ԣ������Ѵ���������δ����
	//			����δ��ɣ����������
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
	
	//���Բ��ԣ������Ѵ���������δ����
	//			����δ��ɣ����������
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
	
	//���Բ���:��������������ȫ��ͬ����������������CA0001��CA01�Ƿ�����ȷ�б�Ϊ��ͬ
	//			�ַ����ֲ���ͬ�ĺ������ƣ����ֲ���ֵ��ͬ�ĺ�������
	@Test
	public void ifTwoSameFlightNameTest() {
		String name1="equals";
		String name2="equals";//��ȫ��ͬ
		assertTrue(fs.ifTwoSameFlightName(name1, name2));
		String name3="FA0007";
		String name4="FG0007";//�ַ����ֲ�ͬ
		assertFalse(fs.ifTwoSameFlightName(name3, name4));
		String name5="FA07";//���ֲ���ֵ��ͬ
		assertTrue(fs.ifTwoSameFlightName(name5, name3));
		String name6="FG0004";//���ֲ���ֵ��ͬ
		assertFalse(fs.ifTwoSameFlightName(name6, name4));
		}
}
