package Exceptions;

public class UnGrammaticalWordException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8087224468754018139L;
	
	private String message=null;
	
	public UnGrammaticalWordException() {
		super();
	}
	
	public UnGrammaticalWordException(String message) {
		super();
		this.message=message;
	}
	
	public String getErrorMessage() {
		return "”Ô∑®πÊ‘Ú¥ÌŒÛ:"+message;
	}
}
