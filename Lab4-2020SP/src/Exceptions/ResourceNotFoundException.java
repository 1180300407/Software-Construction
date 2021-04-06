package Exceptions;

public class ResourceNotFoundException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5492691352250519233L;
	
	private String ResourceName=null;
	
	public ResourceNotFoundException() {
		super();
	}
	
	public ResourceNotFoundException(String ResourceName) {
		this.ResourceName=ResourceName;
	}
	
	public String getResourceName() {
		return ResourceName;
	}
	
	public String getErrorMessage() {
		return "资源："+getResourceName()+"不存在!";
	}
}
