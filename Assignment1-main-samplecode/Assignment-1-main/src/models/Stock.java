package models;

import java.time.LocalDate;
import java.util.ArrayList;

public class Stock {
    private String name;
    private String ticker;
    private String startDate;
    private String description;
    private String exchangeCode;

    public Stock(String name, String ticker, String startDate, String exchangeCode, String description) {
        this.name = name;
        this.ticker = ticker;
        this.startDate = startDate;
        this.description = description;
        this.exchangeCode = exchangeCode;
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
}
