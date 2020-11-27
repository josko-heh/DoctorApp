package main;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import org.ini4j.Wini;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
public class Main extends Application {

	private static Scene basicScene;
	private static BasicSceneController basicSceneController;
	private static ResourceBundle bundle;
	private static boolean alertReturnValue = false;
	private static Language chosenLang = Language.ENG;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		// Try to load previously chosen language. If property couldn't be loaded, show
		// langChoiceAlert and try to save chosen language to lang.ini file.
		try {
			Wini ini = new Wini(new File("lang.ini"));
			String language = ini.get("language", "chosen");

			Main.chosenLang = Language.valueOf(language);

		} catch (Exception e) {
			
			langChoiceAlert("Choose language.", Language.values());

			try {
				Wini ini = new Wini(new File("lang.ini"));

				ini.put("language", "chosen", Main.chosenLang.toString());
				ini.store();

			} catch (Exception e2) {}
		}


		Locale locale = new Locale(chosenLang.toString());
		bundle = ResourceBundle.getBundle("bundles\\Bundle", locale);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("..\\main\\basicScene.fxml"), bundle);

		try {
			BorderPane root = (BorderPane) loader.load();
			basicSceneController = loader.getController();
			basicScene = new Scene(root,838,519);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(basicScene);
			primaryStage.setTitle("DoctorApp");
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
//------------------------------------------------------------------------	

//----------------------- get and set ------------------------------------
	public static BasicSceneController getBasicSceneController() {
		return basicSceneController;
	}
	
	public static ResourceBundle getBundle(){
		return bundle;
	}
	
	public static void setAlertReturnValue(boolean alertReturnValue){
		Main.alertReturnValue = alertReturnValue;
	}
	
	public static boolean getAlertReturnValue() {
		return alertReturnValue;
	}
	
	public static Language getChosenLanguage() {
		return chosenLang;
	}

	
	public static void setCenter(Node node) {
		BorderPane borderPane = (BorderPane) basicScene.lookup("BorderPane");
		borderPane.setCenter(node);
	}
//------------------------- / get and set --------------------------------------	

	public static void alert(String message, AlertType type){
		Alert alert = new Alert(type);
		alert.setTitle(type.name());
		alert.setHeaderText(message);
		alert.showAndWait();
	}

	/**
	 * Default is <code>false</code>.
	 */
	public static void yesNoAlert(String message) {
		Main.setAlertReturnValue(false);

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setContentText(message);
		ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
		ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
		alert.getButtonTypes().setAll(okButton, noButton);
		alert.showAndWait().ifPresent(type -> { 
			if(type.getButtonData() == ButtonBar.ButtonData.YES) Main.setAlertReturnValue(true); });
	}

	/** sets <code>Main.chosenLang</code> */
	private void langChoiceAlert(String message, Language... choices) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Choose");
		alert.setContentText(message);

		alert.getButtonTypes().clear();

		for(Language choice : choices){
			ButtonType btn = new ButtonType(choice.toString(), ButtonBar.ButtonData.OTHER);
			alert.getButtonTypes().add(btn);
		}

		alert.showAndWait().ifPresent(btn -> { 
			Main.chosenLang = Language.valueOf(btn.getText());
		});
	}


	// modify after implementing login system
	/**
	 * @return logged in doctor's id
	 */
	public static Integer getCurrentDoctorId(){
		return 1;
	}

}