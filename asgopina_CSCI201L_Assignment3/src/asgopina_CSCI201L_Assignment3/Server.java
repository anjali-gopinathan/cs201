package asgopina_CSCI201L_Assignment3;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.DataFormatException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

public class Server {
	// represents the trading platform
	// My Tiingo API token: 422d5f8027603fc99cc4579651da55c4ff933ede
	public static final String MY_TIINGO_TOKEN = "422d5f8027603fc99cc4579651da55c4ff933ede";
//		URL url = new URL("https://api.tiingo.com/api/test?token=422d5f8027603fc99cc4579651da55c4ff933ede");
	private static Boolean isDateFormat(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Boolean isDate = true;
		try {
			sdf.parse(dateStr);
		} catch (ParseException e) {
			isDate = false;
		}
		return isDate;
	}

	public static void main(String [] args) {
		Scanner scan = new Scanner(System.in);
		Gson gson = new Gson();		
//		JsonArray jsonarray;
//		JsonObject jo;
		FileReader frSchedule, frTraders;
		BufferedReader brSchedule, brTraders;
		Boolean badInput = false;
		String scheduleFileName = "", tradersFileName = ""; 		

		int startTime, numStocks;
		String ticker, dateStr;
		
		List<StockTrade> schedule = new ArrayList<StockTrade>(); 
		List<Trader> traders = new ArrayList<Trader>(); 
		
		int traderSerialNo, traderStartingBal, numTraders = 0;
		double stockPrice=0.0;
		String dataString = "";
//		List<Object> rawdata;
//		String rawdata;
		Map<String, Object> datamap;
		Map<String, Float> tickerToStockPriceMap = new HashMap<String, Float>();

		
		try {			// read in schedule file
			do {
				badInput = false;
	            System.out.println("What is the name of the file containing the schedule information?");
	            scheduleFileName = scan.nextLine();
	            try {
	            	frSchedule = new FileReader(scheduleFileName);
	            	brSchedule = new BufferedReader(frSchedule);
	            	String line = brSchedule.readLine();
	            	
	            	while(line != null) {
	            		String[] elements = line.split(","); 
	            		
	            		try {
		            		startTime = Integer.parseInt(elements[0]);		// start time, convert string to int
		            		ticker = elements[1];		// ticker symbol as string
		            		numStocks = Integer.parseInt(elements[2]);
		            		dateStr = elements[3];
							if(!isDateFormat(dateStr)) {
								System.out.println("The date " + dateStr + " cannot be converted to the proper date format of YYYY-MM-DD.");
								throw new DataFormatException();
							}
	            		} catch(ArrayIndexOutOfBoundsException e) {
	            			throw new DataFormatException();
	            		}
	            		schedule.add(new StockTrade(startTime, ticker, numStocks, dateStr, 0));
	            		line = brSchedule.readLine();
	            	}
	            	
	            } catch(IOException e) {
	            	System.out.println("The file " + scheduleFileName + " could not be found.\n");
	            	badInput = true;
	            } catch(DataFormatException e) {
					System.out.println("The file " + scheduleFileName + " is not formatted properly.\n");
					badInput = true;
	            }
			} while(badInput);
		} catch (Exception e) {
			System.out.println("ioe: " + e.getMessage());
		} finally {
			System.out.println("The file " + scheduleFileName + " has been properly read.");			
		}

		System.out.println("schedule read in:");
		schedule.forEach(st_temp -> System.out.println(st_temp.toString()));

		try {			// read in traders file
			do {
				badInput = false;
	            System.out.println("What is the name of the file containing the stock traders' information?");
	            tradersFileName = scan.nextLine();
	            try {
	            	frTraders = new FileReader(tradersFileName);
	            	brTraders = new BufferedReader(frTraders);
	            	String line = brTraders.readLine();
	            	
	            	while(line != null) {
	            		String[] elements = line.split(","); 
	            		
	            		try {
		            		traderSerialNo = Integer.parseInt(elements[0]);		// trader's serial number, convert string to int
		            		traderStartingBal = Integer.parseInt(elements[1]);
	            		} catch(ArrayIndexOutOfBoundsException e) {
	            			throw new DataFormatException();
	            		}
	            		traders.add(new Trader(traderSerialNo, traderStartingBal));
	            		numTraders++;
	            		line = brTraders.readLine();
	            	}
	            	
	            } catch(IOException e) {
	            	System.out.println("The file " + tradersFileName + " could not be found.\n");
	            	badInput = true;
	            } catch(DataFormatException e) {
					System.out.println("The file " + tradersFileName + " is not formatted properly.\n");
					badInput = true;
	            }
			} while(badInput);
		} catch (Exception e) {
			System.out.println("ioe: " + e.getMessage());
		} finally {
			System.out.println("The file " + tradersFileName + " has been properly read.");			
		}

		System.out.println("Num traders = " + numTraders + "\n");

		
		try {

			try {
				for(int i=0; i<schedule.size(); i++) {
					ticker = schedule.get(i).getTicker();
					if(!tickerToStockPriceMap.containsKey(ticker)) {		// if we haven't already gotten the stockprice for this ticker symbol
//						System.out.println("in for loop about to request from tiingo. tickerToStockPriceMap does not contain key " + ticker);
						dateStr = schedule.get(i).getDate();
						URL url = new URL("https://api.tiingo.com/tiingo/daily/" + ticker + "/prices?startDate=" + dateStr + "&endDate=" + dateStr + "&token=" + MY_TIINGO_TOKEN);
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setRequestMethod("GET");
						conn.connect();
						//Getting the response code
						int responsecode = conn.getResponseCode();
						dataString = "";
						if(responsecode == 200) {	// Status: OK
							Scanner urlScanner = new Scanner(url.openStream());
							while(urlScanner.hasNext()) {
								dataString += urlScanner.nextLine();
							}
							urlScanner.close();
						}
						else {
						    throw new RuntimeException("HttpResponseCode: " + responsecode);
						}
						System.out.println("Read in from tiingo API stock info on ticker " + ticker + " on date " + dateStr + ". Results:\n");
						System.out.println(dataString);
						System.out.println();
						dataString = dataString.replace("[", "").replace("]", "");
						try {
//							rawdata = gson.fromJson(dataString, List.class);
//							jsonarray = gson.fromJson(dataString, JsonArray.class);
							datamap = gson.fromJson(dataString, Map.class);
//							rawdata = gson.fromJson(dataString, List.class);
						} catch (Exception e) {
							System.out.println("error parsing json");
							throw new DataFormatException();
						}
//						System.out.println("about to extract stockprice adjClose");
//						Map<String, Object> datamap = new LinkedTreeMap<String, Object>(rawdata.get(0));
//						LinkedTreeMap<String, Object> datamap = (LinkedTreeMap<String, Object>) rawdata.get(0);
//						jo = jsonarray.get(0).getAsJsonObject();
						stockPrice = (double) datamap.get("adjClose");
//						stockPrice = (double) datamap.get("adjClose");	//error line
						
//						JsonObject jo = (JsonObject) jsonlist.get(0);
						
//						stockPrice = (String) jo.getAsJsonObject("adjClose");
//						stockPrice = jo.getAsJsonPrimitive("adjClose").getAsDouble();
						
//						System.out.println("found that ticker " + ticker + " has stockPrice " + stockPrice + " . adding this pair to tickerToStockPriceMap.");
						tickerToStockPriceMap.put(ticker, (float)stockPrice);
					}
				}
//				URL url = new URL("https://api.tiingo.com/api/test?token=422d5f8027603fc99cc4579651da55c4ff933ede");
			} catch (IOException ioe) {		// IOException includes MalformedURLException
				System.out.println("Bad URL");
			} catch (RuntimeException re) {
				System.out.println(re.getMessage());
			} catch (DataFormatException e) {
				System.out.println("The file from tiingo is not formatted properly.\n");
			}
			System.out.println("tickerToStockPriceMap, size = " + tickerToStockPriceMap.size());
			tickerToStockPriceMap.forEach((ticker_temp, stockPrice_temp) -> System.out.println(ticker_temp + " : "  + stockPrice_temp));
			System.out.println("done printing tickerToStockPriceMap.");
			// go through schedule list
			//now go through schedule (list of stocktrades) and update balance to be equal to the current (numstocks * stockprice)
//			 where stockprice is obtained from the Tiingo API, now stored in the tickerToStockPriceMap data structure
			
			for(int i=0; i < schedule.size(); i++) {
				StockTrade updatedStockTrade = schedule.get(i);
				float newBalance = (float) (updatedStockTrade.getNumStocks()) * tickerToStockPriceMap.get(updatedStockTrade.getTicker());
				updatedStockTrade.setBalance(newBalance);
				schedule.set(i, updatedStockTrade);
			}
			System.out.println("schedule read in:");
			schedule.forEach(st_temp -> System.out.println(st_temp.toString()));
			System.out.println("\n\n");
			
			
			System.out.println("Listening on port 3456.");
			System.out.println("Waiting for traders...");
			ServerSocket ss = new ServerSocket(3456);
	//		System.out.println("Server bound to port 6789");

			for(int i=0; i<numTraders; i++) {
				Socket s = ss.accept();
		        System.out.println("created socket");
				// get the output stream from the socket.
//		        OutputStream os = s.getOutputStream();
		        // create an object output stream from the output stream so we can send an object through it
		        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
		        System.out.println("created obj output stream");
		        oos.writeObject(schedule);
		        System.out.println("Just sent schedule object to socket.");
		        
		        System.out.println("Connection from " + s.getInetAddress());
				ServerThread st = new ServerThread(s, new Socket(s.getInetAddress(), 3456));
//				st.sendBalance(traders.get(i).getBalance());
//				DataOutputStream dos = new DataOutputStream(os);
//				dos.writeInt(traders.get(i).getBalance());
				if (i < numTraders - 1)
					System.out.println("Waiting for " + (numTraders-1) + " more trader(s)");
				else	// i = numTraders - 1.
					System.out.println("Starting service.");
			}
//				BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
//				PrintWriter pw = new PrintWriter(s.getOutputStream());
	//		while(true) {
	//			String line = br.readLine();
	//			System.out.println("Line from Client: " + line);
	//			String lineToSend = scan.nextLine();
	//			pw.println("Server: " + lineToSend);
	//			pw.flush();
	//		}
		} catch (IOException ioe) {
			System.out.println("ioe: " + ioe.getMessage());
		}
		scan.close();
	}

}
