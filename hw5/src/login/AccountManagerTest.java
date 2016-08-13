package login;

import static org.junit.Assert.*;

import org.junit.Test;

public class AccountManagerTest {
	private AccountManager am = new AccountManager();
	
	@Test
	public void hasAccountTest(){
	    assertTrue(am.hasAccount("Patrick"));
	    assertTrue(am.hasAccount("Molly"));
	    assertFalse(am.hasAccount("Bob"));
	    assertFalse(am.hasAccount("FloPup"));
	}
	
	@Test
	public void isCorrectPwText(){
	    assertTrue(am.isCorrectPw("Patrick", "1234"));
	    assertTrue(am.isCorrectPw("Molly", "FloPup"));
	    assertFalse(am.isCorrectPw("Molly", ""));
	    assertFalse(am.isCorrectPw("Bob", ""));
	}
	
	@Test
	public void createAccountTest(){
	    am.createAccount("Bob", "1234");
	    assertTrue(am.hasAccount("Bob"));
	    assertTrue(am.isCorrectPw("Bob", "1234"));
	}
}
