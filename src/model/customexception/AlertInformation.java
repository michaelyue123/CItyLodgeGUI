package model.customexception;

import javafx.scene.control.Alert;

public class AlertInformation {

    public AlertInformation(String information){
        Alert alert = new Alert(Alert.AlertType.ERROR,information);
        alert.setTitle("error");
        alert.setHeaderText("");
        alert.showAndWait();
    }
}
