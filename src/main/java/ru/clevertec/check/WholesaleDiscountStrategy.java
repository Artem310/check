package main.java.ru.clevertec.check;

public class WholesaleDiscountStrategy implements DiscountStrategy {
    @Override
    public double applyDiscount(double price, int quantity) {
        return price * quantity * 0.9; // 10% скидка
    }
}

