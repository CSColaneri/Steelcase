import java.util.*;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.mysql.cj.xdevapi.Statement;

// much of the methods are grabbed from 
// https://www.quickprogrammingtips.com/java/how-to-securely-store-passwords-in-java.html
// The encryption is the main reason for using this
public class Account {
  private String email = null;
  private String passEncrypted = null;
  private String salt = null;

  private Account(String email, String pword, String salt) {
    this.email = email;
    this.passEncrypted = pword;
    this.salt = salt;
  }

  public boolean validUser(String email) {
    return false;
  }

  public boolean validPassword(String email, String password) {
    return false;
  }

  public void resetPass(String email) {

  }

  /**
   * Author: Ethan B.
   * TODO: Put requirements on password. Maybe new method for it.
   * TODO: Use Account Object or make static.
   * maybe return Account or null.
   */
  public Account authenticateUser(String username, String password) {
    Connection conn = DBConnection.getConnection();
    PreparedStatement ps;
    String pHash;
    String salt;
    // try getting user's details to auth
    try {
      ps = conn.prepareStatement("SELECT * FROM Account where email like ?");
      ps.setString(1, username);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {// account exists in db.
        pHash = rs.getString("password_hash");
        salt = rs.getString("salt");
      } else {
        // no account with that email.
        return null;
      }
    } catch (SQLException e) {
      // TODO: Make log function.
      System.err.println("Failed to prepare a sql statement.");
      e.printStackTrace();
      return null;
    }

    try {
      String userPassHash = getEncryptedPassword(password, salt);
      if (pHash.equals(userPassHash)) {
        // maybe something goes here?
        return new Account(email, userPassHash, salt);
      }
      return null;
    } catch (Exception e) {
      // TODO: Make log function
      System.err.println("Failed to authenticate user.");
      e.printStackTrace();
      return null;
    }
  }

  public void signup(String email, String password) throws Exception {
    salt = getNewSalt();
    passEncrypted = getEncryptedPassword(password, salt);
    this.email = email;
    saveUser();
  }

  // Get a encrypted password using PBKDF2 hash algorithm
  public String getEncryptedPassword(String password, String salt) throws Exception {
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
  public String getNewSalt() throws Exception {
    // Don't use Random!
    SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
    // NIST recommends minimum 4 bytes. We use 8.
    byte[] salt = new byte[8];
    random.nextBytes(salt);
    return Base64.getEncoder().encodeToString(salt);
  }

  private void saveUser() {
    Statement stmt;
  }

  public void logout() {

  }

  public void changeEmail() {

  }
}
