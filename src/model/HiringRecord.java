package model;

import model.until.DateTime;

import java.util.ArrayList;

public class HiringRecord {

	   // instance variables are set as private
		private static final double One_Std_Price = 59;
		private static final double Two_Std_Price = 99;
		private static final double Four_Std_Price = 199;
		private static final double Suite_Price = 999;
		private final double Late_Suite_Price = 1099;
		private final int One_Room = 1;
		private final int Two_Room = 2;
		private final int Four_Room = 4;
		private final double Late_Penalty = 1.35;

		private String recordID;
		private String customerID;
		private DateTime rentDate;
		private DateTime esReturnDate;
		private DateTime acReturnDate;
		private double rentalFee;
		private double lateFee;
		private double dailyRate;

		// constructor
		public HiringRecord(String recordID,String customerID,DateTime rentDate,DateTime esReturnDate,
			DateTime acReturnDate,double rentalFee,double lateFee) {
			this.recordID = recordID;
			this.customerID = customerID;
			this.rentDate = rentDate;
			this.esReturnDate = esReturnDate;
			this.acReturnDate = acReturnDate;
			this.rentalFee = rentalFee;
			this.lateFee = lateFee;
		}

		// hiring record without late fee
		public HiringRecord(String recordID,String customerID,DateTime rentDate,DateTime esReturnDate,
						DateTime acReturnDate,double rentalFee) {
			this.recordID = recordID;
			this.customerID = customerID;
			this.rentDate = rentDate;
			this.esReturnDate = esReturnDate;
			this.acReturnDate = acReturnDate;
			this.rentalFee = rentalFee;
		}


	  //setter
		public void setActualReturnDate(DateTime date) {
			this.acReturnDate = date;
		}

		// overloading
		public HiringRecord(String recordID,String customerID,DateTime rentDate,DateTime esReturnDate) {
			this(recordID,customerID,rentDate,esReturnDate,null,0.00,0.00);
		}		
		// getter 
		public String getRecordID(Room hr) {
			return recordID = hr.getRoomID() + "_" + this.getCustomerID() + "_" + rentDate.getEightDigitDate();
		}

		public String getCustomerID() { return customerID; }

		public String getRentDate() {
			return rentDate.getFormattedDate();	// the date that customer starts renting the room	
		}
		
		public String getEsReturnDate() {
			return esReturnDate.getFormattedDate(); // the estimated return date 
		}
			
		public String getAcReturnDate() {
		    if(acReturnDate == null){
		        return null;
            }
			return acReturnDate.getFormattedDate(); // the actual return date
		}
				
		public double getRentalFee(Room hr) {
			int noOfDays = DateTime.diffDays(esReturnDate,rentDate);
			if(hr.getRoomType().equals("standard") || hr.getRoomType().equals("Standard")) {
				if(hr.getBedRoomNum() == One_Room) {
					this.rentalFee = One_Std_Price * noOfDays;
				}
				if(hr.getBedRoomNum() == Two_Room) {
					this.rentalFee = Two_Std_Price * noOfDays;
				}
				if(hr.getBedRoomNum() == Four_Room) {
					this.rentalFee = Four_Std_Price * noOfDays;
				}
			}else if(hr.getRoomType().equals("suite") || hr.getRoomType().equals("Suite")) {
				this.rentalFee = Suite_Price * noOfDays;
			}else {
				System.out.println("Invalid room type!");
			}
			return this.rentalFee;
		}

		public double getLateFee(Room hr) {
		    if(acReturnDate == null)
		        return 0.00;
			int noOfDays = DateTime.diffDays(acReturnDate,esReturnDate);
			if(noOfDays <0) {
				this.lateFee = 0.0;
				return this.lateFee;
			}			
			else if(hr.getRoomType().equals("standard") || hr.getRoomType().equals("Standard")) {
				if(hr.getBedRoomNum() == One_Room) {
					this.lateFee = Late_Penalty * One_Std_Price * noOfDays;
				}
				if(hr.getBedRoomNum() == Two_Room) {
					this.lateFee = Late_Penalty * Two_Std_Price * noOfDays;
				}
				if(hr.getBedRoomNum() == Four_Room) {
					this.lateFee = Late_Penalty * Four_Std_Price * noOfDays;
				}
			}else if(hr.getRoomType().equals("suite") || hr.getRoomType().equals("Suite")){
				this.lateFee = Late_Suite_Price * noOfDays;
			}else {
				System.out.println("Invalid room type!");
			}
			return this.lateFee;
		}

		// return the daily rate of a room
	public double getDailyRate(Room hr) {
		if(hr.getRoomType().equals("standard") || hr.getRoomType().equals("Standard")) {
			if(hr.getBedRoomNum() == One_Room) {
				this.dailyRate = One_Std_Price;
			}
			if(hr.getBedRoomNum() == Two_Room) {
				this.dailyRate = Two_Std_Price;
			}
			if(hr.getBedRoomNum() == Four_Room) {
				this.dailyRate = Four_Std_Price;
			}
		}else if(hr.getRoomType().equals("suite") || hr.getRoomType().equals("Suite")) {
			this.dailyRate = Suite_Price;
		}else {
			System.out.println("Invalid room type!");
		}
		return this.dailyRate;
	}
		
		public String toString() {
			return recordID + ":" + rentDate + ":" + esReturnDate + ":" + acReturnDate + ":" + rentalFee + ":" + lateFee;
		}
		
		// create a print method to print details
		private void print1() {
			System.out.println("RecordID: " + recordID);
			System.out.println("Rent Date: " + rentDate);
			System.out.println("Estimated Return Date: " + esReturnDate);
		}
		
		private void print2() {
			System.out.println("RecordID: " + recordID);
			System.out.println("Rent Date: " + rentDate);
			System.out.println("Estimated Return Date: " + esReturnDate);
			System.out.println("Actual Return Date: " + acReturnDate);
			System.out.println("Rental Fee: " + rentalFee);
			System.out.println("Late Fee: " + lateFee);
		}
		
		public String getDetails(Room hr) {
			if(hr.getRoomStatus() == "rented") {
				this.print1();
			}else {
				this.print2();
			}
			return " ";
		}

		public static ArrayList<Double> getPriceList() {

			ArrayList<Double> priceList = new ArrayList<>();

			priceList.add(One_Std_Price);
			priceList.add(Two_Std_Price);
			priceList.add(Four_Std_Price);
			priceList.add(Suite_Price);

			return priceList;
		}
}
