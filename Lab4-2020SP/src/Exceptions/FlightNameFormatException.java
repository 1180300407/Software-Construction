package Exceptions;

public class FlightNameFormatException extends UnGrammaticalWordException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2540469260471867881L;
	
	private String FlgihtName=null;
	
	public FlightNameFormatException() {
		super();
	}
	
	public FlightNameFormatException(String FlgihtName) {
		this.FlgihtName=FlgihtName;
	}
	
	@Override
	public String getErrorMessage() {
		return super.getErrorMessage()+"∫Ω∞‡"+FlgihtName+"¥Ê‘⁄∏Ò Ω¥ÌŒÛ!";
	}
}
