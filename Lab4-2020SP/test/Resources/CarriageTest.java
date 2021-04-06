package Resources;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class CarriageTest {

	@Test
	public void getIdtest() {
		Carriage carriage=new Carriage("1", "type", 5, "manufactureyear");
		assertEquals("1", carriage.getId());
	}
	
	@Test
	public void getTypeTest() {
		Carriage carriage=new Carriage("10", "type", 15, "manufactureyear");
		assertEquals("type", carriage.getType());
	}
	
	@Test
	public void getMaxnumTest() {
		Carriage carriage=new Carriage("7", "type", 3, "manufacture");
		assertEquals(3, carriage.getMaxnum());
	}
	
	@Test
	public void getManufactureyearTest() {
		Carriage carriage=new Carriage("4", "type", 5, "manufactureyear");
		assertEquals("manufactureyear", carriage.getManufactureyear());
	}
	
	@Test
	public void hashCodeTest() {
		Carriage carriage=new Carriage("10", "type", 5, "manufactureyear");
		Set<Carriage> carriages=new HashSet<Carriage>();
		carriages.add(carriage);
		Carriage carriage2=new Carriage("10", "type", 5, "manu");
		assertTrue(carriages.contains(carriage2));
	}
	
	@Test
	public void equalsTest() {
		Carriage carriage1=new Carriage("10", "type", 5, "manufactureyear");
		Carriage carriage2=new Carriage("10", "type", 5, "manufactureyear");
		Carriage carriage3=new Carriage("7", "type", 5, "manufactureyear");
		assertEquals(carriage1,carriage2);
		assertNotEquals(carriage1, carriage3);
	}
}
