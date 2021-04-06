package Resources;

import static org.junit.Assert.*;

import org.junit.Test;

public class TeacherTest {

	@Test
	public void getIdtest() {
		Teacher teacher=new Teacher("130283xxx", "name", true, "professtionalTitle");
		assertEquals("130283xxx", teacher.getId());
	}
	
	@Test
	public void getNameTest() {
		Teacher teacher=new Teacher("130283xxx", "name", true, "professtionalTitle");
		assertEquals("name", teacher.getName());
	}
	
	@Test
	public void getSexTest() {
		Teacher teacher=new Teacher("130283xxx", "name", true, "professtionalTitle");
		assertEquals(true,teacher.getSex());
	}
	
	@Test
	public void getProfesstionalTitleTest() {
		Teacher teacher=new Teacher("130283xxx", "name", true, "professtionalTitle");
		assertEquals("professtionalTitle", teacher.getProfesstionalTitle());
	}
	
	@Test
	public void hashCodeTest() {
		Teacher teacher=new Teacher("186", "name", true, "professtionalTitle");
		assertEquals(31+"186".hashCode(), teacher.hashCode());
	}
	
	@Test
	public void equalsTest() {
		Teacher teacher1=new Teacher("130283xxx", "name", true, "professtionalTitle");
		Teacher teacher2=new Teacher("130283", "name", true, "professtionalTitle");
		Teacher teacher3=new Teacher("130283xxx", "name", true, "professtionalTitle");
		assertEquals(teacher1,teacher3);
		assertNotEquals(teacher1, teacher2);
	}

}
