package mainclass;

import javafx.application.Application;
import javafx.stage.Stage;
import model.linkdatabase.CityLodgeDatabase;
import view.DisplayAllRoomDetailsGUI;

public class MainClass extends Application {

	private CityLodgeDatabase cityLodgeDatabase = new CityLodgeDatabase();


	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		cityLodgeDatabase.setUpConnection();
//		cityLodgeDatabase.createRoomTable();
//		cityLodgeDatabase.createHireTable();
		DisplayAllRoomDetailsGUI rdg = new DisplayAllRoomDetailsGUI(cityLodgeDatabase);
		stage.setTitle("All Room Details");
		stage.setScene(rdg.renderRoomScene());
		stage.show();
	}
}









