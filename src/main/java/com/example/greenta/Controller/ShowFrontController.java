package com.example.greenta.Controller;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.Date;
import java.util.ResourceBundle;

import com.example.greenta.Models.Charity;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ShowFrontController {










        public VBox getVbox() {
            return Vbox;
        }




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
        @FXML
        private VBox Vbox;
        /**
         * Initializes the controller class.
         */

        public void initialize(URL url, ResourceBundle rb) {
        }

        public void setData(Charity o){
            Image image = new Image(new File(o.getPicture()).toURI().toString());
            imgid.setImage(image);
            nameId.setText(o.getName_of_charity());
            locationId.setText(o.getLocation());
           amountId.setText(Double.toString(o.getAmount_donated()));
            Date date = (Date) o.getLast_date();


            LocalDate localDate = date.toLocalDate();

            dateId.setText( localDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")));


            Vbox.setStyle(new StringBuilder().append("-fx-background-color: #").append(colors[(int) (Math.random() * colors.length)]).append(";").append(" -fx-background-radius: 15;").append("-fx-effect: dropShadown(three-pass-box, rgba(0,0,0,0.1),10,0 ,0 ,10);").toString());
        }


    }





