package ru.clevertec.check;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class CheckPrinter {
    private final ShoppingCart cart;

    public CheckPrinter(ShoppingCart cart) {
        this.cart = cart;
    }

    public void printToConsole() {
        System.out.println("=====================================================");
        System.out.println("                     CASH RECEIPT                    ");
        System.out.println("=====================================================");
        System.out.println("DATE: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
        System.out.println("-----------------------------------------------------");
        System.out.printf("%-4s %-20s %8s %8s %8s%n", "QTY", "DESCRIPTION", "PRICE", "DISCOUNT", "TOTAL");
        System.out.println("-----------------------------------------------------");

        double totalDiscount = 0;
        double grandTotal = 0;

        for (Map.Entry<Product, Integer> entry : cart.getItems().entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            double price = product.getPrice();
            double discount = calculateDiscount(product, quantity);
            double total = price * quantity - discount;

            System.out.printf("%-4d %-20s %8.2f$ %8.2f$ %8.2f$%n",
                    quantity, product.getDescription(), price, discount, total);

            totalDiscount += discount;
            grandTotal += total;
        }

        System.out.println("-----------------------------------------------------");
        System.out.printf("TOTAL DISCOUNT: %29.2f$%n", totalDiscount);
        System.out.printf("TOTAL: %38.2f$%n", grandTotal);

        if (cart.getDiscountCard() != null) {
            System.out.println("-----------------------------------------------------");
            System.out.printf("DISCOUNT CARD: %d  DISCOUNT: %d%%%n",
                    cart.getDiscountCard().getNumber(),
                    cart.getDiscountCard().getAmount());
        }

        System.out.println("=====================================================");
    }

    public void printToCSV(String fileName) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            LocalDateTime now = LocalDateTime.now();
            writer.println("Date;Time");
            writer.println(now.format(DateTimeFormatter.ofPattern("dd.MM.yyyy;HH:mm:ss")));
            writer.println();
            writer.println("QTY;DESCRIPTION;PRICE;DISCOUNT;TOTAL");

            double totalDiscount = 0;
            double grandTotal = 0;

            for (Map.Entry<Product, Integer> entry : cart.getItems().entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                double price = product.getPrice();
                double discount = calculateDiscount(product, quantity);
                double total = price * quantity - discount;

                writer.printf("%d;%s;%.2f$;%.2f$;%.2f$%n",
                        quantity, product.getDescription(), price, discount, total);

                totalDiscount += discount;
                grandTotal += total;
            }

            writer.println();
            writer.printf("DISCOUNT CARD;DISCOUNT PERCENTAGE%n");
            if (cart.getDiscountCard() != null) {
                writer.printf("%d;%d%%%n",
                        cart.getDiscountCard().getNumber(),
                        cart.getDiscountCard().getAmount());
            } else {
                writer.println("N/A;0%");
            }

            writer.println();
            writer.printf("TOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT%n");
            writer.printf("%.2f$;%.2f$;%.2f$%n", grandTotal + totalDiscount, totalDiscount, grandTotal);
        }
    }

    double calculateDiscount(Product product, int quantity) {
        double regularPrice = product.getPrice() * quantity;
        double discountedPrice;

        if (product.isWholesaleEligible(quantity)) {
            // 10% скидка на оптовые товары
            discountedPrice = regularPrice * 0.9;
        } else if (cart.getDiscountCard() != null) {
            // Скидка по карте
            discountedPrice = regularPrice * (1 - cart.getDiscountCard().getAmount() / 100.0);
        } else {
            // Без скидки
            discountedPrice = regularPrice;
        }

        return regularPrice - discountedPrice;
    }
}
