import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.*;
import javax.mail.internet.*;


public class Email {
	// regex to validate email strings
	public static final String EMAIL_REGEX = "(?:[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-zA-Z0-9-]*[a-zA-Z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
	// this email exists as of 4/17/2022.
	private static final String FROM = "noreply.gcc.teamsteelcase@gmail.com";
	private static final String HOST = "smtp.gmail.com";
	private static final String PORT = "587";	//alternative is 465

	private static final String OTP = System.getenv("EMAIL_OTP");
	private static final String DEBUG = System.getenv("DEBUG");

	private static Session session = null;
	
	static {
		if(OTP == null) {
			System.err.println("Missing email password.");
			System.exit(-1);
		}
		Properties prop = new Properties();
		// using tls, port info from: 
		// https://support.google.com/mail/answer/7126229?hl=en#zippy=%2Cstep-change-smtp-other-settings-in-your-email-client
		prop.put("mail.smtp.host", HOST);
		prop.put("mail.smtp.port", PORT);
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.starttls.require", "true");
		prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
		prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		session = Session.getInstance(prop,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(FROM, OTP);
				}
			});
		// if we're debugging
		if(DEBUG != null && DEBUG.equals("true")) {
			System.err.println("Debug On");
			session.setDebug(true);
		}
	}

	/**
	 * Sends the give schedule to the given account's email. If no password is
	 * set, or it fails, returns false. returns true only on success.
	 * @param account
	 * @param schedule
	 * @return
	 */
	public static boolean sendEmail(Account account, Schedule schedule) throws AddressException {
		return sendEmail(InternetAddress.parse(account.getEmail()), schedule, "Course Schedule", "");
	}
	public static boolean sendEmail(Account account, Schedule schedule, String subject, String body) throws AddressException {
		return sendEmail(InternetAddress.parse(account.getEmail()), schedule, subject, body);
	}

	/**
	 * Sends the given schedule to each email in the given arraylist of
	 * valid emails.
	 * @param emails An arraylist of valid email addresses.
	 * @param schedule A schedule to send out.
	 * @return True if the email is successfully sent. False otherwise.
	 * @throws AddressException
	 */
	public static boolean sendEmail(ArrayList<String> emails, Schedule schedule) throws AddressException {
		// no emails to send to, auto succeed.
		if(emails.size() == 0) {
			return true;
		}
		Address[] emailAddresses = addressFromArraylist(emails);
		return sendEmail(emailAddresses, schedule, "Course Schedule", "");
	}

	/**
	 * Sends the given schedule to each email in the given arraylist of
	 * valid emails. Sets the subject and body of the email to the given
	 * values
	 * @param emails An arraylist of email addresses to send to
	 * @param schedule The schedule to send in emails
	 * @param subject The subject of the email
	 * @param body The body of the email
	 * @return True on success or false on fail.
	 * @throws AddressException 
	 */
	public static boolean sendEmail(ArrayList<String> emails, Schedule schedule, String subject, String body) throws AddressException {
		// no emails to send to, auto succeed.
		if(emails.size() == 0) {
			return true;
		}
		Address[] emailAddresses = addressFromArraylist(emails);
		return sendEmail(emailAddresses, schedule, subject, body);
	}

	/**
	 * Takes an ArrayList of emails and puts them into an {@code Address[]} array.
	 * @param emails An arraylist of valid email addresses.
	 * @return The email addresses from the parameter but in an Address array instead.
	 * @throws AddressException if the addresses couldn't be parsed into the Address array. 
	 */
	private static Address[] addressFromArraylist(ArrayList<String> emails) throws AddressException {
		String addrList = "";
		for(int i = 0; i < emails.size(); ++i) {
			addrList = addrList.concat(emails.get(i)+",");
		}
		return InternetAddress.parse(addrList);
	}

	/**
	 * Sends the given schedule to the given email {@code to}. Returns
	 * true if and only if the email is successfully send. False in 
	 * all other cases.
	 * @param to A valid email address
	 * @param schedule A schedule
	 * @return true if email successfully sent, false otherwise
	 */
	public static boolean sendEmail(Address[] to, Schedule schedule, String subject, String body) {
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(FROM));

			message.setRecipients(Message.RecipientType.BCC, to);
			
			message.setSubject(subject);
			
			// TODO: use html for schedule
			message.setContent(buildHtml(body, schedule), "text/html");
			// maybe attachement. tutorial: https://www.tutorialspoint.com/java/java_sending_email.htm

			Transport.send(message);
			return true;
		} catch (MessagingException e) {
			// TODO: Logging
			e.printStackTrace();
		}
		return false;
	}

	private static String buildHtml(String message, Schedule schedule) {
		// build html
		String html;
		try {// if the template can be found do this
			Scanner fin = new Scanner(new File("Steelcase/emailTemplate.html"));
			StringBuilder body = new StringBuilder();
			while(fin.hasNext()) {
				body.append(fin.nextLine());
			}
	
			//// list view
			StringBuilder ul = new StringBuilder();
			ul.append("<ul>\n");
	
			Scanner schedScan = new Scanner(schedule.toString());
			while(schedScan.hasNext()) {
				ul.append(String.format("<li>%s</li>", schedScan.nextLine()));
			}
			
			ul.append("</ul>\n");
	
			//// calendar view
			StringBuilder calendar = new StringBuilder();
			calendar.append("Calendar Here");
	
			html = String.format(body.toString(),"firstname", "lastname", ul.toString(), calendar.toString());
			// end html
		} catch(FileNotFoundException e) { // else use this.
			html = String.format("<h1>%s</h1>\n", message);
			// User's body set.
			// Build course list. TODO: Check if we can use some other method here.
			html += "<div id=\"list\">\n<ul>";//open div and ul
			for(Course c : schedule.getSchedule()) {
				html += String.format("<li>%s</li>\n", c.toString());
			}
			html += "</ul>\n</div>";// close ul and div
			// open calendar div
			html += "<div id=\"calendar\" style=\"padding:1rem 0 0 0;\">\n";
			html += "CALENDAR HERE"; // TODO: DO THIS
			// close calendar div
			html += "</div>";	
		}

		return html;
	}

	public static boolean isValidEmail(String email) {
		return email.matches(EMAIL_REGEX);
	}

	// If this won't run at school, try running it with a VPN enabled.
	// I keep getting Web Login Required, probably because of the
	// location being flagged as suspicious (vpn's be dropping me
	// in the bermuda triangle).
	public static void main(String[] args) {
		// session.setDebug(true);
		String to = "";

		Scanner scan = new Scanner(System.in);

		// intro
		System.out.println("This tests sending emails. In my tests, it hangs for a long time, and "
		+ "when you pause it, it'll always be in SocketInputStream.socketRead0, a native function "
		+ "call. Hopefully you get past that! If it hangs for a minute or two just kill the program.");
		String again = "";
		do {
			do {
				System.out.println("What email should receive the test email?"
				 + " It should be one you have access to so you can check it.");
				
				to = scan.next().strip();
			} while(!isValidEmail(to));
	
			scan.nextLine();//trailing newline
	
			System.out.println("Enter a subject to use for the email.");
			String subject = scan.nextLine();
	
			try {
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(FROM));
	
				message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
				
				message.setSubject(subject);
				
				// build html
				Scanner fin = new Scanner(new File("Steelcase/emailTemplate.html"));
				StringBuilder body = new StringBuilder();
				while(fin.hasNext()) {
					body.append(fin.nextLine());
				}

				//// list view
				StringBuilder ul = new StringBuilder();
				ul.append("<ul>\n");
				
				// example schedule
				Schedule s = new Schedule();
				s.add(new Course(3,300,"ABRD",'A',"OFFCP","STUDY ABROAD","STUDY ABROAD","","","","","",20,17,"NULL"));
				String desc = "Course topics include accounting for debt and stockholder’s equity, financial statement analysis, statement of cash flows, as well as introductions to managerial accounting techniques including cost-volume-profit analysis, budgeting, product costing...";
				s.add(new Course(4,202,"ACCT",'A',"HAL","PRINCIPLES OF ACCOUNTING II","PRIN OF ACCOUNT",desc,"Tricia Shultz","MWF","08:00:00","08:50:00",44,37,"306"));
				
				Scanner schedScan = new Scanner(s.toString());
				while(schedScan.hasNext()) {
					ul.append(String.format("<li>%s</li>", schedScan.nextLine()));
				}
				ul.append("</ul>\n");

				//// calendar view
				StringBuilder calendar = new StringBuilder();
				calendar.append("Calendar Here");

				String html = String.format(body.toString(),"firstname", "lastname", ul.toString(), calendar.toString());
				// end html

				message.setContent(html, "text/html");
	
				Transport.send(message);
			} catch (MessagingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			System.out.println("Send another email? Y/N");
			again = scan.next();
		} while(again.equalsIgnoreCase("y"));
		scan.close();
	}
}
