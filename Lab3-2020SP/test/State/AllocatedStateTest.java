package State;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Resources.Teacher;
import common.CommonPlanningEntry;
import common.CommonPlanningEntryTest;

public abstract class AllocatedStateTest extends CommonPlanningEntryTest{
	private AllocatedState as;
	private CommonPlanningEntry<Teacher> cpe;
	@Before
	public void prepare() {
		as=new AllocatedState();
		cpe=emptyCommonEntry("test");
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
	
	@Test
	public void endTest() {
		as.end(cpe);
		assertEquals("Allocated", cpe.getStateName());
	}
	
	@Test
	public void cancelTest() {
		as.cancel(cpe);
		assertEquals("Cancelled", cpe.getStateName());
	}
}
