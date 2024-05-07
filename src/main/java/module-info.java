module com.example.greenta {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires org.apache.poi.ooxml;
    requires jakarta.mail;
    requires twilio;
    requires bcrypt;

    opens com.example.greenta.Controller to javafx.fxml; // Add this line to export the Controllers package
    exports com.example.greenta.Greenta;
}
