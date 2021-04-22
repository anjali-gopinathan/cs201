package client;

import models.Message;
import models.StockTrade;
import util.TimestampUtil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Client {
    private final Scanner scan = new Scanner(System.in);
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private HashMap<String, Double> stockPrices;
    private Instant startTime;

    public Client() {
        // Prompt user for parameters
        String hostname = getHostname();
        int port = getPort();

        // Bind to socket given the user parameters
        try {
            Socket socket = new Socket(hostname, port);
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        }
        catch (IOException e) {
            System.out.println("ioe in ChatClient constructor: " + e.getMessage());
        }
    }

    /**
     * Sends a message to the Server
     * @param message The message to send to the Server
     */
    private void sendMessage(Message<?> message) throws IOException {
        outputStream.writeObject(message);
        outputStream.flush();
    }

    /**
     * Get the hostname of server from user
     * @return Hostname of server
     */
    private String getHostname() {
        String hostname;
        while(true) {
            System.out.println("Enter the server hostname:");
            hostname = scan.nextLine();

            // If hostname string is not empty, beak out of this infinite while loop
            if (!hostname.trim().isEmpty()) break;
            else {
                System.out.println("Invalid inputStream!");
            }
        }
        return hostname;
    }

    /**
     * Get the port of server from user
     * @return Port of server
     */
    private int getPort() {
        int port;
        while(true) {
            System.out.println("Enter the server port:");
            String temp = scan.nextLine();
            try {
                port = Integer.parseInt(temp);
                break;
            }
            catch (NumberFormatException ignored) {
                System.out.println("Invalid inputStream!");
            }
        }
        return port;
    }

    private void executeTrades(ArrayList<StockTrade> stockTrades) throws IOException, InterruptedException {
        // Log assigned trades
        for (StockTrade stockTrade: stockTrades) {
            logMessage(String.format("Assigned %s of %d stock(s) of %s. Total %s estimate = %.2f * %d = %.2f.",
                    stockTrade.getQuantity() < 0 ? "sale" : "purchase",
                    Math.abs(stockTrade.getQuantity()),
                    stockTrade.getTicker(),
                    stockTrade.getQuantity() < 0 ? "gain" : "cost",
                    stockPrices.get(stockTrade.getTicker()),
                    Math.abs(stockTrade.getQuantity()),
                    stockPrices.get(stockTrade.getTicker()) * Math.abs(stockTrade.getQuantity())
            ));
        }

        // Perform assigned trades
        for (StockTrade stockTrade: stockTrades) {
            logMessage(String.format("Starting %s of %d stock(s) of %s. Total %s = %.2f * %d = %.2f.",
                    stockTrade.getQuantity() < 0 ? "sale" : "purchase",
                    Math.abs(stockTrade.getQuantity()),
                    stockTrade.getTicker(),
                    stockTrade.getQuantity() < 0 ? "gain" : "cost",
                    stockPrices.get(stockTrade.getTicker()),
                    Math.abs(stockTrade.getQuantity()),
                    stockPrices.get(stockTrade.getTicker()) * Math.abs(stockTrade.getQuantity())
            ));
            Thread.sleep(1000);
            logMessage(String.format("Finished %s of %d stock(s) of %s.",
                    stockTrade.getQuantity() < 0 ? "sale" : "purchase",
                    Math.abs(stockTrade.getQuantity()),
                    stockTrade.getTicker()
            ));
        }

        // Tell server we're available again
        sendMessage(new Message<Integer>(Message.MessageType.STATUS, 1));
    }

    private void logMessage(String message) {
        System.out.printf("[%s] %s\n", TimestampUtil.getTimestamp(startTime), message);
    }

    /**
     * The run method constantly checks for messages sent by the server
     * and prints it to the user if applicable
     */
    @SuppressWarnings("unchecked")
    public void run() {
        try {
            execution: while (true) {
                // Attempt to read line from server
                Message<?> msg = (Message<?>) (inputStream.readObject());
                Message.MessageType type = msg.getType();

                switch (type) {
                    case STRING -> System.out.println(msg.getContent());
                    case LOG -> logMessage((String) msg.getContent());
                    case TICKERS -> {
                        stockPrices = (HashMap<String, Double>) msg.getContent();
                    }
                    case TRADES -> {
                        ArrayList<StockTrade> stockTrades = (ArrayList<StockTrade>) msg.getContent();
                        executeTrades(stockTrades);
                    }
                    case EXIT -> {
                        System.out.println(msg.getContent());
                        break execution;
                    }
                    case START_TIMER -> {
                        startTime = Instant.now();
                    }
                }
            }
        }
        catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to SalStocks v2.0!");

        // Create new client
        Client client = new Client();
        client.run();
    }
}
