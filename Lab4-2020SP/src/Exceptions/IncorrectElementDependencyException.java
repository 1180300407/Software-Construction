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
		return "元素间依赖关系不正确!"+errorMessage;
	}
	
}
