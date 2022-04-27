package exceptions;

public class EmptyFieldException extends Exception {
	
	public EmptyFieldException(String msg) {
		super(msg);
	}
	
	public EmptyFieldException() {
		super ("⚠ There are some empty fields, please fill the necessary fields");
	}
}
