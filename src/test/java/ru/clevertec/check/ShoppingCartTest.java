package ru.clevertec.check;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ShoppingCartTest {
    private ShoppingCart cart;
    private Product product1;
    private Product product2;
    private DiscountCard discountCard;

    @BeforeEach
    public void setUp() {
        cart = new ShoppingCart();
        product1 = new Product(1, "Product1", 10.0, 100, false);
        product2 = new Product(2, "Product2", 20.0, 100, true);
        discountCard = new DiscountCard(1, 12345, 10);
    }

    @Test
    public void testAddProduct() {
        cart.addProduct(product1, 2);
        assertEquals(1, cart.getItems().size());
        assertEquals(2, cart.getItems().get(product1).intValue());
    }

    @Test
    public void testSetDiscountCard() {
        cart.setDiscountCard(discountCard);
        assertEquals(discountCard, cart.getDiscountCard());
    }

}
