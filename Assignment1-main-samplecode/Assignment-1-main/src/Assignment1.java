import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import models.Stock;
import models.StockList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Assignment1 {
    private static Scanner scan;
    private static String fileName;
    private static StockList stockList;
    private static boolean hasEdits = false;

    public static void main(String[] args) {
        scan = new Scanner(System.in);
        loadStocksFile();
        while (true) {
            mainMenu();
        }
    }

    private static void loadStocksFile() {
        boolean validFile = false;
        do {
            System.out.println("What is the name of the company file?");
            fileName = scan.nextLine();

            try {
                stockList = new StockList(fileName);
                System.out.println("The file has been properly read");
                validFile = true;
            } catch (IOException e) {
                System.out.println("The file " + fileName + " could not be found.\n");
            } catch (ClassCastException | JsonSyntaxException e){
                System.out.println("The file " + fileName + " is not formatted properly.\n");
            }
        }
        while (!validFile);
    }

    private static void mainMenu() {
        String menu = """
                1) Display all public companies
                2) Search for a stock (by ticker)
                3) Search for all stocks on an exchange
                4) Add a new company/stocks
                5) Remove a company
                6) Sort companies
                7) Exit
                What would you like to do?""";

        while (true) {
            int selection = getSelection(menu, 1, 7);
            switch (selection) {
                case 1 -> displayCompanies(stockList.getStocks());
                case 2 -> searchTicker();
                case 3 -> searchExchange();
                case 4 -> addCompany();
                case 5 -> removeCompany();
                case 6 -> sortCompanies();
                case 7 -> exit();
            }
        }
    }

    private static void exit() {
        if (hasEdits) {
            String menu = """
                    Would you like to save your edits?
                    1) Yes
                    2) No""";
            int selection = getSelection(menu, 1, 2);
            if (selection == 1) {
                // Save edits
            	Gson gson = new GsonBuilder().setPrettyPrinting().create();
            	String fileOutput = gson.toJson(stockList);
            	FileWriter fileWriter = null;
            	PrintWriter printWriter = null;

            	try {
            		fileWriter = new FileWriter(fileName);
                    printWriter = new PrintWriter(fileWriter);
                    printWriter.print(fileOutput);
            	}
            	catch (IOException ioe) {
            		ioe.printStackTrace();
            	}
            	finally {
            	    if (fileWriter != null && printWriter != null) {
                        try {
                            fileWriter.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
            	        printWriter.close();
                    }
            	}

                System.out.println("Your edits have been saved to " + fileName);
            }
        }

        // Exit
        System.out.println("Thank you for using my program!");
        System.exit(0);
    }

    private static void addCompany() {
    	boolean invalidName = true;
    	String name = null;

    	while(invalidName) {
    		System.out.println("What is the name of the company you would like to add?");
            name = scan.nextLine();
            if (stockList.hasStock(name)) {
            	System.out.println("There is already an entry for " + name + ".");
            }
            else {
            	invalidName = false;
            }
    	}

    	System.out.println("What is the stock symbol for " + name + "?");
        String symbol = scan.nextLine().toUpperCase();

        System.out.println("What is the start date of " + name + "?");
        String startDate = scan.nextLine();

        String exchange = getExchange(name).toUpperCase();

        System.out.println("What is the description of " + name + "?");
        String description = scan.nextLine();


        // Add new stock and print details
        Stock newStock = new Stock(name, symbol, startDate, exchange, description);
        stockList.getStocks().add(newStock);
        hasEdits = true;

        System.out.println("There is now a new entry for:");
        displayCompany(newStock);
    }

    private static String getExchange(String companyName) {
        while (true) {
            System.out.println("What is the exchange where " + companyName + " is listed?");
            String exchange = scan.nextLine();
            if (exchange.equalsIgnoreCase("NASDAQ") || exchange.equalsIgnoreCase("NYSE")) {
                return exchange;
            }
            else {
                System.out.println("Please enter a valid exchange.");
            }
        }
    }

    private static void removeCompany() {
        if (stockList.getStocks().isEmpty()) {
            System.out.println("There are no companies to remove.");
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        int i = 1;
		for (Stock s: stockList.getStocks()) {
			stringBuilder.append("\t").append(i).append(") ").append(s.getName()).append("\n");
			i++;
		}

		System.out.println("Which company would you like to remove?");
        int selection = getSelection(stringBuilder.toString(), 1, i);

        String name = stockList.getStocks().get(selection - 1).getName();
        stockList.getStocks().remove(selection - 1);

        System.out.println("\n" + name + " is now removed.\n");
        hasEdits = true;
    }

    private static void searchTicker() {
        System.out.println("What is the name of the stock you would like to search for?");
        String query = scan.nextLine().toLowerCase();
        Stock searchResult = stockList.searchStocksByTicker(query);

        if (searchResult != null) {
            displayCompany(searchResult);
        }
        else{
            System.out.println(query + " could not be found.");
        }
    }

    private static void searchExchange() {
        System.out.println("What Stock Exchange would you like to search for");
        String query = scan.nextLine().toLowerCase();
        ArrayList<Stock> searchResult = stockList.searchStocksByExchange(query);

        // Print list as proper comma separated sentence
        if (searchResult.size() == 1) {
            System.out.print(searchResult.get(0).getTicker());
        }
        else if (searchResult.size() > 1) {
            StringBuilder stockString = new StringBuilder();

            for (int i = 0; i < searchResult.size() - 1; i++) {
                stockString.append(" ").append(searchResult.get(i).getTicker()).append(",");
            }

            if (searchResult.size() == 2) {
                stockString = new StringBuilder(stockString.substring(0, stockString.length() - 1));
            }

            stockString.append(" and ").append(searchResult.get(searchResult.size() - 1).getTicker());

            System.out.print(stockString.substring(1));
        }
        else {
            System.out.println(query + " could not be found.");
            return;
        }
        System.out.printf(" found on the %s exchange.\n\n", query);
    }

    /**
     * Prints out companies from input to user in a readable format
     *
     * @param stocks ArrayList of Stocks
     */
    private static void displayCompanies(ArrayList<Stock> stocks) {
        for (Stock s: stocks) {
            System.out.printf("%s, symbol %s, started on %s, listed on %s,\n%s \n\n",
                    s.getName(), s.getTicker(), s.getStartDate(), s.getExchangeCode(), s.getDescription());
        }
    }
    private static void displayCompany(Stock stock) {
        System.out.printf("%s, symbol %s, started on %s, listed on %s,\n%s \n\n",
                stock.getName(), stock.getTicker(), stock.getStartDate(), stock.getExchangeCode(), stock.getDescription());
    }

    /**
     * Prints menu and grabs a valid selection given the menu text and the selection range
     * @param menu Menu string
     * @param min  Minimum selection value
     * @param max  Maximum selection value
     * @return The users selection as an integer
     */
    private static int getSelection(String menu, int min, int max) {
        // Initialize our return value
        int selection = -1;
        boolean validSelection = false;

        // Display the menu to the user
        System.out.println(menu);

        do {
            // Get user selection and check if it's valid
            try {
                String selectionString = scan.nextLine(); // Get selection as a string
                selection = Integer.parseInt(selectionString); // Attempt to parse this to an int
                validSelection = selection >= min && selection <= max; // Validate the selection
            }
            // If parsing the selection to an int failed, we do nothing so the loop repeats
            catch (NumberFormatException ignore) { }
            // Before we potentially loop, we want to alter the user if their option was invalid
            finally {
                if (!validSelection) {
                    System.out.println("That is not a valid option.");
                }
            }
        } while(!validSelection);

        return selection;
    }

    private static void sortCompanies() {
        System.out.println("How would you like to sort by?");
        System.out.println("\n1) A to Z\n2) Z to A");

    	String selection = scan.nextLine();
    	int choice = Integer.parseInt(selection);

    	switch(choice) {
    	case 1:
            stockList.getStocks().sort(Comparator.comparing(Stock::getName));
    		System.out.println("Your restaurants are now sorted from A to Z.");
    		break;
    	case 2:
            stockList.getStocks().sort(Collections.reverseOrder(Comparator.comparing(Stock::getName)));
    		System.out.println("Your restaurants are now sorted from Z to A.");
    		break;
    	default:
    		break;
    	}

    	hasEdits = true;
    }
}
