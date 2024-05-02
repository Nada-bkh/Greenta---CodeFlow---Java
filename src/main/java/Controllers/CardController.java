package Controllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import Entities.Charity;
import javafx.scene.layout.VBox;

public class CardController implements Initializable {

    @FXML
    private HBox Hbox;

    @FXML
    private Label amountId;

    @FXML
    private Label dateId;

    @FXML
    private ImageView imgid;

    @FXML
    private Label locationId;

    @FXML
    private Label nameId;

    private String[] colors = new String[]{"#8BC34A", "#689F38", "#4CAF50", "#388E3C", "#43A047"};

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setData(Charity o) {
        // Dynamically load the image
        Image image = o.loadImage();
        if (image != null) {
            imgid.setImage(image);
        } else {
            // Handle the case where image loading failed
            // Set a default image or display an error message
        }

        nameId.setText(o.getName_of_charity());
        locationId.setText(o.getLocation());
        Hbox.setStyle(new StringBuilder().append("-fx-background-color: #").append(colors[(int) (Math.random() * colors.length)]).append(";").append(" -fx-background-radius: 15;").append("-fx-effect: dropShadown(three-pass-box, rgba(0,0,0,0.1),10,0 ,0 ,10);").toString());
    }

}


