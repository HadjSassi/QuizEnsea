package mysql_connection;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class MySqlConnection {

    private static final String DB_FOLDER = Paths.get(System.getProperty("user.dir"), "database").toString();
    private static final String DB_NAME = "QuizENSEA";
    private static final String DB_PATH = DB_FOLDER + "/" + DB_NAME;
    private static final String JDBC_URL = "jdbc:h2:" + DB_PATH + ";MODE=MySQL;AUTO_SERVER=TRUE";

    private static final String ROOT_USERNAME = "sa";
    private static final String ROOT_PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        try {
            // Load H2 driver
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println("error: unable to load H2 driver!");
        }

        // No need to create a database manually – H2 does this automatically.
        return DriverManager.getConnection(JDBC_URL, ROOT_USERNAME, ROOT_PASSWORD);
    }


}
