/*package source.fx;

import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class Popup {

    static boolean answer;

    public static boolean display(){

        Stage popupwindow = new Stage();
        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("This is a pop up window");

        Label label1 = new Label("Do you want to go to the movies?");

        Button yesbutton = new Button("Yes");
        Button nobutton = new Button("No");

        yesbutton.setOnAction(e -> {
            answer = true;
            popupwindow.close();
        });

        nobutton.setOnAction(e -> {
            answer = false;
            popupwindow.close();
        });

        HBox layout = new HBox(10);

        layout.getChildren().addAll(label1, yesbutton, nobutton);

        layout.setAlignment(Pos.CENTER);

        Scene scene1 = new Scene(layout, 300, 250);

        popupwindow.setScene(scene1);

        popupwindow.showAndWait();

        return answer;
    }

}
*/