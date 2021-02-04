package asgopina_CS201L_Assignment1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.DataFormatException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Assignment1 {
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
	private static int getUserChoice(Scanner scan, String prompt, int lowBound, int highBound) {
		int choice = 0;
		do {
			System.out.print(prompt);
			try {
				choice = scan.nextInt();
				scan.nextLine();
				if (choice < lowBound || choice > highBound) {
					System.out.println("\nThat is not a valid option. Please enter an integer between " + lowBound + " and " + highBound + ", inclusive.\n");
					continue;
				}
				else {
					break;
				}
			}
			catch(InputMismatchException e) {
				System.out.println("\nThat is not a valid option.\n");
				scan.next();	//discard non integer input
				continue;
			}
		} while (true);
		return choice;
	}
	private static int choice2_findStockByTicker(List<Stock> stocks, String ticker) {
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
	private static Boolean choice3_findStocksByExchange(List<Stock> stocks, String exchangeTarget) {
		Boolean badInput = false;
		List<String> result = new ArrayList<String>();
		for (int i=0; i<stocks.size(); i++) {
			Stock thisStock = stocks.get(i);
			if (thisStock.getExchangeCode().toLowerCase().equals(exchangeTarget.toLowerCase())) {
				result.add(thisStock.getName());
			}
		}
		badInput = result.size() == 0;		// if no stocks get added to the list,
		
		if(badInput) {
			System.out.println("No exchange named " + exchangeTarget + " found.");
		}
		else {
			String result_commaSeparated = String.join(",", result);
			System.out.println(result_commaSeparated + " found on the " + exchangeTarget + " exchange.");
		}
		
		return badInput;
	}
	private static void choice7_exit(Scanner scan, List<Stock> stocks, String inputFilename) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

		Boolean badInput = false;
		FileWriter wr;
		BufferedWriter bw;
		int choice;
		do {
			System.out.println("\t1) Yes\n\t2) No");
			System.out.println("\nWould you like to save your edits? ");
			if (scan.hasNextInt()) {
				choice = scan.nextInt();
				scan.nextLine();
				if (choice == 1) {
					// save stocks to JSON
					try {
						wr = new FileWriter(inputFilename);
						bw = new BufferedWriter(wr);
						Map<String, List<Stock>> map = new HashMap<String, List<Stock>>();
						map.put("data", stocks);
						gson.toJson(map, bw);
						bw.flush();
						bw.close();
						System.out.println("\nYour edits have been saved to " + inputFilename);
					} catch (IOException e) {
						System.out.println("Error writing to file");
					}	
				}
				else if (choice != 2){
					badInput = true;
				}
			}
			else {
				badInput = true;
			}
		} while(badInput);
		System.out.println("\nThank you for using my program!");
	}
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		Gson gson = new Gson();			

		List<Stock> stocks = new ArrayList<Stock>();
		Boolean badInput;
		FileReader fr;
		BufferedReader br;
		String inputFilename;
		Map<String, List<Map<String, String>>> map;
		List<Map<String, String>> stocks_listOfMaps = null;
		String dateStr = "";
		Boolean done = false;

		do {
			badInput = false;
			System.out.print("What is the name of the company file? ");
			inputFilename = scan.nextLine();		
			try {
				fr = new FileReader(inputFilename);
				br = new BufferedReader(fr);
				// First read in JSON as map with one element (mapping from the single key, "data", to a map of Stock objects)
				try {
					map = gson.fromJson(br, Map.class);
				} catch (Exception e) {
					throw new DataFormatException();
				}
				stocks_listOfMaps = map.get("data"); 
				fr.close();
				br.close();
				
				for(int i=0; i<stocks_listOfMaps.size(); i++) {
					Map<String, String> thisStockMap = stocks_listOfMaps.get(i);
					dateStr = thisStockMap.get("startDate");
					if(!isDateFormat(dateStr)) {
						System.out.println("The startDate " + dateStr + " cannot be converted to the proper date format of YYYY-MM-DD.");
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
				System.out.println("The file " + inputFilename + " is not formatted properly.\n");
				badInput = true;
			}
		} while(badInput);
		System.out.println("The file has been properly read.");

	
		
		do {
			System.out.println(	"\n\t1) Display all public companies\n" +
								"\t2) Search for a stock (by ticker)\n" +
								"\t3) Search for all stocks on an exchange\n" + 
								"\t4) Add a new company/stocks\n" + 
								"\t5) Remove a company\n" + 
								"\t6) Sort companies\n" + 
								"\t7) Exit");
			
			int choice = getUserChoice(scan, "What would you like to do? ", 1, 7);
			switch(choice) {
				case 1:
					// display all public companies
					System.out.println();	//blank line
					for(Stock s : stocks) {
						System.out.println(s);
					}
					break;
				case 2:
					String tickerTarget = null;
					// search for a stock by ticker
					int foundIndex;
					if (stocks.size() > 0) {
						do {
							badInput = false;
							System.out.print("What is the name of the company you would like to search for? ");
							tickerTarget = scan.nextLine().toLowerCase();
							foundIndex = choice2_findStockByTicker(stocks, tickerTarget);
							if (foundIndex == -1) {
								System.out.println(tickerTarget + " could not be found.\n");
								badInput = true;
							}
						} while(badInput);
						Stock foundStock = stocks.get(foundIndex);
						System.out.println("\n" + foundStock.getName() + ", symbol " + foundStock.getTicker() + ", started on " + foundStock.getStartDate() + ", listed on " + foundStock.getExchangeCode());
					}
					else {
						System.out.println("Sorry, there are no stocks.");
					}
					break;
				case 3:
					String exchangeTarget;
					// search for a stock by ticker
					if (stocks.size() > 0) {
						do {
							badInput = false;
							System.out.println("What Stock Exchange would like to search for? ");
							exchangeTarget = scan.nextLine();
							badInput = choice3_findStocksByExchange(stocks, exchangeTarget);
						} while(badInput);
					}
					else {
						System.out.println("Sorry, there are no stocks.");
					}
					
					break;
				case 4:
					// Add a new company/stocks
					String newCompanyName, newTicker, newStartDate, newDescription, newExchangeCode;
					do {
						badInput = false;
						System.out.println("What is the name of the company you would like to add? ");
						newCompanyName = scan.nextLine();
						for (int i=0; i<stocks.size(); i++) {
							Stock thisStock = stocks.get(i);
							if (thisStock.getName().toLowerCase().equals(newCompanyName)) {
								badInput = true;
							}
						}
					} while(badInput);
					
					System.out.println("What is the stock symbol of " + newCompanyName + "? ");
					newTicker = scan.nextLine();
					
					do {
						badInput = false;
						System.out.println("What is the start date of " + newCompanyName + "? ");
						newStartDate = scan.nextLine();
						if (!isDateFormat(newStartDate)) {
							badInput = true;
							System.out.println("The startDate " + newStartDate + " cannot be converted to the proper date format of YYYY-MM-DD.");
						}
					} while(badInput);
					
					System.out.println("What is the exchange where " + newCompanyName + " is listed? ");
					newExchangeCode = scan.nextLine();
					
					System.out.println("What is the description of " + newCompanyName + "? ");
					newDescription = scan.nextLine();
					Stock newStock = new Stock(newCompanyName, newTicker, newStartDate, newDescription, newExchangeCode);
					stocks.add(newStock);
					
					System.out.println("There is now an entry for:");
					System.out.println(newStock);
					break;
				case 5:
					// Remove a company
					for(int i=0; i<stocks.size(); i++) {
						System.out.println((i+1) + ") " + stocks.get(i).getName());
					}
					int removeMe = getUserChoice(scan, "Which company would you like to remove? ", 1, stocks.size());
					String removeMeStockName = stocks.get(removeMe-1).getName();
					stocks.remove(removeMe-1);
					System.out.println(removeMeStockName + " is now removed.");
					break;
				case 6:
					// Sort a company
					List<Stock> stocksCopy = new ArrayList<>(stocks);
					List<String> stocksNamesList = new ArrayList<String>();
					for (int i=0; i<stocks.size(); i++) {
						stocksNamesList.add(stocks.get(i).getName().toLowerCase());
					}
					stocks.clear();					
					System.out.println(	"1) A to Z\n"
									+ 	"2) Z to A\n");
					int sortType = getUserChoice(scan, "How would you like to sort by? ", 1, 2);
					if (sortType == 1) {
						// A to Z
						Collections.sort(stocksNamesList);
						System.out.println("Your companies are now sorted in alphabetical order (A-Z).");
					}
					else if (sortType == 2) {
						// Z to A
						Collections.sort(stocksNamesList, Collections.reverseOrder());
						System.out.println("Your companies are now sorted in reverse alphabetical order (Z-A).");
					}

					for(int i=0; i<stocksCopy.size(); i++) {
						String thisName = stocksNamesList.get(i);
						Stock thisStock = null;
						for(int j=0; j<stocksCopy.size(); j++) {
							if(stocksCopy.get(j).getName().toLowerCase().equals(thisName)) {
								thisStock = stocksCopy.get(j);
								break;
							}
						}
						stocks.add(thisStock);
					}
					
					break;					
				case 7:
					done = true;
					choice7_exit(scan, stocks, inputFilename);
					break;
				default:
					break;
			}
		} while(!done);
	}
}
