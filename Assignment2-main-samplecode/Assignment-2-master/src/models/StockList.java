package models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class StockList {
    @SerializedName("data")
    private ArrayList<Stock> stocks;

    public StockList(String fileName) throws IOException, ClassCastException {
        Gson gson = new Gson();
        StockList stockList = gson.fromJson(new FileReader(fileName), StockList.class);

        // Roundabout way and there may be a better option
        this.stocks = stockList.stocks;
    }

    public ArrayList<Stock> getStocks() {
        return stocks;
    }

    //    /**
//     * @param restaurants Arraylist of Restaurant objects
//     * @throws FieldNotFoundException Indicates that a field is missing, probably since it was parsed wrong
//     */
//    private static void validateFields(ArrayList<Restaurant> restaurants) throws FieldNotFoundException {
//        for (Restaurant restaurant: restaurants) {
//            if(restaurant.getName() == null || restaurant.getMenu() == null || restaurant.getAddress() == null ||
//                    restaurant.getLatitude() == null || restaurant.getLongitude() == null) {
//                throw new FieldNotFoundException("Missing data parameters");
//            }
//        }
//    }
}
