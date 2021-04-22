package server.parser;

import models.StockTrade;
import server.util.ScheduleFormatException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ScheduleParser {
    public static ArrayList<StockTrade> loadSchedule(String fileName) throws IOException, ScheduleFormatException, NumberFormatException {
        ArrayList<StockTrade> stockTrades = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(fileName));

        // Read each line
        String line;
        while ((line = br.readLine()) != null) {
            String[] tokens = line.split(",");
            // If the task is valid, add it to trades list
            if (validateTask(tokens)) {
                stockTrades.add(new StockTrade(Integer.parseInt(tokens[0]), tokens[1].trim(), Integer.parseInt(tokens[2]), tokens[3].trim()));
            }
            else {
                throw new ScheduleFormatException("Schedule file not formatted properly!");
            }
        }

        return stockTrades;
    }

    /**
     * Validate a Schedule Task
     * @param tokens String array of tokens
     * @return True is task is valid, else false
     */
    private static boolean validateTask(String[] tokens) {
        // Check that there are exactly 4 tokens, the first one is a number and the second is all characters
        // We don't validate numbers since those will be validated anyways when we call `parseInt`.
        return tokens.length == 4
                && tokens[0].chars().allMatch(Character::isDigit)
                && tokens[1].chars().allMatch(Character::isLetter)
                && tokens[2].chars().noneMatch(Character::isLetter);

        // Explanation for tokens[1].chars().allMatch(Character::isLetter):
        // Get second token from array with [1], convert to a [Stream] with `chars()`, and then check
        // that all the chars are letters with `allMatch(Character:isLetter)`.
    }
}
