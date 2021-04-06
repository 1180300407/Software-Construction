package compositeinterface;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import Resources.Teacher;
import common.CommonPlanningEntry;
import common.CommonPlanningEntryTest;

public class CoursePlanningEntryTest extends CommonPlanningEntryTest{
	@Override
	public CommonPlanningEntry<Teacher> emptyCommonEntry(String name){
		return new CourseEntry<Teacher>(name);
	}
	//对于分配资源,对于分配位置继承了multiimplement中的规约，并用委托实现，因此测试会与multiimplement中测试重复，这里不予测试
	CoursePlanningEntry<Teacher> cpe;
	List<Teacher> teacher;
	Teacher teacher1=new Teacher("1302832xx", "name", true, "professtionalTitle");
	
	@Before
	public void prepare() {
		cpe=CoursePlanningEntry.CreateCourse("test");
		teacher=new ArrayList<Teacher>();
		teacher.add(teacher1);
	}
	
	@Test
	public void getStateNameTest() {
		assertEquals("Waiting", cpe.getStateName());
	}
	
	//测试策略:未分配教师时启动；分配教师时启动
	@Test
	public void starttest() {
		cpe.start();
		String state=cpe.getStateName();
		assertNotEquals("Running", state);
		cpe.allocateResource(teacher);
		cpe.start();
		state=cpe.getStateName();
		assertEquals("Running", state);
	}
	
	//测试策略:课程未开始时取消；课程开始后取消
	@Test
	public void cancelTest() {
		cpe.cancel();
		assertEquals("Cancelled", cpe.getStateName());
		CoursePlanningEntry<Teacher> cpe2=CoursePlanningEntry.CreateCourse("2");
		cpe2.allocateResource(teacher);
		cpe2.start();
		cpe2.cancel();
		assertNotEquals("Cancelled", cpe2.getStateName());
	}
	
	//测试策略：启动前结束，启动后结束
	@Test
	public void endTest() {
		cpe.end();
		assertNotEquals("Ended", cpe.getStateName());
		cpe.allocateResource(teacher);
		cpe.end();
		assertNotEquals("Ended", cpe.getStateName());
		cpe.start();
		cpe.end();
		assertEquals("Ended", cpe.getStateName());
	}
}
