package Exceptions;

public class PlanEntryStateNotMatchException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6565051025619002671L;
	
	private String EntryState=null;
	
	public PlanEntryStateNotMatchException() {
		super();
	}
	
	public PlanEntryStateNotMatchException(String stateName) {
		EntryState=stateName;
	}
	
	public String getEntryState() {
		return EntryState;
	}
	
	public String getErrorMessage() {
		return "目前计划项状态为:"+getEntryState()+","+"状态不匹配!";
	}
	
}
