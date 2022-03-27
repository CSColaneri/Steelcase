import java.util.*;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

// much of the methods are grabbed from 
// https://www.quickprogrammingtips.com/java/how-to-securely-store-passwords-in-java.html
// The encryption is the main reason for using this
public class Account {
  private String email;
  private String passEncrypted;
  private String salt;

  private Account(String email, String pword, String salt) {
    this.setEmail(email);
    this.setPassEncrypted(pword);
    this.salt = salt;
  }

  public String getPassEncrypted() {
    return passEncrypted;
  }

  private void setPassEncrypted(String passEncrypted) {
    this.passEncrypted = passEncrypted;
  }

  public String getEmail() {
    return email;
  }

  private void setEmail(String email) {
    this.email = email;
  }

  //same as authenticateUser?
  public boolean validUser(String email) {
    return false;
  }

  public boolean validPassword(String email, String password) {
    return false;
  }

  //not vital, save for last
  public void resetPass(String email) {

  }

  public static Account login(String email, String password) {
    Account acc;
    //get account info with that email
    if((acc = getAccountDetails(email)) != null) {
      //compare retrieved account password to the given one
      if((acc = authenticateUser(acc, password)) != null) {
        // passwords and email match, return the account
        return acc;
      }
    }
    //either no email or the passwords don't match
    return null;
  }

  /**
   * Retrieve account info that matches the given email.
   * @param email the email the user is attempting to log in as.
   * @return null if there's no account matching the email, or 
   * An Account object containing the email, password hash, and 
   * salt from the database.
   */
  private static Account getAccountDetails(String email) {
    String sql = "SELECT * FROM Account where email like ?";
    String pHash;
    String salt;
    try(Connection conn = DataSource.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);) {
      ps.setString(1, email);
      ResultSet rs = ps.executeQuery();
      if(rs.next()) {// account exists in db.
        pHash = rs.getString("password_hash");
        salt = rs.getString("salt");
        return new Account(email, pHash, salt);
      }
      rs.close();
    } catch (SQLException e) {
      // TODO: Make log function.
      System.err.println("Failed to get Account Details.");
      e.printStackTrace();
      return null;
    }
    // no account with that email.
    return null;
  }

  /**
   * Checks given password against stored Account's password and returns
   * true or false
   * Adapted from: https://www.quickprogrammingtips.com/java/how-to-securely-store-passwords-in-java.html
   * @param account An account from the db whose email matches what the user gave.
   * @param password The password given by the user logging in.
   * @return True if password matches, false if not.
   */
  public static Account authenticateUser(Account account, String password) {
    // automatically releases connection and closes ps at end or exception
    try {
      String userPassHash = getEncryptedPassword(password, account.salt);
      if (account.getPassEncrypted().equals(userPassHash)) {
        // credentials are valid
        return account;
      }
    } catch (Exception e) {
      // TODO: Make log function
      System.err.println("Failed to authenticate user.");
      e.printStackTrace();
      return null;
    }
    //credentials are not valid
    return null;
  }

  // TODO: Put requirements on password. Maybe new method for it.
  /**
   * Adapted from:
   * https://www.quickprogrammingtips.com/java/how-to-securely-store-passwords-in-java.html
   * @param email User's inputted email.
   * @param password User's inputted password.
   * @param sched User's schedule
   * @return Null if email already in use, or an Account object representing the
   * user's account
   * @throws Exception If something goes wrong while checking for a duplicate email
   */
  public static Account signup(String email, String password, Schedule sched) throws Exception {
    // check to see if the email already exists before any expensive hashing.
    try(Connection conn = DataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT email FROM Account WHERE email = ?")) {
      ps.setString(1, email);
      if(ps.executeQuery().next()) {//if the email already exists, just return null now.
        // System.out.println("Email already in use.");
        return null;
      }
    } catch(Exception e) {
      //on any exception, couldn't check if email already exists. return null.
      //TODO: Make logging function
      System.err.println("Something went wrong, please try again later.");
      e.printStackTrace();
      throw new Exception("Couldn't check for duplicate emails.");
    }

    // At this point, email not in use
    String salt = getNewSalt();
    String encryptedPassword = getEncryptedPassword(password, salt);
    Account account = new Account(email, encryptedPassword, salt);
    return account.saveUser(sched);
  }

  // Get a encrypted password using PBKDF2 hash algorithm
  /**
   * Adapted from:
   * https://www.quickprogrammingtips.com/java/how-to-securely-store-passwords-in-java.html
   * @param password
   * @param salt
   * @return
   * @throws Exception
   */
  public static String getEncryptedPassword(String password, String salt) throws Exception {
    String algorithm = "PBKDF2WithHmacSHA1";
    int derivedKeyLength = 160; // for SHA1
    int iterations = 20000; // NIST specifies 10000

    byte[] saltBytes = Base64.getDecoder().decode(salt);
    KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, iterations, derivedKeyLength);
    SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);

    byte[] encBytes = f.generateSecret(spec).getEncoded();
    return Base64.getEncoder().encodeToString(encBytes);
  }

  // Returns base64 encoded salt
  /**
   * Adapted from:
   * https://www.quickprogrammingtips.com/java/how-to-securely-store-passwords-in-java.html
   * @return
   * @throws Exception
   */
  public static String getNewSalt() throws Exception {
    // Don't use Random!
    SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
    // NIST recommends minimum 4 bytes. We use 8.
    byte[] salt = new byte[8];
    random.nextBytes(salt);
    return Base64.getEncoder().encodeToString(salt);
  }
  
  /**
   * Name comes from 
   * https://www.quickprogrammingtips.com/java/how-to-securely-store-passwords-in-java.html,
   * but all contents are original
   * 
   * @param classes
   */
  private Account saveUser(Schedule classes) {
	  String acc = "insert into Account(email, password_hash, salt) values(?, ?, ?)";
	  String sch = "insert into Schedule(email, courseID) values(?, ?)";
	  try(Connection conn = DataSource.getConnection();
		  PreparedStatement ps1 = conn.prepareStatement(acc);
		  PreparedStatement ps2 = conn.prepareStatement(sch);) {
		  
      ps1.setString(1, email);
      ps1.setString(2, passEncrypted);
      ps1.setString(3, salt);
      ps1.execute();
      ps2.setString(1, email);
      // probably doesn't work
      for(int i = 0; i < classes.getSchedule().size(); i++) {
        ps2.setInt(2, classes.getSchedule().get(i).getID());
        ps2.execute();
      }
	  }
	  catch (SQLException s) {
      // if the account succeeds but the schedule fails, the account
      // will still be in there. This is good
      // TODO: make log function
		  System.err.println("Failed to add new account");
      s.printStackTrace();
	  }
    return this;
  }

  public void logout() {

  }

  public void changeEmail() {

  }

  // public static void main(String[] args) throws Exception {
  //   String salt = "cuPMHgO5LJg=";
  //   String password = "123456";   <---- password
  //   String email = "testEmail@gmail.com";  <-----
  //   System.out.println(salt);
  //   System.out.println(getEncryptedPassword(password, salt));
  // }
}
