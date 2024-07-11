package ru.clevertec.check;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CheckRunner {
    public static void main(String[] args) {
        try {
            Map<String, String> arguments = parseArguments(args);

            String dbUrl = arguments.get("datasource.url");
            String dbUsername = arguments.get("datasource.username");
            String dbPassword = arguments.get("datasource.password");

            if (dbUrl == null || dbUsername == null || dbPassword == null) {
                throw new IllegalArgumentException("Отсутствуют обязательные параметры подключения к базе данных");
            }

            Connection connection = DatabaseConnection.getConnection(dbUrl, dbUsername, dbPassword);

            ProductRepository productRepository = new ProductRepository(connection);
            DiscountCardRepository discountCardRepository = new DiscountCardRepository(connection);

            Map<Integer, Integer> productQuantities = parseProductArguments(args);
            String discountCardNumberStr = arguments.get("discountCard");
            double balanceDebitCard = Double.parseDouble(arguments.get("balanceDebitCard"));
            String saveToFile = arguments.get("saveToFile");

            ShoppingCart cart = new ShoppingCart();
            for (Map.Entry<Integer, Integer> entry : productQuantities.entrySet()) {
                Product product = productRepository.findById(entry.getKey());
                if (product == null) {
                    throw new IllegalArgumentException("Продукт с id " + entry.getKey() + " не найден");
                }
                cart.addProduct(product, entry.getValue());
            }

            if (discountCardNumberStr != null) {
                int discountCardNumber = Integer.parseInt(discountCardNumberStr);
                DiscountCard discountCard = discountCardRepository.findByNumber(discountCardNumber);
                cart.setDiscountCard(discountCard);
            }
            cart.setBalance(balanceDebitCard);

            cart.checkBalance();

            CheckPrinter printer = new CheckPrinter(cart);
            printer.printToConsole();

            if (saveToFile != null) {
                printer.printToCSV(saveToFile);
            }
        } catch (Exception e) {
            handleException(e, args);
        }
    }

    static Map<String, String> parseArguments(String[] args) {
        Map<String, String> arguments = new HashMap<>();
        for (String arg : args) {
            if (arg.contains("=")) {
                String[] parts = arg.split("=", 2);
                arguments.put(parts[0], parts[1]);
            }
        }
        return arguments;
    }

    static Map<Integer, Integer> parseProductArguments(String[] args) {
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

    private static void handleException(Exception e, String[] args) {
        String errorMessage;
        if (e instanceof IllegalArgumentException) {
            errorMessage = "BAD REQUEST";
        } else if (e instanceof InsufficientFundsException) {
            errorMessage = "NOT ENOUGH MONEY";
        } else {
            errorMessage = "INTERNAL SERVER ERROR";
        }

        String saveToFile = null;
        for (String arg : args) {
            if (arg.startsWith("saveToFile=")) {
                saveToFile = arg.substring("saveToFile=".length());
                break;
            }
        }

        String outputFile = saveToFile != null ? saveToFile : "result.csv";

        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
            writer.println("Error");
            writer.println(errorMessage);
        } catch (IOException ioException) {
            System.err.println("Ошибка при записи в файл: " + ioException.getMessage());
        }

        System.err.println("Ошибка: " + errorMessage);
        e.printStackTrace();
    }
}
