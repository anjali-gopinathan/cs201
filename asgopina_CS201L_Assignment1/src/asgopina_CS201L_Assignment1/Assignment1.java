package asgopina_CS201L_Assignment1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.DataFormatException;

import com.google.gson.Gson;

public class Assignment1 {
	private static Boolean isDateFormat(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");
		Boolean isDate = true;
		try {
			Date d = sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			isDate = false;
//			e.printStackTrace();
		}
		return isDate;
	}
	private static List<Stock> getStocksList(Scanner scan){
		List<Stock> stocks = new ArrayList<Stock>();
		Gson gson = new Gson();			
		Boolean badInput;
		FileReader fr;
		BufferedReader br;
		String inputFilename;
		Map<String, List<Map<String, String>>> map;
		List<Map<String, String>> stocks_listOfMaps = null;
		String dateStr = "";
		do {
			badInput = false;
			System.out.print("What is the name of the company file? ");
			inputFilename = scan.next();		
			try {
				fr = new FileReader(inputFilename);
				br = new BufferedReader(fr);
				// First read in json as map with one element (mapping from the single key, "data", to a map of Stock objects)
				map = gson.fromJson(br, Map.class);
				stocks_listOfMaps = map.get("data"); 
				fr.close();
				br.close();
				
				for(int i=0; i<stocks_listOfMaps.size(); i++) {
					Map<String, String> thisStockMap = stocks_listOfMaps.get(i);
					dateStr = thisStockMap.get("startDate");
					if(!isDateFormat(dateStr)) {
						throw new DataFormatException();
					}
					Stock s = new Stock(thisStockMap.get("name"),
										thisStockMap.get("ticker"),
										dateStr,
										thisStockMap.get("description"),
										thisStockMap.get("exchangeCode"));
					stocks.add(s);
				}
				
				
			} catch (IOException e) {
				System.out.println("The file " + inputFilename + " could not be found.\n");
				badInput = true;
			} catch (DataFormatException e) {
				// TODO Auto-generated catch block
				System.out.println("The startDate " + dateStr + " cannot be converted to the proper date format of YYYY-MM-DD.");
				badInput = true;
				//				e.printStackTrace();
			}
		} while(badInput);
		System.out.println("The file has been properly read.\n");

//			System.out.println("Stocks:\n" + stocks);

		return stocks;
	}
	private static int getUserChoice(Scanner scan) {
		int choice = 0;
		do {
			System.out.print("What would you like to do? ");
			try {
				choice = scan.nextInt();
				if (choice < 1 || choice > 7) {
					System.out.println("\nThat is not a valid option. Please enter an integer between 1 and 7, inclusive.\n");
					continue;
				}
				else {
					break;
				}
			}
			catch(InputMismatchException e) {
				System.out.println("\nThat is not a valid option.\n");
				scan.next();	//discard non-int input
				continue;
			}
		} while (true);
		return choice;
	}
	private static int findStockByTicker(List<Stock> stocks, String ticker) {
		int idx = -1;
		// linear search to find matching ticker symbol
		for(int i=0; i<stocks.size(); i++) {
			if(stocks.get(i).getTicker().toLowerCase().equals(ticker)) {
				idx = i;
				break;	//allowing duplicates? 1002
			}
		}
		return idx;
	}
	public static void main(String[] args) {
//		Scanner scan = new Scanner(System.in);
		Scanner scan = new Scanner(System.in);

//		System.out.print("What is the name of the company file? ");
//		String inputFilename = scan.next();
		List<Stock> stocks = getStocksList(scan);
		
		System.out.println(	"\t1) Display all public companies\n" +
							"\t2) Search for a stock (by ticker)\n" +
							"\t3) Search for all stocks on an exchange\n" + 
							"\t4) Add a new company/stocks\n" + 
							"\t5) Remove a company\n" + 
							"\t6) Sort companies\n" + 
							"\t7) Exit");
		
		int choice = getUserChoice(scan);
		Boolean badInput;
		String tickerTarget = null;
		switch(choice) {
			case 1:
				// display all public companies
				for(Stock s : stocks) {
					System.out.println(s);
				}
				break;
			case 2:
				// search for a stock by ticker
				int foundIndex;
				do {
					badInput = false;
					System.out.println("What is the name of the company you would like to search for? ");
					tickerTarget = scan.next().toLowerCase();
					foundIndex = findStockByTicker(stocks, tickerTarget);
					if (foundIndex == -1) {
						System.out.println(tickerTarget + " could not be found.\n");
						badInput = true;
					}
				} while(badInput);
				Stock foundStock = stocks.get(foundIndex);
				System.out.println(foundStock.getName() + ", started on " + foundStock.getStartDate() + ", listed on " + foundStock.getExchangeCode());
				break;
//			case 7:
			default:
				break;
				
		}
	}

}
class Stock{
	private String name;
	private String ticker;
	private String startDate;
	private String description;
	private String exchangeCode;
	
	public Stock() {
		
	}
	public Stock(String name, String ticker, String startDate, String description, String exchangeCode) {
		setName(name);
		setTicker(ticker);
		setStartDate(startDate);
		setDescription(description);
		setExchangeCode(exchangeCode);
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setExchangeCode(String exchangeCode) {
		this.exchangeCode = exchangeCode;
	}
	public String getName() {
		return this.name;
	}
	public String getTicker() {
		return this.ticker;
	}
	public String getStartDate() {
		return this.startDate;
	}
	public String getDescription() {
		return this.description;
	}
	public String getExchangeCode() {
		return this.exchangeCode;
	}
	@Override
	public String toString() {
//		return "Stock [name=" + name + ", ticker=" + ticker + ", startDate=" + startDate + ", description="
//				+ description + ", exchangeCode=" + exchangeCode + "]";
//		String desc_multilineString = WordUtils.wrap(this.description, 80);

		return	this.name + 
				", symbol " + this.ticker + 
				", started on " + this.startDate +
				", listed on " + this.exchangeCode + 
		      	",\n\t" + this.description;

	}

}
