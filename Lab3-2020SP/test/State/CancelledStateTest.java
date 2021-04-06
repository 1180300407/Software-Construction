package State;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Resources.Teacher;
import common.CommonPlanningEntry;
import common.CommonPlanningEntryTest;

public abstract class CancelledStateTest extends CommonPlanningEntryTest{

	private CancelledState cs;
	private CommonPlanningEntry<Teacher> cpe;
	@Before
	public void prepare() {
		cs=new CancelledState();
		cpe=emptyCommonEntry("test");
		cpe.setState(cs);
	}
	
	@Test
	public void getStateNametest() {
		assertEquals("Cancelled", cs.getStateName());
	}
	
	@Test
	public void startTest() {
		cs.start(cpe);
		assertEquals("Cancelled", cpe.getStateName());
	}
	
	@Test
	public void allocateTest() {
		cs.allocate(cpe);
		assertEquals("Cancelled", cpe.getStateName());
	}
	
	@Test
	public void endTest() {
		cs.end(cpe);
		assertEquals("Cancelled", cpe.getStateName());
	}
	
	@Test
	public void cancelTest() {
		cs.cancel(cpe);
		assertEquals("Cancelled", cpe.getStateName());
	}

}
