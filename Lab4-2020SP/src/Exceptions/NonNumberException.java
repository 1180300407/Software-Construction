package Exceptions;

public class NonNumberException extends UnGrammaticalWordException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 748286181174609747L;
	
	@Override
	public String getErrorMessage() {
		return super.getErrorMessage()+"�˴���ʶ����,���ܳ��ַ������ַ�!";
	}
}
