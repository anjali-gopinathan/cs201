package server;

import models.Message;
import models.StockTrade;
import models.Trader;
import server.models.TraderThread;
import server.parser.ScheduleParser;
import server.parser.TraderParser;
import server.util.ScheduleFormatException;
import server.util.TiingoManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


public class Server {
    private final Scanner scan;
    private final int port;
    private ArrayList<StockTrade> stockTrades;
    private ArrayList<Trader> traders;

    // Keep track of all the connected traders (clients)
    private List<TraderThread> traderThreads;
    private HashMap<String, Double> stockPrices;

    public Server(int port, Scanner scan) {
        this.port = port;
        this.scan = scan;
    }

    public void awaitConnections() {
        try {
            // Bind to port and initialize variables
            ServerSocket serverSocket = new ServerSocket(port);
            traderThreads = Collections.synchronizedList(new ArrayList<>());

            // Listen for new connections
            System.out.println("Listening on port " + port + ".");
            System.out.println("Waiting for traders...");

            // Add clients as long as we haven't reached the limit
            while (traderThreads.size() < traders.size()) {
                // Wait to accept a new connection
                Socket socket = serverSocket.accept(); // Blocking
                System.out.println("Connection from: " + socket.getInetAddress());

                TraderThread traderThread = new TraderThread(socket, this, traders.get(traderThreads.size()));
                traderThreads.add(traderThread);

                // If there are still clients remaining, broadcast that information to the clients
                // Also log this to server
                if (traderThreads.size() < traders.size()) {
                    int remainingClients = traders.size() - traderThreads.size();

                    // If only 1 remaining driver, change grammar of sentence
                    String message = remainingClients == 1 ?
                            remainingClients + " more trader is needed before the service can begin.\nWaiting..." :
                            remainingClients + " more trader are needed before the service can begin.\nWaiting...";

                    // Send message to clients
                    broadcastMessage(new Message<>(Message.MessageType.STRING, message));

                    // Log message on server
                    System.out.println("Waiting for " + remainingClients + " more trader(s)...");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a message to all Clients
     *
     * @param message The message to send
     * @throws IOException Throws exception if message can't be sent
     */
    public void broadcastMessage(Message<?> message) throws IOException {
        for (TraderThread traderThread : traderThreads) {
            traderThread.sendMessage(message);
        }
    }

    private boolean tradersFinished() {
        for (TraderThread traderThread: traderThreads) {
            if (!traderThread.isFinished()) {
                return false;
            }
        }
        return true;
    }

    private void loadScheduleFile() {
        while (true) {
            System.out.println("What is the path of the schedule file?");
            String fileName = scan.nextLine();
            System.out.println(); // Print blank line for spacing
            try {
                stockTrades = ScheduleParser.loadSchedule(fileName);
                System.out.println("The schedule file has been properly read.\n");
                break;
            } catch (ScheduleFormatException | NumberFormatException e) {
                System.out.println("Data is malformed!\n");
            } catch (IOException e) {
                System.out.println("The file " + fileName + " could not be found!\n");
            }
        }
    }

    private void loadTradersFile() {
        while (true) {
            System.out.println("What is the path of the traders file?");
            String fileName = scan.nextLine();
            try {
                traders = TraderParser.loadTraders(fileName);
                System.out.println("The traders file has been properly read.\n");
                break;
            } catch (NumberFormatException e) {
                System.out.println("Data is malformed!\n");
            } catch (IOException e) {
                System.out.println("The file " + fileName + " could not be found!\n");
            }
        }
    }

    private void preProcessTickers() throws IOException {
        HashMap<String, Double> stockPrices = new HashMap<>();
        for (StockTrade stockTrade : stockTrades) {
            if (!stockPrices.containsKey(stockTrade.getTicker())) {
                stockPrices.put(stockTrade.getTicker(), TiingoManager.getStockPrice(stockTrade.getTicker(), stockTrade.getDate()));
            }
        }
        this.stockPrices = stockPrices;
    }

    private void assignWaitingTrades(ArrayList<StockTrade> waitingTrades) throws IOException {
        // Loop to check for the first available driver
        for (TraderThread traderThread : traderThreads) {
            if (traderThread.isAvailable()) {
                double amountSpent = 0.0;
                double amountMade = 0.0;
                ArrayList<StockTrade> toSend = new ArrayList<>();
                for (StockTrade stockTrade: waitingTrades) {
                    // If we are making a purchase, check our budget
                    if (stockTrade.getQuantity() > 0) {
                        double tradeCost = stockTrade.getQuantity() * stockPrices.get(stockTrade.getTicker());

                        // If current sum plus this new trade is still less than the trader's remaining budget, add it
                        if ((amountSpent + tradeCost) < (traderThread.getTrader().getBudget() - traderThread.getMoneySpent())) {
                            toSend.add(stockTrade);
                            amountSpent += tradeCost;
                        }
                    }
                    // If sale, just add since we're not losing any money
                    else {
                        double tradeProfit = Math.abs(stockTrade.getQuantity()) * stockPrices.get(stockTrade.getTicker());
                        toSend.add(stockTrade);
                        amountMade += tradeProfit;
                    }
                }

                // Assign all the trades we were able to if applicable
                if (!toSend.isEmpty()) {
                    traderThread.assignTrades(toSend, amountSpent, amountMade);

                    // Remove these assigned trades from the waiting list
                    for (StockTrade stockTrade: toSend) {
                        waitingTrades.remove(stockTrade);
                    }

                    // Break since we don't want to assign to any other trader yet
                    break;
                }
                else {
                    // If we couldn't assign any trades but this trader was available, it must be out of budget
                    traderThread.setFinished();
                }
                // At this point there might still be more trades in the waiting list but they'll
                // have to wait for the next trader
            }
        }
    }

    private void startExecution() {
        try {
            // Give all clients the price list
            broadcastMessage(new Message<>(Message.MessageType.TICKERS, stockPrices));

            // Log start on server
            System.out.println("Starting service.");

            // Log start for all our clients
            broadcastMessage(new Message<>(Message.MessageType.STRING,
                    "All traders have arrived!\n" + "Starting service."));

            // Tell all clients to start timer
            broadcastMessage(new Message<>(Message.MessageType.START_TIMER, null));

            long startTime = System.currentTimeMillis();
            long elapsedTime = 0;
            int nextTradeIndex = 0; // Technically you can just use the time diff but this is to be safe
            ArrayList<StockTrade> waitingTrades = new ArrayList<>();
            // While we haven't assigned all orders
            while (nextTradeIndex != stockTrades.size()) {
                // While there are still trade left and it's time to trade, add to waiting queue
                while (nextTradeIndex < stockTrades.size() && (elapsedTime / 1000) >= stockTrades.get(nextTradeIndex).getStartTime()) {
                    waitingTrades.add(stockTrades.get(nextTradeIndex));
                    nextTradeIndex++;
                }

                // If we have waiting trades, try to assign them
                if (!waitingTrades.isEmpty()) {
                    // Assign to client if possible
                    assignWaitingTrades(waitingTrades);
                }

                // Check if our traders still even have budget to take new orders
                if (tradersFinished()) {
                    break;
                }
                elapsedTime = System.currentTimeMillis() - startTime;
            }

            // Get incomplete trades
            String incompleteTrades = logIncompleteTrades(waitingTrades);

            // Log completion
            for (TraderThread traderThread: traderThreads) {
                traderThread.sendMessage(new Message<>(Message.MessageType.LOG, incompleteTrades + traderThread.getProfit()));
            }

            // Log completion
            broadcastMessage(new Message<>(Message.MessageType.EXIT, "Processing complete."));
            System.out.println("Processing complete.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String logIncompleteTrades(ArrayList<StockTrade> trades) {
        StringBuilder message = new StringBuilder().append("Incomplete Trades: ");
        for (StockTrade trade: trades) {
            message.append(String.format("(%d, %s, %d, %s) ", trade.getStartTime(), trade.getTicker(), trade.getQuantity(), trade.getDate()));
        }
        message.append("\n");
        return message.toString();
    }

    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);

        // Start server
        Server server = new Server(3456, scan);

        // Parse files
        server.loadScheduleFile();
        server.loadTradersFile();

        // Get ticker info
        server.preProcessTickers();

        server.awaitConnections();
        server.startExecution();

        scan.close();
    }
}
