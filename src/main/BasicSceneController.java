package main;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

public class BasicSceneController {
    

    @FXML private void showNewAppointment() {
		try {
			GridPane pane = (GridPane)FXMLLoader.load(getClass().getResource("..\\entity\\NewAppointmentGridOnly.fxml"), Main.getBundle());
			Main.setCenter(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    @FXML private void showNewPatient() {
		try {
			GridPane pane = (GridPane)FXMLLoader.load(getClass().getResource("..\\entity\\NewPatientGridOnly.fxml"), Main.getBundle());
			Main.setCenter(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
