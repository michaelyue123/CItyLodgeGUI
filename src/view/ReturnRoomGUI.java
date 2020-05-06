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
import model.HiringRecord;
import model.Room;
import model.StandardRoom;
import model.Suite;
import model.customexception.AlertInformation;
import model.customexception.NegativeDayDifferenceException;
import model.customexception.RecordNotExistException;
import model.customexception.UnableReturnException;
import model.linkdatabase.CityLodgeDatabase;
import model.until.DateTime;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReturnRoomGUI extends Application {
    private String roomID = null;
    private String date = null;
    private DateTime returnDate;
    private Room r;
    private CityLodgeDatabase cityLodgeDatabase;

    public ReturnRoomGUI(Room r,CityLodgeDatabase cityLodgeDatabase) {
        this.r = r;
        this.cityLodgeDatabase = cityLodgeDatabase;
    }

    public Scene returnRoom() {
        TextField roomIDText, returnDateText;
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        pane.setHgap(5.5);
        pane.setVgap(5.5);

        pane.add(new Label("Room ID:"), 0, 0);
        pane.add(roomIDText = new TextField(), 1, 0);
        pane.add(new Label("return date(dd/mm/yyyy):"), 0, 1);
        pane.add(returnDateText = new TextField(), 1, 1);
        Button btn1 = new Button("Return");
        btn1.setOnAction(event -> {
            roomID = roomIDText.getText().trim();
            if (roomID.isEmpty() || returnDateText.getText().isEmpty()) {
                new AlertInformation("Room id or return date is incorrect!");
                return;
            }
            if (!isRoomID(roomID)) {
                new AlertInformation("please input the same room ID!");
                return;
            }

            date = returnDateText.getText().trim();
            if (!checkDate(date)) {
                new AlertInformation("return date(dd/mm/yyyy)!");
                return;
            }
            String[] arrOfStr = date.split("/", 3);
            returnDate = new DateTime(Integer.parseInt(arrOfStr[0]), Integer.parseInt(arrOfStr[1]), Integer.parseInt(arrOfStr[2]));
            HiringRecord hiringRecord;
            try {
                if (r.getRoomType().equalsIgnoreCase("suite")) {
                    Suite suite = (Suite) r;
                    String recordID = cityLodgeDatabase.queryRecord(suite.getRoomID());
                    hiringRecord = cityLodgeDatabase.queryHire(recordID);
                    suite.returnRoom(returnDate,hiringRecord,r);
                    cityLodgeDatabase.updateHire(hiringRecord.getRecordID(suite),hiringRecord.getAcReturnDate().toString(),
                            hiringRecord.getLateFee(suite));
                } else {
                    StandardRoom standardRoom = (StandardRoom) r;
                    String recordID = cityLodgeDatabase.queryRecord(standardRoom.getRoomID());
                    hiringRecord = cityLodgeDatabase.queryHire(recordID);
                    hiringRecord = standardRoom.returnRoom(returnDate,hiringRecord,r);
                    cityLodgeDatabase.updateHire(hiringRecord.getRecordID(standardRoom),hiringRecord.getAcReturnDate(),
                            hiringRecord.getLateFee(standardRoom));
                }
                cityLodgeDatabase.updateRoomStatus(r.getRoomID(),"available");
                System.out.println("successful");
            } catch (UnableReturnException e) {
                new AlertInformation(e.toString());
            } catch (NegativeDayDifferenceException e) {
                new AlertInformation(e.toString());
            } catch (RecordNotExistException e) {
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


//    //Determine the type of room entered
//    private boolean roomTypeInput(String type) {
//        if (type.equalsIgnoreCase("standard") || type.equalsIgnoreCase("suite")) {
//            return true;
//        }
//        return false;
//    }
//
//    //check standard room ID method
//    private boolean checkRoomID(String roomID) {
//        boolean b = false;
//        return true;
//    }

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
        Scene scene = returnRoom();
        primaryStage.setTitle("Return Room");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private boolean isRoomID(String roomID) {
        if (roomID.equals(r.getRoomID())) {
            return true;
        }
        return false;
    }
}
