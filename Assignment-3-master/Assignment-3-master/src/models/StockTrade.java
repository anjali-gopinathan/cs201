package models;

import java.io.Serializable;

public class StockTrade implements Serializable {
    private final int startTime;
    private final String ticker;
    private final int quantity;
    private final String date;

    public StockTrade(int startTime, String ticker, int quantity, String date) {
        this.startTime = startTime;
        this.ticker = ticker;
        this.quantity = quantity;
        this.date = date;
    }

    public int getStartTime() {
        return startTime;
    }

    public String getTicker() {
        return ticker;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDate() {
        return date;
    }
}
