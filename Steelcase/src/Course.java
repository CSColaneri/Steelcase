
public class Course {
  // TODO: This can't be right...
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

  public Course(int id, int code, String department, char section, String building, String long_title,
      String short_title, String description, String professor, String day, String begin_time, String end_time,
      int capacity, int enrollment, String room) {
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
}
