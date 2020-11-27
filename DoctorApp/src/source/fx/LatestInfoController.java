package source.fx;

import java.io.IOException;

import org.json.JSONObject;

import bundles.Bundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import main.Main;
import source.threads.Covid;
import source.threads.NextEvent;

public class LatestInfoController {

    @FXML private Label dateTimeCovidLabel;
    @FXML private Label casesWorldLabel;
    @FXML private Label casesCroatiaLabel;
    @FXML private Label curedWorldLabel;
    @FXML private Label curedCroatiaLabel;
    @FXML private Label dateTimeEventLabel;
    @FXML private Label notesLabel;


    @FXML
    public void initialize() {

        Covid covidTask = new Covid();
        NextEvent organizerTask = new NextEvent();

        covidTask.start();
        organizerTask.start();

        try {
            organizerTask.join();
            
            JSONObject event = organizerTask.getNextEvent();

            if (event == null) {
                throw new RuntimeException(Bundle.getProperty("error_get_event"));
            }else{
                dateTimeEventLabel.setText(event.getString("DateTime"));
                notesLabel.setText(notesLabel.getText() + ": " + event.getString("Notes"));
            }
        } catch (Exception e) {
			Main.alert(e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }

        try {
            covidTask.join();  
            
            dateTimeCovidLabel.setText(dateTimeCovidLabel.getText() + ": " + covidTask.getDateTime());
            casesWorldLabel.setText(casesWorldLabel.getText() + " " + covidTask.getCasesWorld().toString());
            casesCroatiaLabel.setText(casesCroatiaLabel.getText() + " " + covidTask.getCasesCroatia().toString());
            curedWorldLabel.setText(curedWorldLabel.getText() + " " + covidTask.getCuredWorld().toString());
            curedCroatiaLabel.setText(curedCroatiaLabel.getText() + " " + covidTask.getCuredCroatia().toString());
        } catch (InterruptedException e) {
			Main.alert(e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML public void showNewEvent(){
        try {
			GridPane pane = (GridPane)FXMLLoader.load(getClass().getResource("NewEvent.fxml"), Main.getBundle());
			Main.setCenter(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}
