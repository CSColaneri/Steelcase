/**
 * Author: Ethan Brown
 * Short Descr: Test Connecting to and Authenticating with Google
 *              Cloud SQL Instances.
 * Full Descr:  This file is for experimenting and testing different
 *              connection authentication strategies to google cloud
 *              sql instances. For example: at the moment this will
 *              use end user credentials, but google recommends
 *              using service accounts. If anyone cannot succesfully
 *              run this class, contact me (Ethan) and we'll figure
 *              it out. 
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
// import java.io.FileNotFoundException;
// import java.io.FileInputStream;
// import java.io.IOException;
// import java.io.InputStream;
// import java.security.GeneralSecurityException;

// import com.google.auth.oauth2.GoogleCredentials;
// import com.google.common.collect.Lists;
// import com.google.api.client.auth.oauth2.Credential;
// import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
// import com.google.api.client.http.HttpTransport;
// import com.google.api.client.json.Json;
// import com.google.api.client.json.JsonFactory;
// import com.google.api.services.sqladmin.SQLAdmin;
// import com.google.cloud.sql.mysql.SocketFactory;

public class DBConnectionTest {
  public static Connection conn;

  // private final static String SA_KEY_PATH = "../ambient-scope-342219-3894e6e0fc09.json";

  public static void main(String[] args) throws Exception {
    // Console console = System.console();
    // if(console == null) {
    //   System.out.println("Couldn't get console instance. Closing...");
    //   return;
    // }
    
    // String[] acceptedParams = "".split(",");
    Properties info = new Properties();    
    // String user, password;
    // char[] pword;
    // boolean loggedIn = false;

    // while(!loggedIn) {
      // user = console.readLine("Username: ");
      // pword = console.readPassword("Password: ");
      // password = new String(pword);
      // info.put("user", user);
      // info.put("password", password);
      String user = "admin";
      String password = "admin";
      info.put("user", "admin");
      info.put("password", "admin");
      String dbName = "steelcase";
      String cloudSqlInstance = "ambient-scope-342219:us-central1:steelcase-db";
      // GoogleCredentials apiCredentials = GoogleCredentials.fromStream(new FileInputStream(SA_KEY_PATH))
      //   .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
      
      // SQLAdmin adminApiClient = createAdminApiClient();

      // jdbc:mysql:///<DATABASE_NAME>?cloudSqlInstance=<INSTANCE_CONNECTION_NAME>&socketFactory=com.google.cloud.sql.mysql.SocketFactory&user=<MYSQL_USER_NAME>&password=<MYSQL_USER_PASSWORD>
      String url = String.format("jdbc:mysql:///%s?cloudSqlInstance=%s&socketFactory=com.google.cloud.sql.mysql.SocketFactory&user=%s&password=%s", dbName,cloudSqlInstance, user, password);
      // String dmConn = String.format("jdbc:mysql://%s:%d/",hostname,port);
      System.out.println("This test whether or not this machine can connect "
        + "to the Google Cloud SQL instance and database.");
      try {
        conn = DriverManager.getConnection(url);
        // conn = DriverManager.getConnection(url, info);
        // conn = DriverManager.getConnection(dmConn, info);
        // loggedIn = true;
      } catch(SQLException e) {
        System.out.println("Failed to connect.");
        e.printStackTrace();
        // continue;
      }
    // }
    System.out.println("Connection successful.");

    try {
      PreparedStatement setdb = conn.prepareStatement("use steelcase");
      setdb.execute();
      setdb.close();
      PreparedStatement showHostname = conn.prepareStatement("SELECT * FROM Course");
      ResultSet rs1 = showHostname.executeQuery();
      // Tables have no data atm
      // if(rs1.next()) {
      //   System.out.println("Course column 0: " + rs1.getInt(1));
      //   rs1.close();
      //   showHostname.close();
      // } else {
      //   throw new Exception("Something went wrong.");
      // }
      System.out.println("This concludes the test. Goodbye.");
      rs1.close();
      showHostname.close();
    } catch(Exception e) {
      System.out.println("whaddaFrontdoor happen?");
      e.printStackTrace();
    }
    return;
  }

  // private static SQLAdmin createAdminApiClient(Credential credential) {
  //   HttpTransport httpTransport;
  //   try {
  //       httpTransport = GoogleNetHttpTransport.newTrustedTransport();
  //   } catch (GeneralSecurityException | IOException e) {
  //       throw new RuntimeException("Unable to initialize HTTP transport", e);
  //   }
  //   InputStream in;
  //   try {
  //     in = new FileInputStream(SA_KEY_PATH);
  //   } catch(FileNotFoundException e) {
  //     System.err.println("Couldn't find the Service Account Key file. "
  //       + "Make sure it's located in the project directory (eg. Steelcase/<key_file_here>.json)");
  //     e.printStackTrace();
  //   } 
  //   JsonFactory jsf = 
  //   return new SQLAdmin.Builder(httpTransport, JacksonFactory.getDefaultInstance(), credential)
  //           .setApplicationName("Cloud SQL Example")
  //           .build();
  // }
}
