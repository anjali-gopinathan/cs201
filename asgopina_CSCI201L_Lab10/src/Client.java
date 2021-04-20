//import java.io.IOException;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.Vector;
//
//public class Client {
//
//	private Vector<ServerThread1> serverThreads;
//	public Client(int port) {
//		try {
//			System.out.println("Binding to port " + port);
//			ServerSocket ss = new ServerSocket(port);
//			System.out.println("Bound to port " + port);
//			serverThreads = new Vector<ServerThread1>();
//			while(true) {
//				Socket s = ss.accept(); // blocking
//				System.out.println("Connection from: " + s.getInetAddress());
//				ServerThread1 st = new ServerThread1(s, this);
//				serverThreads.add(st);
//			}
//		} catch (IOException ioe) {
//			System.out.println("ioe in ChatRoom constructor: " + ioe.getMessage());
//		}
//	}
//	
//	public void broadcast(String message, ServerThread1 st) {
//		if (message != null) {
//			System.out.println(message);
//			for(ServerThread1 threads : serverThreads) {
//				if (st != threads) {
//					threads.sendMessage(message);
//				}
//			}
//		}
//	}
//	
//	public static void main(String [] args) {
//		Client c = new Client(6789);
//	}
//}
