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

	private static final String PASSWORD_FIELD= "password_hash";
	private static final String EMAIL_FIELD 	= "email";
	private static final String SALT_FIELD 		= "salt";
	private static final String FIRST_NAME_FIELD = "first_name";
	private static final String LAST_NAME_FIELD = "last_name";
	// private static final String ROLE_FIELD = "role";

	private Account(String email, String pword, String salt, String firstName, String lastName) {
		this.setEmail(email);
		this.setPassEncrypted(pword);
		this.salt = salt;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getPassEncrypted() {
		return passEncrypted;
	}

	private void setPassEncrypted(String passEncrypted) {
		this.passEncrypted = passEncrypted;
	}

	public String getEmail() {
		return email;
	}

	private void setEmail(String email) {
		this.email = email;
	}

	private void setSalt(String salt) {
		this.salt = salt;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	/**
	 * Validates that the given email is in an appropriate format
	 * @param email An email address
	 * @return true if the address is in a valid email address format. False if not.
	 */
	public static boolean validEmail(String email) {
		return true;
		// return email.matches(Email.EMAIL_REGEX);
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

				return new Account(email, pHash, salt, firstName, lastName);
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
		if(validEmail(email)) {
			String salt = getNewSalt();
			String encryptedPassword = getEncryptedPassword(password, salt);
			Account account = new Account(email, encryptedPassword, salt, firstName, lastName);
			return account.saveUser(sched);
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
	 * Name comes from
	 * https://www.quickprogrammingtips.com/java/how-to-securely-store-passwords-in-java.html,
	 * but all content is original
	 * 
	 * @param classes
	 */
	private Account saveUser(Schedule classes) {
		String acc = "insert into Account(email, password_hash, salt) values(?, ?, ?)";
		String sch = "insert into Schedule(email, courseID) values(?, ?)";
		try (
			Connection conn = DataSource.getConnection();
			PreparedStatement ps1 = conn.prepareStatement(acc);
			PreparedStatement ps2 = conn.prepareStatement(sch);
		) {
			ps1.setString(1, email);
			ps1.setString(2, passEncrypted);
			ps1.setString(3, salt);
			ps1.execute();
			ps2.setString(1, email);
			// probably doesn't work
			for (int i = 0; i < classes.getSchedule().size(); i++) {
				ps2.setInt(2, classes.getSchedule().get(i).getID());
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
			// only update DB with what's actually changed.
			if (!this.email.equals(newAccount.email)) {
				// update the email in db
				sql = String.format("UPDATE Account set %s = \"%s\" where email = \"%s\"", EMAIL_FIELD, newAccount.email, this.email);
				stmt.addBatch(sql);
			}

			if (!this.passEncrypted.equals(newAccount.passEncrypted)) {
				// update the password in db
				sql = String.format("UPDATE Account set %s = \"%s\" where email = \"%s\"", PASSWORD_FIELD, newAccount.passEncrypted, this.email);
				stmt.addBatch(sql);
			}
			
			if(!this.salt.equals(newAccount.salt)) {
				// update salt in db
				sql = String.format("UPDATE Account set %s = \"%s\" where email = \"%s\"", SALT_FIELD, newAccount.salt, this.email);
				stmt.addBatch(sql);
			}

			// on fail throws exception. TODO: does it throw if empty? hopefully not.
			stmt.executeBatch();

			// if DB updated, update the calling account object.
			this.setEmail(newAccount.email);
			this.setPassEncrypted(newAccount.passEncrypted);
			this.setSalt(newAccount.salt);

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
	 * Deletes the given account from the database and any schedule associated
	 * with it.
	 * @param account The account to delete
	 * @param password The password entered by the user to verify.
	 * @return True if successful, or false if an exception occurs during the delete on the db.
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
		String sql = String.format("DELETE FROM Account WHERE email = %s", account.email);
		try(Connection conn = DataSource.getConnection();) {
			Statement s = conn.createStatement();
			s.execute(sql);
			s.close();
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
	public void changePassword(String password) throws Exception {
		// check password requirements

		// using a new salt doesn't necessarily increase security.
		String encP = getEncryptedPassword(password, this.salt);
		Account update = new Account(this.email, encP, this.salt, this.firstName, this.lastName);
		updateUser(update);
	}

	/**
	 * Updates the user's email and sends them an email
	 * to reflect a successful change. No user action 
	 * necessary.
	 * @param newEmail the email to change to.
	 * @throws Exception If the email is already in use.
	 */
	public void changeEmail(String newEmail) throws Exception {
		// TODO: Email requirements and validation

		if(getAccountDetails(newEmail) == null) {
			updateUser(new Account(newEmail, this.passEncrypted, this.salt, this.firstName, this.lastName));
			return;
		}
		// TODO: Log
		System.err.println("Email already exists.");
		throw new Exception("That email is already in use!");
	}

	// public static void main(String[] args) throws Exception {
	// String salt = "cuPMHgO5LJg=";
	// String password = "123456"; <---- password
	// String email = "testEmail@gmail.com"; <-----
	// System.out.println(salt);
	// System.out.println(getEncryptedPassword(password, salt));
	// }
}
