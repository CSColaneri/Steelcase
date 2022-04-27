import java.util.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.naming.InvalidNameException;

import com.google.api.client.util.Data;

import org.apache.http.auth.InvalidCredentialsException;

// much of the methods are grabbed from 
// https://www.quickprogrammingtips.com/java/how-to-securely-store-passwords-in-java.html
// The encryption is the main reason for using this
public class Account {
	private String email;
	private String passEncrypted;
	private String salt;
	private String firstName;
	private String lastName;
	private String role;
	private String newEmail;
	private boolean isEmailConfirmed;
	private String confirmationCode;
	private ArrayList<Integer> coursesTaken = new ArrayList<>();

	private static final String PASSWORD_FIELD= "password_hash";
	private static final String EMAIL_FIELD 	= "email";
	private static final String SALT_FIELD 		= "salt";
	private static final String FIRST_NAME_FIELD = "first_name";
	private static final String LAST_NAME_FIELD = "last_name";
	private static final String ROLE_FIELD = "role";
	private static final String NEW_EMAIL_FIELD = "NEW_EMAIL";
	private static final String EMAIL_CONFIRMATION_FIELD = "EMAIL_CONFIRMATION";
	private static final String CONFIRMATION_CODE_FIELD = "CONFIRMATION_CODE";
	// private static final String ROLE_FIELD = "role";

	private Account(Account a) {
		this.email = a.email;
		this.passEncrypted = a.passEncrypted;
		this.salt = a.salt;
		this.firstName = a.firstName;
		this.lastName = a.lastName;
		this.role = a.role;
		this.newEmail = a.newEmail;
		this.isEmailConfirmed = a.isEmailConfirmed;
		this.confirmationCode = a.confirmationCode;
	}

	private Account(String email, String pword, String salt, String firstName, String lastName) {
		this.email =email;
		this.setPassEncrypted(pword);
		this.salt = salt;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = "user";
		this.newEmail = "";
		this.isEmailConfirmed = false;
		this.confirmationCode = getNewEmailConfirmationCode();
	}
	private Account(String email, String pword, String salt, String firstName, String lastName, String role) {
		this.setEmail(email);
		this.setPassEncrypted(pword);
		this.salt = salt;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.newEmail = "";
		this.isEmailConfirmed = false;
		this.confirmationCode = getNewEmailConfirmationCode();
	}
	
	private Account(String email, String pword, String salt, String firstName, String lastName, String newEmail, boolean emailIsConfirmed) {
		this.setEmail(email);
		this.setPassEncrypted(pword);
		this.salt = salt;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = "user";
		this.newEmail = newEmail;
		this.isEmailConfirmed = emailIsConfirmed;
		this.confirmationCode = getNewEmailConfirmationCode();
	}
	
	private Account(String email, String pword, String salt, String firstName, String lastName, String newEmail, boolean emailIsConfirmed, String code) {
		this.setEmail(email);
		this.setPassEncrypted(pword);
		this.salt = salt;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = "user";
		this.newEmail = newEmail;
		this.isEmailConfirmed = emailIsConfirmed;
		this.confirmationCode = code;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		if(role.equals("user") || role.equals("admin")) {
			this.role = role;
		}
	}
	
	public String getPassEncrypted() {
		return passEncrypted;
	}


	/**
	 * String sql2 = "SELECT ID FROM PreReqSave WHERE email = ?";
			PreparedStatement ps2 = conn.prepareStatement(sql2);
			ps2.setString(1, GuiMain.account.getEmail());
			ResultSet rs2 = ps2.executeQuery();
			while(rs2.next())
			{
				coursesTaken.add(rs2.getInt("ID"));
			}
	 * @return
	 */
	private Account(String email, String pword, String salt, String firstName, String lastName, ArrayList<Integer> courses) {
		this.setEmail(email);
		this.setPassEncrypted(pword);
		this.salt = salt;
		this.firstName = firstName;
		this.lastName = lastName;
		this.coursesTaken = courses;
	}

	public ArrayList<Integer> getCoursesTaken()
	{
		return coursesTaken;
	}

	public void addCoursesTaken(ArrayList<Integer> c)
	{
		
		for(int i = 0; i < c.size(); i++)
		{
			if(!coursesTaken.contains(c.get(i)))
			{
				coursesTaken.add(c.get(i));
				try
				{
					Connection conn = DataSource.getConnection();
					pushCourse(c.get(i), conn);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public void pushCourse(int i, Connection conn)
	{
		try
		{
			String sql2 = "INSERT INTO PreReqSave (id, email) VALUES (?, ?)";
				PreparedStatement ps2 = conn.prepareStatement(sql2);
				ps2.setInt(1, i);
				ps2.setString(2, email);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public String getEmail() {
		return email;
	}

	// no getSalt function

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public String getNewEmail() {
		return this.newEmail;
	}

	public boolean emailIsConfirmed() {
		return this.isEmailConfirmed;
	}

	public String getConfirmationCode() {
		return this.confirmationCode;
	}

	private void setEmail(String email) {
		this.email = email;
	}

	private void setPassEncrypted(String passEncrypted) {
		this.passEncrypted = passEncrypted;
	}

	private void setSalt(String salt) {
		this.salt = salt;
	}

	private void setNewEmail(String newEmail) {
		this.newEmail = newEmail;
	}

	private void setIsEmailConfirmed(boolean bool) {
		this.isEmailConfirmed = bool;
	}

	private void setConfirmationCode(String confirmationCode) {
		this.confirmationCode = confirmationCode;
	}

	/**
	 * Validates that the given email is in an appropriate format
	 * @param email An email address
	 * @return true if the address is in a valid email address format. False if not.
	 */
	public static boolean isValidEmail(String email) {
		return email.matches(Email.EMAIL_REGEX);
	}

	public boolean validPassword(String email, String password) {
		return false;
	}

	/**
	 * Logs in the user whose email and password matches a user in our database.
	 * @param email
	 * @param password
	 * @return An account object representing the signed in user, or null.
	 * @throws InvalidNameException If the given email is not in a valid email address.
	 */
	public static Account login(String email, String password) {
		Account acc;
		// get account info with that email
		if ((acc = getAccountDetails(email)) != null) {
			// compare retrieved account password to the given one
			if ((acc = authenticateUser(acc, password)) != null) {
				// passwords and email match, return the account
				return acc;
			}
		}
		// either no email or the passwords don't match
		return null;
	}

	/**
	 * Retrieve account info that matches the given email.
	 * 
	 * @param email the email the user is attempting to log in as.
	 * @return null if there's no account matching the email, or
	 *         An Account object containing the email, password hash, and
	 *         salt from the database.
	 */
	private static Account getAccountDetails(String email) {
		ArrayList<Integer> c = new ArrayList<>();
		String sql = "SELECT * FROM Account WHERE email like ?";
		String pHash,
					salt,
					firstName,
					lastName;
		try (
			Connection conn = DataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
		) {
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {// account exists in db.
				pHash = rs.getString(PASSWORD_FIELD);
				salt = rs.getString(SALT_FIELD);
				firstName = rs.getString(FIRST_NAME_FIELD);
				lastName = rs.getString(LAST_NAME_FIELD);
				String sql2 = "SELECT id FROM PreReqSave WHERE email = ?";
				PreparedStatement ps2 = conn.prepareStatement(sql2);
				ps2.setString(1, email);
				ResultSet rs2 = ps2.executeQuery();
				while(rs2.next())
				{
					c.add(rs2.getInt("id"));
				}
				return new Account(email, pHash, salt, firstName, lastName, c);
			}
			rs.close();
		} catch (SQLException e) {
			// TODO: Make log function.
			System.err.println("Failed to get Account Details.");
			e.printStackTrace();
			return null;
		}
		// no account with that email.
		return null;
	}

	/**
	 * Checks given password against stored Account's password and returns
	 * true or false
	 * Adapted from:
	 * https://www.quickprogrammingtips.com/java/how-to-securely-store-passwords-in-java.html
	 * 
	 * @param account  An account from the db whose email matches what the user
	 *                 gave.
	 * @param password The password given by the user logging in.
	 * @return True if password matches, false if not.
	 */
	public static Account authenticateUser(Account account, String password) {
		// automatically releases connection and closes ps at end or exception
		try {
			String userPassHash = getEncryptedPassword(password, account.salt);
			if (account.getPassEncrypted().equals(userPassHash)) {
				// credentials are valid
				return account;
			}
		} catch (Exception e) {
			// TODO: Make log function
			System.err.println("Failed to authenticate user.");
			e.printStackTrace();
			return null;
		}
		// credentials are not valid
		return null;
	}

	// TODO: Put requirements on password. Maybe new method for it.
	/**
	 * Adapted from:
	 * https://www.quickprogrammingtips.com/java/how-to-securely-store-passwords-in-java.html
	 * 
	 * @param email    User's inputted email.
	 * @param password User's inputted password.
	 * @param sched    User's schedule
	 * @param firstName User's first name
	 * @param lastName User's last name
	 * @return Null if email already in use, or an Account object representing the
	 *         user's account
	 * @throws Exception If something goes wrong while checking for a duplicate
	 *                   email
	 */
	public static Account signup(String email, String password, Schedule sched, String firstName, String lastName) throws Exception {
		// check to see if the email already exists before any expensive hashing.
		try (
			Connection conn = DataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT email FROM Account WHERE email = ?")
		) {
			ps.setString(1, email);
			if (ps.executeQuery().next()) {// if the email already exists, just return null now.
				// System.out.println("Email already in use.");
				return null;
			}
		} catch (Exception e) {
			// on any exception, couldn't check if email already exists. return null.
			// TODO: Make logging function
			System.err.println("Something went wrong, please try again later.");
			e.printStackTrace();
			throw new Exception("Couldn't check for duplicate emails.");
		}

		// At this point, email not already used
		if(isValidEmail(email)) {
			String salt = getNewSalt();
			String encryptedPassword = getEncryptedPassword(password, salt);
			System.out.println("Making account");
			Account account = new Account(email, encryptedPassword, salt, firstName, lastName);
			System.out.println("Uploading user and schedule");
			account = account.saveUser(sched);
			System.out.println("Sending Confirmation Email");
			// if(!Email.sendConfirmationEmail(account)) {
			// 	System.err.println("Failed to send confirmation email");
			// }
			return account;
		} else {
			throw new InvalidNameException("Invalid Email");
		}
	}

	//same as above, but added role, used for admin creation and driver
	public static Account signup(String email, String password, Schedule sched, String firstName, String lastName, String role) throws Exception {
		// check to see if the email already exists before any expensive hashing.
		try (
			Connection conn = DataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement("SELECT email FROM Account WHERE email = ?")
		) {
			ps.setString(1, email);
			if (ps.executeQuery().next()) {// if the email already exists, just return null now.
				// System.out.println("Email already in use.");
				return null;
			}
		} catch (Exception e) {
			// on any exception, couldn't check if email already exists. return null.
			// TODO: Make logging function
			System.err.println("Something went wrong, please try again later.");
			e.printStackTrace();
			throw new Exception("Couldn't check for duplicate emails.");
		}

		// At this point, email not already used
		if(isValidEmail(email)) {
			String salt = getNewSalt();
			String encryptedPassword = getEncryptedPassword(password, salt);
			Account account = new Account(email, encryptedPassword, salt, firstName, lastName, role);
			account = account.saveUser(sched);
			if(!Email.sendConfirmationEmail(account)) {
				System.err.println("Failed to send confirmation email");
			}
			return account;
		} else {
			throw new InvalidNameException("Invalid Email");
		}
	}

	// Get a encrypted password using PBKDF2 hash algorithm
	/**
	 * Adapted from:
	 * https://www.quickprogrammingtips.com/java/how-to-securely-store-passwords-in-java.html
	 * 
	 * @param password
	 * @param salt
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws Exception
	 */
	public static String getEncryptedPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
		String algorithm = "PBKDF2WithHmacSHA1";
		int derivedKeyLength = 160; // for SHA1
		int iterations = 20000; // NIST specifies 10000

		byte[] saltBytes = Base64.getDecoder().decode(salt);
		KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, iterations, derivedKeyLength);
		SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);

		byte[] encBytes = f.generateSecret(spec).getEncoded();
		return Base64.getEncoder().encodeToString(encBytes);
	}

	// Returns base64 encoded salt
	/**
	 * Adapted from:
	 * https://www.quickprogrammingtips.com/java/how-to-securely-store-passwords-in-java.html
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getNewSalt() throws Exception {
		// Don't use Random!
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		// NIST recommends minimum 4 bytes. We use 8.
		byte[] salt = new byte[8];
		random.nextBytes(salt);
		return Base64.getEncoder().encodeToString(salt);
	}

	/**
	 * Returns a pseudorandomly generated 6 digit code.
	 * Random.nextInt(10) is called for each digit.
	 * @return A 6 digit String.
	 */
	public static String getNewEmailConfirmationCode() {
		String code = "";
		Random rand = new Random();
		for(int i = 0; i < 6; ++i) {
			code += ""+rand.nextInt(10);
		}
		return code;
	}

	/**
	 * Name comes from
	 * https://www.quickprogrammingtips.com/java/how-to-securely-store-passwords-in-java.html,
	 * but all content is original
	 * 
	 * @param classes
	 */
	private Account saveUser(Schedule classes) {
		String acc = "insert into Account(email, password_hash, salt, first_name, last_name, email_confirmed, confirmation_code, role) values(?, ?, ?, ?, ?, ?, ?, ?)";
		String sch = "insert into Schedule(email, courseID) values(?, ?)";
		try (
			Connection conn = DataSource.getConnection();
			PreparedStatement ps1 = conn.prepareStatement(acc);
			PreparedStatement ps2 = conn.prepareStatement(sch);
		) {
			ps1.setString(1, email);
			ps1.setString(2, passEncrypted);
			ps1.setString(3, salt);
			ps1.setString(4, firstName);
			ps1.setString(5, lastName);
			ps1.setBoolean(6, isEmailConfirmed);
			ps1.setString(7, confirmationCode);
			ps1.setString(8, role);
			ps1.execute();
			ps2.setString(1, email);

			for (int i = 0; i < classes.getSchedule().size(); i++) {
				ps2.setInt(2, classes.getSchedule().get(i).getId());
				ps2.execute();
			}
		} catch (SQLException s) {
			// if the account succeeds but the schedule fails, the account
			// will still be in there. This is good
			// TODO: make log function
			System.err.println("Failed to add new account");
			s.printStackTrace();
		}
		return this;
	}

	/**
	 * Takes an account object and updates the DB 
	 * based on which fields are different. If none are
	 * 
	 * @param newAccount The account object containing the changes.
	 */
	private void updateUser(Account newAccount) throws SQLException, Exception {
		String sql;
		Connection conn;
		Statement stmt;
		try {
			conn = DataSource.getConnection();
			stmt = conn.createStatement();
			conn.setAutoCommit(false);
			
		} catch (SQLException e) {
			// TODO: make log function
			System.err.println("Failed to get connection. No changes made.");
			throw e;
		}
		try {
			// if actual email is diff, update.
			if(!this.email.equals(newAccount.email)) {
				sql = String.format("UPDATE Account set %s = \"%s\" where email = \"%s\"", EMAIL_FIELD, newAccount.email, this.email);
				stmt.addBatch(sql);
			}
			
			// If password is diff, update
			if (!this.passEncrypted.equals(newAccount.passEncrypted)) {
				sql = String.format("UPDATE Account set %s = \"%s\" where email = \"%s\"", PASSWORD_FIELD, newAccount.passEncrypted, this.email);
				stmt.addBatch(sql);
			}
			
			// if salt is diff, update 
			if(!this.salt.equals(newAccount.salt)) {
				sql = String.format("UPDATE Account set %s = \"%s\" where email = \"%s\"", SALT_FIELD, newAccount.salt, this.email);
				stmt.addBatch(sql);
			}

			// if newEmail is diff, update.
			if(!this.newEmail.equals(newAccount.newEmail)) {
				sql = String.format("UPDATE Account set %s = \"%s\" where email = \"%s\"", NEW_EMAIL_FIELD, newAccount.newEmail, this.email);
				stmt.addBatch(sql);
			}
			
			// email confirmation?
			if(newAccount.emailIsConfirmed()) {
				sql = String.format("UPDATE Account set %s = \"%s\" where email = \"%s\"", EMAIL_CONFIRMATION_FIELD, newAccount.isEmailConfirmed, this.email);
				stmt.addBatch(sql);
			}
			
			// if confirmation code is diff, update
			if(!this.confirmationCode.equals(newAccount.confirmationCode)) {
				sql = String.format("UPDATE Account set %s = \"%s\" where email = \"%s\"", CONFIRMATION_CODE_FIELD, newAccount.confirmationCode, this.email);
				stmt.addBatch(sql);
			}

			// on fail throws exception.
			stmt.executeBatch();

			// if DB updated, update the calling account object.
			this.setEmail(newAccount.email);
			this.setPassEncrypted(newAccount.passEncrypted);
			this.setSalt(newAccount.salt);
			this.setNewEmail(newAccount.newEmail);
			this.setIsEmailConfirmed(newAccount.isEmailConfirmed);
			this.setConfirmationCode(newAccount.confirmationCode);

			conn.commit();
		} catch(SQLException e) {
			System.err.println("Failed to update user account.");
			e.printStackTrace();
			throw e;
		}

		try {
			conn.close();
		} catch(SQLException e) {
			System.err.println("Failed to close connection.");
			e.printStackTrace();
			throw new Exception("Failed to close connection.");
		}
	}

	/**
	 * Checks the given password against the account's password and deletes the given account
	 * from the database if they match, and any schedule associated with it. Throws 
	 * {@code InvalidCredentialException} if the passwords don't match.
	 * @param account The account to delete
	 * @param password The password entered by the user to verify.
	 * @return True if successful, or false if passwords don't match or an exception occurs 
	 * 					during the delete on the db.
	 * @throws NoSuchAlgorithmException			If the hash algorithm for passwords doesn't exist.
	 * @throws InvalidKeySpecException			If the given password can't be hashed.
	 * @throws InvalidCredentialsException 	If the passwords don't match
	 */
	public static boolean deleteAccount(Account account, String password) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidCredentialsException {
		String pHash = getEncryptedPassword(password, account.salt);
		if(!pHash.equals(account.passEncrypted)) {
			throw new InvalidCredentialsException("Passwords do not match");
		}
		// passwords match, continue.
		String sql = "DELETE FROM Account WHERE email = ?";
		try(Connection conn = DataSource.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, account.email);
			ps.execute();
			return true;
		} catch(SQLException e) {
			// TODO: log
			e.printStackTrace();
		}
		return false;		
	}
	/**
	 * Updates the user's password to the given password.
	 * @param password The new password to use.
	 * @throws Exception If the password cannot be hashed, throws an Exception.
	 */
	public void changePassword(String password) throws SQLException, Exception {
		// check password requirements

		// using a new salt doesn't necessarily increase security.
		String encP = getEncryptedPassword(password, this.salt);
		updateUser(new Account(email, encP, salt, firstName, lastName, newEmail, isEmailConfirmed, confirmationCode));
	}

	/**
	 * Updates the user's email and sends them an email
	 * to reflect a successful change. No user action 
	 * necessary.
	 * @param newEmail the email to change to.
	 * @throws Exception If the email is already in use.
	 */
	public void changeEmail(String newEmail) throws InvalidNameException, Exception {
		// Check Email requirements and validation
		if(!isValidEmail(newEmail)) {
			throw new InvalidNameException("Invalid Email");
		}

		if(getAccountDetails(newEmail) == null) {
			// email is available.
			updateUser(new Account(this.email, this.passEncrypted, this.salt, this.firstName, this.lastName, newEmail, false, getNewEmailConfirmationCode()));
			return;
		}
		// TODO: Log
		System.err.println("Email already exists.");
		throw new Exception("That email is already in use!");
	}

	public void confirmEmail(String code) throws SQLException, Exception {
		// if the user provides the right code, update the account
		// with the newEmail as the main email & emailConfirmed as true
		if(code.equals(this.confirmationCode)) {
			updateUser(new Account(newEmail, passEncrypted, salt, firstName, lastName, "", true, ""));
		}
	}

	public static void main(String[] args) throws Exception {
		// String salt = getNewSalt();
		// String password = "changeme"; //<---- password
		// String email = "admin@gmail.com"; //<-----email
		// String role = "admin";//<----- role
		// String encryptedPassword = getEncryptedPassword(password, salt);
		// Account account = new Account(email, encryptedPassword, salt, "admin", "admin", role);
		// account.saveUser(new Schedule());
		// System.out.println("done");
	}
}
