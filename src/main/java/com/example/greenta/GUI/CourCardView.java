package com.example.greenta.GUI;

import com.example.greenta.Interfaces.Listener;
import com.example.greenta.Models.Cour;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

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
        File pdfFile=new File("C:\\Users\\samar\\OneDrive\\Bureau\\Greenta\\src\\main\\resources\\pdf\\"+cour.getPdfpath());
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
            listener.onVoirQuizClicked(cour.getId());
        }
    }

}