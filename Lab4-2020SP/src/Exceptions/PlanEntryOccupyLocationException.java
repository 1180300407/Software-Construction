package Exceptions;

public class PlanEntryOccupyLocationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3450771837499863688L;
	
private String PlanEntryName=null;
	
	public PlanEntryOccupyLocationException() {
		super();
	}
	
	public PlanEntryOccupyLocationException(String PlanEntryName) {
		this.PlanEntryName=PlanEntryName;
	}
	
	public String getErrorMessage() {
		return "目前在该位置有计划项正在执行!该计划项名称为:"+getPlanEntryName()+","+"无法删除!";
	}
	
	public String getPlanEntryName() {
		return this.PlanEntryName;
	}
}
