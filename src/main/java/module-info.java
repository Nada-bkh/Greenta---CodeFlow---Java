module com.example.demo {
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
    requires jdk.jsobject;
    requires google.cloud.recaptchaenterprise;
    requires proto.google.cloud.recaptchaenterprise.v1;
    requires mysql.connector.j;
    requires jdk.httpserver;
    requires java.mail;

    opens greenta to javafx.fxml;
    exports greenta;
    exports greenta.Controller;
    opens greenta.Controller to javafx.fxml;
    opens greenta.models to javafx.base;

}