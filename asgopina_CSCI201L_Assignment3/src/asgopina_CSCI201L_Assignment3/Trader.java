package asgopina_CSCI201L_Assignment3;

public class Trader {
	private int serialNo;
	private int balance;
	public Trader(int serialNo, int balance) {
		setSerialNo(serialNo);
		setBalance(balance);
	}
	public int getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	
}
