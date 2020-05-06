package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import view.AddNewRoomGUI;

public class AddNewRoomController implements EventHandler<ActionEvent> {

//    private Room room;
    private AddNewRoomGUI addNewRoomGUI;

    public AddNewRoomController(AddNewRoomGUI addNewRoomGUI) {
//        this.room = room;
        this.addNewRoomGUI = addNewRoomGUI;
    }

    @Override
    public void handle(ActionEvent event) {
        Button btn = (Button) event.getSource();
        this.switchToAddRoom();
    }

    private Stage switchToAddRoom() {

        Stage window = new Stage();
        window.setScene(addNewRoomGUI.addNewRoom());
        window.setTitle("Add New Room");
        window.show();

        return window;
    }

}
