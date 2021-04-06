package compositeinterface;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Resources.Plane;

public class FlightPlanningEntryTest {
	Plane plane1=new Plane("N5780", "C919", 500, 3.0);
	List<Plane> plane=new ArrayList<Plane>();
	FlightPlanningEntry<Plane> fpe;
	//对于分配资源,对于分配位置继承了multiimplement中的规约，并用委托实现，因此测试会与multiimplement中测试重复，这里不予测试
	@Before
	public void prepare() {
		fpe=FlightPlanningEntry.CreateFlight("TG58");
		plane.add(plane1);
	}
	
	@Test
	public void getStateNameTest() {
		assertEquals("Waiting", fpe.getStateName());
	}
	
	//测试策略:未分配飞机时启动；分配飞机后启动
	@Test
	public void starttest() {
		fpe.start();
		String state=fpe.getStateName();
		assertNotEquals("Running", state);
		fpe.allocateResource(plane);
		fpe.start();
		state=fpe.getStateName();
		assertEquals("Running", state);
	}
	
	//测试策略:课程未开始时取消；课程开始后取消
	@Test
	public void cancelTest() {
		fpe.cancel();
		assertEquals("Cancelled", fpe.getStateName());
		FlightPlanningEntry<Plane> fpe2=FlightPlanningEntry.CreateFlight("FR8741");
		fpe2.allocateResource(plane);
		fpe2.start();
		fpe2.cancel();
		assertNotEquals("Cancelled", fpe2.getStateName());
	}
	
	//测试策略：启动前结束，启动后结束
	@Test
	public void endTest() {
		fpe.end();
		assertNotEquals("Ended", fpe.getStateName());
		fpe.allocateResource(plane);
		fpe.end();
		assertNotEquals("Ended", fpe.getStateName());
		fpe.start();
		fpe.end();
		assertEquals("Ended", fpe.getStateName());
	}
}
