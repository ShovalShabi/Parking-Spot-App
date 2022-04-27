package exceptions;

import model.Address;

public class ParkingSpotTakenException extends Exception {

	public ParkingSpotTakenException(String msg) {
		super(msg);
	}

	public ParkingSpotTakenException(Address address) {
		super("⛔ Oops the parking spot " + address.getCity() + " " + address.getStreet() + " " + address.getNumHouse()
				+ " is not available anymore");
	}
}