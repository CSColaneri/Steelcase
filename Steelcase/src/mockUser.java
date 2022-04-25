import javafx.scene.control.CheckBox;

public class mockUser {
    private String dep;
    private int cid; //course
    private String title; //name of the course
    private String begin_time; //time of the class
    private String end_time;//end_time of class
    private String locationRoom; //location of the course
    private String building; //location of the course
    private CheckBox add;
    private int id;

    public CheckBox getAdd() {
        return add;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public void setLocationRoom(String locationRoom) {
        this.locationRoom = locationRoom;
    }

    public void setBegin_time(String time) {
        this.begin_time = time;
    }

    public void setEnd_time(String time) {
        this.end_time = time;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    public void setBuilding(String building) {
        this.building = building;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public void setAdd(CheckBox add) {
        this.add = add;
    }

    public mockUser(int cid, String locationRoom, String begin_time, String title, String dep, String end_time, String building) {
        this.dep = dep;
        this.cid = cid;
        this.title = title;
        this.begin_time = begin_time;
        this.end_time = end_time;
        this.building = building;
        this.locationRoom = locationRoom;
        this.add = new CheckBox();
    }

    public mockUser(Course c) {
        this.dep = c.getDepartment();
        this.cid = c.getCode();
        this.title = c.getShort_title();
        this.begin_time = c.getBegin_time();
        this.end_time = c.getEnd_time();
        this.building = c.getBuilding();
        this.locationRoom = c.getRoom();
        this.add = new CheckBox();
        this.id = c.getId();
    }

    public int getId(){
        return this.id;
    }

    public mockUser(){}

    public String getDep() {
        return dep;
    }

    public int getCid() {
        return cid;
    }

    public String getTitle() {
        return title;
    }
    
    public String getBegin_time() {
        return begin_time;
    }
    
    public String getEnd_time() {
        return end_time;
    }
    
    public String getBuilding() {
        return building;
    }

    public String getLocationRoom() {
        return locationRoom;
    }
}
