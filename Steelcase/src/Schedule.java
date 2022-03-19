import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

  public boolean hasSchedule() {
    return schedule == null;
  }

  /**
   * Queries the database for the user's schedule
   * @param account The user whose schedule we're pulling
   * @return a Schedule object with the user's schedule in it.
   */
  public static Schedule retrieveSchedule(Account account) {
    String sql = "SELECT * FROM Course c INNER JOIN Schedule s on c.id = s.courseID where s.email = ?";
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

}
