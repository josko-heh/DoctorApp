package source.fx;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import database.Database;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.Main;
import source.entity.Appointment;
import source.entity.Patient;

public class NewAppointmentController {
	private boolean appointmentSaved = false;
	private Appointment savedAppointment;

	@FXML private TableView<Patient> patientsTable;
	@FXML private TableColumn<Patient, String> nameColumn;
	@FXML private TableColumn<Patient, String> surnameColumn;
	@FXML private TableColumn<Patient, String> jmbgColumn;
	@FXML private TableColumn<Patient, String> DOBColumn;
	@FXML private TextArea notesTA;
	@FXML private DatePicker datePicker;
	@FXML private TextField timeTF;

	@FXML public void initialize() {
		nameColumn.setCellValueFactory(new PropertyValueFactory<Patient, String>("name"));
		surnameColumn.setCellValueFactory(new PropertyValueFactory<Patient, String>("surname"));
		jmbgColumn.setCellValueFactory(new PropertyValueFactory<Patient, String>("jmbg"));
		DOBColumn.setCellValueFactory(new PropertyValueFactory<Patient, String>("DOBformatted"));

		try {
			List<Patient> patientsList = Database.getAllPatients();
	
			patientsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			patientsTable.setItems(FXCollections.observableArrayList(patientsList));
		} catch (Exception e) {
			Main.alert(e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}

	// TODO: form validation
	/** 
	 * @throws Exception - temporary solution; DocumentBuilderFactory.newInstance().newDocumentBuilder().parse("server.xml").getDocumentElement(); throw checked exceptions
	*/
	@FXML public void saveAppointment() throws Exception{ 

		String date = datePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		String time = timeTF.getText();
		LocalDateTime dateTime = LocalDateTime.parse(date + time, DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm"));
		String notes = notesTA.getText();
		Integer newId = Appointment.getMaxId() + 1;

		// TODO: handle if nothing is selected in appointmentsTable 
		Integer patientId = patientsTable.getSelectionModel().getSelectedItem().getId();


		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(Appointment.XMLPATH);
		Element rootEl = document.getDocumentElement();
		

		Element appointmentEl = document.createElement("Appointment");

		appointmentEl.setAttribute("id", String.valueOf(newId));

		
		Element patientIDEl = document.createElement("PatientID");
		patientIDEl.appendChild(document.createTextNode(Integer.toString(patientId)));
		appointmentEl.appendChild(patientIDEl);
		/*
		Element doctorIDEl = document.createElement("DoctorID");
		doctorIDEl.appendChild(document.createTextNode("0"));
		appointmentEl.appendChild(doctorIDEl);*/
		Element dateTimeEl = document.createElement("DateTime");
		dateTimeEl.appendChild(document.createTextNode(date + " " + time));
		appointmentEl.appendChild(dateTimeEl);

		Element notesEl = document.createElement("Notes");
		notesEl.appendChild(document.createTextNode(notes));
		appointmentEl.appendChild(notesEl);

		rootEl.appendChild(appointmentEl);


        Transformer transformer = TransformerFactory.newInstance().newTransformer();
		DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(Appointment.XMLPATH);
		transformer.transform(source, result);
		

		savedAppointment = new Appointment(newId, Database.getPatient(patientId), dateTime, notes);
		appointmentSaved = true;
		Main.alert("Appointment saved!", AlertType.INFORMATION);
		// Bug: If controller is serving as change appointment, then window needs to be closed before changing appointment again because new appointment 
		//is saved to xml file every time and old ones are not deleted. Delete is done in AppointmentsController only after closing changeWindow.
	}


	public TableView<Patient> getPatientsTable() {
		return patientsTable;
	}

	public void setPatientsTable(TableView<Patient> patientsTable) {
		this.patientsTable = patientsTable;
	}

	public void setNotesTA(String newNotes) {
		this.notesTA.setText(newNotes);
	}

	public void setDateTime(LocalDateTime newDateTime) {
		this.datePicker.setValue(newDateTime.toLocalDate());
		this.timeTF.setText(newDateTime.toLocalTime().toString()); //needs to bee in hh:mm format
	}

	public void setTimeTF(TextField timeTF) {
		this.timeTF = timeTF;
	}

	public boolean isAppointmentSaved() {
		return appointmentSaved;
	}

	public void setAppointmentSaved(boolean appointmentSaved) {
		this.appointmentSaved = appointmentSaved;
	}

	public Appointment getSavedAppointment() {
		return savedAppointment;
	}


}