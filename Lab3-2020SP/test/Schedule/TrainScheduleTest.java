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
	
	/*Ϊ�˱���get������ú���֮�����ϵ���Ƚ����Ը�Ϊpublic���в���
	//���Բ��ԣ�������ӳ��᣻�ظ���ӳ���
	//			���һ�����᣻��Ӷ������
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
	
	//Ϊ�˱���get������ú���֮�����ϵ���Ƚ����Ը�Ϊpublic���к����Ĳ���
	//���Բ��ԣ��������λ�ã��ظ����λ��
	//			���һ��λ�ã���Ӷ��λ��
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
	
	//Ϊ�˱���get������ú���֮�����ϵ���Ƚ����Ը�Ϊpublic���к����Ĳ���
	//���Բ���:λ����δ�������λ���Ѿ��������
	//		   �г�֮ǰ��δ���������г��Ѿ�����
	//		  վ�������ʱ�������ƥ�䣻վ�������ʱ�����ƥ��
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
	
	//���Բ���ͬǰcreateTrains,��ֱ�������Ի�ȡ�ĵط���Ϊ��get������ȡ
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
	
	//���Բ���ͬǰaddCarriage,��ֱ�������Ի�ȡ�ĵط���Ϊ��get������ȡ
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
	
	//���Բ���ͬǰaddLocation,��ֱ�������Ի�ȡ�ĵط���Ϊ��get������ȡ
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
	
	//���Բ��ԣ������������������δ�������
	//			�����ѱ����䣻����δ������
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
	
	//���Բ��ԣ�λ�����������λ��δ�������
	//			λ�ñ�ռ�ã�λ��δ��ռ��
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
	
	//���Բ��ԣ������Ѵ���������δ����
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
	
	//���Բ��ԣ������Ѵ���������δ����
	//			�����������������δ�������
	//			���η��䣻�ظ�����
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
		assertEquals("Waiting", ts.getTrainState("G1238"));//����δ�������
		ts.addCarriage(carriage);
		ts.allocateCarriage("G1238", carriages);
		assertEquals("Allocated", ts.getTrainState("G1238"));//���η���
		ts.allocateCarriage("G1238", carriages);
		assertEquals("Allocated", ts.getTrainState("G1238"));//�ظ�����
		assertNotEquals("Allocated", ts.getTrainState("G0"));//�г�δ����
	}
	
	//���Բ���:�г��Ѵ������г�δ����
	//		   �г��ѷ��䳵�᣻�г�δ���䳵��
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
		assertEquals("Waiting", ts.getTrainState("G1238"));//�г�δ����
		ts.startTrain("G1238");
		assertEquals("Waiting", ts.getTrainState("G1238"));//�г�δ����
		List<String> carriages=new ArrayList<>();
		carriages.add(carriage.getId());
		ts.addCarriage(carriage);
		ts.allocateCarriage("G1238", carriages);
		ts.startTrain("G1238");
		assertEquals("Running", ts.getTrainState("G1238"));//�г��ѷ���
	}
	
	//���Բ���:�г��Ѵ������г�δ����
	//			�г�δ�������г�������
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
		ts.cancelTrain("F4");//�г�δ����
		assertEquals("null", ts.getTrainState("F4"));
		assertEquals("Waiting", ts.getTrainState("G1238"));
		ts.cancelTrain("G1238");
		assertEquals("Cancelled", ts.getTrainState("G1238"));//�г�δ����
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
		assertEquals("Running", ts.getTrainState("G147"));//�г�������
	}
	
	//���Բ���:�г��Ѵ������г�δ����
	//			�г�δ�������г�������
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
		ts.endTrain("F4");//�г�δ����
		assertEquals("null", ts.getTrainState("F4"));
		assertNotEquals("Ended", ts.getTrainState("G1238"));
		ts.endTrain("G1238");//�г�δ����
		assertNotEquals("Ended", ts.getTrainState("G1238"));
		List<String> carriages=new ArrayList<>();
		carriages.add(carriage.getId());
		ts.addCarriage(carriage);
		ts.allocateCarriage("G1238", carriages);
		ts.startTrain("G1238");
		ts.endTrain("G1238");
		assertEquals("Ended", ts.getTrainState("G1238"));//�г�������
	}
	
	//���Բ���:�г�δ�������г��Ѵ�����
	//		     �г�״̬ΪRunning;�г�״̬����Running
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
		ts.blockTrain("qw");//�г�δ����
		assertNotEquals("Blocked", ts.getTrainState("G1238"));
		ts.blockTrain("G1238");
		assertNotEquals("Blocked", ts.getTrainState("G1238"));//��δ����������Ӧ��ʧ��
		List<String> carriages=new ArrayList<>();
		carriages.add(carriage.getId());
		ts.addCarriage(carriage);
		ts.allocateCarriage("G1238", carriages);
		ts.startTrain("G1238");
		ts.blockTrain("G1238");
		assertEquals("Blocked", ts.getTrainState("G1238"));//������Ӧ�������ɹ�
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
