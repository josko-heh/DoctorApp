package entity;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class NewAppointmentController {

	@FXML private TableView<Patient> patientsTable;
	@FXML private TableColumn<Patient, String> nameColumn;
	@FXML private TableColumn<Patient, String> surnameColumn;
	@FXML private TableColumn<Patient, String> OIBColumn;
	@FXML private Label dateLabel;
	@FXML private Label time_formatLabel;
	@FXML private Label notesLabel;
	@FXML private Button saveBT;
	@FXML private TextArea notesTA;
	@FXML private DatePicker datePicker;
	@FXML private TextField timeTF;


    @FXML
	public void initialize() {
		System.out.println("initialze new appoint");
	}

	
}