package common;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class PlanningEntryTest extends CommonPlanningEntryTest{
	String name;
	PlanningEntry<String> pe;
	@Override
	public CommonPlanningEntry<String> emptyCommonEntry(String name){
		return new CommonPlanningEntryMock(name);
	}
	
	@Before
	public void prepare() {
		name="test";
		pe=new CommonPlanningEntryMock(name);
	}
	
	@Test
	public void getNameTest() {
		assertEquals("test", pe.getName());
	}
	
	//分配策略:状态匹配；状态不匹配
	@Test
	public void stateTest() {
		pe.start();
		assertEquals("Waiting",pe.getStateName());
		pe.end();
		assertEquals("Waiting",pe.getStateName());
		pe.cancel();
		assertEquals("Cancelled", pe.getStateName());
		pe.start();
		assertEquals("Cancelled",pe.getStateName());
		PlanningEntry<String> pe2=new CommonPlanningEntryMock("te");
		List<String> testlist=new ArrayList<String>();
		testlist.add("1");
		pe2.allocateResource(testlist);
		assertEquals("Allocated", pe2.getStateName());
		pe2.start();
		assertEquals("Running", pe2.getStateName());
		pe2.end();
		assertEquals("Ended", pe2.getStateName());
	}
	
	//测试策略:分配一次；分配多次
	@Test
	public void ResourceTest() {
		assertEquals(0, pe.getResource().size());
		List<String> testlist=new ArrayList<String>();
		testlist.add("1");
		testlist.add("2");
		testlist.add("3");
		pe.allocateResource(testlist);
		assertEquals(3,pe.getResource().size());
		assertTrue(pe.getResource().contains("1"));
		assertTrue(pe.getResource().contains("2"));
		assertTrue(pe.getResource().contains("3"));
		testlist.remove(1);
		pe.allocateResource(testlist);
		assertEquals(3,pe.getResource().size());
	}
}
