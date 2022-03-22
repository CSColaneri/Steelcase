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

    //todo: please use String.format instead of string concatenation
    public PreparedStatement buildStatement(Connection conn) throws Exception {
        int i = 0;
        String statement = "SELECT * FROM Course WHERE ";
        boolean hasCode = false;
        int codeSpot = 0;

        if (filters.size() > 1) 
        {
            for (i = 0; i < filters.size() - 1; i++) 
            {
                if (filters.get(i).getParam().equals("code")) 
                {
                    statement = statement + "" + filters.get(i).getParam() + " = ? AND";
                    codeSpot = i;
                    hasCode = true;
                } else if (filters.get(i).getParam().equals("begin_time") || filters.get(i).getParam().equals("end_time")) {
                    statement = statement + "" + filters.get(i).getParam() + " = " + filters.get(i).getValue() + " ";

                }
                else 
                {
                    statement = statement + "" + filters.get(i).getParam() + " LIKE '%" + filters.get(i).getValue()
                    + "%' AND ";
                }
            }
        }

        if(filters.get(i).getParam().equals("code"))
        {
            statement = statement + "" + filters.get(i).getParam() + " = ?";
            codeSpot = i;
            hasCode = true;
        }
        else
        {
            statement = statement + "" + filters.get(i).getParam() + " LIKE '%" + filters.get(i).getValue() + "%'";
        }

        try 
        {
            PreparedStatement setdb = conn.prepareStatement(statement);
            if(hasCode)
            {
                setdb.setInt(i, Integer.parseInt(filters.get(codeSpot).getValue()));
            }
            return setdb;
        }
        catch (SQLException e) 
        {
            System.out.println("Error");
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    public String searchCourses(Connection conn)
    {
        try
        {
            PreparedStatement stmt = buildStatement(conn);
            ResultSet courses = stmt.executeQuery();
            String s = "";
            try
            {
                ResultSetMetaData rsmd = courses.getMetaData();
                int columnsNumber = rsmd.getColumnCount();
                for(int i = 1; i <= columnsNumber; i++)
                {
                    if (i > 1)
                    {
                        s = s + (",  ");
                    }
                    s = s + rsmd.getColumnName(i);
                }
                System.out.println("");
                while (courses.next()) 
                {
                    for (int i = 1; i <= columnsNumber; i++) 
                    {
                        if (i > 1)
                        {
                            s = s + (",  ");
                        }
                        s = s + courses.getString(i);
                    }
                    s = s + "\n";
                }
            }
            catch (Exception e)
            {
                System.out.println(e);
            }
            courses.close();
            stmt.close();
            return s;
        }
        catch (Exception e)
        {
            System.out.println("Error groking courses.");
            e.printStackTrace();
            System.exit(1);
            return null;
        }
    }

    //input: date/time selection
    //output: list of course names from the database
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


