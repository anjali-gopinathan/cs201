package searchstocks;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.DataFormatException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
/**
 * Servlet implementation class SearchStocksServlet
 */
@WebServlet("/SearchStocksServlet")
public class SearchStocksServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String MY_TIINGO_TOKEN = "422d5f8027603fc99cc4579651da55c4ff933ede";

       
    /**
     * @see HttpServlet#HttpServlet()
     */
//    public SearchStocksServlet() {
//        super();
//        // TODO Auto-generated constructor stub
//    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		String query = request.getParameter("searchquery");
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		LocalDateTime now = LocalDateTime.now().minusDays(4);
		String todayDate = dtf.format(now);  
		String stockPriceJson = getStockPriceJson(query, todayDate);
		System.out.println("Received stock price json from Tiingo w query=" + query + " and todayDate= " + todayDate + ":\n");
		System.out.println(stockPriceJson);
		response.setContentType("application/json");
		response.getWriter().write(stockPriceJson);
	}    
	private static String getStockPriceJson(String ticker, String date) throws IOException {
        
		try {
			String urlString = String.format("https://api.tiingo.com/tiingo/daily/%s/prices?startDate=%s&endDate=%s", ticker, date, date);
			System.out.println("URL string:\t" + urlString);
        	URL url = new URL(urlString);
    		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization","Token " + MY_TIINGO_TOKEN);
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            return in.readLine();
        } catch(IOException ioe) {
        	System.out.println(ioe.getMessage());
        }
		return "[]";
        
    }
//
//	/**
//	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
//	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		PrintWriter out = response.getWriter();
//		String query = request.getParameter("searchquery");
//		
//		response.setContentType("application/json");
//		out.println("{");
//		out.println("\"Search query\":\"" + query + "\"");
//		out.println("}");
//	}

}
