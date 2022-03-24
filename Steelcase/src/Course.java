import java.sql.ResultSet;
import java.sql.SQLException;

public class Course {
  private int id;
  private int code;
  private String department;
  private char section;
  private String building;
  private String long_title;
  private String short_title;
  private String description;
  private String professor;
  private String day;
  private String begin_time;
  private String end_time;
  private int capacity;
  private int enrollment;
  private String room;

  public Course() {
    id = 0;
    code = 0;
    department = "";
    section = '\0';
    building = "";
    long_title = "";
    short_title = "";
    description = "";
    professor = "";
    day = "";
    begin_time = "";
    end_time = "";
    capacity = 0;
    enrollment = 0;
    room = "";
  }
  
  // TODO: This can't be right...
  public Course(int id, int code, String department, char section, 
      String building, String long_title, String short_title, 
      String description, String professor, String day, 
      String begin_time, String end_time, int capacity, 
      int enrollment, String room) {
    this.id = id;
    this.code = code;
    this.department = department;
    this.section = section;
    this.building = building;
    this.long_title = long_title;
    this.short_title = short_title;
    this.description = description;
    this.professor = professor;
    this.day = day;
    this.begin_time = begin_time;
    this.end_time = end_time;
    this.capacity = capacity;
    this.enrollment = enrollment;
    this.room = room;
  }
  
  /**
   * Accepts a SQL ResultSet from a select query on the
   * Course table. The ResultSet must include every field
   * from the Course table or else it throws a SQLException
   * @param course
   * @throws SQLException
   */
  public Course (ResultSet course) throws SQLException {
    this();
    if(!course.isClosed() && !course.isAfterLast()) {
      this.id           = course.getInt("id");
      this.code         = course.getInt("code");
      this.department   = course.getString("department");
      this.section      = course.getString("section").charAt(0);
      this.building     = course.getString("building");
      this.long_title   = course.getString("long_title");
      this.short_title  = course.getString("short_title");
      this.description  = course.getString("description");
      this.professor    = course.getString("professor");
      this.day          = course.getString("day");
      this.begin_time   = course.getString("begin_time");
      this.end_time     = course.getString("end_time");
      this.capacity     = course.getInt("capacity");
      this.enrollment   = course.getInt("enrollment");
      this.room         = course.getString("room");
    }
  }
  
  public int getID() {
	  return id;
  }

  public int getCode() {
    return code;
  }

  public String getDepartment() {
    return department;
  }

  public char getSection() {
    return section;
  }

  public String getBuilding() {
    return building;
  }

  public String getLong_title() {
    return long_title;
  }

  public String getShort_title() {
    return short_title;
  }

  public String getDescription() {
    return description;
  }

  public String getProfessor() {
    return professor;
  }

  public String getDay() {
    return day;
  }

  public String getBegin_time() {
    return begin_time;
  }

  public String getEnd_time() {
    return end_time;
  }

  public int getCapacity() {
    return capacity;
  }

  public int getEnrollment() {
    return enrollment;
  }

  public String getRoom() {
    return room;
  }

  //Code, department, Section, buildeing, short_title. begin_time, end_time, day, room
  @Override
  public String toString(){
    return "Course Code: " + this.getCode() + " Department: " + this.getDepartment() + " Section: " + this.getSection() + " Building: " + this.getBuilding() + " Title: " + this.getShort_title() + " Start Time: " + this.getBegin_time() + " End Time: " + this.getEnd_time() + " Day: " + this.getDay() + " Room: " + this.getRoom() + "\n";
  }
}
