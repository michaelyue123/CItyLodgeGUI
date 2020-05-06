package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import view.RoomDetailGUI;

public class RoomDetailGUIController implements EventHandler<ActionEvent> {
    //    private Room room;
    private RoomDetailGUI addNewRoomGUI;

    public RoomDetailGUIController(RoomDetailGUI addNewRoomGUI) {
//        this.room = room;
        this.addNewRoomGUI = addNewRoomGUI;
    }

    @Override
    public void handle(ActionEvent event) {
        event.getSource();
        this.switchToAddRoom();
    }

    private Stage switchToAddRoom() {

        Stage window = new Stage();
        window.setScene(addNewRoomGUI.renderRoomScene());
        window.setTitle("Room Details");
        window.show();

        return window;
    }

}
