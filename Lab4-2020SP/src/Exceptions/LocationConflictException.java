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
		return "该位置已被占用"+"强制分配会出现冲突!";
	}
}
