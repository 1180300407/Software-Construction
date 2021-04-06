package Exceptions;

public class TypeContainsOtherSymbolException extends UnGrammaticalWordException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6458387295236636786L;
	private String type=null;
	
	public TypeContainsOtherSymbolException(String type) {
		this.type=type;
	}
	
	public TypeContainsOtherSymbolException() {
		super();
	}
	
	@Override 
	public String getErrorMessage() {
		return super.getErrorMessage()+"机型"+type+"中出现其他符号!";
	}
}
