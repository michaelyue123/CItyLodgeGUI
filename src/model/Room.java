package model;

import model.customexception.*;
import model.until.DateTime;

import java.util.ArrayList;

public abstract class Room implements Comparable<Room>{
    // instance variables are set as private
    private String roomID;
    private int bedroomNum;
    private String sumFeature;
    private String roomType;
    private String roomStatus;
    private String recordID;
    private ArrayList<HiringRecord> records;
    private int[] priceList = new int[]{59, 99, 199, 999};
    ;

    // constructor
    public Room(String roomID, int bedroomNum, String sumFeature,
                String roomType, String roomStatus) {
        this.roomID = roomID;
        this.bedroomNum = bedroomNum;
        this.sumFeature = sumFeature;
        this.roomType = roomType;
        this.roomStatus = roomStatus;
        records = new ArrayList<>();
    }

    // getter
    public String getRoomID() {
        return roomID;
    }

    public int getBedRoomNum() {
        return bedroomNum;
    }

    public String getSumFeature() {
        return sumFeature;
    }

    public String getRoomType() {
        return this.roomType;
    }

    public String getRecordID() { return recordID; }

    public String getRoomStatus() {

        return this.roomStatus;
    }

    public void setRoomStatusAvailable() {
        this.roomStatus = "available";
    }

    public void setRecordID(String recordID){
        this.recordID = recordID;
    }

    public void setRoomStatusMaintenance() {
        this.roomStatus = "maintenance";
    }

    public void setRoomStatusRented() {
        this.roomStatus = "rented";
    }

    public ArrayList<HiringRecord> getRecords() {
        return this.records;
    }

    public void setRecords(ArrayList<HiringRecord> records) {
        this.records = records;
    }

    // rent method
    public void rent(String customerID, DateTime rentDate, int numOfRentDay) throws UnableRentException, ExceedMaxDayException {
        if (this.getRoomStatus() != "available") {
            if (this.getRoomStatus() == "rented" || this.getRoomStatus() == "maintenance") {
                throw new UnableRentException();
            }
        }
        this.setRoomStatusRented();
    }

    // return room method
    public HiringRecord returnRoom(DateTime returnDate,HiringRecord temp,Room r) throws UnableReturnException, NegativeDayDifferenceException, RecordNotExistException {
            if (this.getRoomStatus().equals("maintenance") || this.getRoomStatus().equals("available")) {
                throw new UnableReturnException();
            }
            String[] arrOfStr = temp.getRentDate().split("/", 3);
            DateTime rentDate = new DateTime(Integer.parseInt(arrOfStr[0]), Integer.parseInt(arrOfStr[1]), Integer.parseInt(arrOfStr[2]));
            if (DateTime.diffDays(returnDate, rentDate) > 0 && DateTime.diffDays(returnDate, rentDate) <= 10) {
                // update the room details
                this.setRoomStatusAvailable();
                temp.setActualReturnDate(returnDate);
                temp.getRentalFee(r);
                temp.getLateFee(r);
            } else {
                throw new NegativeDayDifferenceException();
            }
            return temp;
    }

    // perform maintenance method
    public void performMaintenance() throws UnableMaintainException {
        if (this.getRoomStatus().equals("available")) {
            this.setRoomStatusMaintenance();
//        } else if (this.getRoomStatus().equals("rented")) {
//            throw new UnableMaintainException();
//        } else {
//            throw new UnableMaintainException();
        }
        throw new UnableMaintainException();
    }

    // abstract complete maintenance method
    public abstract void completeMaintenance(DateTime completionDate) throws UnableCompleteMaintenanceException;

    public String toString() {
        return this.getRoomID() + ":" + this.getBedRoomNum() + ":" + this.getRoomType() + ":" + this.getRoomStatus() + ":" + this.getSumFeature();
    }

    public String print1() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Room ID: " + this.getRoomID() + "\n");
        stringBuilder.append("Number of bedroom: " + this.getBedRoomNum() + "\n");
        stringBuilder.append("Type: " + this.getRoomType() + "\n");
        stringBuilder.append("Status: " + this.getRoomStatus() + "\n");
        stringBuilder.append("Feature summary:  " + this.getSumFeature() + "\n");
        return stringBuilder.toString();
    }

    public String print2() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = this.getRecords().size() - 1; i >= 0; i--) {
            if (!this.getRoomStatus().equals("available") && this.getRecords().get(i).getAcReturnDate() == null) {
                stringBuilder.append("Record ID: " + this.getRecords().get(i).getRecordID(this) + "\n");
                stringBuilder.append("Rent Date: " + this.getRecords().get(i).getRentDate() + "\n");
                stringBuilder.append("Estimated Return Date: " + this.getRecords().get(i).getEsReturnDate() + "\n");
            } else {
                stringBuilder.append("--------------------------------------" + "\n");
                stringBuilder.append("Record ID: " + this.getRecords().get(i).getRecordID(this) + "\n");
                stringBuilder.append("Rent Date: " + this.getRecords().get(i).getRentDate() + "\n");
                stringBuilder.append("Estimated Return Date: " + this.getRecords().get(i).getEsReturnDate() + "\n");
                stringBuilder.append("Actual Return Date: " + this.getRecords().get(i).getAcReturnDate() + "\n");
                stringBuilder.append("Rental Fee: " + this.getRecords().get(i).getRentalFee(this) + "\n");
                stringBuilder.append("Late Fee: " + this.getRecords().get(i).getLateFee(this) + "\n");
            }
        }
        return stringBuilder.toString();
    }

    public abstract String getDetails();

    public abstract ArrayList<Object> getAllVariable();

    public abstract String getRoomName();

    public int getDailyRate() {
        int dailyRate = 0;
        if (this.getRoomType().equals("standard")) {
            if (this.getBedRoomNum() == 1) {
                dailyRate = priceList[0];
            } else if (this.getBedRoomNum() == 2) {
                dailyRate = priceList[1];
            } else if (this.getBedRoomNum() == 4) {
                dailyRate = priceList[2];
            }
            // else --> show an alert message "Invalid bedroom numbers"
        } else if (this.getRoomType().equals("suite")) {
            dailyRate = priceList[3];
        }
        return dailyRate;
    }

    @Override
    public int compareTo(Room o) {
        if(this.getRecords().size() > o.getRecords().size()){
            return -1;
        }else if(this.getRecords().size() < o.getRecords().size()){
            return 1;
        }
        return 0;
    }

}

