package State;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Exceptions.PlanEntryStateNotMatchException;
import common.CommonPlanningEntry;
import common.CommonPlanningEntryMock;

public class WaitingStateTest {

	private WaitingState ws;
	private CommonPlanningEntry<String> cpe;
	@Before
	public void prepare() {
		ws=new WaitingState();
		cpe=new CommonPlanningEntryMock("test");
		cpe.setState(ws);
	}
	
	@Test
	public void getStateNametest() {
		assertEquals("Waiting", ws.getStateName());
	}
	
	@Test(expected = PlanEntryStateNotMatchException.class)
	public void startTest() throws PlanEntryStateNotMatchException {
		ws.start(cpe);
	}
	
	@Test
	public void allocateTest() {
		ws.allocate(cpe);
		assertEquals("Allocated", cpe.getStateName());
	}
	
	@Test(expected = PlanEntryStateNotMatchException.class)
	public void endTest() throws PlanEntryStateNotMatchException {
		ws.end(cpe);
	}
	
	@Test
	public void cancelTest() {
		ws.cancel(cpe);
		assertEquals("Cancelled", cpe.getStateName());
	}

}
