package Exceptions;

public class LocationNotFoundException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5476085808182984784L;
	
	private String LocationName=null;
	
	public LocationNotFoundException() {
		super();
	}
	
	public LocationNotFoundException(String LocationName) {
		this.LocationName=LocationName;
	}
	
	public String getLocationName() {
		return LocationName;
	}
	
	public String getErrorMessage() {
		return "Œª÷√£∫"+getLocationName()+"≤ª¥Ê‘⁄!";
	}
}
