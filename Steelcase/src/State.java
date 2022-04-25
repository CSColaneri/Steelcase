import java.util.*;

/**
 *  Alter this so that the functionality is in the driver, and the utility is all here
 *  All this class should do is store the action that occurred, and the necessary data for that action.
 */
public class State {
    private String previousAction;
    private String previousLocation;
    private Course removedCourse;

    public State(String s)
    {
        previousAction = s;
    }

    public Course getRemovedCourse() {
        return removedCourse;
    }

    public void setRemovedCourse(Course removedCourse) {
        this.removedCourse = removedCourse;
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


    
}
