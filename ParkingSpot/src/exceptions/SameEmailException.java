package exceptions;

public class SameEmailException extends Exception {

	public SameEmailException(String msg) {
		super(msg);
	}
	
	public SameEmailException() {
		super ("⚠ There is already a user with the same Email in the app");
	}
}
