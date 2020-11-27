package source.fx;

import bundles.Bundle;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import main.Main;
import source.threads.AddEvent;

public class NewEventController {

    @FXML private TextArea notesTA;
	@FXML private DatePicker datePicker;
	@FXML private TextField timeTF;

    @FXML public void addEvent(){
        String notes = notesTA.getText();
        String date = datePicker.getValue().toString();
        String time = timeTF.getText();

        AddEvent organizerTask = new AddEvent(notes, date, time);
		
        organizerTask.start();
        
		try {
            organizerTask.join();
            
            if (organizerTask.isFailed()) {
                throw new RuntimeException(Bundle.getProperty("error_add_event"));
            } else
                Main.alert(organizerTask.getResponseMsg(), AlertType.INFORMATION);
			
		} catch (Exception e) {
			Main.alert(e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
}
