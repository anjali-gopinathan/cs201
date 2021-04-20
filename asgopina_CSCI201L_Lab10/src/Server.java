import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class Server {
	// to do --> data structure to hold server threads 
	List<Thread> threads = new ArrayList<Thread>();
	static ServerSocket ss;
	
	public Server(int port) {
		// to do --> implement your constructor
		
		try {
			System.out.println("Binding to port 6789");
			ServerSocket ss = new ServerSocket(6789);
			System.out.println("Server bound to port 6789");
		} catch (IOException e) {
			System.out.println("IOE in Server constructor: " + e.getMessage());
		}		
	}
	
	public static void main(String [] args) {
		// to do --> implement your main()
		
		try {
			
//			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
//			PrintWriter pw = new PrintWriter(s.getOutputStream());
//			Scanner scan = new Scanner(System.in);
//			Client c = new Client(6789);
//			while(true) {
//				Socket s = ss.accept();
//				System.out.println("Connection from " + s.getInetAddress());
//				ServerThread1 st1 = new ServerThread1(s, c);
//				String line = br.readLine();
//				System.out.println("Line from Client: " + line);
//				String lineToSend = scan.nextLine();
//				pw.println("Server: " + lineToSend);
//				pw.flush();
//			}
			System.out.println("Binding to port 6777");
			ServerSocket ss = new ServerSocket(6777);
			System.out.println("Server bound to port 6777");
//			Socket s = ss.accept();
//			System.out.println("Connection from " + s.getInetAddress());
//			BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
//			PrintWriter pw = new PrintWriter(s.getOutputStream());
//			Scanner scan = new Scanner(System.in);
			while(true) {
				Socket s1 = ss.accept();
				ServerThread1 st1 = new ServerThread1(s1);
//				String line = br.readLine();
//				System.out.println("Line from Client: " + line);
//				String lineToSend = scan.nextLine();
//				pw.println("Server: " + lineToSend);
//				pw.flush();
			}

			
		} catch (IOException e) {
			System.out.println("IOE in Server constructor: " + e.getMessage());
		}
	}
}
