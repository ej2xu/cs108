package bankAssign;

import java.util.*;
import java.util.concurrent.*;
import java.io.*;

public class Bank {
	private static final int BLOCKINGQUEUE_CAPACITY = 100; //a placeholder
	private static final int NUM_ACCOUNT = 20;
	private static final Transaction nullTrans = new Transaction(-1,0,0);
	
	private static BlockingQueue<Transaction> transQueue = new ArrayBlockingQueue<Transaction>(BLOCKINGQUEUE_CAPACITY);
	private static List<Account> accounts;
	private int numOfWorkers;
	
	public Bank(int numOfWorkers) {
		accounts = new ArrayList<Account>();
		this.numOfWorkers = numOfWorkers;
		
		for (int i = 0; i < NUM_ACCOUNT; i++)
			accounts.add(new Account(i));
		for (int i = 0; i < numOfWorkers; i++)
			new Worker().start();
	}
	
	private void loadFile(String file) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line;
			int[] trans;
			while ((line = in.readLine()) != null) {
				trans = parseLine(line);
				transQueue.put(new Transaction(trans[0], trans[1], trans[2]));
			}			
			in.close();
			for(int i = 0; i < numOfWorkers; i++)
                transQueue.put(nullTrans);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private int[] parseLine(String line) {
		String[] transStrs = line.split(" ");
		int[] trans = new int[transStrs.length];
		for (int i = 0; i < transStrs.length; i++)
			trans[i] = Integer.parseInt(transStrs[i]);
		return trans; 
	}
	
	public static class Worker extends Thread {
		@Override
		public void run() {
			Transaction transaction;
			try {
				while (!(transaction = transQueue.take()).equals(nullTrans)) {
					int amount = transaction.getAmount();
					accounts.get(transaction.getTo()).makeTrans(amount);
					accounts.get(transaction.getFrom()).makeTrans(-amount);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			latch.countDown();
		}
	}
	
	static CountDownLatch latch;
	
	public static void main(String[] args) {
		int num;
		try {
			num = Integer.parseInt(args[1]);
			latch = new CountDownLatch(num);
			Bank bank = new Bank(num);
			bank.loadFile(args[0]);
		} catch (Exception e) {
			System.out.println("Usage: Bank filename number_of_thread");
			System.exit(1);
		}
		
		try {
			latch.await();
		}
		catch (InterruptedException ignored) {}
		for (Account a : accounts)
			System.out.println(a);
	}
}
