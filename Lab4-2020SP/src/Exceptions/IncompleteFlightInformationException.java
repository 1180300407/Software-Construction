package Exceptions;

public class IncompleteFlightInformationException extends UnGrammaticalWordException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3443180420505089271L;
	
	@Override 
	public String getErrorMessage() {
		return super.getErrorMessage()+"航班信息不完整!";
	}
}
