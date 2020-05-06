package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import view.RentRoomGUI;

public class RentRoomController implements EventHandler<ActionEvent> {

    private RentRoomGUI rentRoomGUI;

    public RentRoomController(RentRoomGUI addNewRoomGUI) {
        this.rentRoomGUI = addNewRoomGUI;
    }

    @Override
    public void handle(ActionEvent event) {
        Button btn = (Button) event.getSource();
        this.switchToAddRoom();
    }

    private Stage switchToAddRoom() {

        Stage window = new Stage();
        window.setScene(rentRoomGUI.rentRoom());
        window.setTitle("Rent Room");
        window.show();

        return window;
    }

}

