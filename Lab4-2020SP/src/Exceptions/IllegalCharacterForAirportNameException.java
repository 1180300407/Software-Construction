package Exceptions;

public class IllegalCharacterForAirportNameException extends UnGrammaticalWordException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3701643201791600332L;
	
	private String AirportName=null;
	
	public IllegalCharacterForAirportNameException() {
		super();
	}
	
	public IllegalCharacterForAirportNameException(String AirportName) {
		this.AirportName=AirportName;
	}
	
	@Override
	public String getErrorMessage() {
		return super.getErrorMessage()+"机场"+AirportName+"出现非法字符!";
	}
}
