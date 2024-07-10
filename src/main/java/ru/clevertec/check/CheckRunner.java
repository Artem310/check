package main.java.ru.clevertec.check;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckRunner {
    public static void main(String[] args) {
        try {
            Map<Integer, Integer> productQuantities = parseProductArguments(args);
            String discountCardNumber = parseDiscountCardArgument(args);
            double balanceDebitCard = parseBalanceDebitCardArgument(args);
            String pathToFile = parsePathToFileArgument(args);
            String saveToFile = parseSaveToFileArgument(args);

            if (pathToFile == null) {
                throw new IllegalArgumentException("Отсутствует аргумент pathToFile");
            }

            List<Product> products = CSVReader.readProducts(pathToFile);
            List<DiscountCard> discountCards = CSVReader.readDiscountCards("./src/main/resources/discountCards.csv");

            ShoppingCart cart = new ShoppingCart();
            for (Map.Entry<Integer, Integer> entry : productQuantities.entrySet()) {
                Product product = findProductById(products, entry.getKey());
                if (product == null) {
                    throw new IllegalArgumentException("Продукт с id " + entry.getKey() + " не найден");
                }
                cart.addProduct(product, entry.getValue());
            }

            DiscountCard discountCard = findDiscountCard(discountCards, discountCardNumber);
            cart.setDiscountCard(discountCard);
            cart.setBalance(balanceDebitCard);

            cart.checkBalance();

            CheckPrinter printer = new CheckPrinter(cart);
            printer.printToConsole();

            String outputFile = saveToFile != null ? saveToFile : "result.csv";
            printer.printToCSV(outputFile);
        } catch (Exception e) {
            handleException(e, args);
        }
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
                        productQuantities.put(productId, productQuantities.getOrDefault(productId, 0) + quantity);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Неверный аргумент продукта: " + arg);
                    }
                }
            }
        }
        if (productQuantities.isEmpty()) {
            throw new IllegalArgumentException("Не предоставлены действительные аргументы продуктов");
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
                    throw new IllegalArgumentException("Неверный аргумент баланса: " + arg);
                }
            }
        }
        throw new IllegalArgumentException("Отсутствует аргумент баланса");
    }

    private static String parsePathToFileArgument(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("pathToFile=")) {
                return arg.substring("pathToFile=".length());
            }
        }
        return null;
    }

    private static String parseSaveToFileArgument(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("saveToFile=")) {
                return arg.substring("saveToFile=".length());
            }
        }
        return null;
    }

    private static void handleException(Exception e, String[] args) {
        String errorMessage;
        if (e instanceof IllegalArgumentException) {
            errorMessage = "BAD REQUEST";
        } else if (e instanceof InsufficientFundsException) {
            errorMessage = "NOT ENOUGH MONEY";
        } else {
            errorMessage = "INTERNAL SERVER ERROR";
        }

        String saveToFile = parseSaveToFileArgument(args);
        String outputFile = saveToFile != null ? saveToFile : "result.csv";

        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
            writer.println("Error");
            writer.println(errorMessage);
        } catch (IOException ioException) {
            System.err.println("Ошибка при записи в файл: " + ioException.getMessage());
        }

        System.err.println("Ошибка: " + errorMessage);
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