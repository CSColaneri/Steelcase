// import java.sql.DriverManager;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;

/**
 * TODO: Test
 */
public class DataSource {
  
  private static HikariConfig config = new HikariConfig();
  private static HikariDataSource ds;
  
  // private static Connection conn = null;

  // static initializer block.
  static {
    config.setJdbcUrl(String.format("jdbc:mysql:///%s", "steelcase_test"));
    config.setUsername("admin");
    config.setPassword("admin");
    config.addDataSourceProperty("socketFactory", "com.google.cloud.sql.mysql.SocketFactory");
    config.addDataSourceProperty("cloudSqlInstance", "ambient-scope-342219:us-central1:steelcase-db");
    config.addDataSourceProperty("ipTypes", "PUBLIC,PRIVATE");
    ds = new HikariDataSource(config);
  }

  private DataSource() /*throws SQLException*/ {
    // String user = System.getenv("DB_USERNAME");
    // String password = System.getenv("DB_PASSWORD");
    // String dbName = "steelcase_test";
    // String cloudSqlInstance = "ambient-scope-342219:us-central1:steelcase-db";
    
    // jdbc:mysql:///<DATABASE_NAME>?cloudSqlInstance=<INSTANCE_CONNECTION_NAME>&socketFactory=com.google.cloud.sql.mysql.SocketFactory&user=<MYSQL_USER_NAME>&password=<MYSQL_USER_PASSWORD>
    // String url = String.format("jdbc:mysql:///%s?cloudSqlInstance=%s"
    //     + "&socketFactory=com.google.cloud.sql.mysql.SocketFactory&user=%s&password=%s",
    //     dbName, cloudSqlInstance, user, password);
    // conn = DriverManager.getConnection(url);
  }

  public static Connection getConnection() throws SQLException {
    return ds.getConnection();
    // if(conn == null) {
    //   try {
    //     new DataSource();
    //   } catch(SQLException e) {
    //     // TODO: Make log function.
    //     System.err.println("Failed to establish a connection with the database.");
    //     e.printStackTrace();
    //   }
    // }
    // return conn;
  }

}
