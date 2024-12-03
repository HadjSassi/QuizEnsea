package mysql_connection;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;

import static mysql_connection.MySqlConnection.getOracleConnection;

public class DataBase {
    private static String filePath = "./.init";

    public static void createDatabaseIfDoesNotExist() {
        if (!Files.exists(Paths.get(filePath))) {
            try {
                initializeDatabase();
            } catch (Exception e) {
                System.err.println("Error during setup: " + e.getMessage());
            }
        }
    }

    private static void initializeDatabase() {
        try (Connection connection = getOracleConnection(); Statement statement = connection.createStatement()) {

            useSchema(statement);
            createTable(statement);
            createInitFile(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void useSchema(Statement statement) throws Exception {
        String useSchemaQuery = "USE QuizENSEA;";
        statement.executeUpdate(useSchemaQuery);
//        System.out.println("Switched to schema QuizENSEA.");
    }

    private static void createTable(Statement statement) throws Exception {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS Projet (" + "idProjet INT AUTO_INCREMENT PRIMARY KEY, " + "nomProjet VARCHAR(255) NOT NULL, " + "localisationProjet VARCHAR(255), " + "typeProjet VARCHAR(100), " + "creationDate DATE DEFAULT (CURRENT_DATE)" + ");";
        statement.executeUpdate(createTableQuery);
        System.out.println("Table Projet created successfully!");
    }

    private static void createInitFile(String filePath) {
        try {
            Files.createFile(Paths.get(filePath));
            System.out.println("Initialization file created.");
        } catch (Exception e) {
            System.err.println("Error creating the initialization file: " + e.getMessage());
        }
    }
}
