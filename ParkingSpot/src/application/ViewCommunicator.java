package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import exceptions.EmptyFieldException;
import exceptions.ParkingSpotTakenException;
import exceptions.SameEmailException;
import exceptions.UnvalidAddressException;
import exceptions.UserHasNoAddressException;
import exceptions.UserNotFoundException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Address;
import model.Car;
import model.ParkingSpot;
import model.Report;
import model.User;

public class ViewCommunicator implements UIEventsListener, Initializable {

	//To remove JavaFX warnings on JavaFX versions put this in fxml file after changing it->> xmlns="http://javafx.com/javafx" xmlns:fx="http://javafx.com/fxml" 
	private transient ArrayList<UIEventsListener> allListeners;
	private Stage primaryStage;
	private static ParkingSpot searchedParking;

	////////////////// Welcome Nodes/////////////////////////////////
	@FXML
	private Button signInBtnWelcome;

	@FXML
	private Button signUpBtnWelcome;
	///////////////////////// Sign In Nodes////////////////////////////////
	@FXML
	private Button backBtnSignIn;

	@FXML
	private TextField emailTxtSignIn;

	@FXML
	private Button loginBtn;

	@FXML
	private PasswordField passSignIn;

	@FXML
	private Label msgSignIn;
	////////////////////////////// Sign Up Nodes////////////////////////////
	@FXML
	private Button backBtnSignUp;

	@FXML
	private TextField carModelTxt;

	@FXML
	private ColorPicker colorPicker;

	@FXML
	private TextField emailTxtSignUp;

	@FXML
	private TextField manufacturerTxt;

	@FXML
	private TextField passwordTxtSignUp;

	@FXML
	private TextField plateNumTxt;

	@FXML
	private Button signUpBtnSignUp;

	@FXML
	private TextField userNameTxtSignUp;

	@FXML
	private Label msgSignUp;
	//////////////////////////////// Primary
	//////////////////////////////// View////////////////////////////////////////
	@FXML
	private Label primeEmailLbl;

	@FXML
	private Button primeFindMyCarBtn;

	@FXML
	private Label primeLikesLbl;

	@FXML
	private Button primeLogOutBtn;

	@FXML
	private Button primeReportOnSpotBtn;

	@FXML
	private Button primeSeeMyReportsBtn;

	@FXML
	private Button primeSeelAllReportsBtn;

	@FXML
	private Label primeUserNameLbl;

	@FXML
	private Button primeWhereWeParkBtn;

	@FXML
	private Text textStam;

	// static components
	private static Label statPrimeUserNameLbl;

	private static Label statPrimeEmailLbl;

	private static Label statPrimeLikesLbl;
	//////////////////////////////////////////// Where We Park
	//////////////////////////////////////////// Components//////////////////////////////////////////////

	@FXML
	private Button searchSpotbackBtn;

	@FXML
	private TextField whereWeParkCityTxtField;

	@FXML
	private Label whereWeParkMsgLbl;

	@FXML
	private TextField whereWeParkNumHouseTxtField;

	@FXML
	private Button whereWeParkSearchBtn;

	@FXML
	private TextField whereWeParkStreetTxtField;

	@FXML
	private Button whereWeParkParkHereBtn;

	//////////////////////////////////////////////// Report On
	//////////////////////////////////////////////// Spot
	//////////////////////////////////////////////// Components//////////////////////////////////////////////////////////
	@FXML
	private Button reportOnSpotBackBtn;

	@FXML
	private TextField reportOnSpotCityTxtField;

	@FXML
	private TextField reportOnSpotInfoTxt;

	@FXML
	private TextField reportOnSpotNumHouseTxtField;

	@FXML
	private Button reportOnSpotReportBtn;

	@FXML
	private TextField reportOnSpotStreetTxtField;

	@FXML
	private Label reportOnSpotMsgLbl;
	////////////////////////////////////////////////// User Has No Address
	////////////////////////////////////////////////// components//////////////////////////////////////////////////////////////

	@FXML
	private Label noAddressMsgLbl;

	@FXML
	private Button userHasNoAddressBackBtn;

	@FXML
	private TextField userHasNoAddressCityTxtField;

	@FXML
	private TextField userHasNoAddressNumHouseTxtField;

	@FXML
	private Button userHasNoAddressSearchBtn;

	@FXML
	private TextField userHasNoAddressStreetTxtField;
	/////////////////////////////////////////////////////// User Has Address
	/////////////////////////////////////////////////////// Components/////////////////////////////
	@FXML
	private Button userHasAddressBackBtn;

	@FXML
	private Label userHasAddressLbl;

	private static Label statUserHasAddressLbl;

	@FXML
	private Button userHasAddressLeaveBtn;

	@FXML
	private Label userHasAddressMsgLbl;

	///////////////////////////////////////////////// See all Reports
	///////////////////////////////////////////////// Components//////////////////////////////////////////////////////

	@FXML
	private Button backBtnAllReports;

	@FXML
	private TableColumn<Report, String> contentColumn;

	@FXML
	private TableColumn<Report, Button> likeColumn;

	@FXML
	private TableColumn<Report, String> reporterColumn;

	@FXML
	private TableView<Report> allReportsTable;

	private static TableColumn<Report, String> statContentColumn;

	private static TableColumn<Report, Button> statLikeColumn;

	private static TableColumn<Report, String> statReporterColumn;

	private static TableView<Report> statAllReportsTable;

	/////////////////////////////////////////////////////// See My Report
	/////////////////////////////////////////////////////// Components////////////////////////////////////////////////////////////
	@FXML
	private TableColumn<Report, String> seeMyReportInfoCol;

	@FXML
	private TableColumn<Report, Integer> seeMyReportLikeCol;

	@FXML
	private TableView<Report> seeMyReportTable;

	private static TableColumn<Report, String> statSeeMyReportInfoCol;

	private static TableColumn<Report, Integer> statSeeMyReportLikeCol;

	private static TableView<Report> statSeeMyReportTable;

	///////////////////////////////////////////////// FXML
	///////////////////////////////////////////////// Methods///////////////////////////////////////////////////////////////////
	@FXML
	public void switchToSignIn(ActionEvent event) {
		changeScreen("SignIn.fxml", event);
	}

	@FXML
	public void switchToSignUp(ActionEvent event) {
		changeScreen("SignUp.fxml", event);
	}

	@FXML
	public void logInIsPressed(ActionEvent event) throws EmptyFieldException {
		try {
			msgSignIn.setVisible(false);
			if (emailTxtSignIn.getText().isEmpty() || passSignIn.getText().isEmpty()) {
				throw new EmptyFieldException();
			}
			this.viewUserSignIn(emailTxtSignIn.getText(), passSignIn.getText());
			displayPrimaryView(event);
		} catch (Exception e) {
			if (!(e instanceof IOException)) {
				msgSignIn.setText(e.getMessage());
				Paint paint = Paint.valueOf("red");
				msgSignIn.setTextAlignment(TextAlignment.CENTER);
				msgSignIn.setFont(Font.font(20));
				msgSignIn.setTextFill(paint);
				msgSignIn.setVisible(true);
			}
		}
	}

	@FXML
	void signUpClicked(ActionEvent event) {
		try {
			msgSignUp.setVisible(false);
			if (emailTxtSignUp.getText().isEmpty() || passwordTxtSignUp.getText().isEmpty()
					|| userNameTxtSignUp.getText().isEmpty() || manufacturerTxt.getText().isEmpty()
					|| carModelTxt.getText().isEmpty() || plateNumTxt.getText().isEmpty()
					|| colorPicker.getValue().equals(null)) {
				throw new EmptyFieldException();
			}
			int plateNum = Integer.parseInt(plateNumTxt.getText());
			Car car = new Car(plateNum, manufacturerTxt.getText(), carModelTxt.getText(),
					colorPicker.getValue().toString());
			this.viewUserSignUp(userNameTxtSignUp.getText(), emailTxtSignUp.getText(), passwordTxtSignUp.getText(),
					car);
			changeScreen("SignIn.fxml", event);
		} catch (Exception e) {
			if (!(e instanceof IOException)) {
				msgSignUp.setText(e.getMessage());
				msgSignUp.setTextAlignment(TextAlignment.CENTER);
				msgSignUp.setFont(Font.font(20));
				Paint paint = Paint.valueOf("red");
				msgSignUp.setTextFill(paint);
				msgSignUp.setVisible(true);
			}
		}
	}

	@FXML
	public void searchSpotHasBeenClicked(ActionEvent event) {
		try {
			whereWeParkMsgLbl.setVisible(false);
			if (whereWeParkCityTxtField.getText().isEmpty() || whereWeParkStreetTxtField.getText().isEmpty()) {
				throw new EmptyFieldException();
			}
			User currentUser = this.viewGetCurrentUser();
			searchedParking = this.viewUserSearchParkingSpot(currentUser, whereWeParkCityTxtField.getText(),
					whereWeParkStreetTxtField.getText(), whereWeParkNumHouseTxtField.getText());
			whereWeParkMsgLbl.setText("The address is now available!");
			whereWeParkMsgLbl.setTextAlignment(TextAlignment.CENTER);
			Paint paint = Paint.valueOf("#00ff37");
			whereWeParkMsgLbl.setFont(Font.font(20));
			whereWeParkMsgLbl.setTextFill(paint);
			whereWeParkMsgLbl.setVisible(true);
			whereWeParkParkHereBtn.setVisible(true);
		} catch (Exception e) {
			whereWeParkMsgLbl.setTextAlignment(TextAlignment.CENTER);
			whereWeParkMsgLbl.setFont(Font.font(20));
			whereWeParkMsgLbl.setText(e.getMessage());
			Paint paint = Paint.valueOf("red");
			whereWeParkMsgLbl.setTextFill(paint);
			whereWeParkMsgLbl.setVisible(true);
		}
	}

	@FXML
	public void userTakeSpot(ActionEvent event) {
		try {
			User currentUser = this.viewGetCurrentUser();
			if (searchedParking == null)
				return;
			this.viewUserTakeParkingSpot(currentUser, searchedParking);
		} catch (Exception e) {
			whereWeParkMsgLbl.setTextAlignment(TextAlignment.CENTER);
			whereWeParkMsgLbl.setFont(Font.font(20));
			whereWeParkMsgLbl.setText(e.getMessage());
			Paint paint = Paint.valueOf("red");
			whereWeParkMsgLbl.setTextFill(paint);
			whereWeParkMsgLbl.setVisible(true);
		}

	}

	@FXML
	public void userLeaveSpot(ActionEvent event) {
		User currentUser = this.viewGetCurrentUser();
		this.viewUserLeaveParkingSpot(currentUser);
		userHasAddressMsgLbl.setVisible(true);
	}

	@FXML
	public void reportOnSpot(ActionEvent event) {
		try {
			reportOnSpotMsgLbl.setVisible(false);
			if (reportOnSpotCityTxtField.getText().isEmpty() || reportOnSpotStreetTxtField.getText().isEmpty()
					|| reportOnSpotInfoTxt.getText().isEmpty()) {
				throw new EmptyFieldException();
			}
			User currentUser = this.viewGetCurrentUser();
			ParkingSpot parkingSpot = this.viewUserSearchParkingSpot(currentUser, reportOnSpotCityTxtField.getText(),
					reportOnSpotStreetTxtField.getText(), reportOnSpotNumHouseTxtField.getText());
			Report report = new Report(reportOnSpotInfoTxt.getText(), currentUser, parkingSpot.getAddress());
			this.viewUserReportParkingSpot(currentUser, report);
			reportOnSpotMsgLbl.setText("The report has been sent succussesfully!");
			reportOnSpotMsgLbl.setFont(Font.font(20));
			Paint paint = Paint.valueOf("green");
			reportOnSpotMsgLbl.setTextAlignment(TextAlignment.CENTER);
			reportOnSpotMsgLbl.setTextFill(paint);
			reportOnSpotMsgLbl.setVisible(true);
		} catch (Exception e) {
			reportOnSpotMsgLbl.setText(e.getMessage());
			reportOnSpotMsgLbl.setTextAlignment(TextAlignment.CENTER);
			reportOnSpotMsgLbl.setFont(Font.font(20));
			Paint paint = Paint.valueOf("red");
			reportOnSpotMsgLbl.setTextFill(paint);
			reportOnSpotMsgLbl.setVisible(true);
		}
	}

	@FXML
	public void findOutUserLocation(ActionEvent event) {
		User currentUser = this.viewGetCurrentUser();
		if (currentUser.getCurrentAddress() != null) {
			changeScreen("UserHasAddress.fxml", event);
			statUserHasAddressLbl.setText("Hi " + currentUser.getUserName() + " your car is at: "
					+ currentUser.getCurrentAddress().toString());
			statUserHasAddressLbl.setTextAlignment(TextAlignment.CENTER);
			statUserHasAddressLbl.setFont(Font.font(22));
			Paint paint = Paint.valueOf("#f2d3d3");
			statUserHasAddressLbl.setTextFill(paint);
		} else {
			changeScreen("UserWithoutAddress.fxml", event);
		}
	}

	@FXML
	public void helpUsFindTheUser(ActionEvent event) {
		try {
			noAddressMsgLbl.setVisible(false);
			if (userHasNoAddressCityTxtField.getText().isEmpty()
					|| userHasNoAddressStreetTxtField.getText().isEmpty()) {
				throw new EmptyFieldException();
			}
			User currentUser = this.viewGetCurrentUser();
			this.viewFindUserlocation(currentUser, userHasNoAddressCityTxtField.getText(),
					userHasNoAddressStreetTxtField.getText(), userHasNoAddressNumHouseTxtField.getText());
			noAddressMsgLbl.setText("Ah-Ha there you are!");
			noAddressMsgLbl.setTextAlignment(TextAlignment.CENTER);
			Paint paint = Paint.valueOf("#00ff37");
			noAddressMsgLbl.setFont(Font.font(20));
			noAddressMsgLbl.setTextFill(paint);
			noAddressMsgLbl.setVisible(true);
		} catch (Exception e) {
			noAddressMsgLbl.setTextAlignment(TextAlignment.CENTER);
			noAddressMsgLbl.setFont(Font.font(20));
			noAddressMsgLbl.setText(e.getMessage());
			Paint paint = Paint.valueOf("red");
			noAddressMsgLbl.setTextFill(paint);
			noAddressMsgLbl.setVisible(true);
		}
	}

	@FXML
	public void backToWelcome(ActionEvent event) {
		changeScreen("Welcome.fxml", event);
	}

	@FXML
	public void switchToWhereWePark(ActionEvent event) {
		changeScreen("WhereWePark.fxml", event);
	}

	@FXML
	public void switchToReportOnSpot(ActionEvent event) {
		changeScreen("ReportOnSpot.fxml", event);
	}

	@FXML
	public void switchToFindMyCar(ActionEvent event) {
		changeScreen("FindMyCar.fxml", event);
	}

	@FXML
	public void switchToShowAllReports(ActionEvent event) {
		changeScreen("ShowAllReports.fxml", event);
		List<Report> allReports = this.viewGetAllReportsFromModel();
		statReporterColumn.setCellValueFactory(new PropertyValueFactory<Report, String>("reporterUser"));
		statContentColumn.setCellValueFactory(new PropertyValueFactory<Report, String>("info"));
		statLikeColumn.setCellValueFactory(new PropertyValueFactory<Report, Button>("likeBtn"));
		statAllReportsTable.setItems(FXCollections.observableArrayList(allReports));
	}

	@FXML
	public void switchToSeeMyReports(ActionEvent event) {
		changeScreen("SeeMyReports.fxml", event);
		User currentUser = this.viewGetCurrentUser();
		List<Report> allMyActiveReports = this.viewGetCurrentUserReports(currentUser);
		statSeeMyReportInfoCol.setCellValueFactory(new PropertyValueFactory<Report, String>("info"));
		statSeeMyReportLikeCol.setCellValueFactory(new PropertyValueFactory<Report, Integer>("numOfLikes"));
		statSeeMyReportTable.setItems(FXCollections.observableArrayList(allMyActiveReports));
	}

	@FXML
	public void displayPrimaryView(ActionEvent event) {
		changeScreen("PrimaryView.fxml", event);
		User currentUser = this.viewGetCurrentUser();
		currentUser.getDatabaseConnector().getUserLikesFromDatabase(currentUser);
		statPrimeUserNameLbl.setText("Hi " + currentUser.getUserName() + "!");
		statPrimeUserNameLbl.setFont(Font.font(18));
		statPrimeUserNameLbl.setTextFill(Paint.valueOf("#f2d3d3"));
		statPrimeEmailLbl.setText("E-mail:" + currentUser.getEmail());
		statPrimeEmailLbl.setFont(Font.font(18));
		statPrimeEmailLbl.setTextFill(Paint.valueOf("#f2d3d3"));
		statPrimeLikesLbl.setText("Total likes:" + String.valueOf(currentUser.getTotalLikes()));
		statPrimeLikesLbl.setFont(Font.font(18));
		statPrimeLikesLbl.setTextFill(Paint.valueOf("#f2d3d3"));

	}

	@FXML
	public void backBtnAndSendUserLikes(ActionEvent event) {
		ObservableList<Report> reports = statAllReportsTable.getItems();
		User currentUser = this.viewGetCurrentUser();
		for (Report report : reports) {
			if (report.getLikeBtn().getTextFill() == Color.RED) {
				currentUser.likeReport(report);
			}
		}
		displayPrimaryView(event);
	}

	public void setLabel(Label lbl, String info) {
		lbl.setText(info);
	}

	public void setClosingProgram(Stage primaryStage) {
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				if (JOptionPane.showConfirmDialog(null, "Are you sure you want to exit the app?", "Exit",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					exitApp();
				} else {
					event.consume();
				}
			}
		});
	}

	@FXML
	public void logOut(ActionEvent event) {
		this.viewUserLogOut();
		changeScreen("Welcome.fxml", event);
	}

	//////////////////////////////////////////////////// Interface
	//////////////////////////////////////////////////// Methods///////////////////////////////////////////
	@Override
	public void addUIListener(UIEventsListener listener) {
		this.allListeners.add(listener);
	}

	@Override
	public void viewUserSignIn(String email, String password) throws UserNotFoundException {
		for (UIEventsListener uiEventsListener : allListeners) {
			uiEventsListener.viewUserSignIn(email, password);
		}

	}

	@Override
	public void viewUserSignUp(String name, String Email, String password, Car car) throws SameEmailException {
		for (UIEventsListener uiEventsListener : allListeners) {
			uiEventsListener.viewUserSignUp(name, Email, password, car);
		}

	}

	@Override
	public void viewUserLogOut() {
		for (UIEventsListener uiEventsListener : allListeners) {
			uiEventsListener.viewUserLogOut();
		}

	}

	@Override
	public ParkingSpot viewUserSearchParkingSpot(User user, String city, String street, String numHouse)
			throws UnvalidAddressException, ParkingSpotTakenException {
		for (UIEventsListener uiEventsListener : allListeners) {
			return uiEventsListener.viewUserSearchParkingSpot(user, city, street, numHouse);
		}
		return null;
	}

	@Override
	public ParkingSpot viewFindUserlocation(User user, String city, String street, String numHouse)
			throws UnvalidAddressException, ParkingSpotTakenException {
		for (UIEventsListener uiEventsListener : allListeners) {
			return uiEventsListener.viewFindUserlocation(user, city, street, numHouse);
		}
		return null;
	}

	@Override
	public Address viewGetUserCarLocation(User user) throws UserHasNoAddressException {
		Address address = null;
		for (UIEventsListener uiEventsListener : allListeners) {
			address = uiEventsListener.viewGetUserCarLocation(user);
		}
		return address;
	}

	@Override
	public List<Report> viewGetAllReportsFromModel() {
		List<Report> allReports = null;
		for (UIEventsListener uiEventsListener : allListeners) {
			allReports = uiEventsListener.viewGetAllReportsFromModel();
		}
		return allReports;
	}

	@Override
	public List<Report> viewGetCurrentUserReports(User user) {
		List<Report> userReports = null;
		for (UIEventsListener uiEventsListener : allListeners) {
			userReports = uiEventsListener.viewGetCurrentUserReports(user);
		}
		return userReports;
	}

	@Override
	public void viewUserReportParkingSpot(User user, Report report) {
		for (UIEventsListener uiEventsListener : allListeners) {
			uiEventsListener.viewUserReportParkingSpot(user, report);
		}

	}

	@Override
	public User viewGetCurrentUser() {
		User currentUser = null;
		for (UIEventsListener uiEventsListener : allListeners) {
			currentUser = uiEventsListener.viewGetCurrentUser();
		}
		return currentUser;
	}

	@Override
	public void viewUserTakeParkingSpot(User user, ParkingSpot parkingSpot) throws ParkingSpotTakenException {
		for (UIEventsListener uiEventsListener : allListeners) {
			uiEventsListener.viewUserTakeParkingSpot(user, parkingSpot);
		}

	}

	@Override
	public void viewUserLeaveParkingSpot(User user) {
		for (UIEventsListener uiEventsListener : allListeners) {
			uiEventsListener.viewUserLeaveParkingSpot(user);
		}

	}

	@Override
	public void exitApp() {
		for (UIEventsListener uiEventsListener : allListeners) {
			uiEventsListener.exitApp();
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.allListeners = new ArrayList<UIEventsListener>();
		statPrimeUserNameLbl = primeUserNameLbl;
		statPrimeEmailLbl = primeEmailLbl;
		statPrimeLikesLbl = primeLikesLbl;
		statUserHasAddressLbl = userHasAddressLbl;
		statAllReportsTable = allReportsTable;
		statReporterColumn = reporterColumn;
		statContentColumn = contentColumn;
		statLikeColumn = likeColumn;
		statSeeMyReportTable = seeMyReportTable;
		statSeeMyReportInfoCol = seeMyReportInfoCol;
		statSeeMyReportLikeCol = seeMyReportLikeCol;
	}

	public void changeScreen(String fileName, ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fileName));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			primaryStage.setScene(scene);
			primaryStage.show();
			ArrayList<UIEventsListener> tempListeners = new ArrayList<UIEventsListener>(this.allListeners);
			ViewCommunicator view = this;
			view = loader.getController();
			view.allListeners = tempListeners;
			primaryStage.setTitle("Parking Spot");
			this.setClosingProgram(this.primaryStage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
