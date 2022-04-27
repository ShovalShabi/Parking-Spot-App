package application;

import java.util.List;

import exceptions.ParkingSpotTakenException;
import exceptions.SameEmailException;
import exceptions.UnvalidAddressException;
import exceptions.UserHasNoAddressException;
import exceptions.UserNotFoundException;
import model.Address;
import model.Car;
import model.ParkingSpot;
import model.Report;
import model.User;

public interface UIEventsListener {

	void addUIListener(UIEventsListener listener);
	void viewUserSignIn(String email, String password) throws UserNotFoundException;
	void viewUserSignUp(String name, String Email, String password, Car car) throws SameEmailException;
	void viewUserLogOut();
	ParkingSpot viewUserSearchParkingSpot(User user,String city, String street, String numHouse) throws UnvalidAddressException, ParkingSpotTakenException;
	ParkingSpot viewFindUserlocation(User user,String city, String street, String numHouse) throws UnvalidAddressException, ParkingSpotTakenException;
	void viewUserReportParkingSpot(User user , Report report);
	Address viewGetUserCarLocation(User user) throws UserHasNoAddressException;
	List<Report> viewGetAllReportsFromModel();
	List<Report> viewGetCurrentUserReports(User user);
	User viewGetCurrentUser();
	void viewUserTakeParkingSpot(User user, ParkingSpot parkingSpot) throws ParkingSpotTakenException;
	void viewUserLeaveParkingSpot(User user);
	void exitApp();
}
