import static org.junit.Assert.assertNull;
import org.junit.Test;

public class AccountTest {
  
  // Fails here, but runs normally, idk why
  @Test
  public void testinvalidCredentials() {
    String username = "invalid_email@gmail.com", password = "1234";
    assertNull(Account.authenticateUser(username, password));
  }

  @Test
  public void testGetEncryptedPassword() {

  }

  @Test
  public void testGetNewSalt() {

  }

  @Test
  public void testSignUp() {

  }
}
