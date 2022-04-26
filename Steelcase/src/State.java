import java.util.*;

/**
 *  Alter this so that the functionality is in the driver, and the utility is all here
 *  All this class should do is store the action that occurred, and the necessary data for that action.
 */
public class State {
    private String previousAction;
    private String previousLocation;
    private Course course;
    private ArrayList<Course> multiCourse = new ArrayList<Course>();


    public State(String s)
    {
        previousAction = s;
    }

    public Course getRemovedCourse() {
        return course;
    }

    public void setRemovedCourse(Course removedCourse) {
        this.course = removedCourse;
    }

    public State(String s, String option)
    {
        previousAction = s;
        switch(option)
        {
            
        }
        previousLocation = option;
    }

    public State(String s,  Course c)
    {
        previousAction = s;
        setRemovedCourse(c);
    }

    public State(String s,  ArrayList<Course> c)
    {
        previousAction = s;
        for(int i = 0; i < c.size(); i++)
        {
            multiCourse.add(c.get(i));
        }
    }

    public State(String s, String option, Course c)
    {
        previousAction = s;
        previousLocation = option;
        setRemovedCourse(c);
    }

    public String getPreviousAction()
    {
        return previousAction;
    }

    public String getPreviousLocation()
    {
        return previousLocation;
    }

    public ArrayList<Course> getCourses()
    {
        return multiCourse;
    }

    public void setMultiCourse(ArrayList<Course> c)
    {
        for(int i = 0; i < c.size(); i++)
        {
            multiCourse.add(c.get(i));
        }
    }
}
