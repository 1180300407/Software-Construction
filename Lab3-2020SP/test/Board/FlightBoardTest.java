package Board;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import Location.Location;
import Resources.Plane;
import Timeslot.Timeslot;
import compositeinterface.FlightEntry;
import compositeinterface.FlightPlanningEntry;

public class FlightBoardTest {
	//测试FlightBoard中的函数，需要将其中private属性改为public
	/*
	//测试策略:既有一小时内的航班，又有不是一小时内的航班
	@Test
	public void setRequestFlightstest() throws ParseException {
		Plane plane=new Plane("N2501", "V", 150, 3.0);
		List<Plane> planes=new ArrayList<>();
		planes.add(plane);
		Location startlocation=new Location("130E","45S" , "test", true);
		Location endlocation=new Location("1E","45N" , "tt", true);
		List<Location> locations=new ArrayList<Location>();
		locations.add(startlocation);
		locations.add(endlocation);
		FlightEntry<Plane> fe1=FlightPlanningEntry.CreateFlight("NY5740");
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-05 12:15");
		Timeslot timeslot2=new Timeslot("2020-04-05 01:14", "2020-04-05 02:15");
		Timeslot timeslot3=new Timeslot("2020-04-05 10:11", "2020-04-05 14:15");//不是一小时内的航班
		List<Timeslot> timeslots1=new ArrayList<Timeslot>();
		timeslots1.add(timeslot);
		List<Timeslot> timeslots2=new ArrayList<Timeslot>();
		timeslots2.add(timeslot2);
		List<Timeslot> timeslots3=new ArrayList<Timeslot>();
		timeslots3.add(timeslot3);
		fe1.setLocations(locations);
		fe1.setTime(timeslots1);
		fe1.allocateResource(planes);
		FlightEntry<Plane> fe2=FlightPlanningEntry.CreateFlight("NE7410");
		FlightEntry<Plane> fe3=FlightPlanningEntry.CreateFlight("NU0017");
		fe2.setLocations(locations);
		fe3.setLocations(locations);
		fe2.allocateResource(planes);
		fe3.allocateResource(planes);
		fe2.setTime(timeslots2);
		fe3.setTime(timeslots3);
		List<FlightEntry<Plane>> fes=new ArrayList<>();
		fes.add(fe1);
		fes.add(fe2);
		fes.add(fe3);
		Calendar calendar=Calendar.getInstance();
		calendar.set(2020, 4-1, 5,10,20);
		FlightBoard fb=new FlightBoard(startlocation, fes,calendar);
		fb.setRequestFlights(calendar);
		assertEquals(2, fb.departureFlights.size());//应该只保存两个一小时内航班
		assertEquals(0, fb.reachFlights.size());
	}
	//测试策略:初始顺序相反，观察是否排序成功
	@Test
	public void sortFlightsTest() throws ParseException {
		Plane plane=new Plane("N2501", "V", 150, 3.0);
		List<Plane> planes=new ArrayList<>();
		planes.add(plane);
		Location startlocation=new Location("130E","45S" , "test", true);
		Location endlocation=new Location("1E","45N" , "tt", true);
		List<Location> locations=new ArrayList<Location>();
		locations.add(startlocation);
		locations.add(endlocation);
		FlightEntry<Plane> fe1=FlightPlanningEntry.CreateFlight("NY5740");
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-05 12:15");
		Timeslot timeslot2=new Timeslot("2020-04-05 09:14", "2020-04-05 10:15");
		List<Timeslot> timeslots1=new ArrayList<Timeslot>();
		timeslots1.add(timeslot);
		List<Timeslot> timeslots2=new ArrayList<Timeslot>();
		timeslots2.add(timeslot2);
		fe1.setLocations(locations);
		fe1.setTime(timeslots1);
		fe1.allocateResource(planes);
		FlightEntry<Plane> fe2=FlightPlanningEntry.CreateFlight("NE7410");
		fe2.setLocations(locations);
		fe2.allocateResource(planes);
		fe2.setTime(timeslots2);
		List<FlightEntry<Plane>> fes=new ArrayList<>();
		fes.add(fe1);
		fes.add(fe2);
		Calendar calendar=Calendar.getInstance();
		calendar.set(2020, 4-1, 5,10,1);
		FlightBoard fb=new FlightBoard(startlocation, fes,calendar);
		fb.setRequestFlights(calendar);
		fb.sortFlights();
		assertEquals(fe2.getName(), fb.departureFlights.get(0).getName());
	}*/
	
	@Test
	public void iteratorTest() throws ParseException{
		Plane plane=new Plane("N2501", "V", 150, 3.0);
		List<Plane> planes=new ArrayList<>();
		planes.add(plane);
		Location startlocation=new Location("130E","45S" , "test", true);
		Location endlocation=new Location("1E","45N" , "tt", true);
		List<Location> locations=new ArrayList<Location>();
		locations.add(startlocation);
		locations.add(endlocation);
		FlightEntry<Plane> fe1=FlightPlanningEntry.CreateFlight("NY5740");
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-05 12:15");
		Timeslot timeslot2=new Timeslot("2020-04-05 09:14", "2020-04-05 10:15");
		List<Timeslot> timeslots1=new ArrayList<Timeslot>();
		timeslots1.add(timeslot);
		List<Timeslot> timeslots2=new ArrayList<Timeslot>();
		timeslots2.add(timeslot2);
		fe1.setLocations(locations);
		fe1.setTime(timeslots1);
		fe1.allocateResource(planes);
		FlightEntry<Plane> fe2=FlightPlanningEntry.CreateFlight("NE7410");
		fe2.setLocations(locations);
		fe2.allocateResource(planes);
		fe2.setTime(timeslots2);
		List<FlightEntry<Plane>> fes=new ArrayList<>();
		fes.add(fe1);
		fes.add(fe2);
		Calendar calendar=Calendar.getInstance();
		calendar.set(2020, 4-1, 5,10,1);
		FlightBoard fb=new FlightBoard(startlocation, fes,calendar);
		List<FlightEntry<Plane>> flightEntries=new ArrayList<FlightEntry<Plane>>();
		for(Iterator<FlightEntry<Plane>> iterator=fb.iterator();iterator.hasNext();) {
			flightEntries.add(iterator.next());
		}
		
		assertEquals(2, flightEntries.size());
	}
}
