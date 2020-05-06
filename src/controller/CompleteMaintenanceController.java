package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import view.CompleteMaintenanceGUI;

public class CompleteMaintenanceController implements EventHandler<ActionEvent> {

    private CompleteMaintenanceGUI completeMaintenanceGUI;

    public CompleteMaintenanceController(CompleteMaintenanceGUI completeMaintenanceGUI) {
        this.completeMaintenanceGUI = completeMaintenanceGUI;
    }

    @Override
    public void handle(ActionEvent event) {
        Button btn = (Button) event.getSource();
        this.switchToAddRoom();
    }

    private Stage switchToAddRoom() {

        Stage window = new Stage();
        window.setScene(completeMaintenanceGUI.completeMaintenanceRoom());
        window.setTitle("Complete Maintenance");
        window.show();
        return window;
    }

}
