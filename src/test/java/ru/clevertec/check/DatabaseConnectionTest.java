package ru.clevertec.check;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseConnectionTest {
    @Test
    public void testGetConnection() throws SQLException {
        String url = "jdbc:h2:mem:testdb";
        String username = "sa";
        String password = "";

        Connection connection = DatabaseConnection.getConnection(url, username, password);
        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }
}
