package main;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

public class BasicSceneController {
    private static final String fxmlPath = "..\\source\\fx\\";

    @FXML public void showAppointments() {
		try {
			GridPane pane = (GridPane)FXMLLoader.load(getClass().getResource(fxmlPath + "Appointments.fxml"), Main.getBundle());
			Main.setCenter(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    @FXML public void showNewAppointment() {
		
		try {
			GridPane pane = (GridPane)FXMLLoader.load(getClass().getResource(fxmlPath + "NewAppointment.fxml"), Main.getBundle());
			Main.setCenter(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    @FXML public void showNewPatient() {
		try {
			GridPane pane = (GridPane)FXMLLoader.load(getClass().getResource(fxmlPath + "NewPatient.fxml"), Main.getBundle());
			Main.setCenter(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    @FXML public void showLatestInfo() {
		try {
			GridPane pane = (GridPane)FXMLLoader.load(getClass().getResource(fxmlPath + "LatestInfo.fxml"), Main.getBundle());
			Main.setCenter(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
