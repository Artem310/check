package ru.clevertec.check;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CheckPrinterTest {
    private ShoppingCart cart;
    private CheckPrinter printer;

    @BeforeEach
    public void setUp() {
        cart = Mockito.mock(ShoppingCart.class);
        printer = new CheckPrinter(cart);
    }

    @Test
    public void testCalculateDiscount_NoDiscount() {
        Product product = new Product(1, "Product", 100, 10, false);
        when(cart.getDiscountCard()).thenReturn(null);
        double discount = printer.calculateDiscount(product, 1);
        assertEquals(0, discount, 0.01);
    }

    @Test
    public void testCalculateDiscount_Wholesale() {
        Product product = new Product(1, "Product", 100, 10, true);
        when(cart.getDiscountCard()).thenReturn(null);
        double discount = printer.calculateDiscount(product, 5); // опт
        assertEquals(50, discount, 0.01); // 100 * 5 * 0.1
    }

    @Test
    public void testCalculateDiscount_WithCard() {
        Product product = new Product(1, "Product", 100, 10, false);
        DiscountCard card = new DiscountCard(1, 12345, 10);
        when(cart.getDiscountCard()).thenReturn(card);
        double discount = printer.calculateDiscount(product, 1);
        assertEquals(10, discount, 0.01); // 100 * 1 * 0.1
    }
}
