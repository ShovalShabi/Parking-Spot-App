package model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import database.DatabaseConnector;
import exceptions.ParkingSpotTakenException;
import exceptions.SameEmailException;
import exceptions.UnvalidAddressException;
import exceptions.UserHasNoAddressException;
import exceptions.UserNotFoundException;

public class ParkingSystem extends Thread implements AbstractParkingSystem {

	private List<User> allUsers;
	private List<ParkingSpot> allParkingSpots;
	private List<Report> allReports;
	private List<AbstractParkingSystem> allListeners;
	private User selectedUser;
	private DatabaseConnector databaseConnector;

	public ParkingSystem() {
		this.allUsers = Collections.synchronizedList(new ArrayList<User>());
		this.allParkingSpots = Collections.synchronizedList(new ArrayList<ParkingSpot>());
		this.allReports = Collections.synchronizedList(new ArrayList<Report>());
		this.allListeners = new ArrayList<AbstractParkingSystem>();
		this.selectedUser = null;
		this.databaseConnector = DatabaseConnector.getInstance();
	}

	public List<User> getAllUsers() {
		return allUsers;
	}

	public void setAllUsers(List<User> allUsers) {
		this.allUsers = (ArrayList<User>) allUsers;
		Collections.synchronizedList(this.allUsers);
	}

	public List<ParkingSpot> getAllParkingSpots() {
		return allParkingSpots;
	}

	public void setAllParkingSpots(List<ParkingSpot> allParkingSpots) {
		this.allParkingSpots = (ArrayList<ParkingSpot>) allParkingSpots;
		Collections.synchronizedList(this.allParkingSpots);
	}

	public List<Report> getAllReports() {
		return allReports;
	}

	public void setAllReports(List<Report> allReports) {
		this.allReports = (ArrayList<Report>) allReports;
		Collections.synchronizedList(this.allReports);
	}

	public User getSelecterUser() {
		return selectedUser;
	}

	public void setSelecterUser(User selecterUser) {
		this.selectedUser = selecterUser;
	}

	//Loading Data from DB
	public void loadData() {
		try {
			this.databaseConnector.moveToModel(this);
			for (User user : allUsers) {
				user.setAllReports(this.allReports);
				user.setAllParkingSpots(this.allParkingSpots);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	//Initializing the application
	@Override
	public void startApp() {
		this.databaseConnector.openConnection();
		this.loadData();
		this.start();

	}

	//Connecting model to controller
	@Override
	public void addModelListener(AbstractParkingSystem listener) {
		this.allListeners.add(listener);

	}

	//Signing up an user and running it (will represent bot behavior until the real user logs in to the system)
	@Override
	public synchronized void signUpUserAndRun(String name, String Email, String password, Car car)
			throws SameEmailException {
		User tempUser = new User(Email, name, password, 0);
		if (allUsers.contains(tempUser))
			throw new SameEmailException();
		User user = new User(null, Email, name, password, this.getAllReports(), this.getAllParkingSpots(), car);
		this.addUser(user);
		this.databaseConnector.addUserToDatabase(user);
		user.start();
		System.out.println(name + " signed up successfully!");
	}

	//User log inS
	@Override
	public synchronized void signInUser(String Email, String password) throws UserNotFoundException {
		for (User user : allUsers) {
			if (user.getEmail().equals(Email) && user.getPassword().equals(password)) {
				this.selectedUser = user;
				user.stop();
				this.selectedUser.setSelected(true);
				this.selectedUser.setCurrentAddress(null);
				this.databaseConnector.userLeavesParkingSpot(this.selectedUser);
				System.out.println(user.getUserName() + " got selected!");
				return;
			}
		}
		throw new UserNotFoundException(Email);
	}

	//User search for a valid parking spot
	@Override
	public synchronized ParkingSpot userSearchForAddress(User user, String city, String street, String numHouse)
			throws UnvalidAddressException, ParkingSpotTakenException {
		Address address = user.getAddressFinder().getValidAddress(city, street, numHouse);
		if (address == null)
			throw new UnvalidAddressException(city, street, numHouse);
		ParkingSpot parkingSpot = new ParkingSpot(address);
		if (!this.allParkingSpots.contains(parkingSpot)) {
			this.addParkingSpot(parkingSpot);
			this.databaseConnector.addParkingSpotToDatabase(parkingSpot);
		} else {
			for (ParkingSpot ps : allParkingSpots) {
				if (ps.equals(parkingSpot))
					parkingSpot = ps;
			}
		}
		if (parkingSpot.isTaken())
			throw new ParkingSpotTakenException(parkingSpot.getAddress());
		return parkingSpot;
	}

	//User search for his parking spot location
	@Override
	public synchronized ParkingSpot userSearchForAddressForHisCar(User user, String city, String street,
			String numHouse) throws UnvalidAddressException, ParkingSpotTakenException {
		Address address = user.getAddressFinder().getValidAddress(city, street, numHouse);
		if (address == null)
			throw new UnvalidAddressException(city, street, numHouse);
		ParkingSpot parkingSpot = new ParkingSpot(address);
		if (!this.allParkingSpots.contains(parkingSpot)) {
			this.databaseConnector.addParkingSpotToDatabase(parkingSpot);
		} else {
			for (ParkingSpot ps : allParkingSpots) {
				if (ps.equals(parkingSpot))
					parkingSpot = ps;
			}
		}
		if (parkingSpot.isTaken())
			throw new ParkingSpotTakenException(parkingSpot.getAddress());
		user.userTakeParking(parkingSpot);
		databaseConnector.userGotParkingSpot(user);
		return parkingSpot;
	}

	public void addParkingSpot(ParkingSpot parkingSpot) {
		this.allParkingSpots.add(parkingSpot);
	}

	public void addUser(User user) {
		this.allUsers.add(user);
	}

	//User send a public report to all users
	@Override
	public synchronized void userSendReport(User user, Report report) {
		report.notifyAllObserversOnReport();
	}

	//User want to review its reports
	@Override
	public synchronized List<Report> userGetHisReports(User user) {
		List<Report> userReports = new ArrayList<Report>();
		for (Report report : allReports) {
			if (report.getReporterUser().equals(user))
				userReports.add(report);
		}
		return userReports;
	}

	//User fetch a parking spot
	@Override
	public synchronized void userTakeParkingSpot(User user, ParkingSpot parkingSpot) throws ParkingSpotTakenException {
		if (parkingSpot.isTaken())
			throw new ParkingSpotTakenException(parkingSpot.getAddress());
		user.userTakeParking(parkingSpot);
		this.databaseConnector.userGotParkingSpot(user);
	}

	//User leaving parking spot
	@Override
	public synchronized void userLeaveParkingSpot(User user) {
		this.databaseConnector.userLeavesParkingSpot(user);
		for (ParkingSpot parkingSpot : allParkingSpots) {
			if (parkingSpot.getAddress().equals(user.getCurrentAddress())) {
				user.userLeaveParking(parkingSpot);
				break;
			}
		}
	}

	@Override
	public void run() {
		for (User user : allUsers) {
			user.start();
		}
	}

	@Override
	public User systemGetSignedInUser() {
		return this.selectedUser;
	}

	//Backing up the data and shutting down the application
	@Override
	public void shutdownApp() {
		for (User user : allUsers) {
			user.stop();
		}
		for (Report report : allReports) {
			report.stop();
		}
		this.stop();
		this.databaseConnector.closeConnection();
	}

	@Override
	public void logOutUser() {
		for (ParkingSpot parkingSpot : allParkingSpots) {
			if (this.selectedUser.getCurrentAddress() != null) {
				if (parkingSpot.getAddress().equals(this.selectedUser.getCurrentAddress())) {
					this.databaseConnector.userLeavesParkingSpot(this.selectedUser);
					this.selectedUser.setCurrentAddress(null);
					parkingSpot.setTaken(false);
				}
			}
		}
		User user = new User(this.selectedUser);
		this.allUsers.remove(this.selectedUser);
		this.addUser(user);
		user.setSelected(false);
		user.start();
		System.out.println(user.getUserName() + " logged out of the system!");
		this.setSelecterUser(null);
	}

	@Override
	public List<Report> userGetAllReports() {
		return this.allReports;
	}

	//User request its car location
	@Override
	public Address userGetCarAddress(User user) throws UserHasNoAddressException {
		if (user.getCurrentAddress() == null) {
			throw new UserHasNoAddressException();
		}
		return user.getCurrentAddress();
	}

}
