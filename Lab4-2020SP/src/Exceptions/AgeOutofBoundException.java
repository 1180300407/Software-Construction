package Exceptions;

public class AgeOutofBoundException extends UnGrammaticalWordException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2540050075627556674L;
	
	@Override
	public String getErrorMessage() {
		return super.getErrorMessage()+"机龄大小超出范围!";
	}
}
