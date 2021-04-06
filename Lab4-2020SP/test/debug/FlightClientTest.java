package debug;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class FlightClientTest {
	private Plane plane1,plane2,plane3;
	private Flight flight1,flight2,flight3;
	private List<Plane> planes;
	private List<Flight> flights;
	private FlightClient fc;
	
	//测试策略:1.飞机数大于航班数；飞机数小于航班数
	//		  2.航班间存在日期/时间冲突；航班间不存在日期/航班冲突
	
	
	@Before//准备工作，创建飞机、航班
	public void prepare() {
		plane1=new Plane();
		plane2=new Plane();
		plane3=new Plane();
		plane1.setPlaneNo("N2");
		plane1.setPlaneType("A350");
		plane1.setSeatsNum(200);
		plane1.setPlaneAge(1.5);
		plane2.setPlaneNo("N5");
		plane2.setPlaneType("A350");
		plane2.setSeatsNum(200);
		plane2.setPlaneAge(1.5);
		plane3.setPlaneNo("N11");
		plane3.setPlaneType("A350");
		plane3.setSeatsNum(200);
		plane3.setPlaneAge(1.5);
		flight1=new Flight();
		flight1.setFlightNo("F4");
		flight2=new Flight();
		flight2.setFlightNo("B7");
		flight3=new Flight();
		flight3.setFlightNo("R5");
		planes=new ArrayList<Plane>();
		flights=new ArrayList<Flight>();
		fc=new FlightClient();
	}

	
	//飞机数大于航班数:应该分配成功
	//飞机数大于航班数且航班间不存在日期/航班冲突
	@Test
	public void planeNumBiggerAndNoConflicttest() {//3个飞机，2个航班
		Calendar flight1Date=Calendar.getInstance();
		flight1Date.set(2020, 4, 24);//同一天
		Calendar flight1DepartureTime=Calendar.getInstance();
		flight1DepartureTime.set(2020, 4, 24,16,38);//时间无冲突
		Calendar flight1ArrivalTime=Calendar.getInstance();
		flight1ArrivalTime.set(2020, 4, 24,19,20);
		flight1.setFlightDate(flight1Date);
		flight1.setDepartTime(flight1DepartureTime);
		flight1.setArrivalTime(flight1ArrivalTime);
		Calendar flight2Date=Calendar.getInstance();
		flight2Date.set(2020, 4, 24);//同一天
		Calendar flight2DepartureTime=Calendar.getInstance();
		flight2DepartureTime.set(2020, 4, 24,9,17);//时间无冲突
		Calendar flight2ArrivalTime=Calendar.getInstance();
		flight2ArrivalTime.set(2020, 4, 24,14,20);
		flight2.setFlightDate(flight2Date);
		flight2.setDepartTime(flight2DepartureTime);
		flight2.setArrivalTime(flight2ArrivalTime);
		planes.add(plane1);
		planes.add(plane2);
		planes.add(plane3);
		flights.add(flight1);
		flights.add(flight2);//3个飞机，2个航班
		assertTrue(fc.planeAllocation(planes, flights));
	}
	
	//飞机数大于航班数:应该分配成功
	//飞机数大于航班数且航班间存在日期/航班冲突
	@Test
	public void planeNumBiggerAndWithConflicttest() {//3个飞机，2个航班
		Calendar flight1Date=Calendar.getInstance();
		flight1Date.set(2020, 4, 24);//同一天
		Calendar flight1DepartureTime=Calendar.getInstance();
		flight1DepartureTime.set(2020, 4, 24,16,38);//时间有冲突
		Calendar flight1ArrivalTime=Calendar.getInstance();
		flight1ArrivalTime.set(2020, 4, 24,19,20);
		flight1.setFlightDate(flight1Date);
		flight1.setDepartTime(flight1DepartureTime);
		flight1.setArrivalTime(flight1ArrivalTime);
		Calendar flight2Date=Calendar.getInstance();
		flight2Date.set(2020, 4, 24);//同一天
		Calendar flight2DepartureTime=Calendar.getInstance();
		flight2DepartureTime.set(2020, 4, 24,18,17);//时间有冲突
		Calendar flight2ArrivalTime=Calendar.getInstance();
		flight2ArrivalTime.set(2020, 4, 24,23,20);
		flight2.setFlightDate(flight2Date);
		flight2.setDepartTime(flight2DepartureTime);
		flight2.setArrivalTime(flight2ArrivalTime);
		planes.add(plane1);
		planes.add(plane2);
		planes.add(plane3);
		flights.add(flight1);
		flights.add(flight2);//3个飞机，2个航班
		assertTrue(fc.planeAllocation(planes, flights));
	}
	
	//飞机数小于航班数且航班间不存在日期/航班冲突：应该分配成功
	@Test
	public void flightNumBiggerAndNoConflicttest() {//2个飞机，3个航班
		Calendar flight1Date=Calendar.getInstance();
		flight1Date.set(2020, 4, 24);//同一天
		Calendar flight1DepartureTime=Calendar.getInstance();
		flight1DepartureTime.set(2020, 4, 24,16,38);//时间无冲突
		Calendar flight1ArrivalTime=Calendar.getInstance();
		flight1ArrivalTime.set(2020, 4, 24,19,20);
		flight1.setFlightDate(flight1Date);
		flight1.setDepartTime(flight1DepartureTime);
		flight1.setArrivalTime(flight1ArrivalTime);
		Calendar flight2Date=Calendar.getInstance();
		flight2Date.set(2020, 4, 24);//同一天
		Calendar flight2DepartureTime=Calendar.getInstance();
		flight2DepartureTime.set(2020, 4, 24,9,17);//时间无冲突
		Calendar flight2ArrivalTime=Calendar.getInstance();
		flight2ArrivalTime.set(2020, 4, 24,13,20);
		flight2.setFlightDate(flight2Date);
		flight2.setDepartTime(flight2DepartureTime);
		flight2.setArrivalTime(flight2ArrivalTime);
		Calendar flight3Date=Calendar.getInstance();
		flight3Date.set(2020, 4, 25);//不同天
		Calendar flight3DepartureTime=Calendar.getInstance();
		flight3DepartureTime.set(2020, 4, 24,18,17);//时间无冲突
		Calendar flight3ArrivalTime=Calendar.getInstance();
		flight3ArrivalTime.set(2020, 4, 24,23,20);
		flight3.setFlightDate(flight3Date);
		flight3.setDepartTime(flight3DepartureTime);
		flight3.setArrivalTime(flight3ArrivalTime);
		planes.add(plane1);
		planes.add(plane2);
		flights.add(flight1);
		flights.add(flight2);//2个飞机，3个航班
		flights.add(flight3);
		assertTrue(fc.planeAllocation(planes, flights));
	}
	
	//飞机数小于航班数且航班间存在日期/航班冲突：应该分配失败
	@Test
	public void flightNumBiggerAndWithConflicttest() {//2个飞机，3个航班
		Calendar flight1Date=Calendar.getInstance();
		flight1Date.set(2020, 4, 24);//同一天
		Calendar flight1DepartureTime=Calendar.getInstance();
		flight1DepartureTime.set(2020, 4, 24,16,38);//时间无冲突
		Calendar flight1ArrivalTime=Calendar.getInstance();
		flight1ArrivalTime.set(2020, 4, 24,19,20);
		flight1.setFlightDate(flight1Date);
		flight1.setDepartTime(flight1DepartureTime);
		flight1.setArrivalTime(flight1ArrivalTime);
		Calendar flight2Date=Calendar.getInstance();
		flight2Date.set(2020, 4, 24);//同一天
		Calendar flight2DepartureTime=Calendar.getInstance();
		flight2DepartureTime.set(2020, 4, 24,11,17);//时间无冲突
		Calendar flight2ArrivalTime=Calendar.getInstance();
		flight2ArrivalTime.set(2020, 4, 24,17,20);
		flight2.setFlightDate(flight2Date);
		flight2.setDepartTime(flight2DepartureTime);
		flight2.setArrivalTime(flight2ArrivalTime);
		Calendar flight3Date=Calendar.getInstance();
		flight3Date.set(2020, 4, 24);//同一天
		Calendar flight3DepartureTime=Calendar.getInstance();
		flight3DepartureTime.set(2020, 4, 24,17,17);//时间有冲突
		Calendar flight3ArrivalTime=Calendar.getInstance();
		flight3ArrivalTime.set(2020, 4, 24,23,20);
		flight3.setFlightDate(flight3Date);
		flight3.setDepartTime(flight3DepartureTime);
		flight3.setArrivalTime(flight3ArrivalTime);
		planes.add(plane1);
		planes.add(plane2);
		flights.add(flight1);
		flights.add(flight2);//2个飞机，3个航班
		flights.add(flight3);
		assertFalse(fc.planeAllocation(planes, flights));
	}
}
