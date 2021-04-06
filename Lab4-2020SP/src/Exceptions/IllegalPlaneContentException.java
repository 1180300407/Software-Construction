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
		return super.getErrorMessage()+"�ɻ�"+PlaneID+"�Ѵ���,�˴θ÷ɻ������͡���λ�������������ڲ�һ��!";
	}
}
