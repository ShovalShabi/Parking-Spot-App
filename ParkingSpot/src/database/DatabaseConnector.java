package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import exceptions.UnvalidCarPlateException;
import model.Address;
import model.Car;
import model.ParkingSpot;
import model.ParkingSystem;
import model.Report;
import model.User;
//JDBC class implemented by Singleton Design Pattern
public class DatabaseConnector {

	static private DatabaseConnector instance;
	private Connection con;
	private String url;
	private String username;
	private String password;
	private Statement statement;
	private ResultSet result;
	private Map<Integer, User> allUsers;
	private Map<Integer, ParkingSpot> allParkingSpot;
	private Map<Integer, Address> allAddresses;
	private Map<Integer, Report> allReports;
	private static int userIdGenerator = 1000;
	private static int parkingSpotIdGenerator = 1000;
	private static int addressIdGenerator = 1000;
	private static int reportIdGenerator = 1000;

	private DatabaseConnector() {
		this.allUsers = Collections.synchronizedMap(new TreeMap<Integer, User>());
		this.allParkingSpot = Collections.synchronizedMap(new TreeMap<Integer, ParkingSpot>());
		this.allAddresses = Collections.synchronizedMap(new TreeMap<Integer, Address>());
		this.allReports = Collections.synchronizedMap(new TreeMap<Integer, Report>());
	}

	public static DatabaseConnector getInstance() {
		if (instance == null) {
			return new DatabaseConnector();
		}
		return instance;
	}

	public void moveToModel(ParkingSystem model) throws SQLException {
		try {
			this.readAddresses();
			this.readParkingSpot();
			this.readUsers();
			// this.readReports();
			this.orgenaizeUsersLikes();
			this.orgenaizeUsersAndParkingSpots();
			List<User> listUsers = new ArrayList<User>(this.allUsers.values());
			List<ParkingSpot> listParkingSpots = new ArrayList<ParkingSpot>(this.allParkingSpot.values());
			model.setAllUsers(listUsers);
			model.setAllParkingSpots(listParkingSpots);
		} catch (Exception e) {
			this.closeConnection();
			System.out.println(e.getMessage());
		}
	}

	public synchronized void addUserToDatabase(User user) {
		this.openConnection();
		this.insertCar(user.getCar());
		this.insertUser(user);
		this.closeConnection();
		System.out.println("The user " + user.getUserName() + " " + user.getEmail()
				+ " has been added to the database succuessfully!");
	}

	public synchronized void addParkingSpotToDatabase(ParkingSpot parkingSpot) {
		this.openConnection();
		this.insertAddress(parkingSpot.getAddress());
		int addressId = fetchAddressId(parkingSpot.getAddress());
		this.insertParkingSpot(parkingSpot, addressId);
		this.closeConnection();
		System.out.println(
				"The address " + parkingSpot.getAddress().getCity() + " " + parkingSpot.getAddress().getStreet() + " "
						+ parkingSpot.getAddress().getNumHouse() + " has been entered to the database successfully!");

	}

	public synchronized void addReportToDatabase(Report report) {
		this.openConnection();
		int userId = this.fetchUserId(report.getReporterUser());
		int parkingId = this.fetchAddressId(report.getAddress());
		this.insertReport(report, userId);
		int reportId = this.fetchReportIdByUser(report.getReporterUser());
		int reportlikes = fetchReportLikes(reportId);
		this.updateUserLikes(userId, reportlikes);
		this.insertValuesToRUP(reportId, userId, parkingId);
		this.closeConnection();
		System.out.println("Report on of :" + report.getReporterUser().getEmail() + " with " + report.getNumOfLikes()
				+ " likes has been moved to archive!");
	}

	public synchronized void userGotParkingSpot(User user) {
		this.openConnection();
		int userId = fetchUserId(user);
		int addressId = fetchAddressId(user.getCurrentAddress());
		int parkingSpotId = addressId;
		this.updateUserTableUserGotParking(userId, addressId);
		this.insertValuesToAUP(addressId, userId, parkingSpotId);
		this.closeConnection();
	}

	public synchronized void userLeavesParkingSpot(User user) {
		this.openConnection();
		int userId = fetchUserId(user);
		this.removeUserFromAUPTable(userId);
		this.updateUserTableUserLeftParking(userId);
		this.closeConnection();
	}

	public synchronized void getUserLikesFromDatabase(User user) {
		this.openConnection();
		int numLikes = this.fetchUserlikesByEmail(user.getEmail());
		user.setTotalLikes(numLikes);
		this.closeConnection();
	}

	///////////////////////////////////////////////// Select
	///////////////////////////////////////////////// Methods//////////////////////////////////////////////////

	private void readAddresses() throws SQLException {
		String address_q = "select * from addressTable;";
		Address address = null;
		result = statement.executeQuery(address_q);
		while (result.next()) {
			int addressId = result.getInt("address_id");
			String country = result.getString("country");
			String city = result.getString("city");
			String street = result.getString("street");
			String numHouse = result.getString("num_house");
			double lat = result.getDouble("latitude");
			double lon = result.getDouble("longtitude");
			Coordinate coordinate = new Coordinate(lat, lon);
			address = new Address(country, city, street, numHouse, coordinate);
			this.allAddresses.put(addressId, address);
			addressIdGenerator++;
		}
		System.out.println("reading addresses is finished!");
	}

	private void readParkingSpot() throws SQLException {
		String parkingSpot_q = "select * from parkingSpotTable;";
		result = statement.executeQuery(parkingSpot_q);
		while (result.next()) {
			int pid = result.getInt("pid");
			int addressId = result.getInt("address_id");
			ParkingSpot parkingSpot = new ParkingSpot(allAddresses.get(addressId));
			this.allParkingSpot.put(pid, parkingSpot);
			parkingSpotIdGenerator++;
		}
		System.out.println("reading parking spots is finished!");
	}

	private void readUsers() throws SQLException {
		String all_users = "select * from userTable;";
		result = statement.executeQuery(all_users);
		User newUser = null;
		while (result.next()) {
			int userId = result.getInt("user_id");
			String userName = result.getString("userName");
			String email = result.getString("email");
			String password = result.getString("password");
			int totalLikes = result.getInt("totalLikes");
			int carId = result.getInt("cid");
			newUser = new User(email, userName, password, totalLikes);
			this.readCar(carId, newUser);
			allUsers.put(userId, newUser);
			userIdGenerator = userId;
		}
		System.out.println("reading users is finished!");

	}

	private void readCar(int carId, User user) throws SQLException {
		try {
			Car car = null;
			ResultSet tempResult;
			Statement tempStatement = con.createStatement();
			String car_info = "select * from carTable where cid = " + carId + ";";
			tempResult = tempStatement.executeQuery(car_info);
			tempResult.next();
			int ID = tempResult.getInt("cid");
			String manufactor = tempResult.getString("manufacturer");
			String model = tempResult.getString("model");
			String color = tempResult.getString("color");
			car = new Car(ID, manufactor, model, color);
			user.setCar(car);
		} catch (UnvalidCarPlateException e) {

		}

	}

	private void orgenaizeUsersLikes() throws SQLException {
		for (Integer userId : allUsers.keySet()) {
			String report_q = "select num_likes from reportTable where user_id =" + userId + ";";
			result = statement.executeQuery(report_q);
			int totalLikes = 0;
			while (result.next()) {
				int numLikes = result.getInt("num_likes");
				totalLikes += numLikes;
			}
			allUsers.get(userId).setTotalLikes(totalLikes);
		}
		System.out.println("orgenizing users likes is finished!");
	}

	private void orgenaizeUsersAndParkingSpots() throws SQLException {
		String organize_q = "select user_id, pid from userTable;";
		result = statement.executeQuery(organize_q);
		while (result.next()) {
			int userId = result.getInt("user_id");
			int pId = result.getInt("pid");// the same id as addressId
			if (result.wasNull()) {
				allUsers.get(userId).setCurrentAddress(null);
			} else {
				allUsers.get(userId).setCurrentAddress(allAddresses.get(pId));
				allParkingSpot.get(pId).setTaken(true);
			}
		}
		System.out.println("orgenize users and parking spots for addresses is finished!");
	}

	/////////////////////////////////////////////////// Insert
	/////////////////////////////////////////////////// methods////////////////////////////////////////

	private void insertUser(User user) {
		String insertUser_q = "insert into userTable values(null," + "'" + user.getUserName() + "'" + "," + "'"
				+ user.getEmail() + "'" + "," + "'" + user.getPassword() + "'" + "," + user.getCar().getID() + ",null,"
				+ user.getTotalLikes() + ");";
		try {
			statement.executeUpdate(insertUser_q);
			this.allUsers.put(userIdGenerator++, user);
		} catch (Exception e) {
			System.out.println("Exception From insertUser:" + e.getMessage());
		}
	}

	private void insertCar(Car car) {
		System.out.println("The car id is:" + car.getID());
		String insertCar_q = "insert into carTable values(" + car.getID() + "," + "'" + car.getManufacturer() + "'"
				+ "," + "'" + car.getModel() + "'" + "," + "'" + car.getColor() + "'" + ");";
		try {
			statement.executeUpdate(insertCar_q);
			System.out.println("inserted the car " + car.getManufacturer() + " " + car.getModel() + " " + car.getColor()
					+ " " + car.getID() + " " + " successfully!");
		} catch (Exception e) {
			System.out.println("Exception From insertcCar:" + e.getMessage());
		}
	}

	private void insertReport(Report report, int userId) {
		String insertReport_q = "insert into reportTable values(null," + "'" + report.getInfo() + "'" + ","
				+ report.getNumOfLikes() + "," + userId + ");";
		try {
			statement.executeUpdate(insertReport_q);
			allReports.put(reportIdGenerator++, report);
		} catch (Exception e) {
			System.out.println("Exception From insertReport:" + e.getMessage());
		}
	}

	private void insertAddress(Address address) {
		String insertUser_q = "insert into addressTable values(null," + address.getCoordinate().getLon() + ","
				+ address.getCoordinate().getLat() + "," + "'" + address.getCountry() + "'" + "," + "'"
				+ address.getCity() + "'" + "," + "'" + address.getStreet() + "'" + "," + "'" + address.getNumHouse()
				+ "'" + ");";
		try {
			statement.executeUpdate(insertUser_q);
			allAddresses.put(addressIdGenerator++, address);
		} catch (Exception e) {
			System.out.println("Exception From insertcAddress:" + e.getMessage());
		}
	}

	private void insertParkingSpot(ParkingSpot parkingSpot, int addressId) {
		String insertParkingSpot_q = "insert into parkingSpotTable values(null," + addressId + ");";
		try {
			statement.executeUpdate(insertParkingSpot_q);
			allParkingSpot.put(parkingSpotIdGenerator++, parkingSpot);
		} catch (Exception e) {
			System.out.println("Exception From insertParkingSpot:" + e.getMessage());
		}
	}

	private void insertValuesToAUP(int addressId, int userId, int parkingSpotId) {
		String otherQuery = "insert into address_user_parkingSpotTable values (" + addressId + "," + userId + ","
				+ parkingSpotId + ");";
		try {
			statement.executeUpdate(otherQuery);
		} catch (Exception e) {
			System.out.println("Exception From insertValuesToAUP:" + e.getMessage());
		}
	}

	private void insertValuesToRUP(int reportId, int userId, int parkingSpotId) {
		String otherQuery = "insert into report_user_parkingSpotTable values (" + reportId + "," + userId + ","
				+ parkingSpotId + ");";
		try {
			statement.executeUpdate(otherQuery);
		} catch (Exception e) {
			System.out.println("Exception From insertValuesToRUP:" + e.getMessage());
		}
	}

/////////////////////////////////////////////////// Update
/////////////////////////////////////////////////// methods////////////////////////////////////////
	private void updateUserTableUserLeftParking(int userId) {
		String update_user_q = "update userTable set pid = null where user_id = " + userId + ";";
		try {
			statement.executeUpdate(update_user_q);
		} catch (Exception e) {
			System.out.println("Exception From updateAddressTableUserLeftParking:" + e.getMessage());
		}
	}

	private void updateUserTableUserGotParking(int userId, int parkingId) {
		String update_user_q = "update userTable set pid = " + parkingId + " where user_id = " + userId + ";";
		try {
			statement.executeUpdate(update_user_q);
		} catch (Exception e) {
			System.out.println("Exception From updateAddressTableUserGotParking:" + e.getMessage());
		}
	}

	private void updateUserLikes(int userId, int moreLikes) {
		int userCurrentLikes = fetchUserLikes(userId);
		userCurrentLikes += moreLikes;
		String update_userLikes_q = "update userTable set totalLikes = " + userCurrentLikes + " where user_id = "
				+ userId + ";";
		try {
			statement.executeUpdate(update_userLikes_q);
		} catch (Exception e) {
			System.out.println("Exception From updateAddressTableUserGotParking:" + e.getMessage());
		}
	}

/////////////////////////////////////////////////// Delete
/////////////////////////////////////////////////// methods////////////////////////////////////////
	private void removeUserFromAUPTable(int userId) {
		String deleteUser_q = "delete from address_user_parkingSpotTable where user_id = " + userId + ";";
		try {
			statement.executeUpdate(deleteUser_q);
		} catch (Exception e) {
			System.out.println("Exception From removeUserFromAUPTable:" + e.getMessage());
		}
	}
//////////////////////////////////////////////////////////////////////////////////////////////////////

	private int fetchUserId(User user) {
		String user_q = "select user_id from userTable where email = " + "'" + user.getEmail() + "'" + ";";
		int id = 0;
		try {
			result = statement.executeQuery(user_q);
			while (result.next()) {
				id = result.getInt("user_id");
			}
		} catch (Exception e) {
			System.out.println("Exception From fetchUserId:" + e.getMessage());
		}
		return id;
	}

	private int fetchAddressId(Address address) {
		String addressId_q = "select address_id from addressTable where latitude =" + address.getCoordinate().getLat()
				+ " and longtitude=" + address.getCoordinate().getLon() + ";";
		int addressId = 0;
		try {
			result = statement.executeQuery(addressId_q);
			while (result.next()) {
				addressId = result.getInt("address_id");
			}
		} catch (Exception e) {
			System.out.println("Exception From fetchAddressId:" + e.getMessage());
		}
		return addressId;
	}

	private int fetchReportIdByUser(User user) {
		int userId = fetchUserId(user);
		String reportIdByUser_q = "select report_id from reportTable where user_id =" + userId + ";";
		int reportId = 0;
		try {
			result = statement.executeQuery(reportIdByUser_q);
			while (result.next()) {
				reportId = result.getInt("report_id");
			}
		} catch (Exception e) {
			System.out.println("Exception From fetchReportIdByUser:" + e.getMessage());
		}
		return reportId;
	}

	private int fetchReportLikes(int reportId) {
		int numLikes = 0;
		String getLikesByReportId_q = "select num_likes from reportTable where report_id =" + reportId + ";";
		try {
			result = statement.executeQuery(getLikesByReportId_q);
			while (result.next()) {
				numLikes = result.getInt("num_likes");
			}
		} catch (Exception e) {
			System.out.println("Exception From fetchReportLikes:" + e.getMessage());
		}
		return numLikes;
	}

	private int fetchUserLikes(int userId) {
		int numLikes = 0;
		String getLikesByUserId_q = "select totalLikes from userTable where user_id =" + userId + ";";
		try {
			result = statement.executeQuery(getLikesByUserId_q);
			while (result.next()) {
				numLikes = result.getInt("totalLikes");
			}
		} catch (Exception e) {
			System.out.println("Exception From fetchUserLikes:" + e.getMessage());
		}
		return numLikes;
	}

	private int fetchUserlikesByEmail(String email) {
		int numLikes = 0;
		String getLikesByUserEmail_q = "select totalLikes from userTable where email like " + "'" + email + "'" + ";";
		try {
			result = statement.executeQuery(getLikesByUserEmail_q);
			while (result.next()) {
				numLikes = result.getInt("totalLikes");
			}
		} catch (Exception e) {
			System.out.println("Exception From fetchUserlikesByEmail:" + e.getMessage());
		}
		return numLikes;

	}

	public void openConnection() {
		url = "jdbc:mysql://localhost:3306/parkingspot";
		username = "root";
		password = "My-Secret-Password!";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, username, password);
			statement = con.createStatement();
		} catch (SQLException | ClassNotFoundException e) {
			if (e instanceof SQLException) {
				System.out.println("Oops");
			}
			e.printStackTrace();
		}
	}

	public void closeConnection() {
		try {
			statement.close();
			result.close();
			con.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

}
