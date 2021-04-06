package State;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Exceptions.PlanEntryStateNotMatchException;
import common.CommonPlanningEntry;
import common.CommonPlanningEntryMock;

public class CancelledStateTest {

	private CancelledState cs;
	private CommonPlanningEntry<String> cpe;
	@Before
	public void prepare() {
		cs=new CancelledState();
		cpe=new CommonPlanningEntryMock("test");
		cpe.setState(cs);
	}
	
	@Test
	public void getStateNametest() {
		assertEquals("Cancelled", cs.getStateName());
	}
	
	@Test(expected = PlanEntryStateNotMatchException.class)
	public void startTest() throws PlanEntryStateNotMatchException {
		cs.start(cpe);
	}
	
	@Test(expected = PlanEntryStateNotMatchException.class)
	public void allocateTest() throws PlanEntryStateNotMatchException {
		cs.allocate(cpe);
	}
	
	@Test(expected = PlanEntryStateNotMatchException.class)
	public void endTest() throws PlanEntryStateNotMatchException {
		cs.end(cpe);
	}
	
	@Test
	public void cancelTest() {
		cs.cancel(cpe);
		assertEquals("Cancelled", cpe.getStateName());
	}

}
