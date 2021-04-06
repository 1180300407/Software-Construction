package Exceptions;

public class IllegalPlaneContentException extends IncorrectElementDependencyException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9101263877993671225L;
	private String PlaneID=null;
	
	public IllegalPlaneContentException() {
		super();
	}
	
	public IllegalPlaneContentException(String PlaneID) {
		this.PlaneID=PlaneID;
	}
	
	@Override
	public String getErrorMessage() {
		return super.getErrorMessage()+"飞机"+PlaneID+"已创建,此次该飞机的类型、座位数或机龄与其存在不一致!";
	}
}
