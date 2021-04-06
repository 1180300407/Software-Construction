package debug;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class EventManagerTest {
	//���Բ���:1.����1���¼�������n���¼�
	//		  2.n���¼�λ�ڲ�ͬ�죻n���¼���λ��ͬһ���ڵ�
	//		  3.n���¼������ཻ��n���¼��н���
	
	@Before
	public void prepare() {
		EventManager.reSetEvent();
	}
	
	//�����¼�
	@Test
	public void SingleEventtest() {
		assertEquals(1, EventManager.book(1, 1, 5));
	}
	
	//n���¼���ͬ�죬ʱ��㻥���ཻ���˴�n=4��
	@Test
	public void DifferentDayNotIntersecttest() {
		assertEquals(1, EventManager.book(44, 1, 5));
		assertEquals(1, EventManager.book(40, 7, 10));
		assertEquals(1, EventManager.book(49, 11, 15));
		assertEquals(1, EventManager.book(47, 1, 5));
	}
	
	//n���¼���ͬ�죬ʱ����ཻ���˴�n=4��
	@Test
	public void DifferentDayIntersecttest() {
		assertEquals(1, EventManager.book(77, 1, 10));
		assertEquals(1, EventManager.book(79, 7, 10));
		assertEquals(1, EventManager.book(75, 4, 6));
		assertEquals(1, EventManager.book(80, 1, 8));
	}
	
	//n���¼�ͬ�죬�����ཻ���˴�n=4��
	@Test
	public void SameDayNotIntersecttest() {
		assertEquals(1, EventManager.book(10, 1, 5));
		assertEquals(1, EventManager.book(10, 7, 10));
		assertEquals(1, EventManager.book(10, 11, 15));
		assertEquals(1, EventManager.book(10, 19, 22));
	}
	
	//n���¼�ͬ�죬�ཻ���˴�n=4��
	@Test
	public void SameDayAndIntersecttest() {
		assertEquals(1, EventManager.book(100, 1, 12));
		assertEquals(2, EventManager.book(100, 7, 10));
		assertEquals(2, EventManager.book(100, 11, 15));
		assertEquals(3, EventManager.book(100, 9, 12));
	}
}
