package Location;

import static org.junit.Assert.*;

import org.junit.Test;

public class LocationTest {
	@Test
	public void getLongitudetest() {
		Location location=new Location("130E","45S" , "test", true);
		assertEquals("130E", location.getLongitude());
	}
	
	@Test
	public void getLatitudetest() {
		Location location=new Location("10E","30S" , "test", false);
		assertEquals("30S", location.getLatitude());
	}
	
	@Test
	public void getNametest() {
		Location location=new Location("99W","4N" , "tWt", true);
		assertEquals("tWt", location.getName());
	}
	
	@Test
	public void isshareabletest() {
		Location location=new Location("4W","30N" , "test", false);
		assertFalse(location.isshareable());
	}
}
