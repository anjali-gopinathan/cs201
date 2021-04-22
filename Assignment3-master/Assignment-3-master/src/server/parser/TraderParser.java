package server.parser;

import models.Trader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TraderParser {
    public static ArrayList<Trader> loadTraders(String fileName) throws IOException, NumberFormatException {
        ArrayList<Trader> traders = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(fileName));

        // Read each line
        String line;
        while ((line = br.readLine()) != null) {
            String[] tokens = line.split(",");
            // If the task is valid, add it to trades list
            traders.add(new Trader(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1])));
        }

        return traders;
    }
}
