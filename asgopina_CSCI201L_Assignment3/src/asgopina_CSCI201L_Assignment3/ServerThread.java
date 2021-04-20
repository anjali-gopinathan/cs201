package asgopina_CSCI201L_Assignment3;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class ServerThread extends Thread {
	private DataOutputStream dos;
	private DataInputStream dis;
	private ObjectOutputStream oos;
	
	private PrintWriter pw;
	private BufferedReader br;
	
	private int serialNumber;
	private int currentBalance;
	
	public ServerThread(Socket s1_streams, Socket s2) {
		serialNumber = 0;
		currentBalance = 0;
		try {
			dos = new DataOutputStream(s1_streams.getOutputStream());
			dis = new DataInputStream(s1_streams.getInputStream());
			oos = new ObjectOutputStream(s1_streams.getOutputStream());
			pw = new PrintWriter(s2.getOutputStream());
			br = new BufferedReader(new InputStreamReader(s2.getInputStream()));
		} catch (IOException ioe) {
			System.out.println("ioe in ServerThread constructor: " + ioe.getMessage());
		}		
	}
	public int getSN() {
		return serialNumber;
	}
	public void sendBalance(int bal) {
		try {
			dos.writeInt(bal);
			dos.flush();
			System.out.println(bal);
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
	}
	public void sendMessage(String msg) {
		pw.print(msg);
		pw.flush();
	}
	
	public void run() {
		boolean goodConnection = false;
		String hostname = "";
		int port = 0;
		
		System.out.println("Welcome to SalStocks v2.0!");
		while(!goodConnection) {
			System.out.print("Enter the server hostname: ");
			
			Scanner scan = new Scanner(System.in);
			hostname = scan.next();
			System.out.print("Enter the server port: ");
			port = scan.nextInt();
			
			System.out.println("Trying to connect to " + hostname + ":" + port);
			try {
				Socket s1 = new Socket(hostname, port);
				Socket s2_ = new Socket(hostname, port);
				System.out.println("Connected to " + hostname + ":" + port);
				// if current num traders is 0 (we're ready to start)

				dis = new DataInputStream(s1.getInputStream());
				currentBalance = dis.readInt();
				br = new BufferedReader(new InputStreamReader(s2_.getInputStream()));
				pw = new PrintWriter(s2_.getOutputStream());
				goodConnection = true;
				System.out.println(currentBalance);
				while(!br.ready()) {
					System.out.println(br.readLine());
				}
			} catch (UnknownHostException e) {
				System.out.println("Unable to connect - " + e.getMessage());
			} catch (IOException ioe) {
				System.out.println(ioe.getMessage());
			} 
		}
		
	}
}
