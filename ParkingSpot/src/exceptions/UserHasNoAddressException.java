package exceptions;

public class UserHasNoAddressException extends Exception {
	public UserHasNoAddressException(String msg) {
		super(msg);
	}
	
	public UserHasNoAddressException() {
		super ("⚠  We're sorry we don't know where you are ,Please tell us!");
	}
}
