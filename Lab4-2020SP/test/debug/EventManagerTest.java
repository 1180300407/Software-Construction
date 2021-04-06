package debug;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class EventManagerTest {
	//测试策略:1.新增1个事件；新增n个事件
	//		  2.n个事件位于不同天；n个事件有位于同一天内的
	//		  3.n个事件互不相交；n个事件有交集
	
	@Before
	public void prepare() {
		EventManager.reSetEvent();
	}
	
	//单个事件
	@Test
	public void SingleEventtest() {
		assertEquals(1, EventManager.book(1, 1, 5));
	}
	
	//n个事件不同天，时间点互不相交（此处n=4）
	@Test
	public void DifferentDayNotIntersecttest() {
		assertEquals(1, EventManager.book(44, 1, 5));
		assertEquals(1, EventManager.book(40, 7, 10));
		assertEquals(1, EventManager.book(49, 11, 15));
		assertEquals(1, EventManager.book(47, 1, 5));
	}
	
	//n个事件不同天，时间点相交（此处n=4）
	@Test
	public void DifferentDayIntersecttest() {
		assertEquals(1, EventManager.book(77, 1, 10));
		assertEquals(1, EventManager.book(79, 7, 10));
		assertEquals(1, EventManager.book(75, 4, 6));
		assertEquals(1, EventManager.book(80, 1, 8));
	}
	
	//n个事件同天，互不相交（此处n=4）
	@Test
	public void SameDayNotIntersecttest() {
		assertEquals(1, EventManager.book(10, 1, 5));
		assertEquals(1, EventManager.book(10, 7, 10));
		assertEquals(1, EventManager.book(10, 11, 15));
		assertEquals(1, EventManager.book(10, 19, 22));
	}
	
	//n个事件同天，相交（此处n=4）
	@Test
	public void SameDayAndIntersecttest() {
		assertEquals(1, EventManager.book(100, 1, 12));
		assertEquals(2, EventManager.book(100, 7, 10));
		assertEquals(2, EventManager.book(100, 11, 15));
		assertEquals(3, EventManager.book(100, 9, 12));
	}
}
