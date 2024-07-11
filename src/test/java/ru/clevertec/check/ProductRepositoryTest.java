package ru.clevertec.check;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProductRepositoryTest {
    private Connection connection;
    private ProductRepository repository;

    @BeforeEach
    public void setUp() throws SQLException {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:testdb");
        dataSource.setUser("sa");
        dataSource.setPassword("");

        connection = dataSource.getConnection();
        connection.createStatement().execute("CREATE TABLE product (id INT PRIMARY KEY, description VARCHAR, price DOUBLE, quantity_in_stock INT, wholesale_product BOOLEAN)");
        connection.createStatement().execute("INSERT INTO product (id, description, price, quantity_in_stock, wholesale_product) VALUES (1, 'Test Product', 10.0, 100, true)");

        repository = new ProductRepository(connection);
    }

    @Test
    public void testFindById() throws SQLException {
        Product product = repository.findById(1);
        assertNotNull(product);
        assertEquals(1, product.getId());
        assertEquals("Test Product", product.getDescription());
        assertEquals(10.0, product.getPrice());
    }
}
