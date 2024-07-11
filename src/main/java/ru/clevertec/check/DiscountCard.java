package ru.clevertec.check;

public class DiscountCard {
    private int id;
    private int number;
    private int amount;

    public DiscountCard(int id, int number, int amount) {
        this.id = id;
        this.number = number;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
