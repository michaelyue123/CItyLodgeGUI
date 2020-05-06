package model;

import model.customexception.UnableCompleteMaintenanceException;
import model.until.DateTime;

import java.util.ArrayList;

public class StandardRoom extends Room {

	ArrayList<Object> obj1 = new ArrayList<>();

	// constructor
	public StandardRoom(String roomID, int bedroomNum, String sumFeature, String roomType, String roomStatus) {
		super(roomID, bedroomNum, sumFeature, roomType, roomStatus);
	}

	@Override
	public void completeMaintenance(DateTime completionDate) throws UnableCompleteMaintenanceException {
		System.out.println(this.getRoomStatus());
		if (this.getRoomStatus().equals("maintenance")) {
			this.setRoomStatusAvailable();
		} else if (this.getRoomStatus().equals("rented") || this.getRoomStatus().equals("available")) {
			throw new UnableCompleteMaintenanceException();
		}else{
			throw new UnableCompleteMaintenanceException();
		}

	}

	@Override
	public String getDetails() {
		StringBuilder stringBuilder = new StringBuilder();
		if(this.getRecords().size() == 0) {
			stringBuilder.append(print1());
			stringBuilder.append("RENTAL RECORD: empty"+"\n");
		}else {
			stringBuilder.append(print1());
			stringBuilder.append("RENTAL RECORD"+"\n");
			stringBuilder.append(print2());
		}
		return stringBuilder.toString();
	}

	@Override
	public ArrayList<Object> getAllVariable() {
		// create a method to store all standard room variables inside Arraylist
		obj1.add(this.getRoomID());
		obj1.add(this.getBedRoomNum());
		obj1.add(this.getSumFeature());
		obj1.add(this.getRoomType());
		obj1.add(this.getRoomStatus());

		return obj1;
	}

	@Override
	public String getRoomName() {
		return "Standard Room";
	}

}
