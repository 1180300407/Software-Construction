package Exceptions;

public class DateDifferMuchException extends IncorrectElementDependencyException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5699791644084341533L;
	
	public DateDifferMuchException() {
		super();
	}
	
	public DateDifferMuchException(String errorMessage) {
		super(errorMessage);
	}
	
	@Override
	public String getErrorMessage() {
		return "������ںͽ������ڲ�����1��!"+super.getErrorMessage();
	}
}
