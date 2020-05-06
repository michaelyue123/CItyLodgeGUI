package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import view.ReturnRoomGUI;

public class ReturnRoomController implements EventHandler<ActionEvent> {

    private ReturnRoomGUI returnRoomGUI;

    public ReturnRoomController(ReturnRoomGUI returnRoomGUI) {
        this.returnRoomGUI = returnRoomGUI;
    }

    @Override
    public void handle(ActionEvent event) {
        Button btn = (Button) event.getSource();
        this.switchToAddRoom();
    }

    private Stage switchToAddRoom() {

        Stage window = new Stage();
        window.setScene(returnRoomGUI.returnRoom());
        window.setTitle("Return Room");
        window.show();
        return window;
    }

}

