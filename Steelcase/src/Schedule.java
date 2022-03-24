import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Schedule {
  private ArrayList<Course> schedule = null;
  
  public Schedule() {
    schedule = new ArrayList<>();
  }

  public ArrayList<Course> getSchedule() {
    return this.schedule;
  }

  //TODO: check for duplicate course and time conflicts
  public boolean add(Course c) {
    return schedule.add(c);
  }

  /**
   * Checks if the schedule has any classes in it.
   * @return true if schedule.size() > 0, false if not.
   */
  public boolean hasSchedule() {
    return schedule.size() > 0;
  }

  /**
   * Queries the database for the user's schedule
   * @param account The user whose schedule we're pulling
   * @return a Schedule object with the user's schedule in it.
   */
  public static Schedule retrieveSchedule(Account account) {
    String sql = "SELECT * FROM Course c INNER JOIN Schedule s ON c.id = s.courseID WHERE s.email = ?";
    Schedule schedule = new Schedule();
    try(Connection conn = DataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);) {
      ps.setString(1, account.getEmail());
      ResultSet rs = ps.executeQuery();
      while(rs.next()) {
        // TODO: This can't be right...
        int id = rs.getInt(1);
        int code = rs.getInt(2);
        String department = rs.getString(3);
        char section = rs.getString(4).toCharArray()[0];
        String building = rs.getString(5);
        String long_title = rs.getString(6);
        String short_title = rs.getString(7);
        String description = rs.getString(8);
        String professor = rs.getString(9);
        String day = rs.getString(10);
        String begin_time = rs.getTime(11).toString();
        String end_time = rs.getTime(12).toString();
        int capacity = rs.getInt(13);
        int enrollment = rs.getInt(14);
        String room = rs.getString(15);
        // TODO: only add to schedule when all courses are successfully retrieved.
        schedule.add(new Course(id, code, department, section, building, long_title, short_title, description, professor, day, begin_time, end_time, capacity, enrollment, room));
      }
    } catch(SQLException e) {
      // TODO: make log function
      System.out.println("Failed to retrieve Schedule");
      e.printStackTrace();
    }
    return schedule;//may return an empty Schedule
  }
  
  public long strToTime (String start) {
	  int multiplier[] = {3600000, 60000};
      String startString = start; //read in string
      String splits[]; //array of strings split by colons

      long startTime = 0;
      
      splits = startString.split(":");
      for (int x = 0; x < splits.length; x++) {
              startTime += (Integer.parseInt(splits[x]) * multiplier[x]); 
              //converts to milliseconds
      }
      return startTime;
  }
  
  //TODO: please... add comments and use String.format instead of string concatenation
  public void printCalendar() {
	  ArrayList<String> mon = new ArrayList<String>();
	  ArrayList<String> tue = new ArrayList<String>();
	  ArrayList<String> wed = new ArrayList<String>();
	  ArrayList<String> thu = new ArrayList<String>();
	  ArrayList<String> fri = new ArrayList<String>();
	  ArrayList<Course> copy = schedule;
	  ArrayList<Course> ordered = new ArrayList<Course>();
	  ArrayList<Long> times = new ArrayList<Long>();
	  for(int i = 0; i < schedule.size(); i++) {
		  times.add(strToTime(copy.get(i).getBegin_time()));
	  }
	  for(int i = 0; i < schedule.size(); i++) {
		  Long earliest = times.get(0);
		  int index = 0;
		  for(int j = 0; j < copy.size(); i++) {
			  if(times.get(j) < earliest) {
				  index = j;
			  }
		  }
		  ordered.add(copy.get(index));
		  copy.remove(index);
		  times.remove(index);
	  }
	  for(int i = 0; i < ordered.size(); i++) {
		  Scanner days = new Scanner(ordered.get(i).getDay());
		  days.useDelimiter("");
		  while(days.hasNext()) {
			  char day = days.next().charAt(0);
			  switch (day) {
			  case 'M' :
				  mon.add("(" + ordered.get(i).getShort_title() + ", " +
			  ordered.get(i).getBuilding() + " " + ordered.get(i).getRoom() + ", " +
			  ordered.get(i).getBegin_time() + "-" + ordered.get(i).getEnd_time() + ")");
			  case 'T' :
				  tue.add("(" + ordered.get(i).getShort_title() + ", " +
			  ordered.get(i).getBuilding() + " " + ordered.get(i).getRoom() + ", " +
			  ordered.get(i).getBegin_time() + "-" + ordered.get(i).getEnd_time() + ")");
			  case 'W' :
				  wed.add("(" + ordered.get(i).getShort_title() + ", " +
			  ordered.get(i).getBuilding() + " " + ordered.get(i).getRoom() + ", " +
			  ordered.get(i).getBegin_time() + "-" + ordered.get(i).getEnd_time() + ")");
			  case 'R' :
				  thu.add("(" + ordered.get(i).getShort_title() + ", " +
			  ordered.get(i).getBuilding() + " " + ordered.get(i).getRoom() + ", " +
			  ordered.get(i).getBegin_time() + "-" + ordered.get(i).getEnd_time() + ")");  
			  case 'F' :
				  fri.add("(" + ordered.get(i).getShort_title() + ", " +
			  ordered.get(i).getBuilding() + " " + ordered.get(i).getRoom() + ", " +
			  ordered.get(i).getBegin_time() + "-" + ordered.get(i).getEnd_time() + ")");
			  default :
				  System.out.println("not a valid day");
			  }
		  }
	  }
	  System.out.print("Monday:");
	  for(int i = 0; i < mon.size(); i++) {
		  System.out.print(" " + mon.get(i));
	  }
	  System.out.println();
	  System.out.print("Tuesday:");
	  for(int i = 0; i < tue.size(); i++) {
		  System.out.print(" " + tue.get(i));
	  }
	  System.out.println();
	  System.out.print("Wednesday:");
	  for(int i = 0; i < wed.size(); i++) {
		  System.out.print(" " + wed.get(i));
	  }
	  System.out.println();
	  System.out.print("Thursday:");
	  for(int i = 0; i < thu.size(); i++) {
		  System.out.print(" " + thu.get(i));
	  }
	  System.out.println();
	  System.out.print("Friday:");
	  for(int i = 0; i < fri.size(); i++) {
		  System.out.print(" " + fri.get(i));
	  }
	  System.out.println();
  }


  /**
   * Connects to the database to save this schedule object
   * to the given account. First it clears the account's
   * current schedule, 
   * @param account The account to save to.
   * @return true if successfull, false if it fails at any point
   */
  public boolean saveSchedule(Account account) {
    // clear current schedule
    Connection conn = null;
    boolean status = true;

    try {
      conn = DataSource.getConnection();
    } catch(SQLException e) {
      //TODO: make log function
      System.err.println("Failed to connect to database.");
      e.printStackTrace();
      status = false;
    }

    //if connection failed, skip this
    if(status) {
    try(Statement statement = conn.createStatement()) {
      conn.setAutoCommit(false);// set to false to send as one transaction

      // delete current schedule
      String sql = String.format("DELETE FROM Schedule WHERE email LIKE %s", account.getEmail());
      statement.addBatch(sql);
      
      // insert new schedule
      for(Course c : schedule) {
        sql = String.format("INSERT INTO Schedule(email, courseID) VALUES(%s, %s)", account.getEmail(), c.getID());
        statement.addBatch(sql);
      }
      
      // passing conn because this throws if anything failed
      throwIfFailed(statement.executeBatch(), conn);

      conn.commit();
    } catch(SQLException e) {
      //TODO: make log function
      e.printStackTrace();
      status = false;
    }
    }

    return status;
  }

  // private function to check if each sql statement in saveSchedule succeeded.
  // if any of them failed, rollsback the transaction and throws a SQLException
  private void throwIfFailed(int[] batchStatuses, Connection conn) throws SQLException {
    int i = 0;
    while(i < batchStatuses.length) {
      // if any of them failed, rollback and throw an exception.
      if(batchStatuses[i++] == Statement.EXECUTE_FAILED) {
        conn.rollback();
        if((i-1) == 0) {//batchStatus[0] = delete statement.
          throw new SQLException("Failed to clear current schedule, rolling back transaction.");
        } //any other batch is an insert statement.
        throw new SQLException("Failed to insert new schedule, rolling back transaction.");
      }
    }
  }


  @Override
  public String toString() {
    return "SCHEDULE toString: COMPLETE THIS";
  }
}
