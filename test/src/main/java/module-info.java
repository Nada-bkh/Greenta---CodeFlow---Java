module com.example.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires javafx.swing;
    requires itextpdf;


    opens com.example.greenta to javafx.fxml;
//    exports com.example.project;
    exports com.example.greenta.Models;
    opens com.example.greenta.Models to javafx.fxml;
    exports com.example.greenta.GUI;
    opens com.example.greenta.GUI to javafx.fxml;
    exports com.example.greenta.Greenta;
    opens com.example.greenta.Greenta to javafx.fxml;

    //    exports com.example.project.MainPackage;

}