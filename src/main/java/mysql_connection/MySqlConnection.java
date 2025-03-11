package mysql_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class MySqlConnection {

    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/";
    private static final String ROOT_USERNAME = "root";
    private static final String ROOT_PASSWORD = Password.password;

    private static final String DATABASE_NAME = "QuizENSEA";
    private static final String USERNAME = "quiz_user";
    private static final String PASSWORD = "quiz_password";

    public static Connection getOracleConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException ex) {
            System.out.println("error : unable to load driver class!");
        } catch (IllegalAccessError ex) {
            System.out.println("error : access problem while loading!");
        } catch (IllegalAccessException e) {
            System.out.println("error 10");
        } catch (InstantiationException e) {
            System.out.println("error 100");
        }

        ensureDatabaseAndUserExist();
        String dbUrl = DEFAULT_URL + DATABASE_NAME;
        return DriverManager.getConnection(dbUrl, USERNAME, PASSWORD);
    }

    private static void ensureDatabaseAndUserExist() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DEFAULT_URL, ROOT_USERNAME, ROOT_PASSWORD);
             Statement statement = connection.createStatement()) {

            String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME + ";";
            statement.executeUpdate(createDatabaseQuery);

            String createUserQuery = String.format(
                    "CREATE USER IF NOT EXISTS '%s'@'localhost' IDENTIFIED BY '%s';",
                    USERNAME, PASSWORD
            );
            statement.executeUpdate(createUserQuery);

            String grantPrivilegesQuery = String.format(
                    "GRANT ALL PRIVILEGES ON %s.* TO '%s'@'localhost';",
                    DATABASE_NAME, USERNAME
            );
            statement.executeUpdate(grantPrivilegesQuery);
            statement.executeUpdate("FLUSH PRIVILEGES;");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification/création de la base ou de l'utilisateur.");
            e.printStackTrace();
        }
    }
}
