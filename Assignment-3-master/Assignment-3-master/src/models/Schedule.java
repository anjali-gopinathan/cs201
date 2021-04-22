package common.models;

import java.util.ArrayList;

public class Schedule {
    // Keep track of tasks
    private ArrayList<Task> taskList;

    public Schedule() {
        taskList = new ArrayList<>();
    }

    public void addTask(int time, String ticker, int tradeQuantity) {
        taskList.add(new Task(time, ticker, tradeQuantity));
    }

    public ArrayList<Task> getTaskList() {
        return taskList;
    }

    // Inner class to store task object
    public static class Task {
        private int time;
        private String ticker;
        private int tradeQuantity;

        public Task(int time, String ticker, int tradeQuantity) {
            this.time = time;
            this.ticker = ticker;
            this.tradeQuantity = tradeQuantity;
        }

        public int getTime() {
            return time;
        }

        public String getTicker() {
            return ticker;
        }

        public int getTradeQuantity() {
            return tradeQuantity;
        }
    }
}

