package tn.esprit.greenta.controller;

import com.itextpdf.layout.Document;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import tn.esprit.greenta.FXMain;
import tn.esprit.greenta.entity.Epreuve;
import tn.esprit.greenta.service.ServiceQuestion;
import tn.esprit.greenta.utils.PdfGenerator;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class EpreuveReponse
{
    @FXML
    private Label lmsg;

    @FXML
    private Label lnote;
    private Epreuve epreuve;

    public void setEpreuve(Epreuve epreuve) {
        this.epreuve = epreuve;
    }
    void report(){
        lnote.setText(String.valueOf(epreuve.getNote()));
        if(epreuve.getNote()>0){
            lmsg.setText("Vous avez bien repondu sur le quiz !\n" +
                    "joindre votre Certificat ");

        }
        else{
           if (epreuve.getNote() == 0){
            lmsg.setText("Vous pouvez repasser le quiz")
            ;}
        }
    }

    @FXML
    void exit(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(FXMain.class.getResource("afficher-cours-user.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage stage=new Stage();
        stage.setTitle("Greenta!");
        stage.setScene(scene);
        stage.show();
    }
    ServiceQuestion serviceQuestion=new ServiceQuestion();
    @FXML
    void pdf(ActionEvent event) {
        String date= LocalDate.now().toString();

        String path="C:\\Users\\samar\\OneDrive\\Bureau\\Greenta\\src\\main\\resources\\pdf\\Report"+date+".pdf";
        Document document=PdfGenerator.createPDF(path);

        document.add(PdfGenerator.generateQuestionsTablePDF(serviceQuestion.afficherParQuiz(epreuve.getQuizid_id())));
        PdfGenerator.addEpreuveDetails(document,epreuve);
        document.close();
        File pdfFile=new File("C:\\Users\\samar\\OneDrive\\Bureau\\Greenta\\src\\main\\resources\\pdf\\Report"+date+".pdf");
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
    public void initialize() {

    }
}