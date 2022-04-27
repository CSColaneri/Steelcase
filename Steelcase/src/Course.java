import javafx.scene.control.CheckBox;

import java.sql.*;
import java.util.ArrayList;

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
  private int intStartTime; //used for sorting later
  private String room;
  private CheckBox add;
  private ArrayList<Integer> preReqs = new ArrayList<>();

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
    this.add = new CheckBox();
  }

  public Course(int diff) {
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
      int enrollment, String room, ArrayList<Integer> c) {
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
    this.intStartTime = Integer.parseInt(this.begin_time.split(":")[0]);
    this.add = new CheckBox();
    for(int i = 0; i < c.size(); i++)
    {
      preReqs.add(c.get(i));
    }
  }
  public Course(int id, int code, String department, char section, 
      String building, String long_title, String short_title, 
      String description, String professor, String day, 
      String begin_time, String end_time, int capacity, 
      int enrollment, String room, ArrayList<Integer> c,int diff) {
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
    this.intStartTime = Integer.parseInt(this.begin_time.split(":")[0]);
    for(int i = 0; i < c.size(); i++)
    {
      preReqs.add(c.get(i));
    }
  }
  
  /**
   * Accepts a SQL ResultSet from a select query on the
   * Course table. The ResultSet must include every field
   * from the Course table or else it throws a SQLException
   * @param course
   * @throws SQLException
   */
  public Course (ResultSet course, Connection conn) throws SQLException {
    this();
    if(!course.isClosed() && !course.isAfterLast()) {
      String sql2 = "SELECT id FROM Course c "
      +"INNER JOIN Prereq p"
      +"on c.code = p.prereqCode"
      + "and c.department = p.prereqDep"
      + "WHERE p.courseCode = ? AND p.courseDep = ?;";
      PreparedStatement ps2 = conn.prepareStatement(sql2);

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
      this.add          = new CheckBox();

      ps2.setString(2, department);
      ps2.setInt(1, code);
      ResultSet rs2 = ps2.executeQuery();
      while(rs2.next())
      {
        this.preReqs.add(rs2.getInt("id"));
      }
    }
  }
  
  public int getId() {
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

  public CheckBox getAdd() {
    return add;
  }

  public String getRoom() {
    return room;
  }

  public ArrayList<Integer> getPreReqs()
  {
    return preReqs;
  }

  public int getIntStartTime(){return intStartTime;}

  public void setLong_title(String lt){
    this.long_title = lt;
  }

  /**
   * Returns true if the calling course conflicts in time with the given course.
   * For some cursed reason, java.sql.Time screws up the time its sent.
   * Only compares the hours
   * @param c1 A course to check
   * @return Returns true if the calling course conflicts in time with the given course
   */
  public boolean conflicts(Course c1) {
    boolean conflict = false;
    boolean timeExists = !(this.getBegin_time() == null || this.getEnd_time() == null || c1.getBegin_time() == null || c1.getEnd_time() == null);
    if(timeExists && timeConflict(c1)) {
      System.out.println("Conflicting time");
      conflict = true;
    }
    if(!conflict && c1.code == this.code && c1.department.equals(this.department)) {
      conflict = true;
      System.out.println("Conflicting code & dep");
    }
    if(GuiMain.loggedIn && !GuiMain.account.getCoursesTaken().isEmpty())
    {
        for(int i = 0; i < GuiMain.account.getCoursesTaken().size(); i++)
        {
          if(!c1.preReqs.contains(GuiMain.account.getCoursesTaken().get(i)))
          {
            conflict = true;
            break;
          }
        }
    }
    return conflict;
  }

  private boolean timeConflict(Course c) {
    String[] thisBegin= this.getBegin_time().split(":");
    String[] thisEnd  = this.getEnd_time().split(":");
    String[] cBegin   = c.getBegin_time().split(":");
    String[] cEnd     = c.getEnd_time().split(":");
    
    for(Character day : this.day.toCharArray()) {
      // if days differ, no time conflict.
      if(c.day.contains(day.toString())) {
        // if any day matches check time and return
        // if thisBegin > (after) c1End or thisEnd < (before) c1Begin, no conflict
        boolean conflict = !(Integer.parseInt(thisBegin[0]) > Integer.parseInt(cEnd[0]) || 
          Integer.parseInt(thisEnd[0]) < Integer.parseInt(cBegin[0]));
        // if(!conflict) {
        //   conflict = thisBegin[0].equals(cBegin[0]) || thisEnd[0].equals(cEnd[0]);
        // }
        return conflict;
      }
    }
    return false;
  }

/**
	 * only for admin
	 * adds a new class
	 */
	public void createCourse(){
		String data = "insert into Course (code, department, section, building, long_title, short_title, "
		+ "description, professor, day, begin_time, end_time, capacity, enrollment, room) "
		+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (
			Connection conn = DataSource.getConnection();
			PreparedStatement ps1 = conn.prepareStatement(data);
		){
			ps1.setInt(1, code);
			ps1.setString(2, department);
			ps1.setString(3, ""+section);
			ps1.setString(4, building);
			ps1.setString(5, long_title);
			ps1.setString(6, short_title);
			ps1.setString(7, description);
			ps1.setString(8, professor);
			ps1.setString(9, day);
			ps1.setString(10, begin_time);
			ps1.setString(11, end_time);
			ps1.setInt(12, capacity);
			ps1.setInt(13, enrollment);
			ps1.setString(14, room);
			ps1.execute();
		}
		catch (SQLException s) {
			System.err.println("Failed to create new course");
			s.printStackTrace();
		}
	}

	/**
	 * only for admin
	 * changes the data of a given course to the input data
	 * @param courseCode specifies could to be changed
   * @param field specifies what to change
   * @param newIn the new input
	 */
	public void changeCourse(int courseCode, String field, String newIn){
    //runs for all int fields
    if(field.equals("code") || field.equals("capacity") || field.equals("enrollment")){
		  String stmt = String.format("UPDATE Course set %s = %d where id = %d", field, Integer.parseInt(newIn), courseCode);
      try (
			  Connection conn = DataSource.getConnection();
			  Statement ps1 = conn.createStatement();
		  ){
        ps1.execute(stmt);
      }
      catch (SQLException s) {
			  System.err.println("Failed to change course data");
			  s.printStackTrace();
		  }
    }

    //runs for all string/char fields
    else if(field.equals("department") || field.equals("section") || field.equals("building") || field.equals("long_title") 
    || field.equals("short_title") || field.equals("description") || field.equals("professor") 
    || field.equals("day") || field.equals("begin_time") || field.equals("end_time") || field.equals("room")){
		  String stmt = String.format("UPDATE Course set %s = '%s' where id = %d", field, newIn, courseCode);
      try (
			  Connection conn = DataSource.getConnection();
			  Statement ps1 = conn.createStatement();
		  ){
        ps1.execute(stmt);
      }
      catch (SQLException s) {
			  System.err.println("Failed to change course data");
			  s.printStackTrace();
		  }
    }
    else{
      System.out.println("invalid field");
    }
	}

	/**
	 * only for admin
	 * @param courseCode specifies the course to delete
	 */
	public void delCourse(int courseCode){
    //TODO: Check role.
    String stmt = "Delete from Course where id = " + courseCode;
    try (
			Connection conn = DataSource.getConnection();
			Statement ps1 = conn.createStatement();
		){
      ps1.execute(stmt);
    }
    catch (SQLException s) {
			System.err.println("Failed to delete course");
			s.printStackTrace();
		}
	}

  public String simpleString() {
    return String.format("%s %s %s", this.department, this.code, this.section);
  }

  //Code, department, Section, building, short_title. begin_time, end_time, day, room
  @Override
  public String toString(){
    return "ID: " + this.getId() + " Course Code: " + this.getCode() + " Department: " + this.getDepartment() + " Section: " + this.getSection() + " Building: " + this.getBuilding() + " Title: " + this.getShort_title() + " Start Time: " + this.getBegin_time() + " End Time: " + this.getEnd_time() + " Day: " + this.getDay() + " Room: " + this.getRoom() + "\n";
  }
}
