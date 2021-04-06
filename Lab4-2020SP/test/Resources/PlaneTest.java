package Resources;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class PlaneTest {

	@Test
	public void getIdtest() {
		Plane plane=new Plane("N3025", "type", 50, 2.5);
		assertEquals("N3025", plane.getId());
	}
	
	@Test
	public void getTypeTest() {
		Plane plane=new Plane("B9999", "type", 50, 2.1);
		assertEquals("type", plane.getType());
	}
	
	@Test
	public void getSeatsTest() {
		Plane plane=new Plane("N1552", "type", 50, 2.5);
		assertEquals(50, plane.getSeats());
	}
	
	@Test
	public void getPlaneageTest() {
		Plane plane=new Plane("N0001", "type", 50, 11.5);
		assertEquals(11,(int)plane.getPlaneage());
		assertEquals(12, (int)(plane.getPlaneage()+0.5));
	}
	
	@Test
	public void hashCodeTest() {
		Plane plane1=new Plane("B9210", "type", 50, 2.5);
		Set<Plane> planes=new HashSet<Plane>();
		planes.add(plane1);
		Plane plane2=new Plane("B9210", "typ", 5, 2.5);
		assertTrue(planes.contains(plane2));
	}
	
	@Test
	public void equalsTest() {
		Plane plane1=new Plane("B8521", "type", 50, 2.5);
		Plane plane2=new Plane("N6901", "type", 50, 2.7);
		Plane plane3=new Plane("B8521", "t", 10, 2.5);
		assertEquals(plane1,plane3);
		assertNotEquals(plane1, plane2);
	}
}
