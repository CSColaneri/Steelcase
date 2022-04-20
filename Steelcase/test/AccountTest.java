import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
    assertEquals(expectedHash, Account.getEncryptedPassword(password, salt));
  }
  
  // not testable here without mocking out DB, which is too much rn.
  //@Test
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
