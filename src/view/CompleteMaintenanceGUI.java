package view;

import javafx.application.Application;
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
import model.customexception.UnableCompleteMaintenanceException;
import model.linkdatabase.CityLodgeDatabase;
import model.until.DateTime;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompleteMaintenanceGUI extends Application {
    private String roomID = null;
    private String date = null;
    private DateTime maintenanceDate;
    private Room r;
    private CityLodgeDatabase cityLodgeDatabase;

    public CompleteMaintenanceGUI(Room r,CityLodgeDatabase cityLodgeDatabase)
    {
        this.r = r;
        this.cityLodgeDatabase = cityLodgeDatabase;
    }

    public Scene completeMaintenanceRoom() {
        TextField roomIDText, maintenanceDateText;
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        pane.setHgap(5.5);
        pane.setVgap(5.5);

        pane.add(new Label("Room ID:"), 0, 0);
        pane.add(roomIDText = new TextField(), 1, 0);
        pane.add(new Label("Maintenance completion date(dd/mm/yyyy): "), 0, 1);
        pane.add(maintenanceDateText = new TextField(), 1, 1);
        Button btn1 = new Button("Complete");
        btn1.setOnAction(event -> {
            roomID = roomIDText.getText().trim();
            if ( roomID.isEmpty() || maintenanceDateText.getText().isEmpty()) {
                new AlertInformation("Room id or last maintenance date is incorrect!");
                return;
            }
            if (!isRoomID(roomID)) {
                new AlertInformation("please input the same room ID!");
                return;
            }

            date = maintenanceDateText.getText().trim();
            if (!checkDate(date)) {
                new AlertInformation("return date(dd/mm/yyyy)!");
                return;
            }
            String[] arrOfStr = date.split("/", 3);
            maintenanceDate = new DateTime(Integer.parseInt(arrOfStr[0]), Integer.parseInt(arrOfStr[1]), Integer.parseInt(arrOfStr[2]));
            try {
                if(r.getRoomType().equalsIgnoreCase("standard")){
                    StandardRoom standardRoom = (StandardRoom) r;
                    standardRoom.completeMaintenance(maintenanceDate);
                }else{
                    Suite suite = (Suite) r;
                    suite.completeMaintenance(maintenanceDate);
                }
                cityLodgeDatabase.updateRoomStatus(r.getRoomID(),"available",maintenanceDate.toString());
            } catch (NumberFormatException e) {
                new AlertInformation(e.toString());
            } catch (ArrayIndexOutOfBoundsException e) {
                new AlertInformation(e.toString());
            } catch (UnableCompleteMaintenanceException e) {
                new AlertInformation(e.toString());
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

    // check the the validity of input date
    private boolean checkDate(String readDate) {
        String pattern = "^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}$+";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(readDate);
        return m.matches();
    }

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
        Scene scene = completeMaintenanceRoom();
        primaryStage.setTitle("Complete Maintenance");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
