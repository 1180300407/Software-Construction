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
		return "Ŀǰռ�ø���Դ�ļƻ�������Ϊ:"+getPlanEntryName()+"�޷�ɾ��!";
	}
	
	public String getPlanEntryName() {
		return this.PlanEntryName;
	}
}
