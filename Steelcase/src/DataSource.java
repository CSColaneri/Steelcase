// import java.sql.DriverManager;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;

/**
 * Code dapated from: https://www.baeldung.com/hikaricp
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

  private DataSource() {}

  public static Connection getConnection() throws SQLException {
    Connection conn = ds.getConnection();
    conn.prepareStatement("use steelcase_test").execute();
    return conn;
  }

}
