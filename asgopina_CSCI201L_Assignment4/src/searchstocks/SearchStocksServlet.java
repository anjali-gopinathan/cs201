package searchstocks;

import java.io.Console;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
		String sqlString = "SELECT * FROM Stocks WHERE ticker LIKE '%" + query + "%'";		
		System.out.println("about to execute the following SQL statement: " + sqlString);

		PrintWriter out = response.getWriter();		
		Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        List<Stock> stocks = new ArrayList<Stock>();
		Map<String, Object> datamap;
		Gson gson = new Gson();
		
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("about to make connection");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/salstocks?user=root&password=root");
            System.out.println("made connection");
            st = conn.createStatement();
            rs = st.executeQuery(sqlString);

            while (rs.next()) {
            	Stock stocko = 
            			new Stock(
							rs.getString("name"),
							rs.getString("ticker"),
							rs.getString("startDate"),
							rs.getString("exchangeCode"),
							rs.getString("description"),
							rs.getInt("stockBrokers")
                        );
                stocks.add(stocko);
            }
            
			URL url = new URL("https://api.tiingo.com/tiingo/daily/" + query + "/prices?token=" + MY_TIINGO_TOKEN);
			HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
			httpconn.setRequestMethod("GET");
			httpconn.connect();
			//Getting the response code
			int responsecode = httpconn.getResponseCode();
			String dataString = "";
			if(responsecode == 200) {	// Status: OK
				Scanner urlScanner = new Scanner(url.openStream());
				while(urlScanner.hasNext()) {
					dataString += urlScanner.nextLine();
				}
				urlScanner.close();
				dataString = dataString.replace("[", "").replace("]", "");
				try {
					datamap = gson.fromJson(dataString, Map.class);
				} catch (Exception e) {
					System.out.println("error parsing json from Tiingo api");
					throw new DataFormatException();
				}
			}
			
			else {
			    throw new RuntimeException("HttpResponseCode: " + responsecode);
			}
			System.out.println("Read in from tiingo API stock info on ticker " + query + ". Results:\n");
			System.out.println(dataString);
			System.out.println();
			
			out.println("Read in from tiingo API stock info on ticker " + query + ". Results:\n");
			out.println(dataString);
			out.println();

            
            response.setContentType("application/json");
            out.println(gson.toJson(stocks));
        } catch (SQLException | ClassNotFoundException sqle) {
            System.out.println ("SQLException (likely that value could not be found): " + sqle.getMessage());
        } catch (DataFormatException dfe) {
        	System.out.println("data format issue with Tiingo json. " + dfe.getMessage());
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                System.out.println("sqle: " + sqle.getMessage());
            }
        }
        
        
        
        

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
