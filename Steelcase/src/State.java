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

    public State(String s, String loc)
    {
        previousAction = s;
        previousLocation = loc;
    }

    public State(String s,  Course c)
    {
        previousAction = s;
        removedCourse = c;
    }

    public State(String s, String loc, Course c)
    {
        previousAction = s;
        previousLocation = loc;
        removedCourse = c;
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
