package model;
/*Part of Command Design Pattern*/
public class TakeSpot implements ParkingCommand {
	private ParkingSpot parkingSpot;
	
	public TakeSpot(ParkingSpot parkingSpot) {
		this.parkingSpot=parkingSpot;
	}

	@Override
	public void execute(User user) {
		if (parkingSpot.isTaken() || user.getCurrentAddress() != null)
			return;
		parkingSpot.setTaken(true);
		user.copyAddress(parkingSpot.getAddress());
		user.getDatabaseConnector().userGotParkingSpot(user);
		System.out
				.println("The user " + user.getUserName() + " took the parking spot at " + parkingSpot.getAddress().getCity()
						+ " " + parkingSpot.getAddress().getStreet() + " " + parkingSpot.getAddress().getNumHouse());
		
	}
}
