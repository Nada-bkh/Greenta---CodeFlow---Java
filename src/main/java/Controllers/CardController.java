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


/**
     * FXML Controller class
     *
     * @author chayma2
     */
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
        private String []colors = new String[]{"B9E5FF", "BDB2FE", "FB9AA8", "FF5056"};

        /**
         * Initializes the controller class.
         */
        @Override
        public void initialize(URL url, ResourceBundle rb) {
            // TODO
        }
        public void setData(Charity o){

            //Image image= new Image( o.getImg()) ;
            //         imagSrc.setImage(image);
            //       name.setText(o.getTitleOffer());
            Image image = new Image(new File(o.getPicture()).toURI().toString());
            imgid.setImage(image);
            nameId.setText(o.getName_of_charity());
            locationId.setText(o.getLocation());
            Hbox.setStyle(new StringBuilder().append("-fx-background-color: #").append(colors[(int) (Math.random() * colors.length)]).append(";").append(" -fx-background-radius: 15;").append("-fx-effect: dropShadown(three-pass-box, rgba(0,0,0,0.1),10,0 ,0 ,10);").toString());
        }


    }

