import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class DBConnection {
  
  private static Connection conn = null;

  private DBConnection() throws SQLException {
    String user = "admin";
    String password = "admin";
    String dbName = "steelcase";
    String cloudSqlInstance = "ambient-scope-342219:us-central1:steelcase-db";
    
    // jdbc:mysql:///<DATABASE_NAME>?cloudSqlInstance=<INSTANCE_CONNECTION_NAME>&socketFactory=com.google.cloud.sql.mysql.SocketFactory&user=<MYSQL_USER_NAME>&password=<MYSQL_USER_PASSWORD>
    String url = String.format("jdbc:mysql:///%s?cloudSqlInstance=%s"
        + "&socketFactory=com.google.cloud.sql.mysql.SocketFactory&user=%s&password=%s",
        dbName, cloudSqlInstance, user, password);
    conn = DriverManager.getConnection(url);
  }

  public static Connection getConnection() {
    if(conn == null) {
      try {
        new DBConnection();
      } catch(SQLException e) {
        // TODO: Make log function.
        System.err.println("Failed to establish a connection with the database.");
        e.printStackTrace();
      }
    }
    return conn;
  }

}
