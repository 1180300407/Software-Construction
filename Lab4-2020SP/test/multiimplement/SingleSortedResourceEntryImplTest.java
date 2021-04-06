package multiimplement;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import Resources.Teacher;

public class SingleSortedResourceEntryImplTest {

	//测试策略:分配资源一次；分配资源n次(n>=2)
	//为了解开allocateResource函数与get函数之间的耦合,将类中的resource属性设置为public进行测试
	/*
	@Test
	public void allocateResourcetest() {
		List<Teacher> teacher=new ArrayList<Teacher>();
		Teacher teacher1=new Teacher("id", "name",true, "professtionalTitle");
		teacher.add(teacher1);
		SingleSortedResourceEntryImpl<Teacher> ssre=new SingleSortedResourceEntryImpl<Teacher>();
		ssre.allocateResource(teacher);
		assertEquals(teacher1, ssre.resource.get(0));
		Teacher teacher2=new Teacher("id2", "name",true, "professtionalTitle");
		teacher.remove(teacher1);
		teacher.add(teacher2);
		ssre.allocateResource(teacher);
		assertEquals(teacher1, ssre.resource.get(0));
	}*/
	
	
	//测试策略:分配资源一次；分配资源n次(n>=2)
	@Test
	public void getResourceTest() {
		List<Teacher> teacher=new ArrayList<Teacher>();
		Teacher teacher1=new Teacher("id", "name",true, "professtionalTitle");
		teacher.add(teacher1);
		SingleSortedResourceEntryImpl<Teacher> ssre=new SingleSortedResourceEntryImpl<Teacher>();
		ssre.allocateResource(teacher);
		assertEquals(teacher1, ssre.getResource().get(0));
		Teacher teacher2=new Teacher("id2", "name",true, "professtionalTitle");
		teacher.remove(teacher1);
		teacher.add(teacher2);
		ssre.allocateResource(teacher);
		assertEquals(teacher1, ssre.getResource().get(0));
	}
}
