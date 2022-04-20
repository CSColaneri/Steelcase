package JavaFx;

import javafx.scene.control.CheckBox;

public class mockUser {
    private int cid; //course
    private int locationRoom; //location of the course
    private String time; //time of the class
    private String title; //name of the course
    private CheckBox add;

    public CheckBox getAdd() {
        return add;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public void setLocationRoom(int locationRoom) {
        this.locationRoom = locationRoom;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAdd(CheckBox add) {
        this.add = add;
    }

    public mockUser(int cid, int locationRoom, String time, String title) {
        this.cid = cid;
        this.locationRoom = locationRoom;
        this.time = time;
        this.title = title;
        this.add = new CheckBox();
    }

    public mockUser(){}

    public int getCid() {
        return cid;
    }

    public int getLocationRoom() {
        return locationRoom;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }
}
