package main;

import java.util.ResourceBundle;
import java.util.Locale;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
 
public class Main extends Application {
	
	private static Scene basicScene;
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

			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("..\\main\\basicScene.fxml"), bundle);
			basicScene = new Scene(root,600,500);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(basicScene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
//-----------------------------	
	
	public static ResourceBundle getBundle(){
		return bundle;
	}
	
	public static void setCenter(Node node) {
		BorderPane borderPane = (BorderPane) basicScene.lookup("BorderPane");
		borderPane.setCenter(node);
	}

}