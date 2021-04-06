package State;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Resources.Teacher;
import common.CommonPlanningEntry;
import common.CommonPlanningEntryTest;

public abstract class BlockedStateTest extends CommonPlanningEntryTest{

	private BlockedState bs;
	private CommonPlanningEntry<Teacher> cpe;
	@Before
	public void prepare() {
		bs=new BlockedState();
		cpe=emptyCommonEntry("test");
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
	
	@Test
	public void allocateTest() {
		bs.allocate(cpe);
		assertEquals("Blocked", cpe.getStateName());
	}
	
	@Test
	public void endTest() {
		bs.end(cpe);
		assertEquals("Blocked", cpe.getStateName());
	}
	
	@Test
	public void cancelTest() {
		bs.cancel(cpe);
		assertEquals("Cancelled", cpe.getStateName());
	}

}
