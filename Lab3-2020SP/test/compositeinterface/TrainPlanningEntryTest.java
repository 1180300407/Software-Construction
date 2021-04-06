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
	//对于分配资源,对于分配位置继承了multiimplement中的规约，并用委托实现，因此测试会与multiimplement中测试重复，这里不予测试
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
	
	//测试策略:未分配车厢时启动；分配车厢时启动
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
	
	//测试策略:列车未启动时取消；列车启动后取消
	// 增加的条件:列车未分配时取消；列车分配后取消
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
	
	//测试策略：启动前结束，启动后结束
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
	
	//测试策略:状态匹配；状态不匹配
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
