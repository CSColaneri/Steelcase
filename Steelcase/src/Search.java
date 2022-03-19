import java.util.*;
import java.sql.*;


public class Search {
    ArrayList<Filter> filters = new ArrayList<Filter>();


    protected Search()
    {}

    public void changeFilters(ArrayList<Filter> f)
    {
        filters = f;
    }

    public ArrayList<Filter> getFilters()
    {
        return filters;
    }

    public String printFilters()
    {
        String s = "";
        for(int i = 0; i < filters.size(); i++)
        {
            s = s + filters.get(i).toString() + "\n";
        }
        return s;
    }

    public PreparedStatement buildStatement() throws Exception
    {
        Connection conn;
        String user = "admin";
        String password = "admin";
        String dbName = "steelcase";
        String cloudSqlInstance = "ambient-scope-342219:us-central1:steelcase-db";
        int i = 0;

        // jdbc:mysql:///<DATABASE_NAME>?cloudSqlInstance=<INSTANCE_CONNECTION_NAME>&socketFactory=com.google.cloud.sql.mysql.SocketFactory&user=<MYSQL_USER_NAME>&password=<MYSQL_USER_PASSWORD>
        String url = String.format("jdbc:mysql:///%s?cloudSqlInstance=%s"
            + "&socketFactory=com.google.cloud.sql.mysql.SocketFactory&user=%s&password=%s",
            dbName, cloudSqlInstance, user, password);
        String statement = "SELECT * FROM CLASSES WHERE ";

        if(filters.size() > 1)
        {
            for(i = 0; i < filters.size() - 1; i++)
            {
                statement = statement + "" + filters.get(i).getParam() + " IN " + filters.get(i).getValue() + " AND ";
            }
        }

        statement = statement + "" + filters.get(i).getParam() + " IN " + filters.get(i).getValue();

        try 
        {
            conn = DriverManager.getConnection(url);
            System.out.println("Connection successful.");
            PreparedStatement setdb = conn.prepareStatement(statement);
            return setdb;
        }
        catch (SQLException e) 
        {
            System.out.println("Failed to connect.");
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    public ArrayList<Course> searchCourses()
    {
        ArrayList<Course> c = new ArrayList<Course>();
        
        

        return c;
    }

    //input: date/time selection
    //output: list of course names from the database
    public static void searchByTime(String date, String start, String end) {
      
      

      int multiplier[] = {3600000, 60000};
      String startString = start; //read in string from user
      String endString = end;
      String splits[]; //array of strings split by colons

      long startTime = 0;
      long endTime = 0;
      
      splits = startString.split(":");
      for (int x = 0; x < splits.length; x++) {
              startTime += (Integer.parseInt(splits[x]) * multiplier[x]); 
              //converts to milliseconds
      }
      

      splits = endString.split(":");
      for (int x = 0; x < splits.length; x++) {
              endTime += (Integer.parseInt(splits[x]) * multiplier[x]);
      }

      try {
        Connection conn = DataSource.getConnection();
        ArrayList<String> courseList = new ArrayList<>();

        PreparedStatement stmt = conn.prepareStatement(
          "SELECT long_title FROM Course WHERE day = ? AND begin_time = ? AND end_time = ?");
          stmt.setString(1, date);
          stmt.setTime(2, new Time(startTime));
          stmt.setTime(3, new Time(endTime));
          ResultSet getCourses = stmt.executeQuery();
          while (getCourses.next()) {
            int row = 1;
            courseList.add(getCourses.getString(row));
            row++;
          }
          System.out.print(courseList);
          getCourses.close();
          stmt.close();
        } catch (SQLException e) {
          System.out.println("Failed to connect.");
          e.printStackTrace();
        }
      }

      public static void main(String[] args) {
        //Sample input to test
        //I will be changing this to user input once my DB is fixed and I have tested
        searchByTime("MWF", "14:00", "15:00");
      }


    }


