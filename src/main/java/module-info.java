module com.example.project7 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.project7 to javafx.fxml;
    opens com.example.project7.controller to javafx.fxml;
    opens com.example.project7.controller.edition to javafx.fxml;
    opens com.example.project7.model to javafx.fxml;
    exports com.example.project7;
    exports com.example.project7.model;
    exports com.example.project7.controller.edition;
    exports mysql_connection;
    opens mysql_connection to javafx.fxml;
}