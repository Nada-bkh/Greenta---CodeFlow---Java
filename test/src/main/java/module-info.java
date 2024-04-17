module com.example.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires javafx.swing;


    opens com.example.project to javafx.fxml;
    exports com.example.project;
    exports com.example.project.entities;
    opens com.example.project.entities to javafx.fxml;

    //    exports com.example.project.MainPackage;

}