import java.util.*;
import java.sql.*;

/**
 * A class for building and aiding database searches.
 */
public class Search {
    ArrayList<Filter> filters = new ArrayList<Filter>();

    /**
     * Unused constructor:  As largely a utility class, no constructor is strictly necessary.
     */
    protected Search()
    {}

    /**
     * Adds a list of filters, of the Filter utility class, to be used to search the database.
     * @param f
     */
    public void changeFilters(ArrayList<Filter> f)
    {
        filters = f;
    }

    public void addFilter(Filter f)
    {
        filters.add(f);
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

    //TODO: please use String.format instead of string concatenation
    public PreparedStatement buildStatement(Connection conn) throws Exception {
        int i = 0;
        String statement = "SELECT * FROM Course WHERE ";
        boolean hasCode = false;
        int codeSpot = 0;

        // if there are multiple filters
        if (filters.size() > 1) 
        {
            // concatenate all filters into one SQL query string.
            for (i = 0; i < filters.size() - 1; i++) 
            {
                // handle unique case for numerical data fields which SQL queries handle differently 
                if (filters.get(i).getParam().equals("code") || filters.get(i).getParam().equals("id")) 
                {
                    statement = statement + "" + filters.get(i).getParam() + " = ? AND ";
                    codeSpot = i;
                    hasCode = true;
                } 
                // otherwise build statement normally
                else 
                {
                    statement = statement + "" + filters.get(i).getParam() + " LIKE '%" + filters.get(i).getValue()
                    + "%' AND ";
                }
            }
        }

        // For individual filter requests, handle unique case where filtering by numerical data fields, 
        if(filters.size() != 0 && (filters.get(i).getParam().equals("code") || filters.get(i).getParam().equals("id")))
        {
            statement = statement + "" + filters.get(i).getParam() + " = ?";
            codeSpot = i;
            hasCode = true;
        }
        // otherwise handle regular filters
        else if(!filters.isEmpty())
        {
            statement = statement + "" + filters.get(i).getParam() + " LIKE '%" + filters.get(i).getValue() + "%'";
        }
        // otherwise return entire database - for use in paginated course catalog variant.
        else
        {
            statement = "SELECT * FROM Course";
        }

        try 
        {
            // TODO: Make log function

            //Create prepared statement using pre-established database connection
            PreparedStatement setdb = conn.prepareStatement(statement);

            //in unique case where user is searching numerical data field, pass numerical value to prepared statement
            if(hasCode)
            {
                setdb.setInt(1, Integer.parseInt(filters.get(codeSpot).getValue()));
            }

            // return prepared statement to be used in pulling information from the database.
            return setdb;
        }
        catch (SQLException e) 
        {
            // TODO: Log function
            System.err.println("Search Error");
            e.printStackTrace();
            // System.exit(1);
            return null;
        }
        catch(NumberFormatException e) {
            // TODO: Log function
            System.err.println("Code is not a number");
            e.printStackTrace();
            return null;
        }
    }

    public String searchCourses(Connection conn) {
        if (!filters.isEmpty()) {
            try {
                // Get previously built query
                PreparedStatement stmt = buildStatement(conn);

                //Create result set from data pulled by previously created query
                ResultSet courses = stmt.executeQuery();
                String s = "";
                try {
                    // Using the meta data from the resultset, find the number of columns
                    ResultSetMetaData rsmd = courses.getMetaData();
                    int columnsNumber = rsmd.getColumnCount();
                    // run through each column and get the column name from it, 
                    // formatting it into a presentable string of comma seperated values
                    for (int i = 1; i <= columnsNumber; i++) {
                        if (i > 1) {
                            s = s + (",");
                        }
                        s = s + rsmd.getColumnName(i);
                    }
                    s = s + "\n";

                    // run through each item in the database, and for each column
                    // get the data from the column, 
                    // formatting it into a presentable string of comma seperated values
                    while (courses.next()) {
                        for (int i = 1; i <= columnsNumber; i++) {
                            if (i > 1) {
                                s = s + (",  ");
                            }
                            s = s + courses.getString(i);
                        }
                        s = s + "\n";
                    }
                } catch (Exception e) {
                    e.printStackTrace();;
                }
                courses.close();
                stmt.close();
                return s;
            } catch (Exception e) {
                System.out.println("Error groking courses.");
                e.printStackTrace();
                // System.exit(1);//this quits the program
                return null;
            }
        }else{
            int checkNum = 50;
            int finalNum = 0;
            String s = "";

            try {
                PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM Course");

                ResultSet rs = stmt.executeQuery();

                rs.next();

                finalNum = rs.getInt(1);

                stmt.close();

                rs.close();

                stmt = conn.prepareStatement("SELECT * FROM Course");

                ResultSet courses = stmt.executeQuery();

                ResultSetMetaData rsmd = courses.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
//                for (int i = 1; i <= columnsNumber; i++) {
//                    if (i > 1) {
//                        s = s + (",  ");
//                    }
//                    s = s + rsmd.getColumnName(i);
//                }
              //  s = s + "\n";
                while (courses.next()) {
                    for (int i = 1; i <= columnsNumber; i++) {
                        if (i > 1) {
                            s = s + (",");
                        }
                    }
                    s = s + "~\n";
                }
                stmt.close();
                courses.close();
                return s;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public ArrayList<Course> searchCoursesC(Connection conn) {
        ArrayList<Course> alc = new ArrayList<Course>();
        try(PreparedStatement stmt = buildStatement(conn);
            ResultSet courses = stmt.executeQuery()) {
            while(courses.next()) {
                alc.add(new Course(courses));
            }
        } catch(Exception e) {
            // TODO: Log function
            System.err.println("Couldn't search for courses.");
            e.printStackTrace();
            return null;
        }
        return alc;
    }

    //input: date/time selection
    //output: list of course names from the database
    // TODO: Combine this function with the other search function.  Not a minimum requirement but it would be nice. 
    public static String searchByTime(String date, String start, String end) {
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
          StringBuilder courseString = new StringBuilder();
          for (String s : courseList) {
            courseString.append(s);
            courseString.append("\n");
          }
          getCourses.close();
          stmt.close();
          return courseString.toString();
        } catch (SQLException e) {
          System.out.println("Failed to connect.");
          e.printStackTrace();
          return(e.getMessage());
        }
      }

      public static void main(String[] args) {
        //Sample input to test
        //System.out.println(searchByTime("M", "18:30", "21:00"));

      }
}


