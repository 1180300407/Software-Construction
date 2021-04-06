package Exceptions;

public class InconsistentDateException extends IncorrectElementDependencyException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2203113031138969207L;
	
	public InconsistentDateException(String errorMessage) {
		super(errorMessage);
	}
	
	public InconsistentDateException() {
		super();
	}
	
	@Override
	public String getErrorMessage() {
		return "�����������ڲ����ʱ�����ڲ�һ��!"+super.getErrorMessage();
	}
}
