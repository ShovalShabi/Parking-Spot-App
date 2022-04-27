package model;

import java.util.List;
import exceptions.ParkingSpotTakenException;
import exceptions.SameEmailException;
import exceptions.UnvalidAddressException;
import exceptions.UserHasNoAddressException;
import exceptions.UserNotFoundException;

public interface AbstractParkingSystem {
	
	void startApp();
	void addModelListener(AbstractParkingSystem listener);
	void signUpUserAndRun(String name, String Email, String password, Car car) throws SameEmailException;
	void signInUser(String Email, String password) throws UserNotFoundException;
	void logOutUser();	
	ParkingSpot userSearchForAddress(User user, String city, String street, String numHouse)
			throws UnvalidAddressException, ParkingSpotTakenException;
	public ParkingSpot userSearchForAddressForHisCar(User user, String city, String street, String numHouse)
			throws UnvalidAddressException, ParkingSpotTakenException;
	void userSendReport(User user, Report report);
	List<Report> userGetHisReports(User user);	
	List<Report> userGetAllReports();	
	Address userGetCarAddress(User user) throws UserHasNoAddressException;
	void userTakeParkingSpot(User user, ParkingSpot parkingSpot) throws ParkingSpotTakenException;
	void userLeaveParkingSpot(User user);	
	User systemGetSignedInUser();
	void shutdownApp();

}
