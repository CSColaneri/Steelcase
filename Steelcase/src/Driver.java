import java.util.*;
import java.sql.*;


public class Driver {
	private boolean running = true;
	private boolean loggedIn = false;//maybe make this an Account object and test if null or not
	private Account account;
	private Schedule schedule = new Schedule();
	private String help = "Commands:\n"
		+ "help:  brings up help dialog.\n"
		+ "create: brings up the create schedule dialog.\n"
		+ "view: brings up dialog to view any schedules you have made.\n"
		+ "login: brings up the login dialog.\n"
		+ "signup: Begins the account creation process\n"
		+ "courses: a list of courses.\n"
		+ "exit:  exit the application.";

	public static void main(String argv[]) {
		Driver driver = new Driver();
		driver.run();
	}

	public Driver(){

	}

	public void run() {
		String in = "";
		Scanner input = new Scanner(System.in);

		System.out.println("Welcome, User!");
		while(running) {
			System.out.println("~~~~~Home Page~~~~~\n\n");
			System.out.println(help);
			if(loggedIn) {
				in = input.next();
				switch (in) {
					case "create":
						createSchedulePage();
						break;
					case "view":
						viewSchedulePage();
						break;
					case "courses":
						printCoursesPage();
						break;
					case "help":
						System.out.println(help);
						break;
					case "logout":
						logoutPage();
						help = "Commands:\n"
							+ "help:  brings up help dialog.\n"
							+ "create: brings up the create schedule dialog.\n"
							+ "view: brings up dialog to view any schedules you have made.\n"
							+ "login: brings up the login dialog.\n"
							+ "signup: Begins the account creation process\n"
							+ "courses: a list of courses.\n"
							+ "exit:  exit the application.";
						break;
					case "exit":
						running = false;
						break;
					default:
						System.out.println("No command found: " + in);
						break;
				}
			} else {
				in = input.next();
				switch (in) {
					case "create":
						createSchedulePage();
						break;
					case "view":
						viewSchedulePage();
						break;
					case "courses":
						printCoursesPage();
						break;
					case "help":
						System.out.println(help);
						break;
					case "login":
						loginPage();
						if(account != null) {
							aferLogIn();
						}
						break;
					case "signup":
						signupPage();
						if(account != null) {
							aferLogIn();
						}
						break;
					case "exit":
						running = false;
						break;
					default:
						System.out.println("No command found: " + in);
						break;
				}
			}
		}
		input.close();
	}

	public void createSchedulePage() {
		String help = "~~~~~Create Schedule Page~~~~~\n\n"
			+ "Options:\n"
			+ "help: Print this message again\n"
			+	"add: Add classes by their department, code, and section \n"
			+ "search: Begin searching for courses\n"
			+ "back: return to the main console dialogue\n";

		if(loggedIn){
			help = help + "logout: logs the user out\n";
		}

		Scanner scan = new Scanner(System.in);
		boolean run = true;
		while(run) {
			System.out.println(help);
			String in = scan.next().toLowerCase();
			if(in.equals("add")) {// handle adding by 'code'
				beginAdd(scan);
			} else {
				switch (in) {
					case "search"://go to search
						searchCoursesPage();
						//fallthrough intentional
					case "back":
						run = false;
						break;
					case "logout":
						run = false;
						logoutPage();
						break;
					case "help":
						System.out.println(help);
						break;
					default:
						System.out.println("Command not recognized: " + in);
						break;
				}					
			}
		}
		// scan.close();
	}

	public void viewSchedulePage() {
		// System.out.println("Results should go here.");
		//if have no schedule, say so and return
		boolean viewing = true;
		
		String help = "Options:\n"
			+ "Help: print this message again\n"
			+ "List: View the schedule as a list\n"
			+ "Calendar: View the schedule as a calendar\n"
			+ "Remove: remove a course from your schedule\n"
			+ "Save: upload your schedule to the cloud (requires account)\n"
			+ "Back: go back to the home view\n";

		if(loggedIn){
			help = help + "logout: logs the user out\n";
		}

		while(viewing) {
			System.out.println("~~~~~View Schedule~~~~~");
			System.out.println(help);
			Scanner scan = new Scanner(System.in);
			String in = scan.next().toLowerCase();
			switch (in) {
				case "logout":
					viewing = false;
					logoutPage();
					break;
				case "list":
					if(schedule == null || !schedule.hasSchedule()) {
						System.out.println("No schedule to show.");
					} else {//has a schedule
						System.out.println(schedule.toString());
						//save command saves schedule
					}
					break;
				case "calendar":
					if(schedule == null || !schedule.hasSchedule()) {
						System.out.println("No schedule to show.");
					} else {
						schedule.printCalendar();
					}
					break;
				case "save":
					if(loggedIn) {
						schedule.saveSchedule(account);
						break;
					} else {
						System.out.println("Please create an account if you want to upload your schedule!");
						System.out.println("Would you like to create one now? Y/N");
						if(scan.next().equalsIgnoreCase("y")) {
							signupPage();
						}
						break;
					}
				case "help":
					System.out.println(help);
					break;
				case "remove":
					System.out.println("Provide course id you'd like to remove.");
					
					String courseCode = scan.next();

					try 
					{
						int code = Integer.parseInt(courseCode);
						removeCourse(code);
					} 
					catch (Exception e) 
					{
						e.printStackTrace();
						System.out.println("Please provide an Integer.");
					}
					break;
				case "back":
					viewing = false;
					break;
				default:
					System.out.println("no command matching: " + in);
					break;
			}			
		}
	}

	/**
	 * allows a user to save an empty schedule, as a way to clear it.
	 */
	public void saveScheduleDialogue() {
		System.out.println("Saving your schedule...");
		if(schedule == null) {
			System.out.println("No schedule to save, try creating one first!");
		} else if(!schedule.saveSchedule(account)) {
			System.out.println("Sorry, something went wrong! Please try again later.");
		} else {
			System.out.println("Schedule saved!");
		}
	}

	public void printCoursesPage()
	{
		boolean inSearch = true;
		Scanner input = new Scanner(System.in);
		String in = "";
		Connection conn = null;
		try
		{
			conn = DataSource.getConnection();
		}
		catch (Exception e)
		{
			System.out.println("Connection failed.");
			System.out.println(e);
		}

		String help = "~~~~~Course Catalog Page~~~~~\n\n"
		+ "Options: \n"
		+ "filter:  add filters to your search.\n"
		+ "back: return to main console dialog.\n"
		+ "view: view entire catalog, sorted by department\n";

		if(loggedIn){
			help = help + "logout: log out of your account\n";
		}

		while(inSearch)
		{
			System.out.println(help);
			Search search = new Search();
			in = input.next();
			switch (in) {
				case "filter":
					try
					{
						search.changeFilters(takeFilters());
						System.out.println(search.searchCourses(conn));
					}
					catch (Exception e)
					{
						System.out.println(e);
					}
					break;
				case "back":
					inSearch = false;
					break;
				case "logout":
					inSearch = false;
					logoutPage();
					break;
				case "view":
					viewCourses(conn);
					break;
				default:
					System.out.println("No command found: " + in);
					break;
			}
		}
		
		try
		{
			conn.close();
		}
		catch (Exception e)
		{
			System.out.println("Close failed somehow.");
			System.out.println(e);
		}
	}

	public void viewCourses(Connection conn)
	{
		Search search = new Search();
		int checkNum = 50;
		int finalNum = 0;
		String s = "";

		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM Course");

			ResultSet rs = stmt.executeQuery();

			rs.next();

			finalNum = rs.getInt(1);

			stmt.close();

			rs.close();

			stmt = conn.prepareStatement("SELECT * FROM Course");

			ResultSet courses = stmt.executeQuery();
			
			ResultSetMetaData rsmd = courses.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			for (int i = 1; i <= columnsNumber; i++) {
				if (i > 1) {
					s = s + (",  ");
				}
				s = s + rsmd.getColumnName(i);
			}
			s = s + "\n";
			while (courses.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
					if (i > 1) {
						s = s + (",  ");
					}
					s = s + courses.getString(i);
				}
				s = s + "\n";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		String help = "-----Options Inside Catalog-----\nnext: view the next page\n" +
				"previous: view the last page\n" +
				"exit: exit viewing the catalog\n";

		if(loggedIn){
			help = help + "logout: log out of your account\n";
		}

		while(true)
		{
//			System.out.println("-----Options Inside Catalog-----\nnext: view the next page\n" + removed to other format
//					"previous: view the last page\n" +
//					"exit: exit viewing the catalog\n");
			System.out.println(help);
			Scanner stringRead = new Scanner(s);
			int i = 0;
			while(i < checkNum)
			{
				if(i >= checkNum - 50)
				{
					System.out.println(stringRead.nextLine());
				}
				else
				{
					stringRead.nextLine();
				}
				i++;
			}
			Scanner input = new Scanner(System.in);
			String in = input.next();
			switch(in)
			{
				case "next":
					if(checkNum < finalNum)
					{
						if(finalNum - checkNum < 50)
						{
							checkNum += finalNum%50;
						}
						else
						{
							checkNum += 50;
						}
					}
					else
					{
						System.out.println("No further direction.");
					}
					break;
				case "previous":
					if(finalNum - checkNum <= 0)
					{
						checkNum -= finalNum%50;
					}
					else if(checkNum > 50)
					{
						checkNum -= 50;
					}
					else
					{
						System.out.println("No further pages in this direction.");
					}
					break;
				case "logout":
					logoutPage();
					return;
				case "exit":
					return;
				default:
					System.out.println("Not a valid command.");
					break;
			}
		}
	}

	public void searchCoursesPage() {
		boolean inSearch = true;
		Scanner input = new Scanner(System.in);
		String in = "";
		Connection conn = null;
		try
		{
			conn = DataSource.getConnection();
		}
		catch (Exception e)
		{
			System.out.println("Connection failed.");
			System.out.println(e);
		}

		String help = "~~~~~Course Search Page~~~~~\n\n"
		+ "Options: \n"
		+ "filter:  add filters to your search.\n"
		+ "back: return to main console dialog.\n"
		+ "add: add a course from the course list to your schedule\n";

		if(loggedIn){
			help = help + "logout: log out of your account\n";
		}

		while(inSearch)
		{
			System.out.println(help);
			Search search = new Search();
			in = input.next();
			switch (in) {
				case "filter":
					try
					{
						search.changeFilters(takeFilters());
						System.out.println(search.searchCourses(conn));
					}
					catch (Exception e)
					{
						System.out.println(e);
					}
					break;
				case "back":
					inSearch = false;
					break;
				case "logout":
					inSearch = false;
					logoutPage();
					break;
				case "add":
					System.out.println("Input the ID of the course you'd like to add.");
					
					String addString = input.next();

					try 
					{
						int add = Integer.parseInt(addString);
						addCourse(add, conn);
					} 
					catch (Exception e) 
					{
						e.printStackTrace();
						System.out.println("Please provide an Integer.");
					}
					break;
				default:
					System.out.println("No command found: " + in);
					break;
			}
		}
		
		try
		{
			conn.close();
		}
		catch (Exception e)
		{
			System.out.println("Close failed somehow.");
			System.out.println(e);
		}
	}

	public void courseCatalogPage() {
		System.out.println("Results should go here.");
	}

	public void loginPage() {
		String email;
		Scanner scan = new Scanner(System.in);
		// Console console = System.console();
		// if(console == null) {
		// 	//TODO: make log function
		// 	System.err.println("Sorry, logins are disabled at this time.");
		// 	return;
		// }

		// email = console.readLine("Username: ");
		System.out.printf("Username: ");
		email = scan.nextLine();
		// Avoids storing plaintext password by directly passing it to the login 
		// function.
		// console.readPassword("Password: ").toString()
		System.out.printf("Password: ");
		account = Account.login(email, scan.nextLine());

		if(account == null) {
			System.out.println("Invalid username/password.");
		} else {
			login();
		}
		// scan.close();
	}

	private void login() {
		loggedIn = true;
		System.out.printf("Welcome back %s\n",account.getEmail());
		schedule = Schedule.retrieveSchedule(account);

		// update main menu help to match logged in commands
		help = "Commands:\n"
			+ "help:  brings up help dialog.\n"
			+ "create: brings up the create schedule dialog.\n"
			+ "view: brings up dialog to view any schedules you have made.\n"
			+ "logout: Logs out (unsaved changes are lost)\n"
			+ "courses: a list of courses.\n"
			+ "exit:  exit the application.";
	}

	private void aferLogIn() { //nice
		if(schedule.hasSchedule()) {
			// scan.close();
			viewSchedulePage();
		} else {
			// scan.close();
			createSchedulePage();
		}
	}

	public void logoutPage() {
		account = null; //logs out
		schedule = null;
		loggedIn = false;
		help = "Commands:\n"  //updates help
				+ "help:  brings up help dialog.\n"
				+ "create: brings up the create schedule dialog.\n"
				+ "view: brings up dialog to view any schedules you have made.\n"
				+ "login: Log into your account\n"
				+ "courses: a list of courses.\n"
				+ "exit:  exit the application.";
		System.out.println("Logged out successfully!");

	}

	public void signupPage() {
		String email = "";
		String pass = "";
		boolean match = false;
		Scanner input = new Scanner(System.in);
		boolean signup = true;
		// let the user put in an email. if they want to quit, they can enter
		// 'cancel' to quit out of the signup process and go back to the page
		// they came from.
		do {
			System.out.println("Enter your email (or 'cancel' to abort the signup process)");
			email = input.next();
			
			// if it equals cancel, set signup to false and break out the signup loop;
			if(email.compareToIgnoreCase("cancel") == 0) {
				signup = false;
				break;
			}

			System.out.println("Enter a password");
			pass = input.next();
			
			System.out.println("Confirm your password");
			String confirm = input.next();
			
			if(pass.equals(confirm)) {
				match = true;
			} else {
				System.out.println("Passwords do not match");
			}
		} while(signup && !match);

		// if they aborted the sign up process, skip this.
		if(signup) {
			try {
				//create the new account and log them in.
				account = Account.signup(email, pass, schedule);
				if(account != null) {
					login();
				} else {
					System.out.println("That email is already in use.");
				}
			} catch(Exception e) {
				//TODO: make log function
				System.err.println("Something went wrong. Please try again later.");
				e.printStackTrace();
			}
		}
	}

	public void accountDetailsPage(){
		System.out.println("Results should go here.");
	}

	public ArrayList<Filter> takeFilters()
	{
		ArrayList<Filter> filters = new ArrayList<Filter>();
		boolean inSearch = true;
		Scanner input = new Scanner(System.in);
		String in = "";
		while(inSearch)
		{
			System.out.println("Provide filter type, or type exit to exit\n" + 
			"Sort types: professor, name, description, code, department");
			in = input.next();
			switch (in) {
				case "exit":
					inSearch = false;
					break;
				default:
					Filter filter = new Filter();
					if(filter.isValidParam(in))
					{
						filter.setParam(in);
						System.out.print("Parameter:  ");
						in = input.next();//should be nextLine if by description
						filter.setValue(in);
						System.out.println();
						filters.add(filter);
					}
					else
					{
						System.out.println("Not a valid filter type.");
					}
					break;
			}
		}
		return filters;
	}

	protected void addCourse(int courseCode, Connection conn)
	{

		ArrayList<Filter> fList = new ArrayList<Filter>();
		Filter fil = new Filter();
		fil.setParam("id");
		fil.setValue(Integer.toString(courseCode));

		fList.add(fil);
		Search search = new Search();

		search.changeFilters(fList);
		try
		{
			ResultSet rs = search.buildStatement(conn).executeQuery();

			if(rs.next())
			{
				Course course = new Course(rs);

				for(int i = 0; i < schedule.getSchedule().size(); i++)
				{
					if(schedule.getSchedule().get(i).getBegin_time().equals(course.getBegin_time()))
					{
						System.out.println("There exists a course conflict within your schedule!");
						System.out.println(schedule.getSchedule().get(i).getDepartment() + " " + schedule.getSchedule().get(i).getCode() + "\nhas a time conflict with\n"
							+ course.getCode() + " " + course.getDepartment() + ".");
						return;
					}
				}

			schedule.add(course);

			}
			else
			{
				System.out.println("There is no course with that ID.");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("AddCourse error.");
		}
	}

	protected void removeCourse(int courseCode)
	{
		schedule.removeCourse(courseCode);
	}

	public void beginAdd(Scanner scan) {
		Search search = new Search();
		while(true) {
			System.out.println("Add course one at a time by entering their department, code, and section (i.e. COMP 300 A)"
			+ ", or 'exit' to stop adding courses.");
			String course = scan.nextLine().stripLeading().stripTrailing();
			
			//break from loop on user's command
			if(course.equalsIgnoreCase("exit")) {
				break;
			}
			// force lowercase for simpler equality checking.
			String[] depCodeSec = course.toLowerCase().split(" ");
			
			// need 3 strings, like COMP 300 A, anything more is ignored.
			if(depCodeSec.length >= 3) {
				// if the code isn't a number, don't try to add it to the schedule
				if(!depCodeSec[1].matches("[0-9]+")) {
					System.out.println("The course's code should be a number");
				} else if(!addByDepCodeSec(depCodeSec, search)) {//if it is a number but doesn't exist, say so
					System.out.printf("The course %s doesn't seem to exist. Check your spelling and try again.\n", course);
				}
				// If the course is found, addByDepCodeSec prints whether it is added or if it conflicts.
			} else {
				System.out.println("Make sure each piece (department, code, section) is seperated by a space and try again");
			}
		}
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
	public boolean addByDepCodeSec(String[] in, Search s) {
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
			return true;
		}
		return false;
	}

}
