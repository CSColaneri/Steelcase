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

}
