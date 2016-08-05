package crackerAssign;

import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class Cracker {
	// Array of chars used to produce strings
	public static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();	
	public static final int MAX_WORKERS = CHARS.length;
	
	/*
	 Given a byte[] array, produces a hex String,
	 such as "234a6f". with 2 chars for each byte in the array.
	 (provided code)
	*/
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}
	
	/*
	 Given a string of hex byte values such as "24a26f", creates
	 a byte[] array of those values, one byte value -128..127
	 for each 2 chars.
	 (provided code)
	*/
	public static byte[] hexToArray(String hex) {
		byte[] result = new byte[hex.length()/2];
		for (int i=0; i<hex.length(); i+=2) {
			result[i/2] = (byte) Integer.parseInt(hex.substring(i, i+2), 16);
		}
		return result;
	}
	
	public static byte[] genHash(String pw) {
			MessageDigest md;
			try {
				md = MessageDigest.getInstance("SHA");
				md.update(pw.getBytes());
			    return md.digest();	
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}		  	     	 
		return null;
	}
	
	public static class Worker extends Thread {
		int step;
		int index;
		int length;
		byte[] hashBytes;
		public Worker(int i, int s, int l, String hash, byte[] hb) {
			index = i;
			step = s;
			length = l;
			hashBytes = hb;
		}

		@Override
		public void run() {
			int start = index * step;
			int tmp = (index + 1) * step;
			int end = (tmp+step > MAX_WORKERS) ? MAX_WORKERS : tmp;
			for (int i = start; i < end; i++)
				crackHash("" + CHARS[i]);
			
			latch.countDown();
		}
		
		public void crackHash(String result) {
			if (result.length() <= length) {
				if (Arrays.equals(hashBytes, genHash(result)))
					System.out.println(result);
				
				for (char ch : CHARS)
					crackHash(result + ch);
			}
		}
	}
	
	static CountDownLatch latch;
	
	public static void main(String[] args) {
		if (args.length == 1) {
			System.out.println(hexToString(genHash(args[0])));
		} else if (args.length == 3) {
			try {
				int maxLength = Integer.parseInt(args[1]);
				int numOfWorkers = Integer.parseInt(args[2]);
				byte[] hashBytes = hexToArray(args[0]);
						
				latch = new CountDownLatch(numOfWorkers);
				
				int step = MAX_WORKERS / numOfWorkers;
				for (int i = 0; i < numOfWorkers; i++)
					new Worker(i, step, maxLength, args[0], hashBytes).start();
				
				try { latch.await(); }
				catch (InterruptedException ignored) {}	
				
				System.out.println("All done");
			} catch (Exception ignored) { hintAndExit(); }
		} else {
			hintAndExit();
		}
	}
	
	private static void hintAndExit() {
		System.out.println("Usage: Cracker password / Cracker pwHash max_length num_of_thread");
		System.exit(1);
	}
	// possible test values:
	// a 86f7e437faa5a7fce15d1ddcb9eaeaea377667b8
	// fm adeb6f2a18fe33af368d91b09587b68e3abcb9a7
	// a! 34800e15707fae815d7c90d49de44aca97e2d759
	// xyz 66b27417d37e024c46526c2f6d358a754fc552f3

}
