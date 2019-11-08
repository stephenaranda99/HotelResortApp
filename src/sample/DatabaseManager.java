package sample;


import com.sun.xml.internal.fastinfoset.util.StringArray;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javax.xml.crypto.Data;
import org.h2.command.Prepared;

public class DatabaseManager extends Main {

  private Connection con = null;
  String verified = null;


  //final String JDBC_DRIVER = "org.h2.Driver";
  public DatabaseManager() throws SQLException {

    this.con = DriverManager
        .getConnection("jdbc:h2:C:\\Users\\shafi\\IdeaProjects\\HotelResortApp\\res\\ResortData");

  }
  public void startDatabase(String userName, String password ,String role) {
    LogInAccount(userName, password, role);

  }
  public ArrayList<String> parseString(String stringlist){
    ArrayList<String> result = new ArrayList<>();
    for(String token: stringlist.substring(1,stringlist.length()-1).split(", ")){
      result.add(token);
    }
    return result;


  }
  public boolean verifyPasswordorPin(String userType, String userID, String pinOrPassword, String type) throws SQLException {
    boolean passwordChanged = false;
    String pinToVerify = null;
    String typeChanged = null;
    Statement stmt = this.con.createStatement();
    try {
      ResultSet rs = null;

      rs = stmt.executeQuery(String.format("SELECT * FROM %s WHERE USERNAME = '%s'",userType, userID));
      if (rs.next()) {

        System.out.println("usimg this pinOrPassword:" + pinOrPassword);
        if(type.equals("PIN")){
          int tempPinToVerify = rs.getInt("PIN");
          pinToVerify = String.valueOf(tempPinToVerify);
          typeChanged = type;
          if (pinToVerify.equals(pinOrPassword)) {
            System.out.println("Successfully " + typeChanged +" old verified");
            passwordChanged = true;
            return true;


          } else {
            System.out.println("Wrong "+typeChanged);
            passwordChanged = false;
            return false;

          }

        }
        else if (type.equals("PASSWORD")){
          pinToVerify  = rs.getString("Password");
          typeChanged = type;
          System.out.println("The password extracted "+ pinToVerify + " " +pinOrPassword);
          if (pinToVerify.equals(pinOrPassword)) {
            System.out.println("Successfully " + typeChanged +" old verified");
            passwordChanged = true;
            return true;


          } else {
            System.out.println("Wrong "+typeChanged);
            passwordChanged = false;
            return false;

          }
        }





      }
    }catch(SQLException var6){
        this.sqlExceptionHandler(var6);
      }
      return passwordChanged;
  }
  public void changePinOrPass(String userType, String userId, String typeChange, String newPinOrPass)
      throws SQLException {
    Statement stmt = this.con.createStatement();
    stmt.executeUpdate(String.format("UPDATE %s SET %s = '%s' where username = '%s'",userType, typeChange,newPinOrPass,userId));


  }

  public String getTheName(String email){
    String id = null;
    try {
      ResultSet rs = null;

      Statement stmt = this.con.createStatement();

      rs = stmt.executeQuery(String.format("SELECT * FROM CUSTOMER WHERE EMAIL = '%s'",email));
      if(rs.next()) {

        System.out.println("usimg this name:" + email);
        id = rs.getString("USERNAME");
        System.out.println(id);

      }




    } catch (SQLException var6) {
      this.sqlExceptionHandler(var6);
    }
    return id;




  }
  public boolean AddCustomer(String fullName, String userName, String email, String phonenumber,
      String password , int verifyPin, String address,String city, String state, int zipcode ,String country ) {
    boolean nameDoesntExisted = false;
    try {

      Statement stmt = this.con.createStatement();

      boolean rssHad = false;
      boolean rsHad = false;

      ResultSet rss = null;
      rss = stmt.executeQuery(String.format("SELECT * FROM CUSTOMER WHERE email = '%s'",email));
      if(rss.next()){
        String emailExist = rss.getString("email");
        nameDoesntExisted = false;
        rssHad = true;
      }

//      System.out.println("rf.close is " + rs.isClosed());
//      System.out.println("rf.close is " + rss.isClosed());


      ResultSet rs = null;
      rs = stmt.executeQuery(String.format("SELECT * FROM CUSTOMER WHERE USERNAME = '%s'",userName));
      if(!rssHad && rs.next()){

        String userNameExisted = rs.getString("USERNAME");
        System.out.println("it exist");
        nameDoesntExisted = false;
        rsHad = true;
      }

      if (!rssHad && !rsHad) {

        stmt.executeUpdate(String.format(
            "INSERT INTO CUSTOMER (FULLNAME, USERNAME, email, phonenumber, password ,verifyPin, address,city, state, zipcode ,country ) VALUES ('%s' ,'%s','%s', '%s','%s','%d','%s', '%s','%s','%d','%s')",
             fullName,  userName,  email,  phonenumber,
             password ,  verifyPin,  address, city,  state,  zipcode , country ));
        System.out.println("HEy it pushed");
        nameDoesntExisted= true;
      }


    } catch (SQLException var6) {
      this.sqlExceptionHandler(var6);
    }
    return nameDoesntExisted;

  }
  public boolean LogInAccount( String email, String password ,String role) {
    Boolean verified = false;
    try {
      ResultSet rs = null;

      Statement stmt = this.con.createStatement();

      rs = stmt.executeQuery(String.format("SELECT * FROM  %s WHERE EMAIL = '%s'", role, email));
      if(rs.next()){

        System.out.println("usimg this name:" + password);
        String pass = rs.getString("PASSWORD");
        System.out.println(pass);

        if (pass.equals(password)){
          System.out.println("Successfull");
          verified = true;

        }
        else{
          System.out.println("Wrong Password");
          verified = false;
        }

      }
      else{
        System.out.println("Email DOESNT EXIST");
        verified = false;
      }

    } catch (SQLException var6) {
      this.sqlExceptionHandler(var6);
    }
    return verified;

  }
  public void saveEmailCardTable(String userName, String email){

    try {
      ResultSet rs = null;

      Statement stmt = this.con.createStatement();
      stmt.executeUpdate(String.format("Insert Into CARDSAVED (CARDMEMBEREMAIL, USERID) VALUES ('%s','%s')",email,userName));
    } catch (SQLException var6) {
      this.sqlExceptionHandler(var6);
    }
  }
  public void saveCardInfo(String userId, String cardType, long cardNumber, int month, int year, int cvv, int zipcode){
    try {


      Statement stmt = this.con.createStatement();
      System.out.println(userId+"0000000000005");
      System.out.println(cardNumber);
      System.out.println(month);
      System.out.println(userId+"0000000000005");
      stmt.executeUpdate(String.format("UPDATE CARDSAVED SET (cardtype, cardNumber, month, year, cvv, billingzipcode) = ('%s','%d','%d','%d','%d','%d') where userid = '%s'",cardType,cardNumber,month,
          year,cvv,zipcode,userId));
      System.out.println("Card Saved");
    } catch (SQLException var6) {
      this.sqlExceptionHandler(var6);
    }
  }


  public void createCustomerTable(String userName){
    try {
      System.out.println("SOB");
      String lol = userName;
      System.out.println(lol);

      Statement stmt = this.con.createStatement();
      System.out.println("Creating table in given database...");

      String sql = "CREATE TABLE "+userName +
          "(ORDERNO INTEGER , " +
          " ROOMNO VARCHAR(255), " +
          " DaysBooked VARCHAR(4555), "+
          " DISPLAYDATE VARCHAR(255), " +
          "COST DOUBLE ) ";
      stmt.executeUpdate(sql);


      System.out.println("table created");



    } catch (SQLException var6) {
      this.sqlExceptionHandler(var6);
    }

  }
  public void paidColumn(int orderNO){
    try {
      Statement stmt = this.con.createStatement();
      String paymentDone = "Yes";
      ResultSet rs = null;
      stmt.executeUpdate(String.format("UPDATE INVOICENO SET pay = 'yes' where orderno = '%d'",orderNO));

    } catch (Exception e) {
      e.printStackTrace();
    }

  }
  public int orderNumber(){
    int orderNo = 0;
    try{
      Statement stmt = this.con.createStatement();

      ResultSet rs = null;
      rs = stmt.executeQuery(String.format("Select MAX(ORDERNO) FROM INVOICENO"));
      while(rs.next()){
        String orderNo1 = rs.getString(1);
        orderNo = Integer.parseInt(orderNo1);
        System.out.println("HAPPYYYY " + orderNo);

        System.out.println(orderNo+ " I finally pushed the order no for the client screen");


      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return orderNo;
  }
  public void pushDate(String userName, String roomNo, ArrayList<String> dateBooked,double cost, String dateToDisplay){
    try {
      int orderNo = 0;

      long timeStamp = System.currentTimeMillis();
      String defaultPay  = "NO";

      Statement stmt = this.con.createStatement();
      stmt.executeUpdate(String.format("INSERT INTO INVOICENO (roomname, username, dateBooked, pay, cost, displaydate) VALUES ('%s','%s','%s','%s','%f','%s')",roomNo,userName,dateBooked, defaultPay, cost, dateToDisplay));
/*
      ResultSet rs = stmt.executeQuery(String.format("Select ORDERNO FROM INVOICENO"));

*/

      ResultSet rs = stmt.executeQuery(String.format("Select MAX(ORDERNO)  FROM INVOICENO"));

      System.out.println(rs + "2222");

      while(rs.next()){
        String orderNo1 = rs.getString(1);
        orderNo = Integer.parseInt(orderNo1);
        System.out.println("HAPPYYYY " + orderNo);

        System.out.println(orderNo+ " I finally pushed the order no");


      }
      System.out.println(orderNo+ " I finally pushed the order no");

      stmt.executeUpdate(String.format("INSERT INTO %s (orderno, username, dateBooked, cost, displaydate) VALUES ('%s','%s','%s','%f','%s')",roomNo,orderNo,userName,dateBooked, cost, dateToDisplay));
      stmt.executeUpdate(String.format("INSERT INTO %s (orderno, roomno, daysBooked, cost, displaydate) VALUES ('%d', '%s' ,'%s','%f','%s')",userName,orderNo, roomNo,dateBooked, cost, dateToDisplay));


    } catch (SQLException var6) {
      this.sqlExceptionHandler(var6);
    }

  }
  public void pushDateToUserTable(String userName, String roomNo, ArrayList<String> dateBooked){
    try {

      Timestamp timestamp = new Timestamp(System.currentTimeMillis());

      Statement stmt = this.con.createStatement();
      stmt.executeUpdate(String.format("INSERT INTO %s (orderno, roomno, daysBooked) VALUES ('%s', 12 ,'%s')",userName, roomNo,dateBooked));
    } catch (SQLException var6) {
      this.sqlExceptionHandler(var6);
    }

  }
  public String pushDateToRoom(String room, String userName, ArrayList<String> Datetobebook){
    try {
/*
      PreparedStatement stmt = this.con.prepareStatement("SELECT DATEBOOKED FROM ?");
*/
      HashMap<String, String[]> roomMatch = new HashMap<>();
      roomMatch.put("A",new String[]{"ROOMA101", "ROOMA102"});
      roomMatch.put("B",new String[]{"ROOMB103", "ROOMB104"});
      roomMatch.put("C",new String[]{"ROOMC105", "ROOMC106"});
      roomMatch.put("D",new String[]{"ROOMD107", "ROOMD108"});
      boolean matchfound = false;

        String [] roomListSelected  = roomMatch.get(room);
        roomLoop:
        for(String roomName: roomListSelected){
          /*stmt.setString(1,roomName);
          ResultSet rs = stmt.executeQuery();*/
          Statement stmt = this.con.createStatement();
          System.out.println("JARED LOOKING FOR ROOM NAME " + roomName + " else " + room);
          ResultSet rs = stmt.executeQuery("SELECT DATEBOOKED FROM " + roomName);

          ArrayList <String> roomAlreadyBooked = new ArrayList<>();
          while(rs.next()){
            String bookedListed = rs.getString("DateBooked");
            roomAlreadyBooked.addAll(parseString(bookedListed));
          }
          for(String day : Datetobebook)
          //for(int i =0 ; i<datetobebooked.size(); i++){   }
          {
           if(roomAlreadyBooked.contains(day)) {
             continue roomLoop;
           }
          }
          /*pushDate(userName, roomName, Datetobebook);*/
          return roomName;
        }

    } catch (SQLException var6) {
      this.sqlExceptionHandler(var6);
    }
    return null;

  }



  public void sqlExceptionHandler(SQLException error) {

    System.out.println("Standard Failure: " + error.getMessage());
  }
}