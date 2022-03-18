import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.util.ArrayList;


public class UploadToDB {
  public static void main(String[] args) throws Exception {
    Connection conn;
    String user = "admin";
    String password = "admin";
    String dbName = "steelcase_test";
    String cloudSqlInstance = "ambient-scope-342219:us-central1:steelcase-db";
    
    // jdbc:mysql:///<DATABASE_NAME>?cloudSqlInstance=<INSTANCE_CONNECTION_NAME>&socketFactory=com.google.cloud.sql.mysql.SocketFactory&user=<MYSQL_USER_NAME>&password=<MYSQL_USER_PASSWORD>
    String url = String.format("jdbc:mysql:///%s?cloudSqlInstance=%s"
    + "&socketFactory=com.google.cloud.sql.mysql.SocketFactory&user=%s&password=%s",
    dbName, cloudSqlInstance, user, password);


    try {
      conn = DriverManager.getConnection(url);
      System.out.println("Connection successful.");
      PreparedStatement setdb = conn.prepareStatement("use " + dbName);
      setdb.execute();
      setdb.close();
    } catch (SQLException e) {
      System.out.println("Failed to connect.");
      e.printStackTrace();
      System.exit(1);
      return;
    }
    
    // initial data
    // insert_initial_data(conn);
    
    // initial prereq data
    // insert_prereq_data(conn);
    
    // Insert some test accounts
    
    // Insert some test Schedules
  }

  /**
   * Takes a table's name and a list of attributes and returns a simple
   * MySQL insert statement.
   * @param table The table to insert into
   * @param attrs array of Strings denoting the attributes to insert.
   * @return "INSERT INTO table_name(attr1, attr2, attr3, ...) VALUES(?, ?, ?, ... )"
   * @throws IndexOutOfBoundsException 
   */
  public static String createInsert(String table, String[] attrs) throws IndexOutOfBoundsException {
    System.out.println("UploadToDB.createInsert()");
    StringBuilder fields = new StringBuilder();
    for (String string : attrs) {
      fields.append(string + ", ");
    }
    fields.replace(fields.length()-2, fields.length(), "");
    StringBuilder sqlInsertsBuilder = new StringBuilder();
    for(int i = 0; i < attrs.length; ++i) {
      sqlInsertsBuilder.append("?,");
    }
    sqlInsertsBuilder.replace((attrs.length)*2-1, (attrs.length)*2, "");
    return String.format("INSERT INTO %s(%s) VALUES (%s)", table, fields.toString(), sqlInsertsBuilder.toString());
  }

  /**
   * Adds the sql statement to a batch of identical statements,
   * but with the specfied values.
   * @param ps
   * @param line
   * @throws SQLException
   */
  private static void addToInitialInsertBatch(PreparedStatement ps, String csv_line) throws SQLException {
    System.out.println("UploadToDB.addToInitialInsertBatch()");
    // there are many double spaces, replace them with single spaces
    String line = csv_line.replace("  ", " ");
    String[] arrayLine = line.split(",");
    /*
      Go through each string. If it contains a quote but isn't
      at the end of the string, mark it as split up. 
      For each subsequent string that doesn't end in a quote,
      add it to the first one and null that entry. Quit after
      finding one that ends in a quote, then add it to the first and null.
    
      EG: {List, "Eggs, Butter, and Milk", ingredients} will go from
      {
        List,
        "Eggs,
        Butter,
        and Milk",
        ingredients
      }
      to:
      {
        List,
        "Eggs, Butter, and Milk",
        ingredients
      }
    */
    for(int i = 0; i < arrayLine.length; ++i) {
      // has quotation but doesn't end with it
      if(arrayLine[i].contains("\"") && !arrayLine[i].endsWith("\"")) {
        // add the next quoted strings
        int j = i+1;
        // doesn't end in " and not at end of array
        while(!arrayLine[j].endsWith("\"") && j < arrayLine.length) {
          arrayLine[i] += ", " + arrayLine[j];
          arrayLine[j] = null;
          j++;
        }
        // add the string that closes the quotation
        arrayLine[i] += arrayLine[j];
        arrayLine[j] = null;
        
        // point past the null
        i = j;
      }
    }
    // get rid of nulls
    ArrayList<String> thing = new ArrayList<>();
    for(int i = 0; i < arrayLine.length; ++i) {
      if(arrayLine[i] != null) {
        thing.add(arrayLine[i]);
      }
    }

    // Logging
    // System.out.println("After: " + thing.toString());

    // split code into code, dep, and section
    String csvCode = thing.get(0);
    String dep = csvCode.split(" ")[0];
    String code = csvCode.split(" ")[1];
    String section = csvCode.split(" ")[2];

    ArrayList<String> values = new ArrayList<>();

    values.add(code);
    values.add(dep);

    for(int i = 1; i < thing.size(); ++i) {
      values.add(thing.get(i));
    }
    values.add(section);
    // 3 null values here:
    // prereqID
    // description
    // professor

    setInitialSqlValues(values, ps);
    ps.addBatch();
  }

  private static void setInitialSqlValues(ArrayList<String> values, PreparedStatement ps) throws SQLException {
    System.out.println("UploadToDB.setInitialSqlValues()");
    int multiplier[] = {3600000, 60000, 100};
    String timeString;
    String splits[];
    long time;

    try {
      ps.setInt(1, Integer.valueOf(values.get(0))); // code
      ps.setString(2, values.get(1));     // dep
      ps.setString(3, values.get(2));     // short_title
      ps.setString(4, values.get(3));     // long_title
      // ps.setTime(5, value made below); // begin_time
      // ps.setTime(6, value made below); // end_time
      ps.setString(7, values.get(6));     // day
      ps.setString(8, values.get(7));     // building
      ps.setString(9, values.get(8));     // room
      if(values.get(9).equals("NULL")) {  // enrollment
        ps.setNull(10, Types.SMALLINT);  
      } else {
        ps.setInt(10, Integer.valueOf(values.get(9)));
      }
      if(values.get(10).equals("NULL")) { // capacity
        ps.setNull(11, Types.SMALLINT);    
      } else {
        ps.setInt(11, Integer.valueOf(values.get(10)));
      }
      ps.setString(12, values.get(11));   // section
      ps.setNull(13, Types.BIGINT);       // prereqID
      ps.setNull(14, Types.VARCHAR);      // description
      ps.setNull(15, Types.VARCHAR);      // professor
      if(!values.get(4).equals("NULL")) { // begin time
        timeString = values.get(4);
        splits = timeString.split(":");
        time = 0;
        for (int x = 0; x < splits.length; x++) {
          time += (Integer.parseInt(splits[x]) * multiplier[x]);
        }
        ps.setTime(5, new Time(time));
      } else {
        ps.setNull(5, Types.TIME);
      }
      if(!values.get(5).equals("NULL")) {// end_time
        timeString = values.get(5);
        splits = timeString.split(":");
        time = 0;
        for (int x = 0; x < splits.length; x++) {
            time += (Integer.parseInt(splits[x]) * multiplier[x]);
        }
        ps.setTime(6, new Time(time));
      } else {
        ps.setNull(6, Types.TIME);
      }
    }
    catch (NumberFormatException e) {
      for(String s : values) {
        System.out.println(s);
      }
      e.printStackTrace();
      System.exit(1);
    }
  }

  private static void insert_initial_data(Connection conn) throws SQLException, FileNotFoundException, IOException {
    System.out.println("UploadToDB.insert_initial_data()");
    String table = "Course";
    String[] attributes = {
      "code",
      "department",
      "short_title",
      "long_title",
      "begin_time",
      "end_time",
      "day",
      "building",
      "room",
      "enrollment",
      "capacity",
      "section",
      "prereqID",
      "description",
      "professor"
    };
    
    String sql = createInsert(table, attributes);
    // System.out.println(sql);
    PreparedStatement ps = conn.prepareStatement(sql);

    BufferedReader br = new BufferedReader(new FileReader("CourseDB_WithFictionalCapacities.csv"));
    br.readLine();
    String line;
    // use PreparedStatement.addBatch() to do multiple insert statements while reading in from file. 
    // 1 Add Courses.
    int counter = 0;
    while((line = br.readLine()) != null) {
      addToInitialInsertBatch(ps, line);
      ++counter;
      if(counter % 100 == 0) {
        ps.executeBatch();
      }
    }
    br.close();
    ps.executeBatch();
    ps.close();
  }

  private static void insert_prereq_data(Connection conn) throws SQLException, FileNotFoundException, IOException {
    System.out.println("UploadToDB.insert_prereq_data()");
    String[] attrs = {"courseCode", "courseDep", "prereqCode", "prereqDep"};
    String sql = createInsert("Prereq", attrs);
    PreparedStatement ps = conn.prepareStatement(sql);

    BufferedReader br = new BufferedReader(new FileReader("PrereqsDB.csv"));
    br.readLine();
    String line;
    int counter = 0;
    while((line = br.readLine()) != null) {
      //line = "ACCT 200,ACCT 201"
      //codes[0] = "ACCT 200"
      //codes[1] = "ACCT 201"
      // codes[0].split(" ")[0] = "ACCT"
      // codes[0].split(" ")[1] = "200"
      // codes[1].split(" ")[0] = "ACCT"
      // codes[1].split(" ")[1] = "201"
      String[] codes = line.split(",");
      ps.setInt(1, Integer.valueOf(codes[0].split(" ")[1]));//courseCode
      ps.setString(2, codes[0].split(" ")[0]);              //courseDep
      ps.setInt(3, Integer.valueOf(codes[1].split(" ")[1]));//pereqCode
      ps.setString(4, codes[1].split(" ")[0]);              //prereqDep
      ps.addBatch();
      counter++;
      if(counter % 100 == 0) {
        ps.executeBatch();
      }
    }
    ps.executeBatch();
    ps.close();
    br.close();
  }
}