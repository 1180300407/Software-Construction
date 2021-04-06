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
		if(locationortime)//时间不一致
			return "航班号相同的航班出发或到达时间不一致!"+super.getErrorMessage();
		else//位置不一致
			return "航班号相同的航班出发或到达机场不一致!"+super.getErrorMessage();
	}
}
