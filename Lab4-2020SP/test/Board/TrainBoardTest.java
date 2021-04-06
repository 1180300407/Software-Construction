package Board;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;


import org.junit.Test;

import Location.Location;
import Resources.Carriage;
import Timeslot.Timeslot;
import compositeinterface.TrainEntry;
import compositeinterface.TrainPlanningEntry;


public class TrainBoardTest {

	//测试TrainBoard中的private函数，需要将其中private方法以及属性改为public
	/*需要导入包import java.util.Map; 已注释掉
	//测试策略:既有一小时内的列车，又有不是一小时内的列车
		@Test
		public void setRequestTrainstest() throws ParseException {
			Carriage carriage=new Carriage("1", "f", 240, "man");
			List<Carriage> carriages=new ArrayList<>();
			carriages.add(carriage);
			Location startlocation=new Location("130E","45S" , "test", true);
			Location endlocation=new Location("1E","45N" , "tt", true);
			List<Location> locations=new ArrayList<Location>();
			locations.add(startlocation);
			locations.add(endlocation);
			TrainEntry<Carriage> te1=TrainPlanningEntry.CreateTrain("G475");
			Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-05 12:15");
			Timeslot timeslot2=new Timeslot("2020-04-05 01:14", "2020-04-05 02:15");
			Timeslot timeslot3=new Timeslot("2020-04-05 10:11", "2020-04-05 14:15");//不是一小时内的列车
			List<Timeslot> timeslots1=new ArrayList<Timeslot>();
			timeslots1.add(timeslot);
			List<Timeslot> timeslots2=new ArrayList<Timeslot>();
			timeslots2.add(timeslot2);
			List<Timeslot> timeslots3=new ArrayList<Timeslot>();
			timeslots3.add(timeslot3);
			te1.setLocations(locations);
			te1.setTime(timeslots1);
			te1.allocateResource(carriages);
			TrainEntry<Carriage> te2=TrainPlanningEntry.CreateTrain("G520");
			TrainEntry<Carriage> te3=TrainPlanningEntry.CreateTrain("T961");
			te2.setLocations(locations);
			te3.setLocations(locations);
			te2.allocateResource(carriages);
			te3.allocateResource(carriages);
			te2.setTime(timeslots2);
			te3.setTime(timeslots3);
			List<TrainEntry<Carriage>> tes=new ArrayList<>();
			tes.add(te1);
			tes.add(te2);
			tes.add(te3);
			Calendar calendar=Calendar.getInstance();
			calendar.set(2020, 4-1, 5,10,20);
			TrainBoard tb=new TrainBoard(startlocation, tes,calendar);
			tb.setRequestTrains(calendar);
			assertEquals(2, tb.leaveTrains.size());//应该只保存两个一小时内列车
			assertEquals(0, tb.reachTrains.size());
		}
		//测试策略:初始顺序相反，观察是否排序成功
		@Test
		public void sortTrainsTest() throws ParseException {
			Carriage carriage=new Carriage("1", "f", 240, "man");
			List<Carriage> carriages=new ArrayList<>();
			carriages.add(carriage);
			Location startlocation=new Location("130E","45S" , "test", true);
			Location endlocation=new Location("1E","45N" , "tt", true);
			List<Location> locations=new ArrayList<Location>();
			locations.add(startlocation);
			locations.add(endlocation);
			TrainEntry<Carriage> te1=TrainPlanningEntry.CreateTrain("G475");
			Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-05 12:15");
			Timeslot timeslot2=new Timeslot("2020-04-05 10:11", "2020-04-05 14:15");
			Timeslot timeslot3=new Timeslot("2020-04-05 09:45", "2020-04-05 11:15");
			List<Timeslot> timeslots1=new ArrayList<Timeslot>();
			timeslots1.add(timeslot);
			List<Timeslot> timeslots2=new ArrayList<Timeslot>();
			List<Timeslot> timeslots3=new ArrayList<Timeslot>();
			timeslots2.add(timeslot2);
			timeslots3.add(timeslot3);
			te1.setLocations(locations);
			te1.setTime(timeslots1);
			te1.allocateResource(carriages);
			TrainEntry<Carriage> te2=TrainPlanningEntry.CreateTrain("G520");
			TrainEntry<Carriage> te3=TrainPlanningEntry.CreateTrain("G111");
			te2.setLocations(locations);
			te2.allocateResource(carriages);
			te2.setTime(timeslots2);
			te3.setLocations(locations);
			te3.allocateResource(carriages);
			te3.setTime(timeslots3);
			List<TrainEntry<Carriage>> tes=new ArrayList<>();
			tes.add(te1);
			tes.add(te2);
			tes.add(te3);
			Calendar calendar=Calendar.getInstance();
			calendar.set(2020, 4-1, 5,10,20);
			TrainBoard tb=new TrainBoard(startlocation, tes,calendar);
			tb.setRequestTrains(calendar);
			tb.sortTrains();
			assertEquals(te3.getName(), tb.trains.get(0).getName());
		}*/
		
	
		@Test
		public void iteratorTest() throws ParseException{
			Carriage carriage=new Carriage("1", "f", 240, "man");
			List<Carriage> carriages=new ArrayList<>();
			carriages.add(carriage);
			Location startlocation=new Location("130E","45S" , "test", true);
			Location endlocation=new Location("1E","45N" , "tt", true);
			List<Location> locations=new ArrayList<Location>();
			locations.add(startlocation);
			locations.add(endlocation);
			TrainEntry<Carriage> te1=TrainPlanningEntry.CreateTrain("G475");
			Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-05 12:15");
			Timeslot timeslot2=new Timeslot("2020-04-05 10:11", "2020-04-05 14:15");//不是一小时内的列车
			List<Timeslot> timeslots1=new ArrayList<Timeslot>();
			timeslots1.add(timeslot);
			List<Timeslot> timeslots2=new ArrayList<Timeslot>();
			timeslots2.add(timeslot2);
			te1.setLocations(locations);
			te1.setTime(timeslots1);
			te1.allocateResource(carriages);
			TrainEntry<Carriage> te2=TrainPlanningEntry.CreateTrain("G520");
			te2.setLocations(locations);
			te2.allocateResource(carriages);
			te2.setTime(timeslots2);
			List<TrainEntry<Carriage>> tes=new ArrayList<>();
			tes.add(te1);
			tes.add(te2);
			Calendar calendar=Calendar.getInstance();
			calendar.set(2020, 4-1, 5,10,20);
			TrainBoard tb=new TrainBoard(startlocation, tes,calendar);
			List<TrainEntry<Carriage>> trainEntries=new ArrayList<TrainEntry<Carriage>>();
			for(Iterator<TrainEntry<Carriage>> iterator=tb.iterator();iterator.hasNext();) {
				trainEntries.add(iterator.next());
			}
			
			assertEquals(2, trainEntries.size());
		}
}
