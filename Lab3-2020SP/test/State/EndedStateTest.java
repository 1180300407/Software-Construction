package State;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Resources.Teacher;
import common.CommonPlanningEntry;
import common.CommonPlanningEntryTest;

public abstract class EndedStateTest extends CommonPlanningEntryTest{

	private EndedState es;
	private CommonPlanningEntry<Teacher> cpe;
	@Before
	public void prepare() {
		es=new EndedState();
		cpe=emptyCommonEntry("test");
		cpe.setState(es);
	}
	
	@Test
	public void getStateNametest() {
		assertEquals("Ended", es.getStateName());
	}
	
	@Test
	public void startTest() {
		es.start(cpe);
		assertEquals("Ended", cpe.getStateName());
	}
	
	@Test
	public void allocateTest() {
		es.allocate(cpe);
		assertEquals("Ended", cpe.getStateName());
	}
	
	@Test
	public void endTest() {
		es.end(cpe);
		assertEquals("Ended", cpe.getStateName());
	}
	
	@Test
	public void cancelTest() {
		es.cancel(cpe);
		assertEquals("Ended", cpe.getStateName());
	}
}
