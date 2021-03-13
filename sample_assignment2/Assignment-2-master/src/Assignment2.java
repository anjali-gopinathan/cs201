import com.google.gson.JsonSyntaxException;

import models.Trade;
import models.Schedule;
import models.Schedule.Task;
import models.Stock;
import models.StockList;
import parser.ScheduleParser;
import util.ScheduleFormatException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class Assignment2 {
    private static Scanner scan;
    private static StockList stockList;
    private static Schedule schedule;
    private static Map<String, Semaphore> companyLocks;

    public static void main(String[] args) throws InterruptedException {
        scan = new Scanner(System.in);
        loadStockFile();
        loadScheduleFile();
        initializeSemaphores();
        executeTrades();
        scan.close();
    }

    private static void loadStockFile() {
        while (true){
            System.out.println("What is the name of the company information file?");
            String fileName = scan.nextLine();
            try {
                 stockList = new StockList(fileName);
                System.out.println("The restaurant file has been properly read.\n");
                break;
            } catch (IOException e) {
                System.out.println("The file " + fileName + " could not be found!\n");
            } catch (ClassCastException | JsonSyntaxException e){
                System.out.println("Data is malformed!\n");
            }
            finally {
                System.out.println(); // Blank line
            }
        }
    }
    
    private static void loadScheduleFile() {
        while(true){
            System.out.println("What is the name of the schedule file?");
            String fileName = scan.nextLine();
            System.out.println(); // Print blank line for spacing
            try {
                schedule = ScheduleParser.loadSchedule(fileName);
                System.out.println("The schedule file has been properly read.\n");
                break;
            } catch (ScheduleFormatException e) {
                System.out.println("Data is malformed!\n");
            } catch (IOException e) {
                System.out.println("The file " + fileName + " could not be found!\n");
            }
        }
    }

    // We set up one Semaphore for each restaurant based on that Restaurant's number of drivers
    private static void initializeSemaphores() {
    	companyLocks = new HashMap<String, Semaphore>();
    	for (Stock s : stockList.getStocks()) {
    		companyLocks.put(s.getTicker(), new Semaphore(s.getStockBrokers()));
    	}
    }

    private static void executeTrades() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        long elapsedTime = 0;
        int nextTaskIndex = 0;

        System.out.println("Starting execution of program...");

        // Can probably use a scheduled thread pool for better design
        ExecutorService executors = Executors.newCachedThreadPool();

        while(true) {
        	while ((elapsedTime / 1000) >= schedule.getTaskList().get(nextTaskIndex).getTime()) {
        		Task task = schedule.getTaskList().get(nextTaskIndex);
        		String ticker = task.getTicker();
        		executors.execute(new Trade(companyLocks.get(ticker), task.getTicker(), task.getTradeQuantity()));

        		nextTaskIndex += 1;
        		if (nextTaskIndex == schedule.getTaskList().size()) {
        			break;
        		}
        	}

        	if (nextTaskIndex == schedule.getTaskList().size()) {
    			break;
    		}

        	Thread.sleep(1000);
        	elapsedTime = System.currentTimeMillis() - startTime;
        }

		executors.shutdown();
		while(!executors.isTerminated()) {
			Thread.yield();
		}

		System.out.println("All trades completed!");
    }
}
