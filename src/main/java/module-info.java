module application {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.h2database;


    opens application to javafx.fxml;
    exports application;
    exports application.tools;
    opens application.tools to javafx.fxml;
}