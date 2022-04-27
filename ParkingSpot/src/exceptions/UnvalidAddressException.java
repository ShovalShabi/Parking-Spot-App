package exceptions;

public class UnvalidAddressException extends Exception {
	public UnvalidAddressException(String msg) {
		super(msg);
	}
	
	public UnvalidAddressException(String city, String street, String numHouse) {
		super ("⚠ We're sorry we couldn't find "+city+" "+street+" "+numHouse+", please try again");
	}
}
