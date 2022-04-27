package exceptions;

public class UnvalidCarPlateException extends Exception {
	public UnvalidCarPlateException(String msg) {
		super(msg);
	}
	
	public UnvalidCarPlateException() {
		super ("⚠ Please enter a car plate number between 6 to 8 digits!");
	}
}
