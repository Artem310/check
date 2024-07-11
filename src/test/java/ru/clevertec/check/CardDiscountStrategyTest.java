package ru.clevertec.check;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardDiscountStrategyTest {
    @Test
    public void testApplyDiscount() {
        CardDiscountStrategy strategy = new CardDiscountStrategy(10);
        double price = 100;
        int quantity = 2;
        double expected = 180; // 100 * 2 * 0.9
        double actual = strategy.applyDiscount(price, quantity);
        assertEquals(expected, actual, 0.01);
    }
}
