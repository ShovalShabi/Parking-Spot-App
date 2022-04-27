package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import database.DatabaseConnector;

public class User extends Thread implements UserActions, ReportObserver {
	private Address currentAddress;
	private String email;
	private String userName;
	private String password;
	private AddressFinder addressFinder;
	private List<Report> allReports;
	private List<ParkingSpot> allParkingSpots;
	private boolean isSelected;
	private Car car;
	private int totalLikes;
	private DatabaseConnector databaseConnector;

	public User(Address currentAddress, String email, String userName, String password, List<Report> reports,
			List<ParkingSpot> allParkingSpots, Car car) {
		this.currentAddress = currentAddress;
		this.email = email;
		this.userName = userName;
		this.password = password;
		this.addressFinder = new AddressFinder();
		this.allReports = Collections.synchronizedList(reports);
		this.allParkingSpots = Collections.synchronizedList(allParkingSpots);
		this.isSelected = false;
		this.car = car;
		this.totalLikes = 0;
		this.databaseConnector = DatabaseConnector.getInstance();
	}

	public User(String email, String userName, String password, int totalLikes) {
		this.currentAddress = null;
		this.email = email;
		this.userName = userName;
		this.password = password;
		this.addressFinder = new AddressFinder();
		this.allReports = Collections.synchronizedList(this.allReports = new ArrayList<Report>());
		this.allParkingSpots = Collections.synchronizedList(this.allParkingSpots = new ArrayList<ParkingSpot>());
		this.isSelected = false;
		this.car = null;
		this.totalLikes = totalLikes;
		this.databaseConnector = DatabaseConnector.getInstance();
	}

	public User(User otherUser) {
		this.currentAddress = otherUser.getCurrentAddress();
		this.email = otherUser.getEmail();
		this.userName = otherUser.getUserName();
		this.password = otherUser.getPassword();
		this.addressFinder = new AddressFinder();
		this.allReports = Collections.synchronizedList(otherUser.getAllReports());
		this.allParkingSpots = Collections.synchronizedList(otherUser.getAllParkingSpots());
		this.setSelected(otherUser.isSelected);
		this.car = otherUser.getCar();
		this.totalLikes = otherUser.getTotalLikes();
		this.databaseConnector = DatabaseConnector.getInstance();
	}

	public Address getCurrentAddress() {
		return currentAddress;
	}

	public void setCurrentAddress(Address currentAddress) {
		this.currentAddress = currentAddress;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public AddressFinder getAddressFinder() {
		return addressFinder;
	}

	public void setAddressFinder(AddressFinder userAddressFinder) {
		this.addressFinder = userAddressFinder;
	}

	public List<Report> getAllReports() {
		return allReports;
	}

	public void setAllReports(List<Report> reports) {
		this.allReports = Collections.synchronizedList(reports);
	}

	public List<ParkingSpot> getAllParkingSpots() {
		return allParkingSpots;
	}

	public void setAllParkingSpots(List<ParkingSpot> allParkingSpots) {
		this.allParkingSpots = Collections.synchronizedList(allParkingSpots);
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public int getTotalLikes() {
		return totalLikes;
	}

	public void setTotalLikes(int totalLikes) {
		this.totalLikes = totalLikes;
	}

	public DatabaseConnector getDatabaseConnector() {
		return databaseConnector;
	}

	public void setDatabaseConnector(DatabaseConnector databaseConnector) {
		this.databaseConnector = databaseConnector;
	}

	public synchronized void likeReport(Report report) {
		if (!report.getUsersLiked().contains(this)) {
			report.addLikedUser(this);
			report.setNumOfLikes(report.getNumOfLikes() + 1);
		}
	}

	public void copyAddress(Address address) {
		this.currentAddress = new Address(address.getCountry(), address.getCity(), address.getStreet(),
				address.getNumHouse(), address.getCoordinate());
	}

	// Command pattern: the user declares on specific parking spot to be not
	// available
	@Override
	public synchronized void userTakeParking(ParkingSpot parkingSpot) {
		TakeSpot takePS = new TakeSpot(parkingSpot);
		takePS.execute(this);
	}

	// Command pattern: the user declares on specific parking spot to be available
	@Override
	public synchronized void userLeaveParking(ParkingSpot parkingSpot) {
		LeaveSpot leavePS=new LeaveSpot(parkingSpot);
		leavePS.execute(this);
	}

	// Observer pattern: the user notifies to other user on new report that he
	// reported
	@Override
	public synchronized void notifyObserverOnNewReport(Report report) {
		report.notifyAllObserversOnReport();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			User other = (User) obj;
			if (other.getEmail().equals(this.getEmail())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return userName;
	}

	/*Method that makes the unsigned users to act like a bot:
	 * The user will send reports, like active reports and will switch places
	 * **********************************************************************/
	@Override
	public void run() {
		System.out.println("The user " + this.userName + " is running!");
		int randomParkingNum;
		ParkingSpot parkingSpot = null;
		try {
			while (!isSelected) {
				if (!this.allReports.isEmpty()) {//user making likes
					for (Report report : allReports) {
						int like = (int) (Math.random() * 3);
						if (like > 0)
							this.likeReport(report);
					}
				}
				if (this.currentAddress != null) {// making sure the user left the parking spot
					for (ParkingSpot parkingSpot1 : allParkingSpots) {
						if (parkingSpot1.getAddress().equals(this.currentAddress))
							this.userLeaveParking(parkingSpot1);
					}
				}
				if (!allParkingSpots.isEmpty()) {// user going to other address
					randomParkingNum = (int) (Math.random() * allParkingSpots.size());
					parkingSpot = allParkingSpots.get(randomParkingNum);
					if (!parkingSpot.isTaken())
						this.userTakeParking(parkingSpot);
				}
				sleep(60000);// waits outside of parking spot

			}
		} catch (Exception e) {

		}
	}

}
