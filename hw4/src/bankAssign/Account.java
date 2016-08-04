package bankAssign;

public class Account {
	private static final int DEFAULT_BALANCE = 1000;
	private int id;
	private int balance;
	private int numTrans;
	
	public Account(int id){
		this.id = id;
		this.balance = DEFAULT_BALANCE;
		this.numTrans = 0;
	}
	
	public synchronized void makeTrans(int amount) {
		numTrans++;
		balance += amount;
	}
	
	@Override
	public synchronized String toString() {
		return "acct:" + id + " bal:" + balance + " trans:" + numTrans;
	}
}
