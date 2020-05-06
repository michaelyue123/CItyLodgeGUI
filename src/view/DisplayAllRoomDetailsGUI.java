package view;

import controller.AddNewRoomController;
import controller.RoomDetailGUIController;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.HiringRecord;
import model.Room;
import model.StandardRoom;
import model.Suite;
import model.linkdatabase.CityLodgeDatabase;
import model.until.DateTime;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class DisplayAllRoomDetailsGUI {
    private CityLodgeDatabase cityLodgeDatabase;

    public DisplayAllRoomDetailsGUI(CityLodgeDatabase cityLodgeDatabase) {
        this.cityLodgeDatabase = cityLodgeDatabase;
    }

    private Pane renderRoomPane(Room r) {

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
        roomImage.setFitHeight(240);
        roomImage.setFitWidth(400);
        Pane pane1 = new Pane();
        pane1.setPadding(new Insets(0));
        pane1.getChildren().add(roomImage);

        Pane pane2 = new Pane();
        pane2.setPadding(new Insets(0, 0, 0, 10));
        Text title = new Text(15, 40, r.getRoomName());
        Text bedRoomNum = new Text(25, 70, "Number of Bedroom:" + r.getBedRoomNum());
        String room_feature = "Feature:  " + r.getSumFeature();
        room_feature += "\n";
        Text feature = new Text(15, 190, room_feature.replaceAll("(.{1,35})\\s+", "$1\n"));
        Text dailyRate = new Text(325, 55, "$" + r.getDailyRate());
        Text perNight = new Text(360, 70, "per night");

        Color c1 = Color.rgb(191, 136, 48);
        Color c2 = Color.rgb(121, 145, 97);
        title.setFill(c1);
        bedRoomNum.setFill(c2);
        dailyRate.setFill(c2);
        perNight.setFill(c2);
        title.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 20));
        bedRoomNum.setFont(Font.font("verdana", FontWeight.LIGHT, FontPosture.ITALIC, 13));
        feature.setFont(Font.font("verdana", FontWeight.LIGHT, FontPosture.ITALIC, 15));
        dailyRate.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 28));
        perNight.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 13));

        Button b1 = new Button("view room details");
        String style = "-fx-background-color: rgba(104,216,255,0.84);-fx-font-size: 1.2em;";
        b1.setStyle(style);
        b1.setLayoutX(340);
        b1.setLayoutY(170);
        b1.setOnAction(new RoomDetailGUIController(new RoomDetailGUI(r, cityLodgeDatabase)));
        pane2.getChildren().addAll(title, bedRoomNum, feature, dailyRate, perNight, b1);
        HBox hb1 = new HBox();
        hb1.setPadding(new Insets(0, 0, 20, 0));
        hb1.getChildren().addAll(pane1, pane2);
        hb1.setLayoutX(60);
        hb1.setLayoutY(25);

        Pane pane3 = new Pane();
        Rectangle rectangle = new Rectangle(900, 240);
        rectangle.xProperty().bind(pane3.widthProperty().divide(2));
        rectangle.yProperty().bind(pane3.heightProperty().divide(2));
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.TRANSPARENT);
        pane3.setStyle("-fx-background-color: #e7ebc2");
        pane3.setBackground(Background.EMPTY);
        pane3.getChildren().addAll(rectangle, hb1);

        return pane3;
    }

    private void changeButtonSize(Button[] btn) {
        for (Button button : btn) {
            button.setStyle("-fx-font-size: 1.2em;");
        }
    }

    public Scene renderRoomScene() {

        List<Room> rooms = cityLodgeDatabase.getRoomList();
        List<Pane> panes = new ArrayList<>();
        for (Room room : rooms) {
            panes.add(renderRoomPane(room));
        }

        VBox vb = new VBox(25);

        vb.getChildren().addAll(panes);
        FlowPane fp = new FlowPane();
        fp.getChildren().add(vb);

        ScrollPane sp = new ScrollPane();
        sp.setContent(fp);
        sp.setStyle("-fx-background-color:transparent;");
        sp.setPadding(new Insets(20, 10, 20, 40));
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        HBox hb2 = new HBox(65);
        hb2.setPadding(new Insets(30, 30, 15, 80));
        Button btn1 = new Button("Add New Room");
        btn1.setOnAction(new AddNewRoomController(new AddNewRoomGUI(cityLodgeDatabase)));
        Button btn2 = new Button("Import Room Data");
        btn2.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            Stage s = new Stage();
            File file = fileChooser.showOpenDialog(s);
            if (file == null)
                return;
            List<String> strings;
            try {
                strings = readFromTextFile(file.getAbsolutePath());
                for(int i = 0;i<strings.size();i++){
                    String string = strings.get(i);
                    if(string.contains("standard")){
                        String[] roomInfos = string.split(":");
                        StandardRoom standardRoom = new StandardRoom(roomInfos[0],Integer.parseInt(roomInfos[1]),
                                roomInfos[4],roomInfos[2],roomInfos[3]);
                        if(!cityLodgeDatabase.query(roomInfos[0])){
                            cityLodgeDatabase.insertRooms(standardRoom);
                        }else{
                            cityLodgeDatabase.updateStandard(roomInfos[0],standardRoom);
                        }
                        for(int j=i+1;j<strings.size();j++){
                                String record = strings.get(j);
                                if(record.contains("suite") || record.contains("standard")){
                                    break;
                                }else{
                                    String[] records = record.split(":");
                                    String[] customers = records[0].split("_");
                                    String customer = null;
                                    for(String st : customers){
                                        if(st.contains("CUS")){
                                            customer = st;
                                            break;
                                        }
                                    }
                                    if(cityLodgeDatabase.queryHire(records[0]) == null){
                                        cityLodgeDatabase.insertHireFormText(records[0],standardRoom.getRoomID(),customer,records[1],
                                                records[2],records[3],Double.parseDouble(records[4]),Double.parseDouble(records[5]));
                                    }else{
                                        cityLodgeDatabase.updateHire(records[0],records[3],Double.parseDouble(records[5]));
                                    }
                                    if(records[3].isEmpty()){
                                        cityLodgeDatabase.updateRecordID(standardRoom.getRoomID(),records[0]);
                                    }
                                }
                        }
                    }else if(string.contains("suite")){
                        String[] roomInfos = string.split(":");
                        String[] arrOfStr = roomInfos[4].split("/", 3);
                        DateTime dateTime = new DateTime(Integer.parseInt(arrOfStr[0]), Integer.parseInt(arrOfStr[1]), Integer.parseInt(arrOfStr[2]));
                        Suite suite = new Suite(roomInfos[0],Integer.parseInt(roomInfos[1]),
                                roomInfos[5],roomInfos[2],roomInfos[3],dateTime);
                        if(!cityLodgeDatabase.query(roomInfos[0])){
                            cityLodgeDatabase.insertRooms(suite);
                        }else{
                            cityLodgeDatabase.updateSuite(roomInfos[0],suite);
                        }
                        for(int j=i+1;j<strings.size();j++){
                            String record = strings.get(j);
                            if(record.contains("suite") || record.contains("standard")){
                                break;
                            }else{
                                String[] records = record.split(":");
                                String[] customers = records[0].split("_");
                                String customer = null;
                                for(String st :customers){
                                    if(st.contains("CUS")){
                                        customer = st;
                                        break;
                                    }
                                }
                                if(cityLodgeDatabase.queryHire(records[0]) == null){
                                    cityLodgeDatabase.insertHireFormText(records[0],suite.getRoomID(),customer,records[1],
                                            records[2],records[3],Double.parseDouble(records[4]),Double.parseDouble(records[5]));
                                }else{
                                    cityLodgeDatabase.updateHire(records[0],records[3],Double.parseDouble(records[5]));
                                }
                                if(records[3].isEmpty()){
                                    cityLodgeDatabase.updateRecordID(suite.getRoomID(),records[0]);
                                }
                            }
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("Could not locate input file.");
            } catch (Exception e) {
                System.out.println(e.toString());
            }


        });
        Button btn3 = new Button("Export Room Data");
        btn3.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            Stage s = new Stage();
            File file = fileChooser.showSaveDialog(s);
            if (file == null)
                return;
            if (file.exists()) {
                file.delete();
            }

            List<Room> list = cityLodgeDatabase.getRoomList();
            for (Room room : list) {
                ArrayList<HiringRecord> hiringRecords = cityLodgeDatabase.getHireList(room.getRoomID());
                room.setRecords(hiringRecords);
            }
            Collections.sort(list);
            StringBuilder sBuilder = new StringBuilder();
            if (list.size() > 0) {
                for (Room room : list) {
                    sBuilder.append(room.toString() +":"+ getImage(room) + "\r\n");
                    int i = 0;
                    for (HiringRecord hiringRecord : room.getRecords()) {
                        i++;
                        if (i < 11) {
                            sBuilder.append(hiringRecord.toString() + "\r\n");
                        }
                    }
                }
            }
            Writer out;
            try {
                out = new FileWriter(file);
                out.write(sBuilder.toString());
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Button btn4 = new Button("Refresh");
        btn4.setOnAction(event -> {
            Stage stage = (Stage) btn4.getScene().getWindow();
            stage.setScene(renderRoomScene());
            stage.show();
        });
        Button btn5 = new Button("Exit the program");
        btn5.setOnAction(event -> {
            Stage stage = (Stage) btn4.getScene().getWindow();
            stage.close();
        });
        Button[] btn = {btn1, btn2, btn3, btn4,btn5};
        changeButtonSize(btn);
        hb2.getChildren().addAll(btn1, btn2, btn3, btn4,btn5);

        VBox vbox = new VBox(hb2, sp);
        vbox.setPadding(new Insets(20, 65, 10, 20));

        return new Scene(vbox, 1120, 1000);
    }

    private ArrayList<String> readFromTextFile(String pathname) throws IOException{
        ArrayList<String> strArray = new ArrayList<>();
        File filename = new File(pathname);
        InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
        BufferedReader br = new BufferedReader(reader);
        String line;
        line = br.readLine();
        while(line != null) {
            strArray.add(line);
            line = br.readLine();
        }
        return strArray;
    }

    private String getImage(Room r) {
        String image = null;
        if (r instanceof StandardRoom) {
            if (r.getRoomStatus().equals("available")) {
                if (r.getBedRoomNum() == 1)
                    image = "images/Standard Room 1.png";
                if (r.getBedRoomNum() == 2)
                    image = "images/Standard Room 2.png";
                if (r.getBedRoomNum() == 4)
                    image = "images/Standard Room 3.png";
            } else
                image = "images/Not Available.png";
        } else if (r instanceof Suite) {
            if (r.getRoomStatus().equals("available"))
                image = "images/Suite 1.png";
            else
                image = "images/Not Available.png";
        }
        return image;
    }
}

