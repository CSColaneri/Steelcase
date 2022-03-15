import java.util.*;

public class Driver {
	private boolean running = true;
	private boolean loggedIn = false;

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
		if (loggedIn) {
			while (running) {
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
			}
		} else {
			while (running) {
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

	}

	public void viewSchedulePage() {

	}

	public void searchCoursesPage() {

	}

	public void courseCatalogPage() {

	}

	public void loginPage() {

	}

	public void logoutPage() {

	}

	public void signupPage() {

	}

	public void accoutnDetailsPage(){

	}

}
