package exceptions;

public class UserNotFoundException extends Exception{

	public UserNotFoundException(String userName) {
		super ("⚠ We're sorry we couldn't find "+userName+" please let us know you!");
	}
}
