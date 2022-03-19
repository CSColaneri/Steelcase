import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.security.SecureRandom;

import org.junit.BeforeClass;
import org.junit.Test;

public class AccountTest {
  
  private static Account account;
 
  @Test
  public void testGetNewSalt() throws Exception {
    
  }

  @Test
  public void testGetEncryptedPassword() throws Exception {
    String password = ";jajdjvoe";
    String salt = "aO+k5fGwCVo=";
    String expectedHash = "MadWSBwLgOKB2x2T7j6zYVyrN4Y=";
    assertEquals(Account.getEncryptedPassword(password, salt), expectedHash);
  }
  
  // Fails here, but runs normally, idk why
  @Test
  public void testinvalidCredentials() {
    String invalidPassword = ";alksjd;lasjkd;lakjs";
    assertNull(Account.authenticateUser(account, invalidPassword));
  }

  
  @Test
  public void testSignUp() {

  }

  @Test
  public void testLogin() {

  }
}
