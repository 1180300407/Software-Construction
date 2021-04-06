package Schedule;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import Exceptions.*;
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
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	/*Ϊ�˱���get������ú���֮�����ϵ���Ƚ����Ը�Ϊpublic���в���
	//���Բ��ԣ�������ӷɻ����ظ���ӷɻ�
	//			���һ���ɻ�����Ӷ���ɻ�
	@Test
	public void addPlanetest() throws IllegalPlaneContentException {
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
	public void getPlanesTest() throws IllegalPlaneContentException {
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
	
	//Ϊ�˱���get������ú���֮�����ϵ���Ƚ����Ը�Ϊpublic���к����Ĳ���
	//���Բ���:λ����δ�������λ���Ѿ��������
	//		   ����֮ǰ��δ�������������Ѿ�������λ�øı䣻�����Ѵ���,����ͬ���ڵ�ʱ���ı�;
	//		  �����Ѵ�������ȫ�ظ��ض��δ���;�����Ѵ��������ڲ�ͬʱ��㲻ͬ�������Ѵ��������ڲ�ͬʱ�����ͬ(��������Ӧ���ǳ�����)��
	//�쳣�����λ����δ�������
	@Test
	public void createFlightTest() throws InconsistentStartOrEndException, LocationNotFoundException, SameLabelException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		expectedEx.expect(LocationNotFoundException.class);
		fs.createFlight("NR7412", "test", "tet", timeslot);
	}
	
	/*λ���Ѿ������������֮ǰ��δ������
	@Test
	public void createFlightTest2() throws InconsistentStartOrEndException, LocationNotFoundException, SameLabelException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		fs.addLocation(start);
		fs.addLocation(end);
		fs.createFlight("NR7412", "test", "tet", timeslot);
		assertEquals(1, fs.flights.size());
	}
	
	//�쳣����������Ѵ�������ȫ�ظ��ض��δ���
	@Test
	public void createFlightTest3() throws InconsistentStartOrEndException, LocationNotFoundException, SameLabelException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		fs.addLocation(start);
		fs.addLocation(end);
		fs.createFlight("NR7412", "test", "tet", timeslot);
		assertEquals(1, fs.flights.size());
		expectedEx.expect(SameLabelException.class);
		fs.createFlight("NR7412", "test", "tet", timeslot);
	}
	
	//�쳣����������Ѿ�������λ�øı�
	@Test
	public void createFlightTest4() throws InconsistentStartOrEndException, LocationNotFoundException, SameLabelException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		fs.addLocation(start);
		fs.addLocation(end);
		fs.createFlight("NR7412", "test", "tet", timeslot);
		assertEquals(1, fs.flights.size());
		expectedEx.expect(InconsistentStartOrEndException.class);
		fs.createFlight("NR7412", "tet", "test", timeslot);
	}
	
	//�쳣����������Ѵ���,����ͬ���ڵ�ʱ���ı�;
	@Test
	public void createFlightTest5() throws InconsistentStartOrEndException, LocationNotFoundException, SameLabelException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		fs.addLocation(start);
		fs.addLocation(end);
		fs.createFlight("NR7412", "test", "tet", timeslot);
		assertEquals(1, fs.flights.size());
		Timeslot timeslot2=new Timeslot("2020-04-06 10:14", "2020-04-07 14:15");
		expectedEx.expect(InconsistentStartOrEndException.class);
		fs.createFlight("NR7412", "test", "tet", timeslot2);
	}
	
	//�쳣����������Ѵ��������ڲ�ͬʱ��㲻ͬ
	@Test
	public void createFlightTest6() throws InconsistentStartOrEndException, LocationNotFoundException, SameLabelException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		fs.addLocation(start);
		fs.addLocation(end);
		fs.createFlight("NR7412", "test", "tet", timeslot);
		assertEquals(1, fs.flights.size());
		Timeslot timeslot3=new Timeslot("2020-04-03 12:14", "2020-04-04 14:15");
		expectedEx.expect(InconsistentStartOrEndException.class);
		fs.createFlight("NR7412", "test", "tet", timeslot3);
	}
	
	//�����Ѵ��������ڲ�ͬʱ�����ͬ
	@Test
	public void createFlightTest7() throws InconsistentStartOrEndException, LocationNotFoundException, SameLabelException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		fs.addLocation(start);
		fs.addLocation(end);
		fs.createFlight("NR7412", "test", "tet", timeslot);
		assertEquals(1, fs.flights.size());
		Timeslot timeslot4=new Timeslot("2020-04-03 10:14", "2020-04-04 14:15");
		fs.createFlight("NR7412", "test", "tet", timeslot4);
		assertEquals(2, fs.flights.size());
	}*/
	
	//���Բ���: λ��������� ��λ����δ�������
	//			����֮ǰ��δ�������������Ѿ�������
	//�쳣���:λ����δ�������
	@Test
	public void getFlightsTest() throws ParseException, InconsistentStartOrEndException, LocationNotFoundException, SameLabelException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		expectedEx.expect(LocationNotFoundException.class);
		fs.createFlight("NR7412","test", "tet", timeslot);
	}
	
	// λ���������  ����֮ǰ��δ��������
	@Test
	public void getFlightsTest2() throws ParseException, InconsistentStartOrEndException, LocationNotFoundException, SameLabelException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		fs.addLocation(start);
		fs.addLocation(end);
		fs.createFlight("NR7412", "test", "tet", timeslot);
		assertEquals(1, fs.getFlights().size());
	}
	
	//�쳣���������֮ǰ�Ѵ�����
	@Test
	public void getFlightsTest3() throws ParseException, InconsistentStartOrEndException, LocationNotFoundException, SameLabelException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		fs.addLocation(start);
		fs.addLocation(end);
		fs.createFlight("NR7412", "test", "tet", timeslot);
		assertEquals(1, fs.getFlights().size());
		expectedEx.expect(SameLabelException.class);
		fs.createFlight("NR7412", "test", "tet", timeslot);
	}
	
	//���Բ��ԣ�ָ�������Ѵ�����ָ������δ����
	//ָ�������Ѵ���
	@Test
	public void getFlightStateTest() throws InconsistentStartOrEndException, LocationNotFoundException, SameLabelException, PlanEntryNotCreateException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		fs.addLocation(end);
		fs.addLocation(start);
		fs.createFlight("NR7412", "test", "tet", timeslot);
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
		assertEquals("Waiting", fs.getFlightState("NR7412",timeslots));
	}
	
	//ָ������δ����
	@Test
	public void getFlightStateTest2() throws InconsistentStartOrEndException, LocationNotFoundException, SameLabelException, PlanEntryNotCreateException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
		expectedEx.expect(PlanEntryNotCreateException.class);
		fs.getFlightState("nul;",timeslots);
	}
	
	//���Բ���:ɾ���ķɻ���δ������ɾ���ķɻ��Ѵ���
	//	   	     ɾ���ķɻ���ռ�ã�ɾ���ķɻ�δ��ռ��
	//�쳣�����ɾ���ķɻ���δ����
	@Test
	public void deletePlaneTest() throws IllegalPlaneContentException, InconsistentStartOrEndException, LocationNotFoundException, SameLabelException, ResourceNotFoundException, PlanEntryOccupyResourceException, ResourceConflictException, PlanEntryNotCreateException {
		expectedEx.expect(ResourceNotFoundException.class);
		fs.deletePlane("N3025");
	}
	
	//ɾ���ķɻ��Ѵ�����δ��ռ��
	@Test
	public void deletePlaneTest2() throws IllegalPlaneContentException, InconsistentStartOrEndException, LocationNotFoundException, SameLabelException, ResourceNotFoundException, PlanEntryOccupyResourceException, ResourceConflictException, PlanEntryNotCreateException {
		fs.addPlane(plane);
		Plane plane2=new Plane("N3074", "type", 50, 2.5);
		assertEquals(1, fs.getPlanes().size());
		fs.addPlane(plane2);
		fs.deletePlane("N3074");
		assertEquals(1, fs.getPlanes().size());
	}
	
	//�쳣�����ɾ���ķɻ���ռ��
	@Test
	public void deletePlaneTest3() throws IllegalPlaneContentException, InconsistentStartOrEndException, LocationNotFoundException, SameLabelException, ResourceNotFoundException, PlanEntryOccupyResourceException, ResourceConflictException, PlanEntryNotCreateException {
		fs.addPlane(plane);
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		fs.addLocation(start);
		fs.addLocation(end);
		fs.createFlight("NQ7851", start.getName(), end.getName(), timeslot);
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
		fs.allocatePlane("NQ7851", "N3025",timeslots);
		expectedEx.expect(PlanEntryOccupyResourceException.class);
		fs.deletePlane("N3025");
	}
	
	//���Բ���:ɾ����λ�û�δ������ɾ����λ���Ѵ���
	//		   ɾ����λ�ñ�ռ�ã�ɾ����λ��δ��ռ��
	//�쳣���:ɾ����λ��δ����
	@Test
	public void deleteLocationTest() throws InconsistentStartOrEndException, LocationNotFoundException, SameLabelException, PlanEntryOccupyLocationException {
		expectedEx.expect(LocationNotFoundException.class);
		fs.deleteLocation("tet");
	}
	
	//ɾ����λ���Ѵ�����ɾ����λ��δ��ռ��
	@Test
	public void deleteLocationTest2() throws InconsistentStartOrEndException, LocationNotFoundException, SameLabelException, PlanEntryOccupyLocationException {
		fs.addLocation(start);
		fs.addLocation(end);
		assertEquals(2, fs.getLocations().size());
		fs.deleteLocation("tet");
		assertEquals(1, fs.getLocations().size());
	}
	
	//�쳣���:ɾ����λ�ñ�ռ��
	@Test
	public void deleteLocationTest3() throws InconsistentStartOrEndException, LocationNotFoundException, SameLabelException, PlanEntryOccupyLocationException {
		fs.addLocation(start);
		fs.addLocation(end);
		assertEquals(2, fs.getLocations().size());
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		fs.createFlight("NQ7851", start.getName(), end.getName(), timeslot);
		expectedEx.expect(PlanEntryOccupyLocationException.class);
		fs.deleteLocation("tet");
	}
	
	//���Բ��ԣ�����ķɻ��Ѿ������������ķɻ���δ�������
	//			����δ�����������Ѵ���
	//�쳣���:����ķɻ���δ�������
	@Test
	public void allocatePlaneTest() throws ResourceNotFoundException, ResourceConflictException, PlanEntryNotCreateException, InconsistentStartOrEndException, LocationNotFoundException, SameLabelException, IllegalPlaneContentException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		fs.addLocation(start);
		fs.addLocation(end);
		fs.createFlight("NR7412", "test", "tet", timeslot);
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
		expectedEx.expect(ResourceNotFoundException.class);
		fs.allocatePlane("NR7412", "N3025",timeslots);
	}
	
	//����ķɻ��Ѿ�������������Ѵ���
	@Test
	public void allocatePlaneTest2() throws ResourceNotFoundException, ResourceConflictException, PlanEntryNotCreateException, InconsistentStartOrEndException, LocationNotFoundException, SameLabelException, IllegalPlaneContentException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		fs.addLocation(start);
		fs.addLocation(end);
		fs.createFlight("NR7412", "test", "tet", timeslot);
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
		fs.addPlane(plane);
		fs.allocatePlane("NR7412", "N3025",timeslots);
		assertEquals("Allocated", fs.getFlightState("NR7412",timeslots));
	}

	//�쳣�����	����δ������
	@Test
	public void allocatePlaneTest3() throws ResourceNotFoundException, ResourceConflictException, PlanEntryNotCreateException, InconsistentStartOrEndException, LocationNotFoundException, SameLabelException, IllegalPlaneContentException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		fs.addLocation(start);
		fs.addLocation(end);
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
		fs.addPlane(plane);
		expectedEx.expect(PlanEntryNotCreateException.class);
		fs.allocatePlane("NULL", "N3025",timeslots);
	}
	
	//���Բ��ԣ������Ѵ���������δ����
	//			�����ѷ���ɻ�������δ����ɻ�
	//�쳣���������δ����
	@Test
	public void depatureTest() throws ResourceNotFoundException, ResourceConflictException, PlanEntryNotCreateException, InconsistentStartOrEndException, LocationNotFoundException, SameLabelException, IllegalPlaneContentException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
		expectedEx.expect(PlanEntryNotCreateException.class);
		fs.departure("null",timeslots);
	}
	
	//�����Ѵ����������ѷ���ɻ��������Ѵ���������δ����ɻ�
	@Test
	public void depatureTest2() throws ResourceNotFoundException, ResourceConflictException, PlanEntryNotCreateException, InconsistentStartOrEndException, LocationNotFoundException, SameLabelException, IllegalPlaneContentException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		fs.addLocation(start);
		fs.addLocation(end);
		fs.addPlane(plane);
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
		fs.createFlight("NR7412", "test", "tet", timeslot);
		Timeslot timeslot2=new Timeslot("2020-04-01 10:14", "2020-04-03 14:15");
		List<Timeslot> timeslots2=new ArrayList<Timeslot>();
		timeslots2.add(timeslot2);
		fs.createFlight("NR7417", "test", "tet", timeslot2);
		fs.allocatePlane("NR7417", "N3025",timeslots2);
		fs.departure("NR7412",timeslots);
		assertNotEquals("Running", fs.getFlightState("NR7412",timeslots));
		fs.departure("NR7417",timeslots2);
		assertEquals("Running", fs.getFlightState("NR7417",timeslots2));
	}
	
	//���Բ��ԣ������Ѵ���������δ����
	//			����δ��ɣ����������
	//�쳣���������δ����
	@Test
	public void cancelFlightTest() throws InconsistentStartOrEndException, LocationNotFoundException, SameLabelException, ResourceNotFoundException, ResourceConflictException, PlanEntryNotCreateException, IllegalPlaneContentException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
		expectedEx.expect(PlanEntryNotCreateException.class);
		fs.cancelFlight("null",timeslots);
	}
	
	//�����Ѵ���������δ��ɣ������Ѵ��������������
	@Test
	public void cancelFlightTest2() throws InconsistentStartOrEndException, LocationNotFoundException, SameLabelException, ResourceNotFoundException, ResourceConflictException, PlanEntryNotCreateException, IllegalPlaneContentException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		fs.addLocation(start);
		fs.addLocation(end);
		fs.addPlane(plane);
		fs.createFlight("NR7412", "test", "tet", timeslot);
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
		Timeslot timeslot2=new Timeslot("2020-04-01 10:14", "2020-04-03 14:15");
		fs.createFlight("NR7417", "test", "tet", timeslot2);
		List<Timeslot> timeslots2=new ArrayList<Timeslot>();
		timeslots2.add(timeslot2);
		fs.allocatePlane("NR7412", "N3025",timeslots);
		fs.departure("NR7412",timeslots);
		fs.cancelFlight("NR7412",timeslots);
		fs.cancelFlight("NR7417",timeslots2);
		assertNotEquals("Cancelled", fs.getFlightState("NR7412",timeslots));
		assertEquals("Cancelled", fs.getFlightState("NR7417",timeslots2));
	}
	
	//���Բ��ԣ������Ѵ���������δ����
	//			����δ��ɣ����������
	//�쳣���������δ����
	@Test
	public void endFlightTest() throws ResourceNotFoundException, ResourceConflictException, PlanEntryNotCreateException, InconsistentStartOrEndException, LocationNotFoundException, SameLabelException, IllegalPlaneContentException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
		expectedEx.expect(PlanEntryNotCreateException.class);
		fs.endFlight("null",timeslots);
	}
	
	//�����Ѵ���������δ��ɣ������Ѵ��������������
	@Test
	public void endFlightTest2() throws ResourceNotFoundException, ResourceConflictException, PlanEntryNotCreateException, InconsistentStartOrEndException, LocationNotFoundException, SameLabelException, IllegalPlaneContentException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		fs.addLocation(start);
		fs.addLocation(end);
		fs.addPlane(plane);
		fs.createFlight("NR7412", "test", "tet", timeslot);
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
		Timeslot timeslot2=new Timeslot("2020-04-01 10:14", "2020-04-03 14:15");
		fs.createFlight("NR7417", "test", "tet", timeslot2);
		List<Timeslot> timeslots2=new ArrayList<Timeslot>();
		timeslots2.add(timeslot2);
		fs.allocatePlane("NR7412", "N3025",timeslots);
		fs.departure("NR7412",timeslots);
		fs.endFlight("NR7412",timeslots);
		fs.endFlight("NR7417",timeslots2);
		assertNotEquals("Ended", fs.getFlightState("NR7417",timeslots2));
		assertEquals("Ended", fs.getFlightState("NR7412",timeslots));
	}
	
	@Test
	public void getFlightsofassignPlanetest() throws InconsistentStartOrEndException, LocationNotFoundException, SameLabelException, IllegalPlaneContentException, ResourceNotFoundException, ResourceConflictException, PlanEntryNotCreateException {
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-05 14:15");
		fs.addPlane(plane);
		Plane plane2=new Plane("N3125", "type", 50, 2.5);
		fs.addPlane(plane2);
		fs.addLocation(start);
		fs.addLocation(end);
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
		Timeslot timeslot2=new Timeslot("2020-04-04 10:14", "2020-04-04 14:15");
		List<Timeslot> timeslots2=new ArrayList<Timeslot>();
		timeslots2.add(timeslot2);
		fs.createFlight("NA5874", "test", "tet", timeslot2);
		fs.createFlight("WR7412", "test", "tet", timeslot);
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
	public void getPlanebyIDTest() throws IllegalPlaneContentException {
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
	public void getFlightbyNameTest() throws InconsistentStartOrEndException, LocationNotFoundException, SameLabelException {
		fs.addLocation(start);
		fs.addLocation(end);
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-05 14:15");
		fs.createFlight("NH7412", "test", "tet", timeslot);
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
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
	
	//�쳣���ԣ��ɻ����䳬����Χ
	@Test
	public void AgeOutofBoundExceptionTest() throws FileNotFoundException, LocationNotFoundException, IncorrectElementDependencyException, SameLabelException, UnGrammaticalWordException, ResourceNotFoundException, ResourceConflictException, PlanEntryNotCreateException {
		String path="test\\ExceptionTXT\\AgeOutofBoundException.txt";
		expectedEx.expect(Exceptions.AgeOutofBoundException.class);
		fs.createFlightByFile(path);
	}
	
	//�쳣���ԣ���������뽵�����ڳ���һ��
	@Test
	public void DateDifferMuchExceptionTest() throws FileNotFoundException, LocationNotFoundException, IncorrectElementDependencyException, SameLabelException, UnGrammaticalWordException, ResourceNotFoundException, ResourceConflictException, PlanEntryNotCreateException {			
		String path="test\\ExceptionTXT\\DateDifferMuchException.txt";
		expectedEx.expect(DateDifferMuchException.class);
		fs.createFlightByFile(path);
	}
	
	//�쳣���ԣ����ڸ�ʽ����
	@Test
	public void DateFormatExceptionTest() throws FileNotFoundException, LocationNotFoundException, IncorrectElementDependencyException, SameLabelException, UnGrammaticalWordException, ResourceNotFoundException, ResourceConflictException, PlanEntryNotCreateException {			
		String path="test\\ExceptionTXT\\DateFormatException.txt";
		expectedEx.expect(DateFormatException.class);
		fs.createFlightByFile(path);
	}
	
	//�쳣���ԣ��������Ƹ�ʽ����
	@Test
	public void FlightNameFormatExceptionTest() throws FileNotFoundException, LocationNotFoundException, IncorrectElementDependencyException, SameLabelException, UnGrammaticalWordException, ResourceNotFoundException, ResourceConflictException, PlanEntryNotCreateException {			
		String path="test\\ExceptionTXT\\FlightNameFormatException.txt";
		expectedEx.expect(FlightNameFormatException.class);
		fs.createFlightByFile(path);
	}
	
	//�쳣���ԣ��������Ƴ��ַǷ��ַ�
	@Test
	public void IllegalCharacterForAirportNameExceptionTest() throws FileNotFoundException, LocationNotFoundException, IncorrectElementDependencyException, SameLabelException, UnGrammaticalWordException, ResourceNotFoundException, ResourceConflictException, PlanEntryNotCreateException {			
		String path="test\\ExceptionTXT\\IllegalCharacterForAirportNameException.txt";
		expectedEx.expect(IllegalCharacterForAirportNameException.class);
		fs.createFlightByFile(path);
	}
	
	//�쳣���ԣ���ͬ��ŵķɻ����������ݲ�һ��
	@Test
	public void IllegalPlaneContentExceptionTest() throws FileNotFoundException, LocationNotFoundException, IncorrectElementDependencyException, SameLabelException, UnGrammaticalWordException, ResourceNotFoundException, ResourceConflictException, PlanEntryNotCreateException {			
		String path="test\\ExceptionTXT\\IllegalPlaneContentException.txt";
		expectedEx.expect(IllegalPlaneContentException.class);
		fs.createFlightByFile(path);
	}
	
	//�쳣���ԣ����ֺ�����Ϣ������
	@Test
	public void IncompleteFlightInformationExceptionTest() throws FileNotFoundException, LocationNotFoundException, IncorrectElementDependencyException, SameLabelException, UnGrammaticalWordException, ResourceNotFoundException, ResourceConflictException, PlanEntryNotCreateException {			
		String path="test\\ExceptionTXT\\IncompleteFlightInformationException.txt";
		expectedEx.expect(IncompleteFlightInformationException.class);
		fs.createFlightByFile(path);
	}
	
	//�쳣���ԣ������һ��ָ��������������ڲ�һ��
	@Test
	public void InconsistentDateExceptionTest() throws FileNotFoundException, LocationNotFoundException, IncorrectElementDependencyException, SameLabelException, UnGrammaticalWordException, ResourceNotFoundException, ResourceConflictException, PlanEntryNotCreateException {			
		String path="test\\ExceptionTXT\\InconsistentDateException.txt";
		expectedEx.expect(InconsistentDateException.class);
		fs.createFlightByFile(path);
	}
	
	//�쳣���ԣ�Ӧ��ֻ�������ֵĵط������˷�����
	@Test
	public void NonNumberExceptionTest() throws FileNotFoundException, LocationNotFoundException, IncorrectElementDependencyException, SameLabelException, UnGrammaticalWordException, ResourceNotFoundException, ResourceConflictException, PlanEntryNotCreateException {			
		String path="test\\ExceptionTXT\\NonNumberException.txt";
		expectedEx.expect(NonNumberException.class);
		fs.createFlightByFile(path);
	}
	
	//�쳣���ԣ��ɻ�ID��ʽ����
	@Test
	public void PlaneIDFormatExceptionTest() throws FileNotFoundException, LocationNotFoundException, IncorrectElementDependencyException, SameLabelException, UnGrammaticalWordException, ResourceNotFoundException, ResourceConflictException, PlanEntryNotCreateException {			
		String path="test\\ExceptionTXT\\PlaneIDFormatException.txt";
		expectedEx.expect(PlaneIDFormatException.class);
		fs.createFlightByFile(path);
	}
	
	//�쳣���ԣ��ɻ���λ��������Χ
	@Test
	public void SeatsSizeOutofBoundExceptionTest() throws FileNotFoundException, LocationNotFoundException, IncorrectElementDependencyException, SameLabelException, UnGrammaticalWordException, ResourceNotFoundException, ResourceConflictException, PlanEntryNotCreateException {			
		String path="test\\ExceptionTXT\\SeatsSizeOutofBoundException.txt";
		expectedEx.expect(SeatsSizeOutofBoundException.class);
		fs.createFlightByFile(path);
	}
	
	//�쳣���ԣ����ͳ��ַǷ��ַ�
	@Test
	public void TypeContainsOtherSymbolExceptionTest() throws FileNotFoundException, LocationNotFoundException, IncorrectElementDependencyException, SameLabelException, UnGrammaticalWordException, ResourceNotFoundException, ResourceConflictException, PlanEntryNotCreateException {			
		String path="test\\ExceptionTXT\\TypeContainsOtherSymbolException.txt";
		expectedEx.expect(TypeContainsOtherSymbolException.class);
		fs.createFlightByFile(path);
	}
	
	//�쳣���ԣ��ļ�������
	@Test
	public void FileNotFoundExceptionTest() throws FileNotFoundException, LocationNotFoundException, IncorrectElementDependencyException, SameLabelException, UnGrammaticalWordException, ResourceNotFoundException, ResourceConflictException, PlanEntryNotCreateException {			
		String path="test\\ExceptionTXT\\null.txt";
		expectedEx.expect(FileNotFoundException.class);
		fs.createFlightByFile(path);
	}
	
	//�쳣���ԣ���ǩ��ͬ
	@Test
	public void SameLabelExceptionTest() throws FileNotFoundException, LocationNotFoundException, IncorrectElementDependencyException, SameLabelException, UnGrammaticalWordException, ResourceNotFoundException, ResourceConflictException, PlanEntryNotCreateException {			
		String path="test\\ExceptionTXT\\SameLabelException.txt";
		expectedEx.expect(SameLabelException.class);
		fs.createFlightByFile(path);
	}
	
	//�쳣���ԣ���ͬ���ƺ������/����Ļ���/ʱ��㲻ͬ
	@Test
	public void InconsistentStartOrEndExceptionTest() throws FileNotFoundException, LocationNotFoundException, IncorrectElementDependencyException, SameLabelException, UnGrammaticalWordException, ResourceNotFoundException, ResourceConflictException, PlanEntryNotCreateException {			
		String path="test\\ExceptionTXT\\InconsistentStartOrEndException.txt";
		expectedEx.expect(InconsistentStartOrEndException.class);
		fs.createFlightByFile(path);
	}
	
	//�쳣���ԣ���ͬ���ƺ������/����Ļ���/ʱ��㲻ͬ
	@Test
	public void ResourceConflictExceptionTest() throws FileNotFoundException, LocationNotFoundException, IncorrectElementDependencyException, SameLabelException, UnGrammaticalWordException, ResourceNotFoundException, ResourceConflictException, PlanEntryNotCreateException {			
		String path="test\\ExceptionTXT\\ResourceConflictException.txt";
		expectedEx.expect(ResourceConflictException.class);
		fs.createFlightByFile(path);
	}
	
}
