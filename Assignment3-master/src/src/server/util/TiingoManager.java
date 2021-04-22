package server.util;

import client.models.StockPrice;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TiingoManager {
    private static final String API_KEY = "6af808f3212a2076bf8aefb97835022531450322";

    private static String getStockPriceJson(String ticker, String date) throws IOException {
        URL url = new URL(String.format("https://api.tiingo.com/tiingo/daily/%s/prices?startDate=%s&endDate=%s", ticker, date, date));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization","Token " + API_KEY);
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        return in.readLine();
    }

    private static double getPriceFromJson(String json) {
        Gson gson = new Gson();
        StockPrice[] stockPrice = gson.fromJson(json, StockPrice[].class);
        return stockPrice.length == 0 ? 0.0 : stockPrice[0].getClose();
    }

    public static double getStockPrice(String ticker, String date) throws IOException {
        return getPriceFromJson(getStockPriceJson(ticker, date));
    }

//    public static void main(String[] args) throws IOException {
//        getStockPrice("AAPL", "2021-02-19");
//    }
}
