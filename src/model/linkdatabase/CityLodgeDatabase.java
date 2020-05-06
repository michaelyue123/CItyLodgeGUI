package model.linkdatabase;

import model.HiringRecord;
import model.Room;
import model.StandardRoom;
import model.Suite;
import model.until.DateTime;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CityLodgeDatabase {

    private static final String DB_NAME = "CityLodgeDB";
    private static Connection con = null;

    private void getConnection(String dbName) throws SQLException, ClassNotFoundException {

        //Registering the HSQLDB JDBC driver
        Class.forName("org.hsqldb.jdbc.JDBCDriver");
        con = DriverManager.getConnection
                ("jdbc:hsqldb:file:database/" + dbName, "SA", "");
        con.setAutoCommit(true);
    }

    public void setUpConnection() {
        try {
            getConnection(DB_NAME);
            if (con != null) {
                System.out.println("Connection to database " + DB_NAME + " created successfully!");
            } else {
                System.out.println("Problem with creating connection.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // create room table
    public void createRoomTable() {
        try {
            Statement statement = con.createStatement();
            String sql_create = "CREATE TABLE room ("
                    + "roomID VARCHAR(20) NOT NULL,"
                    + "roomType VARCHAR(20) NOT NULL,"
                    + "roomFeature VARCHAR(200),"
                    + "roomStatus VARCHAR(20) NOT NULL,"
                    + "bedRoomNum INT NOT NULL,"
                    + "lastMaintenanceDate VARCHAR(20) ,"
                    + "recordID VARCHAR(40) ,"
                    + "PRIMARY KEY (roomID))";
            int result = statement.executeUpdate(sql_create);
            if(result == 0) {
                System.out.println("Table " + " has been created successfully");
            } else {
                System.out.println("Table "  + " is not created");
            }
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void createHireTable() {
        try {
            Statement statement = con.createStatement();
            String sql_create = "CREATE TABLE record ("
                    + "recordID VARCHAR(40) NOT NULL,"
                    + "roomID VARCHAR(20) NOT NULL,"
                    + "customerID VARCHAR(20) NOT NULL,"
                    + "rentDate VARCHAR(20) NOT NULL,"
                    + "esReturnDate VARCHAR(20),"
                    + "acReturnDate VARCHAR(20),"
                    + "rentalFee DOUBLE,"
                    + "lateFee DOUBLE,"
                    + "PRIMARY KEY (recordID))";
            int result = statement.executeUpdate(sql_create);
            if(result == 0) {
                System.out.println("Table " + " has been created successfully");
            } else {
                System.out.println("Table "  + " is not created");
            }
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void insertHireFormText(String recordID,String roomID,String customerID,String rentDate,
                                   String esReturnDate,String acReturnDate,Double rentalFee,Double lateFee){

        recordID = "\'" + recordID + "\'";
        roomID = "\'" + roomID + "\'";
        customerID = "\'" + customerID + "\'";
        rentDate = "\'" + rentDate + "\'";
        esReturnDate = "\'" + esReturnDate + "\'";
        acReturnDate = "\'" + acReturnDate + "\'";
        String sql_insert = "insert into record values ("+recordID+","+roomID+","+customerID+","+rentDate+
                ","+esReturnDate+","+acReturnDate+","+rentalFee+","+lateFee+")";
        try {
            Statement statement = con.createStatement();
            statement.executeUpdate(sql_insert);
        } catch (Exception e) {
        }
    }

    public static void insertHireTable(HiringRecord hiringRecord,Room room){
        String sql_insert = "insert into record values (?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pst = con.prepareStatement(sql_insert);
            pst.setString(1, hiringRecord.getRecordID(room));
            pst.setString(2, room.getRoomID());
            pst.setString(3, hiringRecord.getCustomerID());
            pst.setString(4, hiringRecord.getRentDate());
            pst.setString(5, hiringRecord.getEsReturnDate());
            pst.setString(6, null);
            pst.setDouble(7, hiringRecord.getRentalFee(room));
            pst.setDouble(8,0.00);
            pst.executeUpdate();
        } catch (Exception e) {
        }
    }

    public static void insertRooms(Object object){
        Room room = null;
        if(object instanceof StandardRoom){
            room = (StandardRoom) object;
        }else if(object instanceof Suite){
            room = (Suite) object;
        }
        String sql_insert = "insert into room values (?,?,?,?,?,?,?)";
        try {
            PreparedStatement pst = con.prepareStatement(sql_insert);
            pst.setString(1,room.getRoomID() );
            pst.setString(2, room.getRoomType());
            pst.setString(3, room.getSumFeature());
            pst.setString(4, room.getRoomStatus());
            pst.setInt( 5, room.getBedRoomNum());
            if (room instanceof Suite){
                pst.setString(6,((Suite) room).getMaintenanceDate().toString());
            }else{
                pst.setString(6,null);
            }
            pst.setString(7, null);
            pst.executeUpdate();
        } catch (Exception e) {
        }
    }

    // query room info
    public ResultSet queryHireTable(String roomID) {
        roomID = "\'" + roomID + "\'";
        String sql_insert = "select * from record where roomID="+roomID;
        try {
            PreparedStatement pst = con.prepareStatement(sql_insert);
            return pst.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateHire(String recordID,String acReturnDate,Double lateFee){
        recordID = "\'" + recordID + "\'";
        acReturnDate = "\'" + acReturnDate + "\'";
        String update = "update record set acReturnDate ="+acReturnDate+",lateFee="+lateFee+" where recordID ="+recordID;
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(update);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<HiringRecord> getHireList(String roomID) {
        ArrayList<HiringRecord> hiringRecords = new ArrayList<HiringRecord>();
        ResultSet rs = queryHireTable(roomID);
        try {
            while (rs.next()) {
                String recordID = rs.getString("recordID");
                String roomID1 = rs.getString("roomID");
                String customerID = rs.getString("customerID");
                String rentDateString = rs.getString("rentDate");
                String[] arrOfStr = rentDateString.split("/", 3);
                DateTime rentDate = new DateTime(Integer.parseInt(arrOfStr[0]), Integer.parseInt(arrOfStr[1]), Integer.parseInt(arrOfStr[2]));

                String esReturnDateString = rs.getString("esReturnDate");
                String[] esReturnDates = esReturnDateString.split("/", 3);
                DateTime esReturnDate = new DateTime(Integer.parseInt(esReturnDates[0]), Integer.parseInt(esReturnDates[1]), Integer.parseInt(esReturnDates[2]));

                String acReturnDateString = rs.getString("acReturnDate");
                DateTime acReturnDate = null;
                if(acReturnDateString != null && !acReturnDateString.isEmpty()){
                    String[] acReturnDates  = acReturnDateString.split("/", 3);
                    acReturnDate = new DateTime(Integer.parseInt(acReturnDates[0]), Integer.parseInt(acReturnDates[1]), Integer.parseInt(acReturnDates[2]));
                }
                Double rentalFee = rs.getDouble(7);
                Double lateFee = rs.getDouble(8);
                HiringRecord hiringRecord = new HiringRecord(recordID,customerID,rentDate,esReturnDate,
                        acReturnDate,rentalFee,lateFee);
                hiringRecords.add(hiringRecord);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hiringRecords;
    }

    // query room info
    public ResultSet queryRooms() {
        String sql_insert = "select * from room";
        try {
            PreparedStatement pst = con.prepareStatement(sql_insert);
            return pst.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateRoomStatus(String roomID,String select){
        roomID = "\'" + roomID + "\'";
        select = "\'" + select + "\'";
        String update = "update room set roomStatus ="+select+" where roomID ="+roomID;
        try {
            Statement stmt = con.createStatement();
            int rs = stmt.executeUpdate(update);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateRecordID(String roomID,String select){
        roomID = "\'" + roomID + "\'";
        select = "\'" + select + "\'";
        String update = "update room set recordID ="+select+" where roomID ="+roomID;
        try {
            Statement stmt = con.createStatement();
            int rs = stmt.executeUpdate(update);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateStandard(String roomID,StandardRoom room){
        roomID = "\'" + roomID + "\'";
        String roomStatus= "\'" + room.getRoomStatus() + "\'";
        String roomFeature = "\'" + room.getSumFeature() + "\'";
        String update = "update room set bedRoomNum ="+room.getBedRoomNum()+",roomStatus ="
                +roomStatus+",roomFeature ="+roomFeature+" where roomID ="+roomID;
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(update);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateSuite(String roomID,Suite room){
        roomID = "\'" + roomID + "\'";
        String roomStatus= "\'" + room.getRoomStatus() + "\'";
        String roomFeature = "\'" + room.getSumFeature() + "\'";
        String lastDate = "\'" +room.getMaintenanceDate().toString()+ "\'";
        String update = "update room set bedRoomNum ="+room.getBedRoomNum()+",roomStatus ="
                +roomStatus+",roomFeature ="+roomFeature+",lastMaintenanceDate="+lastDate+" where roomID ="+roomID;
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(update);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateRoomStatus(String roomID,String select1,String select2){
        roomID = "\'" + roomID + "\'";
        select1 = "\'" + select1 + "\'";
        select2 = "\'" + select2 + "\'";
        String update = "update room set roomStatus ="+select1+","+" lastMaintenanceDate ="+select2+" where roomID ="+roomID;
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(update);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //query roomID check existing room ID
    public boolean query(String roomID) {
        roomID = "\'" + roomID + "\'";
        String sql_query = "select * from room where roomID=" + roomID;
        try {
            PreparedStatement pst = con.prepareStatement(sql_query);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                return true;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //query roomID check existing room ID
    public String queryRecord(String roomID) {
        roomID = "\'" + roomID + "\'";
        String sql_query = "select * from room where roomID=" + roomID;
        try {
            PreparedStatement pst = con.prepareStatement(sql_query);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                String recordID = rs.getString("recordID");
                return recordID;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //query roomID check existing room ID
    public HiringRecord queryHire(String recordID) {
        recordID = "\'" + recordID + "\'";
        String sql_query = "select * from record where recordID=" + recordID;
        try {
            PreparedStatement pst = con.prepareStatement(sql_query);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                String recordID1= rs.getString("recordID");
                String roomID1 = rs.getString("roomID");
                String customerID = rs.getString("customerID");
                String rentDateString = rs.getString("rentDate");
                String[] arrOfStr = rentDateString.split("/", 3);
                DateTime rentDate = new DateTime(Integer.parseInt(arrOfStr[0]), Integer.parseInt(arrOfStr[1]), Integer.parseInt(arrOfStr[2]));

                String esReturnDateString = rs.getString("esReturnDate");
                String[] esReturnDates = esReturnDateString.split("/", 3);
                DateTime esReturnDate = new DateTime(Integer.parseInt(esReturnDates[0]), Integer.parseInt(esReturnDates[1]), Integer.parseInt(esReturnDates[2]));

                String acReturnDateString = rs.getString("acReturnDate");
                DateTime acReturnDate = null;
                if(acReturnDateString != null){
                    String[] acReturnDates  = acReturnDateString.split("/", 3);
                    acReturnDate = new DateTime(Integer.parseInt(acReturnDates[0]), Integer.parseInt(acReturnDates[1]), Integer.parseInt(acReturnDates[2]));
                }
                Double rentalFee = rs.getDouble(7);
                Double lateFee = rs.getDouble(8);
                HiringRecord hiringRecord = new HiringRecord(recordID1,customerID,rentDate,esReturnDate,
                        acReturnDate,rentalFee,lateFee);
                return  hiringRecord;
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Room> getRoomList() {
        List<Room> rooms = new ArrayList<Room>();
        ResultSet rs = queryRooms();
        try {
            while (rs.next()) {
                String roomId = rs.getString("roomID");
                String roomType = rs.getString("roomType");
                String roomFeature = rs.getString("roomFeature");
                String roomStatus = rs.getString("roomStatus");
                int bedRoomNum = rs.getInt(5);
                if(roomType.equalsIgnoreCase("standard")){
                    rooms.add(new StandardRoom(roomId,bedRoomNum,roomFeature,roomType,roomStatus));
                }else{
                    String date = rs.getString("lastMaintenanceDate");
                    String[] arrOfStr = date.split("/", 3);
                    DateTime lastDate = new DateTime(Integer.parseInt(arrOfStr[0]), Integer.parseInt(arrOfStr[1]), Integer.parseInt(arrOfStr[2]));
                    rooms.add(new Suite(roomId,bedRoomNum,roomFeature,roomType,roomStatus,lastDate));
                }

            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public static void closeConnection() throws SQLException {
        con.close();
    }

}