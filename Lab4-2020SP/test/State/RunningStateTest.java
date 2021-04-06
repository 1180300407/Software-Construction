package State;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Exceptions.PlanEntryStateNotMatchException;
import common.CommonPlanningEntry;
import common.CommonPlanningEntryMock;

public class RunningStateTest {

	private RunningState rs;
	private CommonPlanningEntry<String> cpe;
	@Before
	public void prepare() {
		rs=new RunningState();
		cpe=new CommonPlanningEntryMock("test");
		cpe.setState(rs);
	}
	
	@Test
	public void getStateNametest() {
		assertEquals("Running", rs.getStateName());
	}
	
	@Test
	public void startTest() {
		rs.start(cpe);
		assertEquals("Running", cpe.getStateName());
	}
	
	@Test(expected = PlanEntryStateNotMatchException.class)
	public void allocateTest() throws PlanEntryStateNotMatchException {
		rs.allocate(cpe);
	}
	
	@Test
	public void endTest() {
		rs.end(cpe);
		assertEquals("Ended", cpe.getStateName());
	}
	
	@Test(expected = PlanEntryStateNotMatchException.class)
	public void cancelTest() throws PlanEntryStateNotMatchException {
		rs.cancel(cpe);
	}

}
