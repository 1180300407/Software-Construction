package Exceptions;

public class IncorrectElementDependencyException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1458958125032189435L;
	
	private String errorMessage=null;
	
	public IncorrectElementDependencyException() {
		super();
	}
	
	public IncorrectElementDependencyException(String errorMessage) {
		this.errorMessage=errorMessage;
	}
	
	public String getErrorMessage() {
		return "Ԫ�ؼ�������ϵ����ȷ!"+errorMessage;
	}
	
}
