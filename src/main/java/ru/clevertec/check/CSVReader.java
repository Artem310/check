package ru.clevertec.check;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    public static List<Product> readProducts(String filePath) throws IOException {
        List<Product> products = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Пропускаем заголовок
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                if (values.length == 5) {
                    Product product = new Product(
                            Integer.parseInt(values[0]),
                            values[1],
                            Double.parseDouble(values[2].replace(",", ".")),
                            Integer.parseInt(values[3]),
                            values[4].equals("+")
                    );
                    products.add(product);
                }
            }
        }
        return products;
    }

    public static List<DiscountCard> readDiscountCards(String filePath) throws IOException {
        List<DiscountCard> discountCards = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // Пропускаем заголовок
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                if (values.length == 3) {
                    DiscountCard card = new DiscountCard(
                            Integer.parseInt(values[0]),
                            Integer.parseInt(values[1]),
                            (int) Double.parseDouble(values[2])
                    );
                    discountCards.add(card);
                }
            }
        }
        return discountCards;
    }
}
