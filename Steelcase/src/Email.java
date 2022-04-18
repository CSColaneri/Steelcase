import java.util.Properties;
import java.util.Scanner;

import javax.mail.*;
import javax.mail.internet.*;

public class Email {
	public static void main(String[] args) {
		String regex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
		// this email exists as of 4/17/2022.
		
		String from = "noreply.gcc.teamsteelcase@gmail.com";
		String to = "";
		String port = "587";	//alternative is 465
		String host = "smtp.gmail.com";

		Scanner scan = new Scanner(System.in);

		// intro
		System.out.println("This tests sending emails. In my tests, it hangs for a long time, and "
		+ "when you pause it, it'll always be in SocketInputStream.socketRead0, a native function "
		+ "call. Hopefully you get past that! If it hangs for a minute or two just kill the program.");

		do {
			System.out.println("What email should receive the test email?"
			 + " It should be one you have access to so you can check it.");
			
			to = scan.next().strip();
		} while(!to.matches(regex));

		scan.nextLine();//trailing newlin

		System.out.println("Enter a subject to use for the email.");
		String subject = scan.nextLine();

		Properties prop = new Properties();
		// using tls, port info from: 
		// https://support.google.com/mail/answer/7126229?hl=en#zippy=%2Cstep-change-smtp-other-settings-in-your-email-client
		prop.put("mail.smtp.host", host);
		prop.put("mail.smtp.port", port);
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.starttls.require", "true");
		prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
		prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		Session session = Session.getInstance(prop,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(from, "steelcase");
					}
				});
		session.setDebug(true);
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.setRecipients(
					Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);

			message.setText("This is the body of the test email. Nice!");;

			Transport.send(message);
			System.out.println("Email sent! If you see this let me (Ethan) know, I never got this far.");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
