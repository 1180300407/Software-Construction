package compositeinterface;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Resources.Plane;

public class FlightPlanningEntryTest {
	Plane plane1=new Plane("N5780", "C919", 500, 3.0);
	List<Plane> plane=new ArrayList<Plane>();
	FlightPlanningEntry<Plane> fpe;
	//���ڷ�����Դ,���ڷ���λ�ü̳���multiimplement�еĹ�Լ������ί��ʵ�֣���˲��Ի���multiimplement�в����ظ������ﲻ�����
	@Before
	public void prepare() {
		fpe=FlightPlanningEntry.CreateFlight("TG58");
		plane.add(plane1);
	}
	
	@Test
	public void getStateNameTest() {
		assertEquals("Waiting", fpe.getStateName());
	}
	
	//���Բ���:δ����ɻ�ʱ����������ɻ�������
	@Test
	public void starttest() {
		fpe.start();
		String state=fpe.getStateName();
		assertNotEquals("Running", state);
		fpe.allocateResource(plane);
		fpe.start();
		state=fpe.getStateName();
		assertEquals("Running", state);
	}
	
	//���Բ���:�γ�δ��ʼʱȡ�����γ̿�ʼ��ȡ��
	@Test
	public void cancelTest() {
		fpe.cancel();
		assertEquals("Cancelled", fpe.getStateName());
		FlightPlanningEntry<Plane> fpe2=FlightPlanningEntry.CreateFlight("FR8741");
		fpe2.allocateResource(plane);
		fpe2.start();
		fpe2.cancel();
		assertNotEquals("Cancelled", fpe2.getStateName());
	}
	
	//���Բ��ԣ�����ǰ���������������
	@Test
	public void endTest() {
		fpe.end();
		assertNotEquals("Ended", fpe.getStateName());
		fpe.allocateResource(plane);
		fpe.end();
		assertNotEquals("Ended", fpe.getStateName());
		fpe.start();
		fpe.end();
		assertEquals("Ended", fpe.getStateName());
	}
}
