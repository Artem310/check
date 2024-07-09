package main.java.ru.clevertec.check;

public class CardDiscountStrategy implements DiscountStrategy {
    private final double discountPercentage;

    public CardDiscountStrategy(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    @Override
    public double applyDiscount(double price, int quantity) {
        return price * quantity * (1 - discountPercentage / 100);
    }
}