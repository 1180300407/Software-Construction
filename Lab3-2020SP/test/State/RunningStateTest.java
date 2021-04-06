package State;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Resources.Teacher;
import common.CommonPlanningEntry;
import common.CommonPlanningEntryTest;

public abstract class RunningStateTest extends CommonPlanningEntryTest{

	private RunningState rs;
	private CommonPlanningEntry<Teacher> cpe;
	@Before
	public void prepare() {
		rs=new RunningState();
		cpe=emptyCommonEntry("test");
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
	
	@Test
	public void allocateTest() {
		rs.allocate(cpe);
		assertEquals("Running", cpe.getStateName());
	}
	
	@Test
	public void endTest() {
		rs.end(cpe);
		assertEquals("Ended", cpe.getStateName());
	}
	
	@Test
	public void cancelTest() {
		rs.cancel(cpe);
		assertEquals("Running", cpe.getStateName());
	}

}
