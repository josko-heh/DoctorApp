package main;

import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
 
public class Main extends Application {
	
	private static Scene basicScene;
	private static BasicSceneController basicSceneController;
	private static ResourceBundle bundle;

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		//mainStage = primaryStage;
		
		try {
			Locale locale = new Locale("en");
			bundle = ResourceBundle.getBundle("bundles\\Bundle", locale);

			FXMLLoader loader = new FXMLLoader(getClass().getResource("..\\main\\basicScene.fxml"), bundle);
			BorderPane root = (BorderPane)loader.load();
			basicSceneController = loader.getController();
			basicScene = new Scene(root,838,519);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(basicScene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
//-----------------------------	
	
	public static BasicSceneController getBasicSceneController() {
		return basicSceneController;
	}
	
	public static ResourceBundle getBundle(){
		return bundle;
	}
	
	public static void setCenter(Node node) {
		BorderPane borderPane = (BorderPane) basicScene.lookup("BorderPane");
		borderPane.setCenter(node);
	}

	public static void alert(String message, AlertType type){
		Alert alert = new Alert(type);
		alert.setTitle(type.name());
		alert.setHeaderText(message);
		alert.showAndWait();
	}

	// modify after implementing login system
	/**
	 * @return logged in doctor's id
	 */
	public static Integer getCurrentDoctorId(){
		return 1;
	}
}