package Exceptions;

public class NonNumberException extends UnGrammaticalWordException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 748286181174609747L;
	
	@Override
	public String getErrorMessage() {
		return super.getErrorMessage()+"此处标识数量,不能出现非数字字符!";
	}
}
