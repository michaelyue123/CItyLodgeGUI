package model;

import model.customexception.ExceedMaxDayException;
import model.customexception.UnableCompleteMaintenanceException;
import model.customexception.UnableRentException;
import model.until.DateTime;

import java.util.ArrayList;

public class Suite extends Room {
	// private instance variables

	private DateTime maintenanceDate;
	private final int MAX_DAYS_CAN_BE_RENT = 10;
	ArrayList<Object> obj2 = new ArrayList<>();

	// constructor
	public Suite(String roomID, int bedRoomNum, String sumFeature, String roomType, String roomStatus,DateTime maintenanceDate) {
		super(roomID, bedRoomNum, sumFeature, roomType, roomStatus);
		this.maintenanceDate = maintenanceDate;
	}

	// constructor
	public Suite(String roomID, int bedRoomNum, String sumFeature, String roomType, String roomStatus) {
		super(roomID, bedRoomNum, sumFeature, roomType, roomStatus);
	}

	public DateTime getMaintenanceDate() {
		return maintenanceDate;
	}

	@Override
	public void rent(String customerID, DateTime rentDate, int numOfRentDay) throws UnableRentException, ExceedMaxDayException {
		if(this.getRoomStatus() != "available") {
			if(this.getRoomStatus() == "rented" || this.getRoomStatus() == "maintenance") {
				throw new UnableRentException();
			}
		}else {

			int numOfDaysCanBeRent = MAX_DAYS_CAN_BE_RENT - DateTime.diffDays(rentDate, this.maintenanceDate);
			if(numOfRentDay > numOfDaysCanBeRent) {
				throw new ExceedMaxDayException();
			}
		}
		this.setRoomStatusAvailable();
	}

	@Override
	public void completeMaintenance(DateTime completionDate) throws UnableCompleteMaintenanceException {
		if(this.getRoomStatus().equals("maintenance") || this.getRoomStatus().equals("available")) {
			this.setRoomStatusAvailable();
			//	update the maintenance date
			this.maintenanceDate = completionDate;
		}else{
			throw new UnableCompleteMaintenanceException();
		}
	}

	@Override
	public String toString() {
		return this.getRoomID() + ":" + this.getBedRoomNum() + ":" + this.getRoomType()+ ":" + this.getRoomStatus()+ ":"+ this.getMaintenanceDate()+":" + this.getSumFeature();
	}

	@Override
	public String getDetails() {
		StringBuilder stringBuilder = new StringBuilder();
		if(this.getRecords().size() == 0) {
			stringBuilder.append(print1());
			stringBuilder.append("Maintenance date: "+this.maintenanceDate+"\n");
			stringBuilder.append("RENTAL RECORD: empty"+"\n");
		}else {
			stringBuilder.append(print1());
			stringBuilder.append("Maintenance date: "+ this.maintenanceDate+"\n");
			stringBuilder.append("RENTAL RECORD"+"\n");
			stringBuilder.append(print2());
		}
		return stringBuilder.toString();
	}

	@Override
	public ArrayList<Object> getAllVariable() {

		// create a method to store all suite variables inside arraylist

		obj2.add(this.getRoomID());
		obj2.add(this.getBedRoomNum());
		obj2.add(this.getSumFeature());
		obj2.add(this.getRoomType());
		obj2.add(this.getRoomStatus());
		obj2.add(this.getMaintenanceDate());

		return obj2;
	}

	@Override
	public String getRoomName() {
		return "Luxury Suite";
	}
}
