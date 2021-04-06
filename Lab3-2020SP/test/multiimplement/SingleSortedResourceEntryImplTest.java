package multiimplement;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import Resources.Teacher;

public class SingleSortedResourceEntryImplTest {

	//���Բ���:������Դһ�Σ�������Դn��(n>=2)
	//Ϊ�˽⿪allocateResource������get����֮������,�����е�resource��������Ϊpublic���в���
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
	
	
	//���Բ���:������Դһ�Σ�������Դn��(n>=2)
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
