package asgopina_CSCI201L_Assignment2;

public class StockTrade {
	private int startTime;
	private String ticker;
	private int numStocks;
	public StockTrade(int startTime, String ticker, int numStocks) {
		this.startTime = startTime;
		this.ticker = ticker;
		this.numStocks = numStocks;
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
	public String toString() {
		return "[startTime: " + this.startTime +
				", ticker: " + this.ticker + 
				", numStocks: " + this.numStocks + "]";
	}
}
