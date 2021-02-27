package models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.JsonSyntaxException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class StockList {
    @SerializedName("data")
    private ArrayList<Stock> stocks;

    public StockList(String fileName) throws IOException, ClassCastException, JsonSyntaxException {
        Gson gson = new Gson();
        StockList stockList = gson.fromJson(new FileReader(fileName), StockList.class);
        validateFields(stockList.stocks);

        // Roundabout way and there may be a better option
        this.stocks = stockList.stocks;
    }

    /**
     * @param stocks Arraylist of Restaurant objects
     * @throws JsonSyntaxException Indicates that a field was parsed wrong or is incorrect
     */
    private void validateFields(ArrayList<Stock> stocks) throws JsonSyntaxException {
        for (Stock s: stocks) {
            if (!s.getTicker().chars().allMatch(Character::isLetter) ||
                    (!s.getExchangeCode().toUpperCase().equals("NASDAQ") && !s.getExchangeCode().equals("NYSE"))) {
                throw new JsonSyntaxException("Data malformed!");
            }
        }
    }

    public Stock searchStocksByTicker(String ticker) {
        for (Stock stock: this.stocks) {
            if (stock.getTicker().equals(ticker)) return stock;
        }
        return null;
    }

    public ArrayList<Stock> searchStocksByExchange(String exchange) {
        ArrayList<Stock> temp = new ArrayList<>();
        for (Stock stock: this.stocks) {
            if (stock.getExchangeCode().equalsIgnoreCase(exchange)) {
                temp.add(stock);
            }
        }
        return temp;
    }

    public ArrayList<Stock> getStocks() {
        return stocks;
    }

    public boolean hasStock(String companyName) {
        return this.stocks.stream().anyMatch(s -> s.getName().equalsIgnoreCase(companyName));
    }
}
