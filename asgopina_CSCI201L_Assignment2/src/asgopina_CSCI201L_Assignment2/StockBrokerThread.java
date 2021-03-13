package asgopina_CSCI201L_Assignment2;

import java.util.List;
import java.util.Map;
//import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StockBrokerThread implements Runnable {
	private Map<String, Integer> tickerToNumBrokers;
	private List<StockTrade> schedule;
	private Lock lock = new ReentrantLock();
	
	public StockBrokerThread(Map<String, Integer> tickerToNumBrokers, List<StockTrade> schedule) {
		this.tickerToNumBrokers = tickerToNumBrokers;
		this.schedule = schedule;
	}
	public void run() {
		boolean flag = false;
		try {
			int maxTime = schedule.get(schedule.size()-1).getStartTime();
			int currTime = 0;
//			while(schedule.size() > 0) {	//while list of transactions is not empty
			for(int i=0; i<schedule.size(); i++) {	
				flag = false;
//				System.out.println("in while loop");
				StockTrade thisTransaction = schedule.get(i);
				String ticker = thisTransaction.getTicker();
				int numStocks = thisTransaction.getNumStocks();
				int numBrokers = tickerToNumBrokers.get(ticker);
				while(currTime != thisTransaction.getStartTime()) {
					currTime ++;
				}
//				System.out.println("entering for loop");
				for(int j=0; j<numBrokers; j++){
//					System.out.println("in inner for loop");
//					System.out.println("about to enter if statement if currTime " + currTime + " equals start time for this transaction " + thisTransaction.getStartTime() + " for " + ticker);
					if(thisTransaction.getStartTime() == currTime) {	//while this transaction's start time == curr time
						flag = true;
						schedule.remove(0);
//						System.out.println("in if statement, broker index = " + j);
						lock.lock();
						if(numStocks < 0) {
							System.out.println(java.time.LocalTime.now() + " Starting sale of " + (-1*numStocks) + " stocks of " + ticker);
							Thread.sleep(1000);
							currTime++;

							Thread.yield();
							System.out.println(java.time.LocalTime.now() + " Finished sale of " + (-1*numStocks) + " stocks of " + ticker);
						}
						else {
							System.out.println(java.time.LocalTime.now() + " Starting purchase of " + numStocks + " stocks of " + ticker);
							Thread.sleep(1000);
							currTime++;

							Thread.yield();
							System.out.println(java.time.LocalTime.now() + " Finished purchase of " + numStocks + " stocks of " + ticker);
						}
						lock.unlock();
					
					}
					else {
//						System.out.println("breaking out of innerfor loop");
						break;
					}
				}	
//				System.out.println("about to exit outer for loop");
//					currTime++;
//				if((currTime < maxTime) && flag) {
//					Thread.yield();
//					currTime ++;
//				}	
			}
				
//			}			
//			lock.unlock();

		} catch(InterruptedException e) {
			System.out.println("Interrupted - " + e.getMessage());
		} catch (IllegalMonitorStateException e) {
			System.out.println("illegal monitor state ");
		}
	}
}
