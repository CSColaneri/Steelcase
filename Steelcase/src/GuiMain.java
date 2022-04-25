import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class GuiMain extends Application{
    public static Account account;
    public static Schedule schedule = new Schedule();
    public static boolean loggedIn = false;
    public static Connection conn = null;

    public static void main(String[] args) {
        try{
            conn = DataSource.getConnection();
        }catch (Exception e){
            e.getCause();
        }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                System.out.println("Hi");
                try{
                    conn.close();
                }catch (Exception ex){
                    ex.getCause();
                }
                System.exit(0);
            }
        });

        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Main");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
	 * Receives a course's department, code, and section as string
	 * and performs a search for that single course. If successfull,
	 * that course is then added to the user's schedule.
	 * PRINTS TO CONSOLE whether the course was added or if it
	 * conflicts. If no course found, returns false.
	 * @param in String array containing course department, code (numerical), and section
	 * @param s The search instance to add filters to and search using.
	 * @return False if no course found, true otherwise
	 */
	public static boolean addByDepCodeSec(String[] in) {
        Search s = new Search();
		ArrayList<Course> alc = new ArrayList<>();
		
		Filter fdep = new Filter("department", in[0]);
		Filter fcode = new Filter("code", in[1]);
		Filter fsection = new Filter("section", in[2]);
		
		// TODO: Log function
		// System.out.println("Driver.addByDepCodeSec()");
		// System.out.printf("Department: %s, code: %s, section: %s.\n", fdep.getValue(), fcode.getValue(), fsection.getValue());
		
		// add filters to specify the class
		ArrayList<Filter> af = new ArrayList<>();
		af.add(fcode);
		af.add(fdep);
		af.add(fsection);
		s.changeFilters(af);
		
		// search for the course
		try(Connection conn = DataSource.getConnection()) {
			alc = s.searchCoursesC(conn);
		} catch(Exception e) {
			System.err.println("Couldn't connect to the database.");
			e.printStackTrace();
			alc = null;
		}

		// if the course was found, see if it conflicts with current scheduel
		// and add if it doesn't
		if(alc != null && alc.size() != 0) {
			for(Course c : alc) {
				if(schedule.conflicts(c)) {
					System.out.printf("Course %s conflicts with your schedule!\n", c.simpleString());
					continue;//don't add this course
				}
				schedule.add(c);
				System.out.printf("Course %s added successfully\n", c.simpleString());
			}
			/**
		 	*   BOOKKEEPING:  ACTION ADDED TO STATE, PREVIOUS ACTION NOW addToSchedule
		 	*/
            //  TODO: Adapt state to gui
			// state.add(new State("addToSchedule"));


			return true;
		}
		return false;
	}
}
