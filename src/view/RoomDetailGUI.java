package view;

import controller.CompleteMaintenanceController;
import controller.PerformMaintenanceController;
import controller.RentRoomController;
import controller.ReturnRoomController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Room;
import model.StandardRoom;
import model.Suite;
import model.linkdatabase.CityLodgeDatabase;

public class RoomDetailGUI  {

    private Room r;

    private CityLodgeDatabase cityLodgeDatabase;

    public RoomDetailGUI(Room r,CityLodgeDatabase cityLodgeDatabase){
        this.r = r;
        this.cityLodgeDatabase =cityLodgeDatabase;
    }

    public Pane renderRoomPane() {
        ImageView roomImage = null;
        if (r instanceof StandardRoom) {
            if (r.getRoomStatus().equals("available")) {
                if (r.getBedRoomNum() == 1)
                    roomImage = new ImageView("images/Standard Room 1.png");
                if (r.getBedRoomNum() == 2)
                    roomImage = new ImageView("images/Standard Room 2.png");
                if (r.getBedRoomNum() == 4)
                    roomImage = new ImageView("images/Standard Room 3.png");
            } else
                roomImage = new ImageView("images/Not Available.png");
        } else if (r instanceof Suite) {
            if (r.getRoomStatus().equals("available"))
                roomImage = new ImageView("images/Suite 1.png");
            else
                roomImage = new ImageView("images/Not Available.png");
        }
        roomImage.setFitHeight(300);
        roomImage.setFitWidth(500);
        Pane pane1 = new Pane();
        pane1.setPadding(new Insets(0));
        pane1.getChildren().add(roomImage);

        r.setRecords(cityLodgeDatabase.getHireList(r.getRoomID()));
        Text roomCords;
        if(r instanceof StandardRoom){
            StandardRoom standardRoom = (StandardRoom) r;
            roomCords = new Text(15, 95,standardRoom.getDetails());
        }else{
            Suite suite = (Suite) r;
            roomCords = new Text(15, 95,suite.getDetails());
        }

        ScrollPane sp = new ScrollPane();
        sp.setContent(roomCords);
        sp.setStyle("-fx-background-color:transparent;");
        sp.setPadding(new Insets(10));
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        Color c1 = Color.rgb(121, 145, 97);
        roomCords.setFill(c1);
        roomCords.setFont(Font.font("verdana", FontWeight.LIGHT, FontPosture.ITALIC, 15));

        HBox hb1 = new HBox(20);
        hb1.setPadding(new Insets(0, 0, 20, 0));
        hb1.getChildren().addAll(pane1,sp);
        hb1.setLayoutX(60);
        hb1.setLayoutY(50);

        Pane pane3 = new Pane();
        Rectangle rectangle = new Rectangle(950, 300);

        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.TRANSPARENT);
        pane3.setStyle("-fx-background-color: #e7ebc2");
        pane3.setBackground(Background.EMPTY);
        pane3.getChildren().addAll(rectangle, hb1);

        return pane3;
    }

    public Scene renderRoomScene() {
        Pane pane = renderRoomPane();
        VBox vb = new VBox(25);
        vb.getChildren().addAll(pane);
        FlowPane fp = new FlowPane();
        fp.getChildren().add(vb);
        vb.setPadding(new Insets(30, 30, 15, 50));
        HBox hb2 = new HBox(65);
        hb2.setPadding(new Insets(30, 30, 15, 75));
        Button btn1 = new Button("Rent Room");
        btn1.setOnAction(new RentRoomController(new RentRoomGUI(r,cityLodgeDatabase)));
        Button btn2 = new Button("Return Room");
        btn2.setOnAction(new ReturnRoomController(new ReturnRoomGUI(r,cityLodgeDatabase)));
        Button btn3 = new Button("Perform Maintenance");
        btn3.setOnAction(new PerformMaintenanceController(new PerformMaintenanceGUI(r,cityLodgeDatabase)));
        Button btn4 = new Button("Complete Maintenance");
        btn4.setOnAction(new CompleteMaintenanceController(new CompleteMaintenanceGUI(r,cityLodgeDatabase)));
        Button btn5 = new Button("Refresh");
        btn5.setOnAction(event -> {
            Stage stage = (Stage) btn5.getScene().getWindow();
            stage.setScene(renderRoomScene());
            stage.show();
        });
        Button btn6 = new Button("Back");
        btn6.setOnAction(event -> {
            Stage stage = (Stage) btn6.getScene().getWindow();
            stage.close();
        });
        Button[] btn = {btn1, btn2, btn3, btn4,btn5,btn6};
        changeButtonSize(btn);
        hb2.getChildren().addAll(btn1, btn2, btn3, btn4,btn5,btn6);

        VBox vbox = new VBox(hb2, fp);
        vbox.setPadding(new Insets(20, 50, 10, 20));

        return new Scene(vbox, 1100, 650);
    }


    private void changeButtonSize(Button[] btn) {
        for (Button button : btn) {
            button.setStyle("-fx-font-size: 1.0em;");
        }
    }
}


