package models;

public class Trader {
    private final int id;
    private final int budget;

    public Trader(int id, int budget) {
        this.id = id;
        this.budget = budget;
    }

    public int getId() {
        return id;
    }

    public int getBudget() {
        return budget;
    }
}
