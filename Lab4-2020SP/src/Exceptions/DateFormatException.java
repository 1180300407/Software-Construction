package Exceptions;

public class DateFormatException extends UnGrammaticalWordException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -319347588953378537L;
	
	@Override
	public String getErrorMessage() {
		return super.getErrorMessage()+"�������ڴ��ڸ�ʽ����!";
	}
}
