module com.example.greenta {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires jakarta.mail;
    requires twilio;
    requires bcrypt;

    exports com.example.greenta.GUI;
    opens com.example.greenta.GUI to javafx.fxml;
}