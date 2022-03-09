import java.util.*;
import java.io.*;


public class Driver {
	public static void main(String argv[])
	{
		boolean running = true;
		String in = "";
		String help = "Commands:\n"
				+ "help:  brings up help dialog\n"
				+ "create: brings up the create schedule dialog\n"
				+ ""
				+ "\n";
		String info = "";
		Scanner input = new Scanner(System.in);
		
		System.out.println("Welcome, User!");
		System.out.println(help);
		System.out.println(info);
		while(running)
		{
			switch (in)
			{
				case "create":
					break;
				case "view":
					break;
				case "search":
					break;
				case "help":
			}
		}
		
	}

}
