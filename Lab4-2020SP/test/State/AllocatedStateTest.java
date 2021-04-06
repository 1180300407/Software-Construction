package State;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Exceptions.PlanEntryStateNotMatchException;
import common.CommonPlanningEntry;
import common.CommonPlanningEntryMock;

public class AllocatedStateTest{
	private AllocatedState as;
	private CommonPlanningEntry<String> cpe;
	@Before
	public void prepare() {
		as=new AllocatedState();
		cpe=new CommonPlanningEntryMock("test");
		cpe.setState(as);
	}
	
	@Test
	public void getStateNametest() {
		assertEquals("Allocated", as.getStateName());
	}
	
	@Test
	public void startTest() {
		as.start(cpe);
		assertEquals("Running", cpe.getStateName());
	}
	
	@Test
	public void allocateTest() {
		as.allocate(cpe);
		assertEquals("Allocated", cpe.getStateName());
	}
	
	@Test(expected = PlanEntryStateNotMatchException.class)
	public void endTest() throws PlanEntryStateNotMatchException {
		as.end(cpe);
	}
	
	@Test
	public void cancelTest() {
		as.cancel(cpe);
		assertEquals("Cancelled", cpe.getStateName());
	}
}
