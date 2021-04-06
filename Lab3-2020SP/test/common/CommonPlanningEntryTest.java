package common;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Resources.Teacher;
import State.CancelledState;
import State.EndedState;
import State.WaitingState;

public abstract class CommonPlanningEntryTest{
	String name;
	//特有方法测试
	public abstract CommonPlanningEntry<Teacher> emptyCommonEntry(String name);
	
	@Before
	public void prepare() {
		name="test";
	}

	//其他共性函数已经在PlanningEntryTest里面测试
	@Test
	public void setStateTest() {
		WaitingState ws=new WaitingState();
		CancelledState cs=new CancelledState();
		EndedState es=new EndedState();
		CommonPlanningEntry<Teacher> cpe=emptyCommonEntry(name);
		
		cpe.setState(ws);
		assertEquals("Waiting", cpe.getStateName());
		cpe.setState(cs);
		assertEquals("Cancelled", cpe.getStateName());
		cpe.setState(es);
		assertEquals("Ended", cpe.getStateName());
	}
}
