package tn.esprit.greenta.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import tn.esprit.greenta.FXMain;
import tn.esprit.greenta.entity.Cour;
import tn.esprit.greenta.service.ServiceCour;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class GestionCourAdmin {

    @FXML
    private TextField tftitre;

    @FXML
    private TextField tfpdf;

    @FXML
    private ComboBox<String> cbcategorie;

    @FXML
    private TextArea tfdesc;

    @FXML
    private ComboBox<String> cbniveau;
    @FXML
    private TableView<Cour> tvcour;

    @FXML
    private TableColumn<Cour, String> tctitre;

    @FXML
    private TableColumn<Cour, String> tcdesc;

    @FXML
    private TableColumn<Cour, String> tcniveau;

    @FXML
    private TableColumn<Cour, String> tccategorie;

    @FXML
    private TableColumn<Cour, LocalDateTime> tcdate;

    @FXML
    private TableColumn<Cour, String> tcpdf;
    @FXML
    private TableColumn<Cour, Void> tcquiz;
    @FXML
    private TextField tfrecherche;

    @FXML
    private ComboBox<String> cbtri;

    ServiceCour sc=new ServiceCour();
    public static int idCour;
    @FXML
    void initialize(){
        cbcategorie.getItems().setAll("Nature","Sensitization");
        cbniveau.getItems().setAll("Beginner","Intermediate","Advanced");
        cbtri.getItems().setAll("Titre","Description","Categorie","Niveau","Date");
        tfpdf.setDisable(true);
        refresh();
        recherche_avance();
    }
    @FXML
    void tri(ActionEvent event) {
        tvcour.getItems().setAll(sc.triParCritere(cbtri.getValue()));
    }

    @FXML
    void ajouterCour(ActionEvent event) {
        if(controlleDeSaisie().length()>0){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Formulaire invalide");
            alert.setContentText(controlleDeSaisie());
            alert.showAndWait();
        }
        else{
            Cour c=new Cour();
            c.setTitre(tftitre.getText());
            c.setDescription(tfdesc.getText());
            c.setNiveau(cbniveau.getValue());
            c.setCategorie(cbcategorie.getValue());
            c.setCreated_at(LocalDateTime.now());
            c.setPdfpath(tfpdf.getText());
            sc.ajouter(c);
            refresh();
        }


    }

    @FXML
    void modifierCour(ActionEvent event) {
        Cour c=tvcour.getSelectionModel().getSelectedItem();
        if(c!=null){
            if(controlleDeSaisie().length()>0){
                Alert alert=new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Formulaire invalide");
                alert.setContentText(controlleDeSaisie());
                alert.showAndWait();
            }else{
                c.setTitre(tftitre.getText());
                c.setDescription(tfdesc.getText());
                c.setNiveau(cbniveau.getValue());
                c.setCategorie(cbcategorie.getValue());
                c.setCreated_at(LocalDateTime.now());
                c.setPdfpath(tfpdf.getText());
                sc.modifier(c);
                refresh();
            }

        }
    }

    @FXML
    void uploadPDF(ActionEvent event) {
        FileChooser fileChooser=new FileChooser();
        File file=fileChooser.showOpenDialog(tfdesc.getScene().getWindow());
        if(file!=null){
            tfpdf.setText(file.getName());
        }
    }
    public void refresh(){
        List<Cour> list=sc.afficher();
        tvcour.getItems().setAll(list);
        tctitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        tcdesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        tcniveau.setCellValueFactory(new PropertyValueFactory<>("niveau"));
        tccategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        tcdate.setCellValueFactory(new PropertyValueFactory<>("created_at"));
        tcpdf.setCellValueFactory(new PropertyValueFactory<>("pdfpath"));
        tcpdf.setCellFactory(new Callback<TableColumn<Cour, String>, TableCell<Cour, String>>() {
            @Override
            public TableCell<Cour, String> call(TableColumn<Cour, String> courStringTableColumn) {
                final TableCell<Cour,String> cell=new TableCell<>(){
                    private final Button viewPdf=new Button("View");

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty){
                            setGraphic(null);
                        }
                        else{
                            viewPdf.setOnAction(event->{
                                Cour cour=getTableView().getItems().get(getIndex());
                                openPDF(cour.getPdfpath());
                            });
                            setGraphic(viewPdf);
                        }
                    }
                };
                return cell;
            }
        });
        tcquiz.setCellFactory(c->{
            return new TableCell<Cour,Void>() {
                private final Button addQuiz = new Button("Add quiz");

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty){
                        setGraphic(null);
                    }
                    else{
                        setGraphic(addQuiz);
                        addQuiz.setOnAction(event->{
                            Cour cour=getTableView().getItems().get(getIndex());
                            idCour=cour.getId();
                            FXMLLoader fxmlLoader = new FXMLLoader(FXMain.class.getResource("gestion-quiz-admin.fxml"));
                            Scene scene = null;
                            try {
                                scene = new Scene(fxmlLoader.load());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            ((Stage) tfdesc.getScene().getWindow()).close();
                            Stage stage=new Stage();
                            stage.setTitle("Greenta!");
                            stage.setScene(scene);
                            stage.show();
                        });
                    }
                }
            };

        });
    }
    private void openPDF(String pdfPath){
        File pdfFile=new File("C:\\Users\\samar\\OneDrive\\Bureau\\Greenta\\src\\main\\resources\\pdf\\"+pdfPath);
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
    String controlleDeSaisie(){
        String erreur="";
        if(tftitre.getText().isEmpty()){
            erreur+="Titre vide!\n";
        }
        if(tfdesc.getText().isEmpty()){
            erreur+="Description vide!\n";
        }
        if(tfpdf.getText().isEmpty()){
            erreur+="PDF vide!\n";
        }
        if(cbniveau.getValue()==null){
            erreur+="Niveau vide!\n";
        }
        if(cbcategorie.getValue()==null){
            erreur+="Categorie vide!\n";
        }
        return erreur;

    }
    @FXML
    void supprimer(ActionEvent event) {
        Cour c=tvcour.getSelectionModel().getSelectedItem();
        if(c!=null){
            sc.supprimer(c.getId());
            refresh();
        }
    }
    void recherche_avance(){
        ObservableList<Cour> data= FXCollections.observableArrayList(sc.afficher());
        FilteredList<Cour> filteredList=new FilteredList<>(data,c->true);
        tfrecherche.textProperty().addListener((observable,oldValue,newValue)->{
            filteredList.setPredicate(c->{
                if(newValue.isEmpty()|| newValue==null){
                    return true;
                }
                if(c.getTitre().toLowerCase().contains(newValue.toLowerCase())){
                    return true;
                }
                if(c.getDescription().toLowerCase().contains(newValue.toLowerCase())){
                    return true;
                }
                if(c.getCategorie().toLowerCase().contains(newValue.toLowerCase())){
                    return true;
                }
                if(c.getNiveau().toLowerCase().contains(newValue.toLowerCase())){
                    return true;
                }
                return false;
            });
            tvcour.getItems().setAll(filteredList);
        });
    }

}
