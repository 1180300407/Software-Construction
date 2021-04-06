package common;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import State.CancelledState;
import State.EndedState;
import State.WaitingState;

public abstract class CommonPlanningEntryTest{
	String name;
	//���з�������
	public abstract CommonPlanningEntry<String> emptyCommonEntry(String name);
	
	@Before
	public void prepare() {
		name="test";
	}

	//�������Ժ����Ѿ���PlanningEntryTest�������
	@Test
	public void setStateTest() {
		WaitingState ws=new WaitingState();
		CancelledState cs=new CancelledState();
		EndedState es=new EndedState();
		CommonPlanningEntry<String> cpe=emptyCommonEntry(name);
		
		cpe.setState(ws);
		assertEquals("Waiting", cpe.getStateName());
		cpe.setState(cs);
		assertEquals("Cancelled", cpe.getStateName());
		cpe.setState(es);
		assertEquals("Ended", cpe.getStateName());
	}
}
