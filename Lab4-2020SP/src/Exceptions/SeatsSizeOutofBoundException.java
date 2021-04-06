package Exceptions;

public class SeatsSizeOutofBoundException extends UnGrammaticalWordException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3378487329463392989L;
	
	@Override
	public String getErrorMessage() {
		return super.getErrorMessage()+"飞机座位数超出范围!";
	}
}
