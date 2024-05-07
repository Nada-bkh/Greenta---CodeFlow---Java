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
    requires layout;
    requires kernel;
    requires io;
    requires java.desktop;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires google.cloud.recaptchaenterprise;
    requires proto.google.cloud.recaptchaenterprise.v1;
    requires java.mail;
    requires javafx.swing;

    exports com.example.greenta.GUI;
    opens com.example.greenta.GUI to javafx.fxml;
}