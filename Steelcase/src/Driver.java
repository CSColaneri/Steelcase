import java.io.Console;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class Driver {
	private boolean running = true;
	private boolean loggedIn = false;//maybe make this an Account object and test if null or not
	private Account account;
	private Schedule schedule;

	public static void main(String argv[]) {
		Driver driver = new Driver();
		driver.run();
	}

	public Driver(){

	}

	public void run() {
		String in = "";
		String help = "Commands:\n"
				+ "help:  brings up help dialog.\n"
				+ "create: brings up the create schedule dialog.\n"
				+ "view: brings up dialog to view any schedules you have made.\n"
				+ "login: brings up the login dialog.\n"
				+ "search: search for courses using the course search dialog.\n"
				+ "courses: a list of courses.\n";
		Scanner input = new Scanner(System.in);

		System.out.println("Welcome, User!");
		System.out.println(help);
		while(running) {
			System.out.println("~~~~~Home Page~~~~~\n\n");
			if(loggedIn) {
				in = input.next();
				switch (in) {
					case "create":
						createSchedulePage();
						break;
					case "view":
						viewSchedulePage();
						break;
					case "search":
						searchCoursesPage();
						break;
					case "help":
						System.out.println(help);
						break;
					case "logout":
						logoutPage();
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
					case "search":
						searchCoursesPage();
						break;
					case "help":
						System.out.println(help);
						break;
					case "login":
						loginPage();
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
	}

	public void createSchedulePage() {
		System.out.println("Results should go here.");
	}

	public void viewSchedulePage() {
		System.out.println("Results should go here.");
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

		System.out.println(help);

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
				case "add":
					System.out.println("Input the ID of the course you'd like to add.");
					int add = input.nextInt();
					addCourse(add, conn);
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

	// TODO: if succesful and:
	// TODO		user has schedule, goto view schedule page.
	// TODO		user has no schedule, goto create schedule page.
	public void loginPage() {
		String email;
		Console console = System.console();

		email = console.readLine("Username: ");
		// Avoids storing plaintext password by directly passing it to the login 
		// function.
		account = Account.login(email, console.readPassword("Password: ").toString());

		if(account == null) {
			System.out.println("Invalid username/password.");
		} else {
			System.out.printf("Welcome back %s\n",email);
			schedule = Schedule.retrieveSchedule(account);
			if(schedule.hasSchedule()) {
				viewSchedulePage();
			} else {
				createSchedulePage();
			}
		}
	}

	public void logoutPage() {
		System.out.println("Results should go here.");
	}

	public void signupPage() {
		System.out.println("Results should go here.");
	}

	public void accoutnDetailsPage(){
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
						in = input.next();
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
		String s = "INSERT INTO Schedule VALUES (?, ?)";
		try
		{
			PreparedStatement p = conn.prepareStatement(s);
			p.setInt(1, courseCode);
			p.setString(2, account.getEmail());
			p.executeQuery();
			p.close();

			s = "SELECT * FROM Schedule";

			p = conn.prepareStatement(s);
			ResultSet g = p.executeQuery();

			System.out.println(g.toString());

			p.close();

			System.out.println("Course added.");
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	protected void removeCourse(int courseCode, Connection conn)
	{
		String s = "DELETE FROM Schedule VALUES WHERE courseID = ?";
		try
		{
			PreparedStatement p = conn.prepareStatement(s);
			p.setInt(1, courseCode);
			p.executeQuery();
			p.close();

			s = "SELECT * FROM Schedule";

			p = conn.prepareStatement(s);
			ResultSet g = p.executeQuery();

			System.out.println(g.toString());

			p.close();

			System.out.println("Course added.");
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

}
