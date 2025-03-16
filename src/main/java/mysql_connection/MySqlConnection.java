package mysql_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class MySqlConnection {

    private static final String DEFAULT_URL = "jdbc:h2:./QuizENSEA;MODE=MySQL;AUTO_SERVER=TRUE";

    private static final String ROOT_USERNAME = "sa";
    private static final String ROOT_PASSWORD = "";

    private static final String DATABASE_NAME = "QuizENSEA";
    private static final String USERNAME = "quiz_user";
    private static final String PASSWORD = "quiz_password";

    public static Connection getConnection() throws SQLException {
        try {
            // Load H2 driver
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println("error: unable to load H2 driver!");
        }

        // No need to create a database manually – H2 does this automatically.
        return DriverManager.getConnection(DEFAULT_URL, ROOT_USERNAME, ROOT_PASSWORD);
    }

    /*private static void ensureDatabaseAndUserExist() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DEFAULT_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {

            String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME + ";";
            statement.executeUpdate(createDatabaseQuery);

            *//*String createUserQuery = String.format(
                    "CREATE USER IF NOT EXISTS '%s'@'localhost' IDENTIFIED BY '%s';",
                    USERNAME, PASSWORD
            );
            statement.executeUpdate(createUserQuery);

            String grantPrivilegesQuery = String.format(
                    "GRANT ALL PRIVILEGES ON %s.* TO '%s'@'localhost';",
                    DATABASE_NAME, USERNAME
            );
            statement.executeUpdate(grantPrivilegesQuery);
            statement.executeUpdate("FLUSH PRIVILEGES;");*//*
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification/création de la base ou de l'utilisateur.");
            e.printStackTrace();
        }
    }*/
}
