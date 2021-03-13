package asgopina_CSCI201L_Assignment2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.Semaphore;
import java.util.zip.DataFormatException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Assignment2 {
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

		List<Stock> stocks = new ArrayList<Stock>();
		
		List<StockTrade> schedule = new ArrayList<StockTrade>(); 
		
		Boolean badInput = false;
		FileReader frCompany, frSchedule;
		BufferedReader brCompany, brSchedule;
		String companyFileName = "", scheduleFileName = "";
		Map<String, List<Map<String, String>>> map;
		Map<String, Integer> tickerToNumBrokers = new HashMap<String, Integer>();
		List<Map<String, String>> stocks_listOfMaps = null;
		String dateStr = "";
		Boolean done = false;
		int stockBrokers = 0;
		int startTime, numStocks;
		String ticker;
		try {
			do {
				badInput = false;
	            System.out.println("What is the name of the file containing the company information?");
	            companyFileName = scan.nextLine();
				try {
					frCompany = new FileReader(companyFileName);
					brCompany = new BufferedReader(frCompany);
					// First read in JSON as map with one element (mapping from the single key, "data", to a map of Stock objects)
					try {
						map = gson.fromJson(brCompany, Map.class);
					} catch (Exception e) {
						throw new DataFormatException();
					}
					stocks_listOfMaps = map.get("data"); 
					frCompany.close();
					brCompany.close();
	//				System.out.println("stocks_listOfMaps:\n" + stocks_listOfMaps);
					
					for(int i=0; i<stocks_listOfMaps.size(); i++) {
						Map<String, String> thisStockMap = stocks_listOfMaps.get(i);
						dateStr = thisStockMap.get("startDate");
						if(!isDateFormat(dateStr)) {
							System.out.println("The startDate " + dateStr + " cannot be converted to the proper date format of YYYY-MM-DD.");
							throw new DataFormatException();
						}
						Object stockBrokersObject = thisStockMap.get("stockBrokers");
						try {
							double stockBrokersDouble = (double)(stockBrokersObject);
							stockBrokers = (int)(stockBrokersDouble);						
						} catch (Exception e) {
							throw new DataFormatException();
						}
						Stock s = new Stock(thisStockMap.get("name"),
											thisStockMap.get("ticker"),
											dateStr,
											thisStockMap.get("description"),
											stockBrokers,
											thisStockMap.get("exchangeCode"));
						tickerToNumBrokers.put(thisStockMap.get("ticker"), stockBrokers);
						stocks.add(s);
					}
				} catch (IOException e) {
					System.out.println("The file " + companyFileName + " could not be found.\n");
					badInput = true;
				} catch (DataFormatException e) {
					System.out.println("The file " + companyFileName + " is not formatted properly.\n");
					badInput = true;
				}
	
			} while(badInput);
		} finally {
			System.out.println("The file has been properly read.");			
		}
		
		try {
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
		            		startTime = Integer.parseInt(elements[0]);
		            		ticker = elements[1];
		            		numStocks = Integer.parseInt(elements[2]);
	            		} catch(ArrayIndexOutOfBoundsException e) {
	            			throw new DataFormatException();
	            		}
	            		schedule.add(new StockTrade(startTime, ticker, numStocks));
	            		line = brSchedule.readLine();
	            	}
	            	
	            } catch(IOException e) {
	            	System.out.println("The file " + scheduleFileName + " could not be found.\n");
	            	badInput = true;
	            } catch(DataFormatException e) {
					System.out.println("The file " + companyFileName + " is not formatted properly.\n");
					badInput = true;
	            }
			} while(badInput);
		} finally {
			System.out.println("The file has been properly read.");
		}				
		scan.close();
		
		System.out.println("Stocks:\n" + stocks);
		System.out.println("Schedule:\n" + schedule);
		
		System.out.println("Starting execution of program...");
//		execute(stocks, schedule);
//		for(int i=0; i<5; i++) {
//			Thread t = new Thread(new StockBrokerThread)
//		}
//		try {
//			ExecutorService executor = Executors.newCachedThreadPool();
			ScheduledThreadPoolExecutor scheduler = (ScheduledThreadPoolExecutor)Executors.newScheduledThreadPool(1);
			StockBrokerThread sbt = new StockBrokerThread(tickerToNumBrokers, schedule);
			Thread t = new Thread(sbt);
			t.start();
			scheduler.execute(sbt);
			scheduler.shutdown();

			System.out.println("All trades completed!");
	}

}
