package tn.esprit.greenta.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import tn.esprit.greenta.entity.Cour;
import tn.esprit.greenta.service.Listener;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class CourCardView {

    @FXML
    private Label ltitre;

    @FXML
    private Label ldesc;

    @FXML
    private Label lnv;

    @FXML
    private Label lcat;

    @FXML
    private Label ldate;
    Cour cour;
    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    void remplireData(Cour cour){
        this.cour=cour;
        lcat.setText(cour.getCategorie());
        ltitre.setText(cour.getTitre());
        ldate.setText(String.valueOf(cour.getCreated_at()));
        lnv.setText(cour.getNiveau());
        ldesc.setText(cour.getDescription());
    }

    @FXML
    void pdf(ActionEvent event) {
        File pdfFile=new File("C:\\Users\\Skymil\\Desktop\\Work\\MindsAcademy\\JavaFX\\Greenta\\src\\main\\resources\\pdf\\"+cour.getPdfpath());
        if(pdfFile.exists()){
            if(Desktop.isDesktopSupported()){
                try {
                    Desktop.getDesktop().open(pdfFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @FXML
    void voirQuiz(ActionEvent event) {
        if(listener!=null){
            listener.onVoirQuizClicked();
        }
    }

}