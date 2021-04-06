package State;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Resources.Teacher;
import common.CommonPlanningEntry;
import common.CommonPlanningEntryTest;

public abstract class WaitingStateTest extends CommonPlanningEntryTest{

	private WaitingState ws;
	private CommonPlanningEntry<Teacher> cpe;
	@Before
	public void prepare() {
		ws=new WaitingState();
		cpe=emptyCommonEntry("test");
		cpe.setState(ws);
	}
	
	@Test
	public void getStateNametest() {
		assertEquals("Waiting", ws.getStateName());
	}
	
	@Test
	public void startTest() {
		ws.start(cpe);
		assertEquals("Waiting", cpe.getStateName());
	}
	
	@Test
	public void allocateTest() {
		ws.allocate(cpe);
		assertEquals("Allocated", cpe.getStateName());
	}
	
	@Test
	public void endTest() {
		ws.end(cpe);
		assertEquals("Waiting", cpe.getStateName());
	}
	
	@Test
	public void cancelTest() {
		ws.cancel(cpe);
		assertEquals("Cancelled", cpe.getStateName());
	}

}
