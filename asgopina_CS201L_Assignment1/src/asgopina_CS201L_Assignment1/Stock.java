package asgopina_CS201L_Assignment1;

public class Stock {
	private String name;
	private String ticker;
	private String startDate;
	private String description;
	private String exchangeCode;
	
	public Stock() {
		setName(null);
		setTicker(null);
		setStartDate(null);
		setDescription(null);
		setExchangeCode(null);
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
	@Override
	public String toString() {
//		return "Stock [name=" + name + ", ticker=" + ticker + ", startDate=" + startDate + ", description="
//				+ description + ", exchangeCode=" + exchangeCode + "]";
//		String desc_multilineString = WordUtils.wrap(this.description, 80);

		return	this.name + 
				", symbol " + this.ticker + 
				", started on " + this.startDate +
				", listed on " + this.exchangeCode + 
		      	",\n\t" + this.description;

	}


}
