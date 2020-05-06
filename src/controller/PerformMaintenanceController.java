package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import view.PerformMaintenanceGUI;

public class PerformMaintenanceController implements EventHandler<ActionEvent> {

    private PerformMaintenanceGUI performMaintenanceGUI;

    public PerformMaintenanceController(PerformMaintenanceGUI performMaintenanceGUI) {
        this.performMaintenanceGUI = performMaintenanceGUI;
    }

    @Override
    public void handle(ActionEvent event) {
        Button btn = (Button) event.getSource();
        this.switchToAddRoom();
    }

    private Stage switchToAddRoom() {

        Stage window = new Stage();
        window.setScene(performMaintenanceGUI.performRoom());
        window.setTitle("Perform Maintenance");
        window.show();
        return window;
    }

}
