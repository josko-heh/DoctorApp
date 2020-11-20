package source.fx;

import java.time.LocalDate;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import main.Main;
import source.entity.Patient;
import source.entity.Sex;

public class NewPatientController {
    @FXML
    private TextField nameTF;
    @FXML
    private TextField surnameTF;
    @FXML
    private TextField jmbgTF;
    @FXML
    private TextField massTF;
    @FXML
    private DatePicker DOBDPicker;
    @FXML
    private ChoiceBox<Sex> sexCB;

    @FXML
    public void initialize() {
        try {
            ObservableList<Sex> options = FXCollections.observableArrayList(Database.getAllSexes());
            sexCB.setItems(options);
        } catch (Exception e) {
			Main.alert(e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }

    }

    // Add form validation
    @FXML public void savePatient() {
        String name = nameTF.getText();
        String surname = surnameTF.getText();
        String jmbg = jmbgTF.getText();
        LocalDate dob = DOBDPicker.getValue();

        String massKgString = massTF.getText();// if (!massKgString.matches("\\d*"))
        Integer massKg = Integer.parseInt(massKgString);

        Integer sexId = sexCB.getSelectionModel().getSelectedItem().getId();

        try {
            Database.savePatient(new Patient(null, name, surname, jmbg, dob, massKg, sexId));
            Main.alert("Patient saved!", AlertType.INFORMATION);
        } catch (Exception e) {
			Main.alert(e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
    }
}
