package asgopina_CS201L_Assignment1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.Map;
import java.util.Scanner;
import java.util.List;

import com.google.gson.Gson;

public class Assignment1 {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scan = new Scanner(System.in);
		
		System.out.print("What is the name of the company file?");
		String inputFilename = scan.next();
		Gson gson = new Gson();			
		
		try {
			
			FileReader fr = new FileReader(inputFilename);
			BufferedReader br = new BufferedReader(fr);
			
			// First read in json as map with one element (mapping from the single key, "data", to the list of Stock objects)
//			java.lang.reflect.Type obj = new TypeToken<List<Stock>>() {}.getType();

			Map<String, List<Stock>> map = gson.fromJson(br, Map.class);
			fr.close();

			br.close();

			
//			String stocksString = map.get("data");
//			List<Stock> stocks = gson.fromJson(stocksString, new TypeToken<List<Stock>>() {}.getType());
			List<Stock>stocks = map.get("data"); 
//			System.out.println("Stocks:\n" + stocks);
			for(int i=0; i<stocks.size(); i++) {
				System.out.println(stocks.get(i).toString());
			}
//			for(Iterator<Stock> s = stocks.iterator(); s.hasNext();) {
//				Stock stock = s.next();
//				System.out.println(stock.toString());
//			}
//			
		} catch (FileNotFoundException fnfe) {
			System.out.println("Input file could not be found: " + fnfe.getMessage());
		} catch (IOException ioe) {
			System.out.println("IOException occurred: " + ioe.getMessage());	
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("The file has been properly read.\n");
		}
		
		System.out.print(		"\t1) Display all public companies\n" +
								"\t2) Search for a stock (by ticker)\n" +
								"\t3) Search for all stocks on an exchange\n" + 
								"\t4) Add a new company/stocks\n" + 
								"\t5) Remove a company\n" + 
								"\t6) Sort companies\n" + 
								"\t7) Exit\n" + 
							"What would you like to do?");
		int choice;
//		ds
		

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
	public String toString() {
//		return		"name: " + this.name + "\n" + 
//			      	"ticker: " + this.ticker + "\n" + 
//			      	"startDate: " + this.startDate + "\n" + 
//			      	"description: " + this.description + "\n" + 
//			      	"exchangeCode: " + this.exchangeCode;
		String desc_multilineString = this.description.replaceAll("(?m)^", "\t");
		return		this.name + 
					", symbol " + this.ticker + 
					", started on " + this.startDate +
					", listed on" + this.exchangeCode + 
			      	",\n\t" + desc_multilineString;
	}
}
