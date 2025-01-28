package mysql_connection;

import com.example.project7.model.TypeDevoir;
import com.example.project7.model.TypeNumero;

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
            insertTypeDevoirData(statement);
            insertTypeNumeroData(statement);
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
        String createProjetQuery = "CREATE TABLE IF NOT EXISTS Projet (" +
                "idProjet INT AUTO_INCREMENT PRIMARY KEY, " +
                "nomProjet VARCHAR(255) NOT NULL, " +
                "localisationProjet VARCHAR(255), " +
                "typeProjet VARCHAR(100), " +
                "creationDate DATE DEFAULT (CURRENT_DATE));";
        statement.executeUpdate(createProjetQuery);

        String createControleQuery = "CREATE TABLE IF NOT EXISTS Controle (" +
                "idControle INT AUTO_INCREMENT PRIMARY KEY, " +
                "nomDevoir VARCHAR(255) NOT NULL, " +
                "typeDevoir VARCHAR(255), " +
                "fontDevoir  VARCHAR(255), " +
                "fontSize  INT DEFAULT (15), " +
                "formatQuestionNumber INT, " +
                "formatQuestionTexte VARCHAR(255), " +
                "creationDate DATE DEFAULT (CURRENT_DATE), " +
                "projetID INT, " +
                "FOREIGN KEY (projetID) REFERENCES Projet(idProjet) ON DELETE CASCADE);";
        statement.executeUpdate(createControleQuery);

        String createSectionQuery = "CREATE TABLE IF NOT EXISTS Section (" +
                "idSection VARCHAR(255) PRIMARY KEY, " +
                "ordreSection INT, " +
                "numberOfSections INT DEFAULT 0, " +
                "controleID INT, " +
                "FOREIGN KEY (controleID) REFERENCES Controle(idControle) ON DELETE CASCADE);";
        statement.executeUpdate(createSectionQuery);

        String createQCMQuery = "CREATE TABLE IF NOT EXISTS QCM (" +
                "idQCM INT AUTO_INCREMENT PRIMARY KEY, " +
                "question TEXT NOT NULL, " +
                "isQCU BOOLEAN, " +
                "sectionID VARCHAR(255), " +
                "FOREIGN KEY (sectionID) REFERENCES Section(idSection) ON DELETE CASCADE);";
        statement.executeUpdate(createQCMQuery);

        String createQCMReponsesQuery = "CREATE TABLE IF NOT EXISTS QCM_Reponses (" +
                "idReponse INT AUTO_INCREMENT PRIMARY KEY, " +
                "qcmID INT, " +
                "reponse TEXT, " +
                "score INT, " +
                "isCorrect BOOLEAN, " +
                "FOREIGN KEY (qcmID) REFERENCES QCM(idQCM) ON DELETE CASCADE);";
        statement.executeUpdate(createQCMReponsesQuery);

        String createQuestionLibreQuery = "CREATE TABLE IF NOT EXISTS QuestionLibre (" +
                "idQuestionLibre INT AUTO_INCREMENT PRIMARY KEY, " +
                "question TEXT NOT NULL, " +
                "scoreTotal INT, " +
                "nombreScore INT, " +
                "nombreLigne INT, " +
                "tailleLigne FLOAT, " +
                "rappel TEXT, " +
                "controleID INT, " +
                "FOREIGN KEY (controleID) REFERENCES Controle(idControle) ON DELETE CASCADE);";
        statement.executeUpdate(createQuestionLibreQuery);

        String createDescriptionQuery = "CREATE TABLE IF NOT EXISTS Description (" +
                "idDescription INT AUTO_INCREMENT PRIMARY KEY, " +
                "texte TEXT, " +
                "controleID INT, " +
                "FOREIGN KEY (controleID) REFERENCES Controle(idControle) ON DELETE CASCADE);";
        statement.executeUpdate(createDescriptionQuery);

        String createDescriptionImagesQuery = "CREATE TABLE IF NOT EXISTS Description_Images (" +
                "idImage INT AUTO_INCREMENT PRIMARY KEY, " +
                "descriptionID INT, " +
                "imagePath VARCHAR(255), " +
                "FOREIGN KEY (descriptionID) REFERENCES Description(idDescription) ON DELETE CASCADE);";
        statement.executeUpdate(createDescriptionImagesQuery);

        String createDescriptionLegendsQuery = "CREATE TABLE IF NOT EXISTS Description_Legends (" +
                "idLegend INT AUTO_INCREMENT PRIMARY KEY, " +
                "descriptionID INT, " +
                "legendText VARCHAR(255), " +
                "FOREIGN KEY (descriptionID) REFERENCES Description(idDescription) ON DELETE CASCADE);";
        statement.executeUpdate(createDescriptionLegendsQuery);

        String createFontDevoirQuery = "CREATE TABLE IF NOT EXISTS FontDevoir (" +
                "idFontDevoir INT AUTO_INCREMENT PRIMARY KEY, " +
                "nomPolice VARCHAR(100), " +
                "sizePolice INT);";
        statement.executeUpdate(createFontDevoirQuery);

        String createFormatQuestionQuery = "CREATE TABLE IF NOT EXISTS FormatQuestion (" +
                "idFormatQuestion INT AUTO_INCREMENT PRIMARY KEY, " +
                "premierTexte TEXT, " +
                "isNumerated BOOLEAN, " +
                "numeroIncremente VARCHAR(50), " +
                "deuxiemeTexte TEXT);";
        statement.executeUpdate(createFormatQuestionQuery);

        String createReponseQuery = "CREATE TABLE IF NOT EXISTS Reponse (" +
                "idReponse INT AUTO_INCREMENT PRIMARY KEY, " +
                "reponse TEXT, " +
                "score INT);";
        statement.executeUpdate(createReponseQuery);

        String createTypeDevoirQuery = "CREATE TABLE IF NOT EXISTS TypeDevoir (" +
                "idTypeDevoir INT AUTO_INCREMENT PRIMARY KEY, " +
                "nomTypeDevoir VARCHAR(255) NOT NULL);";
        statement.executeUpdate(createTypeDevoirQuery);

        String createTypeNumeroQuery = "CREATE TABLE IF NOT EXISTS TypeNumero (" +
                "idTypeNumero INT AUTO_INCREMENT PRIMARY KEY, " +
                "nomTypeNumero VARCHAR(255) NOT NULL);";
        statement.executeUpdate(createTypeNumeroQuery);

        System.out.println("Tables created successfully!");
    }

    private static void createInitFile(String filePath) {
        try {
            Files.createFile(Paths.get(filePath));
            System.out.println("Initialization file created.");
        } catch (Exception e) {
            System.err.println("Error creating the initialization file: " + e.getMessage());
        }
    }

    public static void insertTypeDevoirData(Statement statement) {
        TypeDevoir[] typeDevoirs = TypeDevoir.values();

        try {
            for (TypeDevoir typeDevoir : typeDevoirs) {
                String insertQuery = "INSERT INTO TypeDevoir (nomTypeDevoir) VALUES ('" + typeDevoir.getNomDevoir() + "');";
                statement.executeUpdate(insertQuery);
            }
            System.out.println("TypeDevoir data inserted successfully!");
        } catch (Exception e) {
            System.err.println("Error inserting TypeDevoir data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void insertTypeNumeroData(Statement statement) {
        TypeNumero[] typeNumeros = TypeNumero.values();

        try {
            for (TypeNumero typeNumero : typeNumeros) {
                String insertQuery = "INSERT INTO TypeNumero (nomTypeNumero) VALUES ('" + typeNumero.getValue() + "');";
                statement.executeUpdate(insertQuery);
            }
            System.out.println("TypeNumero data inserted successfully!");
        } catch (Exception e) {
            System.err.println("Error inserting TypeNumero data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }
}
