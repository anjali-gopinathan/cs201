package models;

import java.util.concurrent.Semaphore;

import util.TimestampUtil;

public class Trade extends Thread {
	Semaphore brokerLock;
	String ticker;
	int quantity;
	String action;
	
	public Trade(Semaphore brokerLock, String ticker, int quantity) {
		this.brokerLock = brokerLock;
		this.ticker = ticker;
		this.quantity = Math.abs(quantity);
		this.action = quantity > 0 ? "purchase" : "sale";
	}
	
	public void run() {
		try {
			this.brokerLock.acquire();
			System.out.printf("[%s] Starting %s of %d stocks of %s\n",
					TimestampUtil.getZeroTimestamp(), action, quantity, ticker);
			// Take 1 second to perform this action
			Thread.sleep(1000);
			System.out.printf("[%s] Finished %s of %d stocks of %s\n",
					TimestampUtil.getZeroTimestamp(), action, quantity, ticker);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			this.brokerLock.release();
		}
	}
}
