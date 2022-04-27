package model;
/*Part of Command Design Pattern*/
public class LeaveSpot implements ParkingCommand {
	private ParkingSpot parkingSpot;

	public LeaveSpot(ParkingSpot parkingSpot) {
		this.parkingSpot = parkingSpot;
	}

	@Override
	public void execute(User user) {
		user.getDatabaseConnector().userLeavesParkingSpot(user);
		user.setCurrentAddress(null);
		Report report = new Report(
				"There is parking spot available at " + parkingSpot.getAddress().getCity() + " "
						+ parkingSpot.getAddress().getStreet() + " " + parkingSpot.getAddress().getNumHouse(),
				user, parkingSpot.getAddress());
		user.notifyObserverOnNewReport(report);
		System.out.println(report.getInfo());
		parkingSpot.setTaken(false);
		System.out.println(
				"The user " + user.getUserName() + " left the parking spot at " + parkingSpot.getAddress().getCity()
						+ " " + parkingSpot.getAddress().getStreet() + " " + parkingSpot.getAddress().getNumHouse());
		

	}

}
