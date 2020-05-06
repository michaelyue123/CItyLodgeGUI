package view;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Room;
import model.StandardRoom;
import model.Suite;
import model.customexception.AlertInformation;
import model.customexception.UnableMaintainException;
import model.linkdatabase.CityLodgeDatabase;

public class PerformMaintenanceGUI extends Application {

    private String roomID = null;

    private CityLodgeDatabase cityLodgeDatabase;

    private Room r;

    public PerformMaintenanceGUI(Room r,CityLodgeDatabase cityLodgeDatabase)
    {
        this.r = r;
        this.cityLodgeDatabase = cityLodgeDatabase;
    }
    public Scene performRoom() {
        TextField roomIDText;
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        pane.setHgap(5.5);
        pane.setVgap(5.5);

        pane.add(new Label("Room ID:"), 0, 0);
        pane.add(roomIDText = new TextField(), 1, 0);
        Button btn1 = new Button("Perform");
        btn1.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                roomID = roomIDText.getText().trim();
                if ( roomID.isEmpty()) {
                    new AlertInformation("Invalid Room Id please re-enter!");
                    return;
                }

                if (!isRoomID(roomID)) {
                    new AlertInformation("please input the same room ID!");
                    return;
                }
                try {
                if(r.getRoomType().equalsIgnoreCase("standard")){
                    StandardRoom standardRoom = (StandardRoom) r;
                    standardRoom.performMaintenance();
                }else{
                    Suite suite = (Suite) r;
                    suite.performMaintenance();
                }
                cityLodgeDatabase.updateRoomStatus(r.getRoomID(),"maintenance");
                } catch (UnableMaintainException e) {
                    new AlertInformation(e.toString());
                }

            }
        });
        Button btn2 = new Button("Cancel");
        btn2.setOnAction(event -> {
            Stage stage = (Stage) btn2.getScene().getWindow();
            stage.close();
        });
        pane.setPrefWidth(125);
        btn1.setMinWidth(pane.getPrefWidth());
        btn2.setMinWidth(pane.getPrefWidth());
        pane.add(btn1, 0, 8);
        pane.add(btn2, 1, 8);
        Scene scene = new Scene(pane, 500, 300);
        return scene;
    }

//    //check standard room ID method
//    private boolean checkRoomID(String roomID) {
//        boolean b = false;
//        return true;
//    }

    private boolean isRoomID(String roomID) {
        if (roomID.equals(r.getRoomID())) {
            return true;
        }
        return false;
    }

    // enter room details method for both standard room and suite
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage = new Stage();
        Scene scene = performRoom();
        primaryStage.setTitle("Perform Maintenance");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
