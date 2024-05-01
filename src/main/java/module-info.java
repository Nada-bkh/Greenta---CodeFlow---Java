module tn.esprit.greenta {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.swing;
    requires kernel;
    requires layout;
    requires io;



    opens tn.esprit.greenta.entity to javafx.base;
    exports tn.esprit.greenta.controller;
    opens tn.esprit.greenta.controller to javafx.fxml;
    exports tn.esprit.greenta;
    opens tn.esprit.greenta to javafx.fxml;
}