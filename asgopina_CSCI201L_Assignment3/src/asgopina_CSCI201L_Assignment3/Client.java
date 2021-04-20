package asgopina_CSCI201L_Assignment3;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Client implements Runnable {
	// represents a trader
	private List<StockTrade> schedule;
//	private Lock lock = new ReentrantLock();

//	private BufferedReader br;
//	private PrintWriter pw;
//	private DataInputStream dis;
//	private int currentBalance;

	public Client() {
	
//		String hostname;
//		int port;
//		try {
			this.schedule = schedule;
//			System.out.println("Welcome to SalStocks v2.0!");
//			System.out.print("Enter the server hostname: ");
//			Scanner scan = new Scanner(System.in);
//			hostname = scan.next();
//			System.out.print("Enter the server port: ");
//			port = scan.nextInt();
//			
//			System.out.println("Trying to connect to " + hostname + ":" + port);
//			Socket s = new Socket(hostname, port);
//			System.out.println("Connected to " + hostname + ":" + port);
//			// if current num traders is 0 (we're ready to start)
//			this.start();
//			while(true) {
//				String line = scan.nextLine();
//				pw.println("Donald: " + line);
//				pw.flush();
//			}
			
//		} catch (IOException ioe) {
//			System.out.println("ioe in Client constructor: " + ioe.getMessage());
//		}
	}
	public void run() {
		boolean flag = false;
		try {
			System.out.println(java.time.LocalTime.now() + " Starting sale of [a few] stocks of [some ticker]");
			Thread.sleep(1000);

			int maxTime = schedule.get(schedule.size()-1).getStartTime();
			int currTime = 0;
//			while(schedule.size() > 0) {	//while list of transactions is not empty
			for(int i=0; i<schedule.size(); i++) {	
				flag = false;
//				System.out.println("in while loop");
				StockTrade thisTransaction = schedule.get(i);
				String ticker = thisTransaction.getTicker();
				int numStocks = thisTransaction.getNumStocks();
				int numBrokers = 2;
				while(currTime != thisTransaction.getStartTime()) {
					currTime ++;
				}
//				System.out.println("entering for loop");
				for(int j=0; j<numBrokers; j++){
//					System.out.println("in inner for loop");
//					System.out.println("about to enter if statement if currTime " + currTime + " equals start time for this transaction " + thisTransaction.getStartTime() + " for " + ticker);
					if(thisTransaction.getStartTime() == currTime) {	//while this transaction's start time == curr time
						flag = true;
						schedule.remove(0);
//						System.out.println("in if statement, broker index = " + j);
//						lock.lock();
						if(numStocks < 0) {
							System.out.println(java.time.LocalTime.now() + " Starting sale of " + (-1*numStocks) + " stocks of " + ticker);
							Thread.sleep(1000);
							currTime++;

							Thread.yield();
							System.out.println(java.time.LocalTime.now() + " Finished sale of " + (-1*numStocks) + " stocks of " + ticker);
						}
						else {
							System.out.println(java.time.LocalTime.now() + " Starting purchase of " + numStocks + " stocks of " + ticker);
							Thread.sleep(1000);
							currTime++;

							Thread.yield();
							System.out.println(java.time.LocalTime.now() + " Finished purchase of " + numStocks + " stocks of " + ticker);
						}
//						lock.unlock();
					
					}
					else {
						break;
					}
				}	
			}
		} catch(InterruptedException e) {
			System.out.println("Interrupted - " + e.getMessage());
		} catch (IllegalMonitorStateException e) {
			System.out.println("illegal monitor state ");
		}
		
	}
	public static void main(String [] args) {
		BufferedReader br;
		PrintWriter pw;
//		DataInputStream dis;
//		InputStream is;
		ObjectInputStream ois;
//		int currentBalance;
		Client c = new Client();
		List<StockTrade> schedule = new ArrayList<>();		// = new ArrayList<StockTrade>(); 
		Socket s = null;
		boolean connected = false;
		try {
			System.out.println("trying to connect to localhost port 3456...");
			s = new Socket("localhost",3456);
		} catch (IOException ioe) {
			System.out.println("ioe in Client main: " + ioe.getMessage());
			ioe.printStackTrace();
		} finally {
			System.out.println("Connected to localhost at port 3456!");
			connected = true;
		}
		while(connected) {
			try {
				ois = new ObjectInputStream(s.getInputStream());
		//		currentBalance = dis.readInt();
		
		//        InputStream inputStream = s.getInputStream();
		        // create a DataInputStream so we can read data from it.
		//        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
				System.out.println("got inputstream");
				if (ois.readObject() != null) {
					schedule = (List<StockTrade>) ois.readObject();
				}
			} catch (SocketException e) {
				System.out.println("socketexception: " + e.getMessage());
				connected = false;
			} catch (IOException ioe) {
				System.out.println("ioe in reading from inputstream: " + ioe.getMessage());
				ioe.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				System.out.println("ClassNotFound Exception in Client main: " + e.getMessage());
				e.printStackTrace();
			}
		}
		System.out.println("received schedule of " + schedule.size() + " elements:");
//		System.out.println();
		schedule.forEach(st_temp -> System.out.println(st_temp.toString()));
	}
}

