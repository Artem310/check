package main.java.ru.clevertec.check;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {
    private Map<Product, Integer> items;
    private DiscountCard discountCard;
    private double balance;

    public ShoppingCart() {
        this.items = new HashMap<>();
    }

    public void addProduct(Product product, int quantity) {
        items.merge(product, quantity, Integer::sum);
    }

    public void setDiscountCard(DiscountCard discountCard) {
        this.discountCard = discountCard;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double calculateTotal() {
        return items.entrySet().stream()
                .mapToDouble(entry -> calculateItemTotal(entry.getKey(), entry.getValue()))
                .sum();
    }

    public double calculateItemTotal(Product product, int quantity) {
        double price = product.getPrice();
        DiscountStrategy strategy = getDiscountStrategy(product, quantity);
        return strategy.applyDiscount(price, quantity);
    }

    private DiscountStrategy getDiscountStrategy(Product product, int quantity) {
        if (product.isWholesaleEligible(quantity)) {
            return new WholesaleDiscountStrategy();
        } else if (discountCard != null) {
            return new CardDiscountStrategy(discountCard.getDiscountAmount());
        } else {
            return (price, qty) -> price * qty; // No discount
        }
    }

    public Map<Product, Integer> getItems() {
        return new HashMap<>(items);
    }

    public DiscountCard getDiscountCard() {
        return discountCard;
    }

    public double getBalance() {
        return balance;
    }
}

