import java.util.*;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.mysql.cj.xdevapi.Statement;

	// much of the methods are grabbed from 
	// https://www.quickprogrammingtips.com/java/how-to-securely-store-passwords-in-java.html
	// The encryption is the main reason for using this
public class Account {
	private String email = null;
	private String passEncrypted = null;
	private String salt = null;
	
	Boolean validUser(String email) {
		return false;
	}
	
	Boolean validPassword(String email, String password) {
		return false;
	}
	
	void resetPass(String email) {
		
	}
	
	void signup(String email, String password) throws Exception{
		salt = getNewSalt();
        passEncrypted = getEncryptedPassword(password, salt);
        this.email = email;
        saveUser();
	}
	
	// Get a encrypted password using PBKDF2 hash algorithm
    public String getEncryptedPassword(String password, String salt) throws Exception {
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
    public String getNewSalt() throws Exception {
        // Don't use Random!
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        // NIST recommends minimum 4 bytes. We use 8.
        byte[] salt = new byte[8];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    
    private void saveUser() {
        Statement stmt;
    }
	
	void logout() {
		
	}
	
	void changeEmail() {
		
	}
}
