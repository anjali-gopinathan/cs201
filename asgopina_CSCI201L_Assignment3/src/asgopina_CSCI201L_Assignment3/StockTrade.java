package asgopina_CSCI201L_Assignment3;

import java.io.Serializable;

public class StockTrade implements Serializable{
	private int startTime;
	private String ticker;
	private int numStocks;
	private String date;
	private float balance;
	public StockTrade(int startTime, String ticker, int numStocks, String date, float balance) {
		this.setStartTime(startTime);
		this.setTicker(ticker);
		this.setNumStocks(numStocks);
		this.setDate(date);
		this.setBalance(balance);		
	}
	public int getStartTime() {
		return startTime;
	}
	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}
	public String getTicker() {
		return ticker;
	}
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	public int getNumStocks() {
		return numStocks;
	}
	public void setNumStocks(int numStocks) {
		this.numStocks = numStocks;
	}	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public float getBalance() {
		return balance;
	}
	public void setBalance(float balance) {
		this.balance = balance;
	}	
	public String toString() {
		return "[startTime: " + this.startTime +
				", ticker: " + this.ticker + 
				", numStocks: " + this.numStocks + 
				", date: " + date + 
				", balance: " + balance + 
				"]";
	}


}
