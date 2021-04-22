package searchstocks;

public class Stock {
    private String name;
    private String ticker;
    private String startDate;
    private String description;
    private String exchangeCode;
    private int stockBrokers;

    public Stock(String name, String ticker, String startDate, String exchangeCode, String description, int stockBrokers) {
        this.name = name;
        this.ticker = ticker;
        this.startDate = startDate;
        this.description = description;
        this.exchangeCode = exchangeCode;
        this.stockBrokers = stockBrokers;
    }

    public String getName() {
        return name;
    }

    public String getTicker() {
        return ticker;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getDescription() {
        return description;
    }

    public String getExchangeCode() {
        return exchangeCode;
    }

    public int getStockBrokers() { return stockBrokers; }
}
