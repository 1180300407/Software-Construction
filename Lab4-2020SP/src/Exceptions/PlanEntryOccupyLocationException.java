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
		return "Ŀǰ�ڸ�λ���мƻ�������ִ��!�üƻ�������Ϊ:"+getPlanEntryName()+","+"�޷�ɾ��!";
	}
	
	public String getPlanEntryName() {
		return this.PlanEntryName;
	}
}
