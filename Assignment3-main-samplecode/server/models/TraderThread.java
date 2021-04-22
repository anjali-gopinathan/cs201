package server.models;

import models.Message;
import models.StockTrade;
import models.Trader;
import server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class TraderThread extends Thread {
    private Trader trader;
    private Server server;
    private int status; // int representing available (1), unavailable (0), or done (-1)
    private ObjectOutputStream pw;
    private ObjectInputStream br;
    private double moneySpent;
    private double moneyMade;

    public TraderThread(Socket socket, Server server, Trader trader) {
        try {
            // Initialize private variables
            this.server = server;
            this.trader = trader;
            this.status = 1;
            this.pw = new ObjectOutputStream(socket.getOutputStream());
            this.br = new ObjectInputStream(socket.getInputStream());

            // Start thread
            this.start();
        }
        catch (IOException e) {
            System.out.println("ioe in DriverThread constructor: " + e.getMessage());
        }
    }

    /**
     * Sends a message to the Trader (client) that it represents
     * @param message The message to send to the Trader (client)
     * @throws IOException Throws Exception if unable to send message
     */
    public void sendMessage(Message<?> message) throws IOException {
        pw.writeObject(message);
        pw.flush();
    }

    public String getProfit() {
        // Amount left + money made - money spent
        return String.format("Total Profit Earned: $%.2f.\n",(trader.getBudget() - moneySpent) + moneyMade - moneySpent);
    }

    /**
     * Assigns trades to client
     * @param stockTrades The trades to assign to the client
     * @throws IOException Throws Exception if unable to send message
     */
    public void assignTrades(ArrayList<StockTrade> stockTrades, double amountSpent, double amountMade) throws IOException {
        // Send Vector of Order objects to client
        Message<ArrayList<StockTrade>> message = new Message<ArrayList<StockTrade>>(Message.MessageType.TRADES, stockTrades);
        this.sendMessage(message);

        // Update amounts
        this.moneyMade += amountMade;
        this.moneySpent += amountSpent;

        // Driver has been assigned tasks so is no longer available
        status = 0;
    }

    /**
     * Check if the client is able to take new trades
     * @return True if the client is not busy with orders
     */
    public boolean isAvailable() {
        return status == 1;
    }

    public boolean isFinished() {
        return status == -1;
    }

    public void setFinished() {
        this.status = -1;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Attempt to read message from Driver (client) if applicable
                Message<?> msg = (Message<?>)(br.readObject()); // Blocking

                // Retrieve message type so we know how to handle it
                Message.MessageType type = msg.getType();

                switch (type) {
                    case STATUS -> status = (int) msg.getContent();
                    case ERROR -> {
                        System.err.println(msg.getContent());
                        System.exit(1);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            // End thread since this error happens when connection closed
            interrupt();
        }
    }

    public Trader getTrader() {
        return trader;
    }

    public double getMoneySpent() {
        return moneySpent;
    }
}
