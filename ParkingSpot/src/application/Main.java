package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import model.ParkingSystem;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		ViewCommunicator viewCommunicator = null;
		ParkingSystem parkingSystem = new ParkingSystem();
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Welcome.fxml"));
			Parent root =loader.load();
			viewCommunicator=loader.getController();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("ParkingSpot");
			primaryStage.show();
			viewCommunicator.setClosingProgram(primaryStage);
		} catch(Exception e) {
			e.printStackTrace();
		}
		Controller controller = new Controller(parkingSystem,viewCommunicator);	
		parkingSystem.startApp();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
