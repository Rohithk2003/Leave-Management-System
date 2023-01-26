package JDBCDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class driverJDBC {
    public Connection getJDBCDriver() {
        try {
            Class.forName("org.postgresql.Driver");
            Connection driver = DriverManager.getConnection("jdbc:postgresql://localhost:5432/leaveSystem", "postgres",
                    "1234");
            return driver;
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
