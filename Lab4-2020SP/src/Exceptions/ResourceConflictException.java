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
		return "��Դ:"+getResource()+" �ѱ�����!���ǿ�Ʒ���������ͻ!";
	}
}
