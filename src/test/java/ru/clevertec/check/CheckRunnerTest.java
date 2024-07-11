package ru.clevertec.check;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import java.util.HashMap;

public class CheckRunnerTest {
    @Test
    public void testParseArguments() {
        String[] args = {"datasource.url=jdbc:postgresql://localhost:5432/test", "datasource.username=user", "datasource.password=pass"};
        Map<String, String> expected = new HashMap<>();
        expected.put("datasource.url", "jdbc:postgresql://localhost:5432/test");
        expected.put("datasource.username", "user");
        expected.put("datasource.password", "pass");

        Map<String, String> actual = CheckRunner.parseArguments(args);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseProductArguments() {
        String[] args = {"1-2", "3-4"};
        Map<Integer, Integer> expected = new HashMap<>();
        expected.put(1, 2);
        expected.put(3, 4);

        Map<Integer, Integer> actual = CheckRunner.parseProductArguments(args);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseProductArguments_InvalidArgument() {
        String[] args = {"invalid"};
        assertThrows(IllegalArgumentException.class, () -> {
            CheckRunner.parseProductArguments(args);
        });
    }
}
