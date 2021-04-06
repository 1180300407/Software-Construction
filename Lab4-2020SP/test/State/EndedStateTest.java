package State;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Exceptions.PlanEntryStateNotMatchException;
import common.CommonPlanningEntry;
import common.CommonPlanningEntryMock;

public class EndedStateTest {

	private EndedState es;
	private CommonPlanningEntry<String> cpe;
	@Before
	public void prepare() {
		es=new EndedState();
		cpe=new CommonPlanningEntryMock("test");
		cpe.setState(es);
	}
	
	@Test
	public void getStateNametest() {
		assertEquals("Ended", es.getStateName());
	}
	
	@Test(expected = PlanEntryStateNotMatchException.class)
	public void startTest() throws PlanEntryStateNotMatchException {
		es.start(cpe);
	}
	
	@Test(expected = PlanEntryStateNotMatchException.class)
	public void allocateTest() throws PlanEntryStateNotMatchException {
		es.allocate(cpe);
	}
	
	@Test
	public void endTest() {
		es.end(cpe);
		assertEquals("Ended", cpe.getStateName());
	}
	
	@Test(expected = PlanEntryStateNotMatchException.class)
	public void cancelTest() throws PlanEntryStateNotMatchException {
		es.cancel(cpe);
	}
}
