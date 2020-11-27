package source.entity;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import database.Database;
import exceptions.AppointmentException;
import javafx.collections.ObservableList;

public class Appointment {
    public static final String XMLPATH = "data\\Appointments.xml";

    private final Integer id;
    private Patient patient;
    private LocalDateTime dateTime;
    private String notes;

    public Appointment(Integer id, Patient patient, LocalDateTime dateTime, String notes) {
        this.id = id;
        this.patient = patient;
        this.dateTime = dateTime;
        this.notes = notes;
    }

    public String getPatientName(){
        return patient.getName();
    }

    public String getPatientSurname(){
        return patient.getSurname();
    }

    public String getPatientJmbg(){
        return patient.getJmbg();
    }

    public String getPatientDOBformated(){
        return patient.getDOBformatted();
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Integer getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }


    public static Integer getMaxId(){
        Integer maxId = 1;

        try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(XMLPATH));
			NodeList nList = doc.getElementsByTagName("Appointment");
					
			for (int i = 0; i < nList.getLength(); i++) {
                Element appointmentEl = (Element) nList.item(i);
                
                Integer id = Integer.valueOf(appointmentEl.getAttribute("id"));
                
                if (id > maxId){
                    maxId = id;
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
        }
        
        return maxId;
    }

    public static void delete(Integer id, ObservableList<? extends Appointment> list) throws AppointmentException{
        try{
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(Appointment.XMLPATH));

            XPathExpression expression = XPathFactory.newInstance().newXPath().compile("//Appointment[@id='" +id+ "']");

            Node node = (Node) expression.evaluate(document, XPathConstants.NODE);

            node.getParentNode().removeChild(node); 

            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.transform(new DOMSource(document), new StreamResult(new FileWriter(new File(Appointment.XMLPATH))));
        }catch(Exception e){
            throw new AppointmentException(e);
        }
    }

    public static List<Appointment> getAllAppointments() throws AppointmentException {
        List<Appointment> appointments = new ArrayList<>();
        
        try {
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
            
            return appointments;

        } catch (Exception e) {
            throw new AppointmentException(e);
        }
    }
    
}
