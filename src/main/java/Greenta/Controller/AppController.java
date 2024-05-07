package greenta.Controller;

import greenta.models.App;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import services.MailSender1;
import services.ServiceApp;

import java.io.File;

public class AppController {

    @FXML
    private Button btnsubmit;

    @FXML
    private Button btnupload;

    @FXML
    private WebView captcha;

    @FXML
    private TextField tfemail;

    @FXML
    private TextField tffirstname;

    @FXML
    private TextField tflastname;

    private String selectedFilePath;

    private ServiceApp serviceApp;

    public AppController() {
        this.serviceApp = new ServiceApp();
    }

    @FXML
    private void handleFileUpload() {
        if (btnupload != null && btnupload.getScene() != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Resume");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
            );
            File selectedFile = fileChooser.showOpenDialog(btnupload.getScene().getWindow()); // Change btupload to btnupload
            if (selectedFile != null) {
                selectedFilePath = selectedFile.getAbsolutePath();
                System.out.println("Selected file: " + selectedFilePath);
            }
        } else {
            System.err.println("Error: btnupload or its scene is null."); // Change btupload to btnupload
        }
    }

    @FXML
    private void handleSubmit() {

        String firstname = tffirstname.getText();
        String lastname = tflastname.getText();
        String email = tfemail.getText();
        String pdf = selectedFilePath;

        if (firstname.isEmpty() || lastname.isEmpty() || email.isEmpty() || pdf.isEmpty()) {
            showAlert("Please fill all fields.");
            return;
        }

        App app = new App();
        app.setFirstname(firstname);
        app.setLastname(lastname);
        app.setEmail(email);
        app.setPdf(pdf);

        serviceApp.add(app);

        new MailSender1(app.getEmail(),"Welcome to Greenta!","Dear "+app.getFirstname() + ",\n" +
                "Welcome to UBank!\n" +
                "\n" +
                "We are thrilled to have you as a new member of our banking family. As you embark on this financial journey with us, we promise to provide exceptional service, personalized solutions, and a seamless banking experience.\n" +
                "\n" +
                "At XYZ Bank, we understand that your financial needs are unique. Whether you’re saving for the future, planning investments, or managing day-to-day transactions, our dedicated team is here to assist you every step of the way.\n" +
                "\n" +
                "As a valued client, you’ll have access to a wide range of services, including online banking, mobile apps, and personalized advice from our expert advisors. We are committed to ensuring your financial well-being and helping you achieve your goals.\n" +
                "\n" +
                "Feel free to explore our website, visit our branches, or reach out to our customer support team for any assistance. We look forward to building a lasting relationship with you and making your banking experience truly exceptional.\n" +
                "\n" +
                "Once again, welcome to UBank!\n" +
                "\n" +
                "Best regards, The  UBank Team");

        showAlert("App added successfully!");
    }

    @FXML
    private void initialize() {
        // Your initialization code here
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
