package Exceptions;

public class PlanEntryOccupyResourceException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8553827254118254541L;
	
	private String PlanEntryName=null;
	
	public PlanEntryOccupyResourceException() {
		super();
	}
	
	public PlanEntryOccupyResourceException(String PlanEntryName) {
		this.PlanEntryName=PlanEntryName;
	}
	
	public String getErrorMessage() {
		return "目前占用该资源的计划项名称为:"+getPlanEntryName()+"无法删除!";
	}
	
	public String getPlanEntryName() {
		return this.PlanEntryName;
	}
}
