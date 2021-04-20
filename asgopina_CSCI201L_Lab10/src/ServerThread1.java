import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

//import javax.servlet.http.HttpServletRequest;



public class ServerThread1 extends Thread{
	// to do --> private variables for the server thread 
	private BufferedReader br;
	private PrintStream ps;

//	private Client c;
	public ServerThread1(Socket s) {
		try {
			// to do --> store them somewhere, you will need them later 
//			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
//			pw = new PrintStream(new BufferedOutputStream(s.getOutputStream()));
			
			
			// to do --> complete the implementation for the constructor
			
			
//			this.c = c;
			ps = new PrintStream(s.getOutputStream());
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			this.start();		 
//			HttpServletRequest request; 

			
			
		} catch (IOException ioe) {
			System.out.println("ioe in ServerThread constructor: " + ioe.getMessage());
		}
	}
	public void sendMessage(String message) {
		ps.println(message);
		ps.flush();
	}
	// to do --> what method are we missing? Implement the missing method 
	public void run() {
		// parse the request and send the appropriate response back to the clients
		
		String header = "";
		try {
			header = br.readLine();
		} catch(IOException e) {
			System.out.println(e.getMessage());
		}
		String fileName = "";
		if(header != null && !header.equals("")) {
			fileName = header.substring(header.indexOf("GET /") + 5, header.indexOf("HTTP"));
		}
        String fileType = "";
//        while (headerNames.hasMoreElements()) {
//            String key = (String) headerNames.nextElement();
//            String value = request.getHeader(key);

//            map.put(key, value);
//        }
		System.out.println("Received request for: " + fileName);
        if(fileName.indexOf(".jpg") != -1 || fileName.indexOf(".jpeg") != -1) {
        	fileType = "image/jpeg";
        } else if (fileName.indexOf(".png") != -1) {
        	fileType = "image/png";
        } else if (fileName.indexOf(".html") != -1) {
        	fileType = "text/html";
        } else if (fileName.indexOf("css") != -1) {
        	fileType = "text/css";
        } else if (fileName.indexOf("js") != -1) {
        	fileType = "text/js";
        }
        String responseHeader =   "HTTP/1.1 200 OK\r\n"
    							+ "Content-Type: " + fileType + "\r\n\r\n";
        
        String filePath = new File("").getAbsolutePath();
//        Path path = Paths.get("~/Documents/Spring2021/cs201/asgopina_CSCI201L_Lab10/src/test.html");
		String pathString = filePath + "/" + fileName.trim();
		Path path = Paths.get(pathString);
        try {
//			String line = br.readLine();
			InputStream f = new FileInputStream(pathString);
//			c.broadcast(line, this);
			ps.print(responseHeader);
			
			//parse this into a filename, html and image
			byte[] file = Files.readAllBytes(path);
			ps.write(file);
//			int temp;
//			while(temp=f.read())
			
		} catch (IOException ioe) {
			System.out.println("ioe in ServerThread.run(): " + ioe.getMessage());
		}
		// Clients will be connecting to your web server using a web browser
	}
	
	
	
	
}
