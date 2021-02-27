package asgopina_CSCI201L_Assignment2;

import java.io.IOException;
import java.util.Scanner;

public class Assignment2 {
    private static Scanner scan;
    private static String fileName;
    private static StockList stockList;
    private static boolean hasEdits = false;
    
    public static void main(String [] args) {
		
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
}
