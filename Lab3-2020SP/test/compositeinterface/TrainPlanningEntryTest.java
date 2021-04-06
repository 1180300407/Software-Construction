package compositeinterface;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Resources.Carriage;
import Timeslot.Timeslot;

public class TrainPlanningEntryTest {
	Carriage carriage=new Carriage("4", "type", 60, "2019-04-08");
	TrainEntry<Carriage> tpe;
	Carriage Carriage2=new Carriage("6", "type", 60, "2019-04-08");
	Carriage Carriage3=new Carriage("10", "type", 60, "2019-04-08");
	List<Carriage> carriages=new ArrayList<Carriage>();
	//���ڷ�����Դ,���ڷ���λ�ü̳���multiimplement�еĹ�Լ������ί��ʵ�֣���˲��Ի���multiimplement�в����ظ������ﲻ�����
	@Before
	public void prepare() {
		tpe=TrainPlanningEntry.CreateTrain("test");
		carriages.add(carriage);
		carriages.add(Carriage2);
		carriages.add(Carriage3);
	}

	@Test
	public void getStateNameTest() {
		assertEquals("Waiting", tpe.getStateName());
	}
	
	//���Բ���:δ���䳵��ʱ���������䳵��ʱ����
	@Test
	public void starttest() {
		tpe.start();
		String state=tpe.getStateName();
		assertNotEquals("Running", state);
		tpe.allocateResource(carriages);
		tpe.start();
		state=tpe.getStateName();
		assertEquals("Running", state);
	}
	
	//���Բ���:�г�δ����ʱȡ�����г�������ȡ��
	// ���ӵ�����:�г�δ����ʱȡ�����г������ȡ��
	@Test
	public void cancelTest() {
		tpe.cancel();
		assertEquals("Cancelled", tpe.getStateName());
		TrainPlanningEntry<Carriage> tpe2=TrainPlanningEntry.CreateTrain("s");
		tpe2.allocateResource(carriages);
		tpe2.cancel();
		assertNotEquals("Cancelled", tpe2.getStateName());
		tpe2.start();
		tpe2.cancel();
		assertNotEquals("Cancelled", tpe2.getStateName());
	}
	
	//���Բ��ԣ�����ǰ���������������
	@Test
	public void endTest() {
		tpe.end();
		assertNotEquals("Ended", tpe.getStateName());
		tpe.allocateResource(carriages);
		tpe.end();
		assertNotEquals("Ended", tpe.getStateName());
		tpe.start();
		tpe.end();
		assertEquals("Ended", tpe.getStateName());
	}
	
	//���Բ���:״̬ƥ�䣻״̬��ƥ��
	@Test
	public void blockTest() throws ParseException {
		tpe.allocateResource(carriages);
		Timeslot timeslot=new Timeslot("2020-04-05 10:14", "2020-04-07 14:15");
		Timeslot timeslot2=new Timeslot("2020-04-07 17:14" ,"2020-04-08 14:15");
		List<Timeslot> timeslots=new ArrayList<Timeslot>();
		timeslots.add(timeslot);
		timeslots.add(timeslot2);
		tpe.setTime(timeslots);
		tpe.block(tpe);
		assertNotEquals("Blocked", tpe.getStateName());
		tpe.start();
		tpe.block(tpe);
		assertEquals("Blocked", tpe.getStateName());
		tpe.start();
		assertEquals("Running", tpe.getStateName());
	}
}
