package Exceptions;

public class SameLabelException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4288103181824210051L;
	
	private String label=null;
	
	public SameLabelException() {
		super();
	}
	
	public SameLabelException(String label) {
		this.label=label;
	}
	
	public String getErrorMessage() {
		return "出现标签相同的元素:"+getLabel();
	}
	
	public String getLabel() {
		return label;
	}
}
