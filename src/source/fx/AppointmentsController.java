package source.fx;

import main.Main;
import source.entity.Appointment;
import source.entity.Patient;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import database.Database;
import exceptions.AppointmentException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AppointmentsController {

	@FXML private TableView<Appointment> appointmentsTable;
	@FXML private TableColumn<Appointment, String> nameColumn;
	@FXML private TableColumn<Appointment, String> surnameColumn;
	@FXML private TableColumn<Appointment, String> jmbgColumn;
	@FXML private TableColumn<Appointment, String> DOBColumn;
	@FXML private TableColumn<Appointment, LocalDateTime> dateTimeColumn;
	@FXML private TableColumn<Appointment, String> notesColumn;
	@FXML private ComboBox<Patient> patientsCBF;
	@FXML private DatePicker datePickerF;
	@FXML private TextField timeF;
	@FXML private TextField notesF;

	@SuppressWarnings("unchecked")
	@FXML public void initialize() {

		List<Appointment> appointments = new ArrayList<>();

		try {
			patientsCBF.setItems(FXCollections.observableArrayList(Database.getAllPatients()));
			

			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("data\\Appointments.xml"));
			NodeList nList = doc.getElementsByTagName("Appointment");

			for (int i = 0; i < nList.getLength(); i++) {
				Integer patientID, appointmentId;
				String notes;
				LocalDateTime dateTime;
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

				Element appointmentEl = (Element) nList.item(i);

				appointmentId = Integer.parseInt(appointmentEl.getAttribute("id"));
				patientID = Integer.parseInt(appointmentEl.getElementsByTagName("PatientID").item(0).getTextContent());
				dateTime = LocalDateTime.parse(appointmentEl.getElementsByTagName("DateTime").item(0).getTextContent(), formatter);
				notes = appointmentEl.getElementsByTagName("Notes").item(0).getTextContent();

				Patient patient = Database.getPatient(patientID);

				appointments.add(new Appointment(appointmentId, patient, dateTime, notes));
			}
		} catch (Exception e) {
			Main.alert(e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}

		nameColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("PatientName"));
		surnameColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("PatientSurname"));
		jmbgColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("PatientJmbg"));
		DOBColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("PatientDOBformated"));
		dateTimeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, LocalDateTime>("dateTime"));
		notesColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("notes"));

		ObservableList<Appointment> appointmentsObservable = FXCollections.observableArrayList(appointments);

		appointmentsTable.setItems(appointmentsObservable);

		dateTimeColumn.setSortType(SortType.DESCENDING);
		appointmentsTable.getSortOrder().addAll(dateTimeColumn); // returns boolean
	}


	// TODO: handle if nothing is selected in appointmentsTable
	@SuppressWarnings("unchecked")
	@FXML public void showChangeAppointment() {
		Stage changeWindow = new Stage();
		changeWindow.initModality(Modality.APPLICATION_MODAL);
		changeWindow.setTitle("Change appointment");

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("NewAppointment.fxml"), Main.getBundle());
			GridPane gridPane = (GridPane) loader.load();
			NewAppointmentController changeController = loader.getController();

			// set values in new window
			Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();

			changeController.getPatientsTable().getSelectionModel().select(selectedAppointment.getPatient());

			changeController.setDateTime(selectedAppointment.getDateTime());

			changeController.setNotesTA(selectedAppointment.getNotes().toString());
			//---------------

			
			Scene changeScene = new Scene(gridPane,694,500);
			changeWindow.setScene(changeScene);
			changeWindow.show();

			changeWindow.setOnHiding( event -> {
				if(changeController.isAppointmentSaved()){
					deleteAppointment();
					appointmentsTable.getItems().add(changeController.getSavedAppointment());
					appointmentsTable.getSortOrder().addAll(dateTimeColumn); //refresh sort
				}
			} );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@FXML public void deleteAppointment() {
		ObservableList<Appointment> appointmetsObservable = appointmentsTable.getItems();
		Integer deleteId = appointmentsTable.getSelectionModel().getSelectedItem().getId();

		try {
			Appointment.delete(deleteId, appointmetsObservable);
		} catch (AppointmentException e) {
			Main.alert(e.getMessage(), AlertType.ERROR);
		}

		
		for (int i = 0; i < appointmetsObservable.size(); i++) {
			if(appointmetsObservable.get(i).getId() == deleteId){
				appointmetsObservable.remove(i);
				break;
			}
		}
    }

	// TODO: validation
	@FXML public void filterAppointments(){
		// if nothing is selected/written in the field
		Patient patient = patientsCBF.getSelectionModel().getSelectedItem();
		LocalDate date = datePickerF.getValue();
		String notes = notesF.getText().trim();
		String timeString = timeF.getText().trim();

		/*if(timeString.isEmpty()){
			appointmentsTable.getItems().removeIf(app ->{	
				if(patient != app.getPatient()) return true;
				if(date != app.getDateTime().toLocalDate()) return true;
				if(!app.getNotes().contains(notes)) return true;
				return false;
			});
		}else{*/
			//LocalTime time = LocalTime.parse(timeString);

			appointmentsTable.getItems().removeIf(app ->{	
				if(patient != null && !patient.equals(app.getPatient())) return true;
				if(date != null && !date.equals(app.getDateTime().toLocalDate())) return true;
				if(!notes.isEmpty() && !app.getNotes().contains(notes)) return true;
				if(!timeString.isEmpty() && !LocalTime.parse(timeString).equals(app.getDateTime().toLocalTime())) return true;
				return false;
			});
		
	}

	@FXML public void refreshAppointments(){
		Main.getBasicSceneController().showAppointments();
	}
	
	@FXML public void generatePDF(){}

	@FXML public void generateRTF(){}
}
