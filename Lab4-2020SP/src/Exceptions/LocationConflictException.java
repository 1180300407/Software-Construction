package Exceptions;

public class LocationConflictException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7729158045271103478L;
	
	public LocationConflictException() {
		super();
	}
	
	public String getErrorMessage() {
		return "��λ���ѱ�ռ��"+"ǿ�Ʒ������ֳ�ͻ!";
	}
}
