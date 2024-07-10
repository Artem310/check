package main.java.ru.clevertec.check;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckRunner {
    public static void main(String[] args) {
        try {
            Map<Integer, Integer> productQuantities = parseProductArguments(args);
            String discountCardNumber = parseDiscountCardArgument(args);
            double balanceDebitCard = parseBalanceDebitCardArgument(args);

            List<Product> products = CSVReader.readProducts("./src/main/resources/products.csv");
            List<DiscountCard> discountCards = CSVReader.readDiscountCards("./src/main/resources/discountCards.csv");

            ShoppingCart cart = createShoppingCart(productQuantities, discountCardNumber, balanceDebitCard, products, discountCards);

            CheckPrinter printer = new CheckPrinter(cart);
            printer.printToConsole();
            printer.printToCSV("result.csv");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static ShoppingCart createShoppingCart(Map<Integer, Integer> productQuantities,
                                                   String discountCardNumber,
                                                   double balanceDebitCard,
                                                   List<Product> products,
                                                   List<DiscountCard> discountCards) {
        ShoppingCart cart = new ShoppingCart();
        for (Map.Entry<Integer, Integer> entry : productQuantities.entrySet()) {
            Product product = findProductById(products, entry.getKey());
            if (product == null) {
                throw new IllegalArgumentException("Product with id " + entry.getKey() + " not found");
            }
            cart.addProduct(product, entry.getValue());
        }

        DiscountCard discountCard = findDiscountCard(discountCards, discountCardNumber);
        cart.setDiscountCard(discountCard);
        cart.setBalance(balanceDebitCard);
        return cart;
    }

    private static Map<Integer, Integer> parseProductArguments(String[] args) {
        Map<Integer, Integer> productQuantities = new HashMap<>();
        for (String arg : args) {
            if (arg.contains("-")) {
                String[] parts = arg.split("-");
                if (parts.length == 2) {
                    try {
                        int productId = Integer.parseInt(parts[0]);
                        int quantity = Integer.parseInt(parts[1]);
                        productQuantities.merge(productId, quantity, Integer::sum);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid product argument: " + arg);
                    }
                }
            }
        }
        if (productQuantities.isEmpty()) {
            throw new IllegalArgumentException("No valid product arguments provided");
        }
        return productQuantities;
    }

    private static String parseDiscountCardArgument(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("discountCard=")) {
                String cardNumber = arg.substring("discountCard=".length());
                if (cardNumber.length() == 4 && cardNumber.matches("\\d{4}")) {
                    return cardNumber;
                }
            }
        }
        return null;
    }

    private static double parseBalanceDebitCardArgument(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("balanceDebitCard=")) {
                try {
                    return Double.parseDouble(arg.substring("balanceDebitCard=".length()));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid balance argument: " + arg);
                }
            }
        }
        throw new IllegalArgumentException("Balance argument is missing");
    }

    private static Product findProductById(List<Product> products, int id) {
        return products.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private static DiscountCard findDiscountCard(List<DiscountCard> cards, String number) {
        if (number == null) {
            return null;
        }
        return cards.stream()
                .filter(c -> c.getNumber().equals(number))
                .findFirst()
                .orElse(null);
    }
}
