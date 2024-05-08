package com.example.greenta.GUI;

import com.example.greenta.Exceptions.UserNotFoundException;
import com.example.greenta.Models.Cour;
import com.example.greenta.Models.User;
import com.example.greenta.Services.ServiceCour;
import com.example.greenta.Services.SessionService;
import com.example.greenta.Services.UserService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

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
    private Button profileLabel;

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
    private final UserService userService = UserService.getInstance();
    private SessionService sessionService = SessionService.getInstance();
    private User currentUser;
    @FXML
    void initialize(int userId) throws UserNotFoundException{
        currentUser = userService.getUserbyID(userId);
        profileLabel.setText(currentUser.getFirstname());
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
    void ajouterCour(ActionEvent event) throws UserNotFoundException {
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
        tctitre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitre()));
        tcdesc.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        tcniveau.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNiveau()));
//        tcdate.setCellValueFactory(courLocalDateTimeCellDataFeatures -> (tcdate.getCellObservableValue()) );
        tccategorie.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategorie()));
        tcpdf.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPdfpath()));
        tcpdf.setCellFactory(new Callback<TableColumn<Cour, String>, TableCell<Cour, String>>()
        {
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

                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/greenta/gestion-quiz-admin.fxml"));
                            Scene scene = null;
                            try {
                                User user = userService.getUserbyEmail(currentUser.getEmail());
                                scene = new Scene(fxmlLoader.load());
                                GestionQuizAdmin gestionQuizAdmin = fxmlLoader.getController();
                                gestionQuizAdmin.initialize(user.getId());
                            } catch (UserNotFoundException | IOException e) {
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
    @FXML
    void charityButton(MouseEvent event) throws UserNotFoundException {
        User user = userService.getUserbyEmail(currentUser.getEmail());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/Back.fxml"));
            Parent root = loader.load();
            BackController backController = loader.getController();
            backController.initialize(user.getId());
            Scene scene = new Scene(root, 800, 600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void coursesButton(MouseEvent event) throws UserNotFoundException {
        User user = userService.getUserbyEmail(currentUser.getEmail());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/gestion-cour-admin.fxml"));
            Parent root = loader.load();
            GestionCourAdmin gestionCourAdmin = loader.getController();
            gestionCourAdmin.initialize(user.getId());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void eventButton(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/AjouterEvent.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 800, 600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void homeButton(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/FrontHome.fxml"));
            Parent root = loader.load();
            FrontHomeController frontHomeController = loader.getController();
            frontHomeController.initialize(currentUser.getId());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException | UserNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void profileClicked(ActionEvent event) throws UserNotFoundException {
        User user = userService.getUserbyEmail(currentUser.getEmail());
        try {
            sessionService.setCurrentUser(user);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/Profile.fxml"));
            Parent root = loader.load();
            ProfileController profileController = loader.getController();
            profileController.initializeProfile(user.getId());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void recruitmentButton(MouseEvent event) throws UserNotFoundException{
        User user = userService.getUserbyEmail(currentUser.getEmail());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/FrontJob.fxml"));
            Parent root = loader.load();
            FrontJob frontJob = loader.getController();
            frontJob.initialize(user.getId());
            Scene scene = new Scene(root, 800, 600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void shopButton(MouseEvent event) throws UserNotFoundException {
        User user = userService.getUserbyEmail(currentUser.getEmail());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/Product.fxml"));
            Parent root = loader.load();
            ProductController productController = loader.getController();
            productController.initialize(currentUser.getId());
            Scene scene = new Scene(root, 800, 600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void signOut(MouseEvent event) {
        sessionService.logout();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/greenta/User.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void backOffice(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/greenta/BackOffice.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void donation(MouseEvent event) throws UserNotFoundException {
        User user = userService.getUserbyEmail(currentUser.getEmail());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/AddCharity.fxml"));
            Parent root = loader.load();
            AddCharityController addCharityController = loader.getController();
            addCharityController.initialize(user.getId());
            Scene scene = new Scene(root, 800, 600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
