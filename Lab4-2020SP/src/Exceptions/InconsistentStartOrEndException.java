package Exceptions;

public class InconsistentStartOrEndException extends IncorrectElementDependencyException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2666315505492282312L;
	
	private boolean locationortime=false;
	public InconsistentStartOrEndException(boolean locationortime) {
		this.locationortime=locationortime;
	}
	
	public InconsistentStartOrEndException() {
		super();
	}
	
	@Override
	public String getErrorMessage() {
		if(locationortime)//ʱ�䲻һ��
			return "�������ͬ�ĺ�������򵽴�ʱ�䲻һ��!"+super.getErrorMessage();
		else//λ�ò�һ��
			return "�������ͬ�ĺ�������򵽴������һ��!"+super.getErrorMessage();
	}
}
