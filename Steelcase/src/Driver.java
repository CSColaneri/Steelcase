import java.io.Console;
import java.util.*;

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
				+ "login: brings up the login dialog"
				+ "search: search for courses using the course search dialog"
				+ "courses: a list of courses "
				+ "\n";
		Scanner input = new Scanner(System.in);

		System.out.println("Welcome, User!");
		System.out.println(help);
		while(running) {
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
		input.close();
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
		while(inSearch)
		{
			Search search = new Search();
			in = input.next();
			switch (in) {
				case "filter":
					search.changeFilters(takeFilters());
					try
					{
						search.buildStatement();
					}
					catch (Exception e)
					{
						System.out.println(e);
					}
					break;
				case "back":
					inSearch = false;
					break;
				default:
					System.out.println("No command found: " + in);
					break;
			}
		}
		input.close();
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
			"Sort types: Professor, Name, Description, Code, Department");
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
		input.close();
		return filters;
	}

}
