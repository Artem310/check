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
        items.put(product, items.getOrDefault(product, 0) + quantity);
    }

    public void setDiscountCard(DiscountCard discountCard) {
        this.discountCard = discountCard;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double calculateTotal() {
        double total = 0;
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();

            DiscountStrategy strategy;
            if (product.isWholesaleEligible(quantity)) {
                strategy = new WholesaleDiscountStrategy();
            } else if (discountCard != null) {
                strategy = new CardDiscountStrategy(discountCard.getDiscountAmount());
            } else {
                strategy = (price, qty) -> price * qty; // Без скидки
            }

            total += strategy.applyDiscount(product.getPrice(), quantity);
        }
        return total;
    }

    public void checkBalance() throws InsufficientFundsException {
        double total = calculateTotal();
        if (total > balance) {
            throw new InsufficientFundsException("Недостаточно средств. Требуется: " + total + ", Доступно: " + balance);
        }
    }

    public Map<Product, Integer> getItems() {
        return items;
    }

    public DiscountCard getDiscountCard() {
        return discountCard;
    }

    public double getBalance() {
        return balance;
    }
}