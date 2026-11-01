package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String HOST = System.getenv().getOrDefault("DB_HOST", "localhost");
    private static final String PORT = System.getenv().getOrDefault("DB_PORT", "3306");
    private static final String DB_NAME = System.getenv().getOrDefault("DB_NAME", "svg_temp_db");
    private static final String USER = System.getenv().getOrDefault("DB_USER", "tempuser");
    private static final String PASSWORD = System.getenv().getOrDefault("DB_PASSWORD", "temp123");

    private static final String URL = String.format("jdbc:mariadb://%s:%s/%s", HOST, PORT, DB_NAME);

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
