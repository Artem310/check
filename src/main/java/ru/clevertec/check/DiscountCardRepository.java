package ru.clevertec.check;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DiscountCardRepository {
    private final Connection connection;

    public DiscountCardRepository(Connection connection) {
        this.connection = connection;
    }

    public DiscountCard findByNumber(int number) throws SQLException {
        String sql = "SELECT * FROM discount_card WHERE number = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, number);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new DiscountCard(
                            resultSet.getInt("id"),
                            resultSet.getInt("number"),
                            resultSet.getInt("amount")
                    );
                }
            }
        }
        return null;
    }
}
