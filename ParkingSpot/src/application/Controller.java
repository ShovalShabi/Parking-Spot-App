package application;

import java.util.List;
import exceptions.ParkingSpotTakenException;
import exceptions.SameEmailException;
import exceptions.UnvalidAddressException;
import exceptions.UserHasNoAddressException;
import exceptions.UserNotFoundException;
import model.AbstractParkingSystem;
import model.Address;
import model.Car;
import model.ParkingSpot;
import model.Report;
import model.User;

public class Controller implements UIEventsListener, AbstractParkingSystem {
	AbstractParkingSystem system;
	UIEventsListener viewListener;

	public Controller(AbstractParkingSystem system, UIEventsListener view) {
		this.system = system;
		this.viewListener = view;
		this.addUIListener(this);
		this.addModelListener(this);
	}

///////////////////////////////////////////////// UIEventsListener
///////////////////////////////////////////////// Methods////////////////////////////////////////////////////////

	@Override
	public void addUIListener(UIEventsListener listener) {
		this.viewListener.addUIListener(listener);
	}

	@Override
	public void viewUserSignIn(String email, String password) throws UserNotFoundException {
		this.signInUser(email, password);
	}

	@Override
	public void viewUserSignUp(String name, String Email, String password, Car car) throws SameEmailException {
		this.signUpUserAndRun(name, Email, password, car);
	}

	@Override
	public void viewUserLogOut() {
		this.logOutUser();

	}

	@Override
	public ParkingSpot viewUserSearchParkingSpot(User user, String city, String street, String numHouse)
			throws UnvalidAddressException, ParkingSpotTakenException {
		return this.userSearchForAddress(user, city, street, numHouse);
	}

	@Override
	public void viewUserReportParkingSpot(User user, Report report) {
		this.userSendReport(user, report);

	}

	@Override
	public ParkingSpot viewFindUserlocation(User user, String city, String street, String numHouse)
			throws UnvalidAddressException, ParkingSpotTakenException {
		return this.userSearchForAddressForHisCar(user, city, street, numHouse);
	}

	@Override
	public Address viewGetUserCarLocation(User user) throws UserHasNoAddressException {
		return this.userGetCarAddress(user);
	}

	@Override
	public List<Report> viewGetAllReportsFromModel() {
		return this.userGetAllReports();
	}

	@Override
	public List<Report> viewGetCurrentUserReports(User user) {
		return this.userGetHisReports(user);
	}

	@Override
	public User viewGetCurrentUser() {
		return this.systemGetSignedInUser();
	}
	
	@Override
	public void viewUserTakeParkingSpot(User user, ParkingSpot parkingSpot) throws ParkingSpotTakenException {
		this.userTakeParkingSpot(user, parkingSpot);
		
	}

	@Override
	public void viewUserLeaveParkingSpot(User user) {
		this.userLeaveParkingSpot(user);
		
	}

	@Override
	public void exitApp() {
		this.shutdownApp();

	}

	///////////////////////////////////////////////// AbstractParkingSytem
	///////////////////////////////////////////////// Methods////////////////////////////////////////////////////////

	@Override
	public void startApp() {
		this.system.startApp();
	}

	@Override
	public void addModelListener(AbstractParkingSystem listener) {
		this.system.addModelListener(listener);
	}

	@Override
	public void signUpUserAndRun(String name, String Email, String password, Car car) throws SameEmailException {
		this.system.signUpUserAndRun(name, Email, password, car);
	}

	@Override
	public void signInUser(String Email, String password) throws UserNotFoundException {
		this.system.signInUser(Email, password);

	}

	@Override
	public void logOutUser() {
		this.system.logOutUser();

	}

	@Override
	public ParkingSpot userSearchForAddress(User user, String city, String street, String numHouse)
			throws UnvalidAddressException, ParkingSpotTakenException {
		return this.system.userSearchForAddress(user, city, street, numHouse);
	}

	@Override
	public void userSendReport(User user, Report report) {
		this.system.userSendReport(user, report);
	}

	@Override
	public ParkingSpot userSearchForAddressForHisCar(User user, String city, String street, String numHouse)
			throws UnvalidAddressException, ParkingSpotTakenException {
		return this.system.userSearchForAddressForHisCar(user, city, street, numHouse);
	}

	@Override
	public List<Report> userGetHisReports(User user) {
		return system.userGetHisReports(user);
	}

	@Override
	public List<Report> userGetAllReports() {
		return this.system.userGetAllReports();
	}

	@Override
	public void userTakeParkingSpot(User user, ParkingSpot parkingSpot) throws ParkingSpotTakenException {
		this.system.userTakeParkingSpot(user, parkingSpot);

	}

	@Override
	public void userLeaveParkingSpot(User user) {
		this.system.userLeaveParkingSpot(user);

	}

	@Override
	public User systemGetSignedInUser() {
		return system.systemGetSignedInUser();
	}

	@Override
	public void shutdownApp() {
		this.system.shutdownApp();

	}

	@Override
	public Address userGetCarAddress(User user) throws UserHasNoAddressException {
		return this.system.userGetCarAddress(user);
	}



}
