package login;
import java.util.*;

public class AccountManager {
	private Map<String, String> db;
	public static final String AM_NAME = "Account Manager";
	
	public AccountManager() {
		db = new HashMap<String, String>();
		db.put("Patrick", "1234");
		db.put("Molly", "FloPup");
	}
	
	public boolean hasAccount(String name) {
		return db.containsKey(name);
	}
	
	public boolean isCorrectPw(String name, String pw) {
		return db.containsKey(name) && db.get(name).equals(pw);
	}
	
	public void createAccount(String name, String pw) {
		db.put(name, pw);
	}
}
