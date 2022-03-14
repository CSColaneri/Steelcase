import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


// TODO: Add section to Course db, split csv code into department,
public class UploadToDB {
  public static void main(String[] args) throws Exception {
    Connection conn;
    String user = "admin";
    String password = "admin";
    String dbName = "steelcase";
    String cloudSqlInstance = "ambient-scope-342219:us-central1:steelcase-db";
    
    // jdbc:mysql:///<DATABASE_NAME>?cloudSqlInstance=<INSTANCE_CONNECTION_NAME>&socketFactory=com.google.cloud.sql.mysql.SocketFactory&user=<MYSQL_USER_NAME>&password=<MYSQL_USER_PASSWORD>
    String url = String.format("jdbc:mysql:///%s?cloudSqlInstance=%s"
    + "&socketFactory=com.google.cloud.sql.mysql.SocketFactory&user=%s&password=%s",
    dbName, cloudSqlInstance, user, password);


    try {
      conn = DriverManager.getConnection(url);
      System.out.println("Connection successful.");
      PreparedStatement setdb = conn.prepareStatement("use steelcase");
      setdb.execute();
      setdb.close();
    } catch (SQLException e) {
      System.out.println("Failed to connect.");
      e.printStackTrace();
      System.exit(1);
      return;
    }


    // use steelcase_test database
    // Start with courses, then do their prereqs.
    BufferedReader br = new BufferedReader(new FileReader("CourseDB_WithFictionalCapacities.csv"));
    String line;
    List<List<String>> records = new ArrayList<>();
    while((line = br.readLine()) != null) {
      int lineLen = line.split(",").length;
      String first = line.split(",")[0];
      
      first.split(" ");

      String[] values = new String[lineLen+1];
      records.add(Arrays.asList(values));
    }
    br.close();
    // each record is a line in the csv.
    String insert = "INSERT INTO %s(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s) VALUES ";
    // int numAttrs = 13;
    String sqlSatement = String.format(insert,
      "Course", "code", "department", "building", "name", "description", 
      "professor", "day", "begin_time", "end_time", "capacity", "enrolled", 
      "prereqID");

    for(int i = 0; i < records.size(); ++i) {
      insert = insert.concat("(");
      for(int j = 0; j < records.get(i).size(); ++j) {
        if(j != records.get(i).size()-1) {
          insert = insert.concat("?,");
        } else {
          insert = insert.concat("?");
        }
      }
      if(i != records.size()-1) {
        insert = insert.concat("), ");
      } else {
        insert = insert.concat(");");
      }
    }

    int multiplier[] = {3600000, 60000, 100};
    String timeString;
    String splits[];
    long time;
    PreparedStatement addCourses = conn.prepareStatement(sqlSatement);
    int i = 1;
    int j = 0;
    for(List<String> record : records) {
      addCourses.setInt(i++, Integer.valueOf(record.get(j++)));
      addCourses.setString(i++, record.get(j++));
      addCourses.setString(i++, record.get(j++));
      addCourses.setString(i++, record.get(j++));
      addCourses.setString(i++, record.get(j++));
      addCourses.setString(i++, record.get(j++));
      addCourses.setString(i++, record.get(j++));

      timeString = record.get(j++);
      splits = timeString.split(":");
      time = 0;
      for (int x = 0; x < splits.length; x++) {
          time += (Integer.parseInt(splits[x]) * multiplier[x]);
      }
      addCourses.setTimestamp(i++, new Timestamp(time));

      timeString = record.get(j++);
      splits = timeString.split(":");
      time = 0;
      for (int x = 0; x < splits.length; x++) {
          time += (Integer.parseInt(splits[x]) * multiplier[x]);
      }
      addCourses.setTimestamp(i++, new Timestamp(time));
      addCourses.setInt(i++, Integer.valueOf(record.get(j++)));
      addCourses.setInt(i++, Integer.valueOf(record.get(j++)));
      addCourses.setNull(i++, Types.BIGINT);
      System.out.printf("i: %d, j: %d\n", i, j);
    }


    // File prereqs = new File("PrereqsDB.csv");
    
    
    // Insert some test accounts
    
    // Insert some test Schedules
  }
}
