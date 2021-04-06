package State;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Exceptions.PlanEntryStateNotMatchException;
import common.CommonPlanningEntry;
import common.CommonPlanningEntryMock;

public class BlockedStateTest{

	private BlockedState bs;
	private CommonPlanningEntry<String> cpe;
	@Before
	public void prepare() {
		bs=new BlockedState();
		cpe=new CommonPlanningEntryMock("test");
		cpe.setState(bs);
	}
	
	@Test
	public void getStateNametest() {
		assertEquals("Blocked", bs.getStateName());
	}
	
	@Test
	public void startTest() {
		bs.start(cpe);
		assertEquals("Running", cpe.getStateName());
	}
	
	@Test(expected = PlanEntryStateNotMatchException.class)
	public void allocateTest() throws PlanEntryStateNotMatchException {
		bs.allocate(cpe);
	}
	
	@Test(expected = PlanEntryStateNotMatchException.class)
	public void endTest() throws PlanEntryStateNotMatchException {
		bs.end(cpe);
	}
	
	@Test
	public void cancelTest() {
		bs.cancel(cpe);
		assertEquals("Cancelled", cpe.getStateName());
	}

}
