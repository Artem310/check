package main.java.ru.clevertec.check;

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
        StringBuilder sb = new StringBuilder();
        appendHeader(sb);
        appendItems(sb);
        appendFooter(sb);
        System.out.println(sb.toString());
    }

    public void printToCSV(String fileName) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("Date;Time");
            writer.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy;HH:mm:ss")));
            writer.println();
            writer.println("QTY;DESCRIPTION;PRICE;DISCOUNT;TOTAL");

            for (Map.Entry<Product, Integer> entry : cart.getItems().entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                double price = product.getPrice();
                double total = cart.calculateItemTotal(product, quantity);
                double discount = price * quantity - total;

                writer.printf("%d;%s;%.2f$;%.2f$;%.2f$%n",
                        quantity, product.getDescription(), price, discount, total);
            }

            appendCSVFooter(writer);
        }
    }

    private void appendHeader(StringBuilder sb) {
        sb.append("=====================================================\n");
        sb.append("                     CASH RECEIPT                    \n");
        sb.append("=====================================================\n");
        sb.append("DATE: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))).append("\n");
        sb.append("-----------------------------------------------------\n");
        sb.append(String.format("%-4s %-20s %8s %8s %8s%n", "QTY", "DESCRIPTION", "PRICE", "DISCOUNT", "TOTAL"));
        sb.append("-----------------------------------------------------\n");
    }

    private void appendItems(StringBuilder sb) {
        double totalDiscount = 0;
        double grandTotal = 0;

        for (Map.Entry<Product, Integer> entry : cart.getItems().entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            double price = product.getPrice();
            double total = cart.calculateItemTotal(product, quantity);
            double discount = price * quantity - total;

            sb.append(String.format("%-4d %-20s %8.2f$ %8.2f$ %8.2f$%n",
                    quantity, product.getDescription(), price, discount, total));

            totalDiscount += discount;
            grandTotal += total;
        }

        sb.append("-----------------------------------------------------\n");
        sb.append(String.format("TOTAL DISCOUNT: %29.2f$%n", totalDiscount));
        sb.append(String.format("TOTAL: %38.2f$%n", grandTotal));
    }

    private void appendFooter(StringBuilder sb) {
        if (cart.getDiscountCard() != null) {
            sb.append("-----------------------------------------------------\n");
            sb.append(String.format("DISCOUNT CARD: %s  DISCOUNT: %.1f%%%n",
                    cart.getDiscountCard().getNumber(),
                    cart.getDiscountCard().getDiscountAmount()));
        }
        sb.append("=====================================================\n");
    }

    private void appendCSVFooter(PrintWriter writer) {
        writer.println();
        writer.printf("DISCOUNT CARD;DISCOUNT PERCENTAGE%n");
        if (cart.getDiscountCard() != null) {
            writer.printf("%s;%.1f%%%n",
                    cart.getDiscountCard().getNumber(),
                    cart.getDiscountCard().getDiscountAmount());
        } else {
            writer.println("N/A;0%");
        }

        writer.println();
        writer.printf("TOTAL PRICE;TOTAL DISCOUNT;TOTAL WITH DISCOUNT%n");
        double total = cart.calculateTotal();
        double regularTotal = cart.getItems().entrySet().stream()
                .mapToDouble(e -> e.getKey().getPrice() * e.getValue())
                .sum();
        double totalDiscount = regularTotal - total;
        writer.printf("%.2f$;%.2f$;%.2f$%n", regularTotal, totalDiscount, total);
    }
}

