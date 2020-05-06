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
import model.linkdatabase.CityLodgeDatabase;
import model.until.DateTime;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RentRoomGUI extends Application {
    private String roomID = null;
    private String customerID = null;
    private String rentDate = null;
    private DateTime date;
    private int day;
    private Room r;
    private CityLodgeDatabase cityLodgeDatabase;

    public RentRoomGUI(Room r, CityLodgeDatabase cityLodgeDatabase) {
        this.r = r;
        this.cityLodgeDatabase = cityLodgeDatabase;
    }

    public Scene rentRoom() {
        TextField roomIDText, customerText, rentDateText, dayNumberText;
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        pane.setHgap(5.5);
        pane.setVgap(5.5);
        pane.add(new Label("Room ID:"), 0, 0);
        pane.add(roomIDText = new TextField(), 1, 0);
        pane.add(new Label("Customer ID(CUS123 for example):"), 0, 1);
        pane.add(customerText = new TextField(), 1, 1);
        pane.add(new Label("Rent date(dd/mm/yyyy):"), 0, 2);
        pane.add(rentDateText = new TextField(), 1, 2);
        pane.add(new Label("How many days? "), 0, 3);
        pane.add(dayNumberText = new TextField(), 1, 3);
        Button btn1 = new Button("Add");
        btn1.setOnAction(event -> {
            roomID = roomIDText.getText().trim();
            customerID = customerText.getText().trim();
            rentDate = rentDateText.getText().trim();
            if (roomID.isEmpty() || customerID.isEmpty() || rentDate.isEmpty() || dayNumberText.getText().isEmpty()) {
                new AlertInformation("Room id or customer id or rent date or number of day is incorrect!");
                return;
            }
            if (!checkRoomID(roomID)) {
                new AlertInformation("Invalid Room Id please re-enter!");
                return;
            }
            if (!checkCustomerID(customerID)) {
                new AlertInformation("Invalid customer ID!");
                return;
            }
            if (!checkDate(rentDate)) {
                new AlertInformation("Rent date(dd/mm/yyyy)!");
                return;
            }
            String[] arrOfStr = rentDate.split("/", 3);
            date = new DateTime(Integer.parseInt(arrOfStr[0]), Integer.parseInt(arrOfStr[1]), Integer.parseInt(arrOfStr[2]));

            try {
                day = Integer.parseInt(dayNumberText.getText());
            } catch (NumberFormatException e) {
                new AlertInformation("please input number!");
            }
            if (r.getRoomType().equalsIgnoreCase("standard") && checkNameOfDay(date) && (day >= 3 && day <= 10)) {
                // standard room book condition
                rentedByCustomer(roomID, customerID, date, day);
            } else if (r.getRoomType().equalsIgnoreCase("suite")) {    // suit room book condition
                rentedByCustomer(roomID, customerID, date, day);
            } else {
                new AlertInformation("Invalid no of days as minimum of 2 days(Monday~Friday) and maximum no of days is 10\n" + "Invalid no of days as minimum of 3 days(Saturday/Sunday) and maximum no of days is 10");
                return;
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

    private boolean checkRoomID(String roomID) {
        if (roomID.equals(r.getRoomID())) {
            return true;
        }
        return false;
    }

    // check the the validity of input date
    private boolean checkDate(String readDate) {
        String pattern = "^([0-2][0-9]|(3)[0-1])(/)(((0)[0-9])|((1)[0-2]))(/)\\d{4}$+";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(readDate);
        return m.matches();
    }

    //     check the validity of customer ID
    private boolean checkCustomerID(String customerID) {
        return customerID.charAt(0) == 'C' && customerID.charAt(1) == 'U' && customerID.charAt(2) == 'S';
    }

    // check name of day method
    private boolean checkNameOfDay(DateTime rentDate) {
        System.out.println(rentDate.getNameOfDay());
        if (rentDate.getNameOfDay().equals("Monday")
                || rentDate.getNameOfDay().equals("Tuesday")
                || rentDate.getNameOfDay().equals("Wednesday")
                || rentDate.getNameOfDay().equals("Thursday")
                || rentDate.getNameOfDay().equals("Friday")) {
            return true;
        } else if (isStandardType(rentDate.getNameOfDay(), "Saturday", "Sunday")) {
            return true;
        } else {
            return true;
        }
    }

    private boolean isStandardType(String type, String standard, String standard2) {
        return type.equals(standard) || type.equals(standard2);
    }

    private void rentedByCustomer(String roomID, String customerID, DateTime rentDate, int day) {
        if (r.getRoomType().equalsIgnoreCase("standard")) {
            try {
                StandardRoom room = (StandardRoom) r;
                room.rent(customerID, rentDate, day);
                String recordID = roomID + "_" + customerID + "_" + rentDate.getEightDigitDate();
                HiringRecord rec = new HiringRecord(recordID, customerID, rentDate, new DateTime(rentDate, day));
                rec.getRentalFee(room);
                cityLodgeDatabase.insertHireTable(rec,room);
                cityLodgeDatabase.updateRecordID(roomID,recordID);
            } catch (Exception e) {
                new AlertInformation(e.toString());
            }
        } else {
            try {
                Suite suite = (Suite) r;
                suite.rent(customerID, rentDate, day);
                suite.setRoomStatusRented();
                String recordID = roomID + "_" + customerID + "_" + rentDate.getEightDigitDate();
                HiringRecord rec = new HiringRecord(recordID, customerID, rentDate, new DateTime(rentDate, day));
                rec.getRentalFee(suite);
                suite.getRecords().add(rec);
                cityLodgeDatabase.insertHireTable(rec,suite);
                cityLodgeDatabase.updateRecordID(roomID,recordID);

            } catch (Exception e) {
                new AlertInformation(e.toString());
            }
        }
        cityLodgeDatabase.updateRoomStatus(r.getRoomID(), "rented");
    }

    // enter room details method for both standard room and suite
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage = new Stage();
        Scene scene = rentRoom();
        primaryStage.setTitle("Rent Room");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
