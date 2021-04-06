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
	//���ڷ�����Դ,���ڷ���λ�ü̳���multiimplement�еĹ�Լ������ί��ʵ�֣���˲��Ի���multiimplement�в����ظ������ﲻ�����
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
	
	//���Բ���:δ�����ʦʱ�����������ʦʱ����
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
	
	//���Բ���:�γ�δ��ʼʱȡ�����γ̿�ʼ��ȡ��
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
	
	//���Բ��ԣ�����ǰ���������������
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
