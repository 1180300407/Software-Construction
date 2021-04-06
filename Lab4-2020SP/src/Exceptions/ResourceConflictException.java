package Exceptions;

public class ResourceConflictException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5907956507323177441L;
	private String Resource=null;
	
	public ResourceConflictException() {
		super();
	}
	
	public ResourceConflictException(String Resource) {
		this.Resource=Resource;
	}
	
	public String getResource() {
		return Resource;
	}
	
	public String getErrorMessage() {
		return "资源:"+getResource()+" 已被分配!如果强制分配会产生冲突!";
	}
}
