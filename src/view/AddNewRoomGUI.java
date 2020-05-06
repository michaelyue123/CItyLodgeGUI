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
import model.linkdatabase.CityLodgeDatabase;
import model.until.DateTime;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddNewRoomGUI extends Application {
    private int numOfStdBedRooms = 0;
    private String type = null;
    private String roomID = null;
    private String date = null;
    private String roomFeature = null;
    private DateTime lastDate;
    private CityLodgeDatabase cityLodgeDatabase;

    public AddNewRoomGUI(CityLodgeDatabase cityLodgeDatabase){
        this.cityLodgeDatabase = cityLodgeDatabase;
    }

    public Scene addNewRoom() {
        TextField roomType, roomIDText, bedRoomNumber, lastMDate,roomFeatureText;
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        pane.setHgap(5.5);
        pane.setVgap(5.5);

        pane.add(new Label("Room Type (standard or suite):"), 0, 0);
        pane.add(roomType = new TextField(), 1, 0);
        pane.add(new Label("Room ID:"), 0, 1);
        pane.add(roomIDText = new TextField(), 1, 1);
        pane.add(new Label("Bedroom Number:"), 0, 2);
        pane.add(bedRoomNumber = new TextField(), 1, 2);
        pane.add(new Label("Last Maintenance Date: " + "\n(dd/mm/yyyy" + "\nno need to enter for standard type)"), 0, 3);
        pane.add(lastMDate = new TextField(), 1, 3);
        pane.add(new Label("Room Feature :"), 0, 4);
        pane.add(roomFeatureText = new TextField(), 1, 4);
        Button btn1 = new Button("Add");
        btn1.setOnAction(event -> {
            type = roomType.getText().trim();
            roomID = roomIDText.getText().trim();
            if (type.isEmpty() || roomID.isEmpty() || bedRoomNumber.getText().isEmpty()) {
                new AlertInformation("Room type or room id or bedroom number is incorrect!");
                return;
            }
            //Determine if the room type input is correct
            if (!roomTypeInput(type)) {
                new AlertInformation("Invalid room type please re-enter!");
                return;
            }
            if (checkExistingRoomID(roomID)) {
                new AlertInformation("roomId has already existed!");
                return;
            }
            if (!checkRoomID(roomID, type)) {
                new AlertInformation(" A room id must begin with R_ if the room is a standard hotel room and S_ if the room is a suite.!");
                return;
            }
            //you must input number
            try {
                numOfStdBedRooms = Integer.parseInt(bedRoomNumber.getText());
            } catch (NumberFormatException e) {
                new AlertInformation(" please input number!");
            }
            if (!checkBedNumber(numOfStdBedRooms, type)) {
                new AlertInformation(" Invalid number of bed rooms entered!");
                return;
            }
            date = lastMDate.getText().trim();
            if (type.equalsIgnoreCase("suite")) {
                if (date.isEmpty()) {
                    new AlertInformation("please input last maintenance date!");
                    return;
                } else {
                    if (!checkDate(date)) {
                        new AlertInformation("last maintenance date(dd/mm/yyyy)!");
                        return;
                    }
                    String[] arrOfStr = date.split("/", 3);
                    lastDate = new DateTime(Integer.parseInt(arrOfStr[0]), Integer.parseInt(arrOfStr[1]), Integer.parseInt(arrOfStr[2]));
                }
            } else {
                if (!date.isEmpty()) {
                    new AlertInformation("standard has no maintenance date recorded!");
                    return;
                }
            }
            roomFeature = roomFeatureText.getText().trim();
            Room room;
            if(type.equalsIgnoreCase("standard")){
                room = new StandardRoom(roomID,numOfStdBedRooms,roomFeature,type,"available");
            }else{
                room = new Suite(roomID,numOfStdBedRooms,roomFeature,type,"available",lastDate);
            }
            cityLodgeDatabase.insertRooms(room);
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



    //Determine the type of room entered
    private boolean roomTypeInput(String type) {
        if (type.equalsIgnoreCase("standard") || type.equalsIgnoreCase("suite")) {
            return true;
        }
        return false;
    }
    // check existing room ID method
    private boolean checkExistingRoomID(String roomID) {
        if(cityLodgeDatabase.query(roomID)){
            return true;
        }
        return false;
    }

    //check standard room ID method
    private boolean checkRoomID(String roomID, String type) {
        boolean b;
        if (type.equalsIgnoreCase("standard")) {
            b = roomID.charAt(0) == 'R' && roomID.charAt(1) == '_';
            return b;
        } else {
            b = roomID.charAt(0) == 'S' && roomID.charAt(1) == '_';
            return b;
        }
    }

    //check standard room ID method
    private boolean checkBedNumber(int bedNumber, String type) {
        boolean b = false;
        if (type.equalsIgnoreCase("standard")) {
            b = bedNumber == 1 || bedNumber == 2 || bedNumber == 4;
            return b;
        } else {
            b = bedNumber == 6;
            return b;
        }
    }

    // check the the validity of input date
    private boolean checkDate(String readDate) {
        String pattern = "^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}$+";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(readDate);
        return m.matches();
    }

    // enter room details method for both standard room and suite
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage = new Stage();
        Scene scene = addNewRoom();
        primaryStage.setTitle("Add New Room");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
