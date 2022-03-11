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


public class DBConnectionTest {
  public static Connection conn;

  private final static String SA_ENV = "GOOGLE_APPLICATION_CREDENTIALS";
  private final static String SA_KEY_ENV = System.getenv(SA_ENV);

  public static void main(String[] args) throws Exception {
    if (SA_KEY_ENV == null) {
      System.err.printf("The environment variable \"%s\" must be set to the " 
        + "location of the service account json key.\n", SA_ENV);
      System.exit(1);
    }
    Properties info = new Properties();

    String user = "admin";
    String password = "admin";
    info.put("user", user);
    info.put("password", password);
    String dbName = "steelcase";
    String cloudSqlInstance = "ambient-scope-342219:us-central1:steelcase-db";
    
    // jdbc:mysql:///<DATABASE_NAME>?cloudSqlInstance=<INSTANCE_CONNECTION_NAME>&socketFactory=com.google.cloud.sql.mysql.SocketFactory&user=<MYSQL_USER_NAME>&password=<MYSQL_USER_PASSWORD>
    String url = String.format("jdbc:mysql:///%s?cloudSqlInstance=%s"
        + "&socketFactory=com.google.cloud.sql.mysql.SocketFactory&user=%s&password=%s",
        dbName, cloudSqlInstance, user, password);
    
    System.out.println("This test whether or not this machine can connect " +
        "to a mysql database hosted on the cloud.");
    try {
      conn = DriverManager.getConnection(url);
      System.out.println("Connection successful.");
    } catch (SQLException e) {
      System.out.println("Failed to connect.");
      e.printStackTrace();
    }

    try {
      PreparedStatement setdb = conn.prepareStatement("use steelcase");
      setdb.execute();
      setdb.close();
      PreparedStatement showHostname = conn.prepareStatement("SELECT * FROM Course");
      ResultSet rs1 = showHostname.executeQuery();
      // Tables have no data atm
      // if(rs1.next()) {
      // System.out.println("Course column 0: " + rs1.getInt(1));
      // rs1.close();
      // showHostname.close();
      // } else {
      // throw new Exception("Something went wrong.");
      // }
      rs1.close();
      showHostname.close();
    } catch (Exception e) {
      System.out.println("Something exploded");
      e.printStackTrace();
    }
    System.out.println("This concludes the test. Goodbye.");
    return;
  }
}
