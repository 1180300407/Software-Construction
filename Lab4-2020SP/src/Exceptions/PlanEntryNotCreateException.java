package Exceptions;

public class PlanEntryNotCreateException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5723732453174153776L;
	
	private String PlanEntryName=null;
	
	public PlanEntryNotCreateException() {
		super();
	}
	
	public PlanEntryNotCreateException(String PlanEntryName) {
		this.PlanEntryName=PlanEntryName;
	}
	
	public String getPlanEntryName() {
		return PlanEntryName;
	}
	
	public String getErrorMessage() {
		return "计划项:"+getPlanEntryName()+" 还未创建!无法进行操作!";
	}
}
