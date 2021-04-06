package Exceptions;

public class PlaneIDFormatException extends UnGrammaticalWordException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 534897311009773495L;
	
	private String PlaneID=null;
	
	public PlaneIDFormatException() {
		super();
	}
	
	public PlaneIDFormatException(String PlaneID) {
		this.PlaneID=PlaneID;
	}
	
	@Override
	public String getErrorMessage() {
		return super.getErrorMessage()+"�ɻ�"+PlaneID+"�ı�Ŵ��ڸ�ʽ����!";
	}
}
