package source.fx;

import java.awt.Desktop;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.tutego.jrtf.Rtf;
import com.tutego.jrtf.RtfInfo;
import com.tutego.jrtf.RtfText;
import com.tutego.jrtf.RtfTextPara;

import bundles.Bundle;
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
import main.Language;
import main.Main;
import source.entity.Appointment;
import source.entity.Patient;

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
		// put patients in patientsCBF
		try {
			List<Patient> allPatients = Database.getAllPatients();
			Patient.setWeightGroup(allPatients);
			patientsCBF.setItems(FXCollections.observableArrayList(allPatients));
			
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
		
		try {
		
			ObservableList<Appointment> appointmentsObservable = FXCollections.observableArrayList(Appointment.getAllAppointments());

			appointmentsTable.setItems(appointmentsObservable);

			dateTimeColumn.setSortType(SortType.DESCENDING);
			appointmentsTable.getSortOrder().addAll(dateTimeColumn); // returns boolean	
			
		} catch (Exception e) {
			Main.alert(Bundle.getProperty("error_appointments_load"), AlertType.ERROR);
			e.printStackTrace();
		}
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
	
	@FXML public void generatePDF(){
		Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
		String name = selectedAppointment.getPatientName();
		String surname = selectedAppointment.getPatientSurname();
		String jmbg = selectedAppointment.getPatientJmbg();
		String dob = selectedAppointment.getPatientDOBformated();
		Integer id = selectedAppointment.getId();
		LocalDateTime dateTime = selectedAppointment.getDateTime();
		String date = dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."));
		String time = dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
		String notes = selectedAppointment.getNotes();
		
		
		try {
			
			String appointmentLang = Bundle.getProperty("Appointment");

			String outputFile = "reports\\" + appointmentLang + "_" + name + "_" + surname + "_" + date.replace(".", "-").substring(0, 10) + "_" + id + ".pdf";


			PdfWriter writer = new PdfWriter(outputFile);
			PdfDocument pdf = new PdfDocument(writer);
			com.itextpdf.layout.Document doc = new com.itextpdf.layout.Document(pdf);

			Paragraph title = new Paragraph(appointmentLang).setTextAlignment(TextAlignment.CENTER);;
			Paragraph patientInfo = new Paragraph("\n" + Bundle.getProperty("Patient") + "\n\n" + 
										Bundle.getProperty("Name_colon") + " " + name + "\n" + 
										Bundle.getProperty("Surname_colon") + " " + surname + "\n" + 
										Bundle.getProperty("jmbg_colon") + " " + jmbg + "\n" + 
										Bundle.getProperty("DOB_colon") + " " + dob);
			Paragraph appointmentInfo = new Paragraph("\n" + appointmentLang + "\n\n" + 
											Bundle.getProperty("Date") + ": " + date + "\n" + 
											Bundle.getProperty("Time") + ": " + time + "\n" + 
											Bundle.getProperty("Notes") + ": " + notes);
			

			doc.add(title);
			doc.add(patientInfo);
			doc.add(appointmentInfo);


			PdfCanvas canvas = new PdfCanvas(pdf, 1); // or 1
			// page size = 595.0x842.0 = Rectangle pageSize = doc.getPdfDocument().getPage(1).getPageSize();
			// 0,0 point is bottom left corner
			canvas.moveTo(30, 605)
            		.lineTo(110, 605)
					.stroke();
			canvas.moveTo(30, 740)
					.lineTo(80, 740)
					.stroke();
			
				
			doc.close();
			pdf.close();

			Main.yesNoAlert(Bundle.getProperty("PDF_saved_Open_it_now"));

			if(Main.getAlertReturnValue())
				Desktop.getDesktop().open(new File(outputFile));
				
		} catch (IOException e) {
			Main.alert(e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}

	@FXML public void generateRTF(){
		Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();
		String name = selectedAppointment.getPatientName();
		String surname = selectedAppointment.getPatientSurname();
		String jmbg = selectedAppointment.getPatientJmbg();
		String dob = selectedAppointment.getPatientDOBformated();
		Integer id = selectedAppointment.getId();
		LocalDateTime dateTime = selectedAppointment.getDateTime();
		String date = dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."));
		String time = dateTime.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"));
		String notes = selectedAppointment.getNotes();
		
		try {
			Properties langProp = new Properties();

			for(Language lang : Language.values()){
				if(lang == Main.getChosenLanguage())
					langProp.load(new FileReader("src\\bundles\\Bundle_" + lang.toString() + ".properties"));
			}
			
			String appointmentLang = Bundle.getProperty("Appointment");

			String outputFile = "reports\\" + appointmentLang + "_" + name + "_" + surname + "_" + date.replace(".", "-").substring(0, 10) + "_" + id + ".rtf";

			Rtf doc = Rtf.rtf().info(RtfInfo.title(appointmentLang + " - " + id));

			RtfTextPara title = RtfTextPara.p( RtfText.fontSize(30, appointmentLang)).alignCentered();
			RtfTextPara patientInfo = RtfTextPara.p(RtfText.bold("\n" + Bundle.getProperty("Patient")), "\n\n" + 
										Bundle.getProperty("Name_colon") + " " + name + "\n" + 
										Bundle.getProperty("Surname_colon") + " " + surname + "\n" + 
										Bundle.getProperty("jmbg_colon") + " " + jmbg + "\n" + 
										Bundle.getProperty("DOB_colon") + " " + dob);
			RtfTextPara appointmentInfo = RtfTextPara.p(RtfText.bold("\n" + appointmentLang), "\n\n" + 
											Bundle.getProperty("Date") + ": " + date + "\n" + 
											Bundle.getProperty("Time") + ": " + time + "\n" + 
											Bundle.getProperty("Notes") + ": " + notes);

			doc.section(title, patientInfo, appointmentInfo);

			
			doc.out( new FileWriter(outputFile) );

			Main.yesNoAlert(Bundle.getProperty("RTF_saved_Open_it_now"));
			if(Main.getAlertReturnValue())
				Desktop.getDesktop().open(new File(outputFile));

		} catch (IOException e) {
			Main.alert(e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}

}
