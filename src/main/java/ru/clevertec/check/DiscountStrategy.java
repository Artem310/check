package ru.clevertec.check;

public interface DiscountStrategy {
    double applyDiscount(double price, int quantity);
}
