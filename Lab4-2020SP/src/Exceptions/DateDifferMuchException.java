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
		return "起飞日期和降落日期差距大于1天!"+super.getErrorMessage();
	}
}
