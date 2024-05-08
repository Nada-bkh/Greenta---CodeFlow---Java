package com.example.greenta.GUI;

import com.example.greenta.Exceptions.UserNotFoundException;
import com.example.greenta.Models.Question;
import com.example.greenta.Models.User;
import com.example.greenta.Services.ServiceQuestion;
import com.example.greenta.Services.SessionService;
import com.example.greenta.Services.UserService;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;

public class GestionQuestionAdmin {

    @FXML
    private TableView<Question> tvquestion;

    @FXML
    private TableColumn<Question, String> tcquestion;

    @FXML
    private TableColumn<Question, String> tcreponse;

    @FXML
    private TableColumn<Question, Integer> tccorrecte;

    @FXML
    private TableColumn<Question, Integer> tcnote;

    @FXML
    private TextField tfqst;

    @FXML
    private TextField tfrep1;

    @FXML
    private TextField tfrep2;

    @FXML
    private TextField tfrep3;

    @FXML
    private TextField tfrep4;

    @FXML
    private TextField tfrepcorr;

    @FXML
    private TextField tfnote;
    @FXML
    private Button profileLabel;
    private final UserService userService = UserService.getInstance();
    private SessionService sessionService = SessionService.getInstance();
    private User currentUser;
    ServiceQuestion serviceQuestion=new ServiceQuestion();
     @FXML
    void initialize(int userId) throws UserNotFoundException {
        currentUser = userService.getUserbyID(userId);
        profileLabel.setText(currentUser.getFirstname());
        refresh();
    }
    void refresh(){
        tvquestion.getItems().setAll(serviceQuestion.afficherParQuiz(GestionQuizAdmin.idQuiz));
        tcquestion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getQuestion()));
        tcreponse.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getReponse()));
        tcreponse.setCellFactory(new Callback<TableColumn<Question, String>, TableCell<Question, String>>() {
            @Override
            public TableCell<Question, String> call(TableColumn<Question, String> questionStringTableColumn) {
                return new TableCell<Question,String>(){
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty || item==null){
                            setText(null);
                            setGraphic(null);
                        }
                        else{
                            setText(item.replace(";","\n"));
                        }
                    }
                };
            }
        });
        tccorrecte.setCellValueFactory(new PropertyValueFactory<>("reponseCorrecte"));
        tcnote.setCellValueFactory(new PropertyValueFactory<>("note"));

    }
    @FXML
    void ajouter(ActionEvent event) {
        Question question=new Question();
        question.setQuestion(tfqst.getText());
        question.setReponseCorrecte(Integer.valueOf(tfrepcorr.getText()));
        question.setNote(Integer.valueOf(tfnote.getText()));
        String reponse=String.join(";",
                tfrep1.getText(),
                tfrep2.getText(),
                tfrep3.getText(),
                tfrep4.getText());
        question.setReponse(reponse);
        question.setQuizid_id(GestionQuizAdmin.idQuiz);
        serviceQuestion.ajouter(question);
        refresh();


    }

    @FXML
    void modifier(ActionEvent event) {
        Question question=tvquestion.getSelectionModel().getSelectedItem();
        if(question!=null){
            question.setQuestion(tfqst.getText());
            question.setReponseCorrecte(Integer.valueOf(tfrepcorr.getText()));
            question.setNote(Integer.valueOf(tfnote.getText()));
            String reponse=String.join(";",
                    tfrep1.getText(),
                    tfrep2.getText(),
                    tfrep3.getText(),
                    tfrep4.getText());
            question.setReponse(reponse);
            question.setQuizid_id(GestionQuizAdmin.idQuiz);
            serviceQuestion.modifier(question);
            refresh();
        }

    }

    @FXML
    void supprimer(ActionEvent event) {
        if(tvquestion.getSelectionModel().getSelectedItem()!=null){
            serviceQuestion.supprimer(tvquestion.getSelectionModel().getSelectedItem().getId());
            refresh();
        }
    }
    @FXML
    void retour(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/greenta/gestion-quiz-admin.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ((Stage) tfnote.getScene().getWindow()).close();
        Stage stage=new Stage();
        stage.setTitle("Greenta!");
        stage.setScene(scene);
        stage.show();
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
    void coursesButton(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/gestion-cour-admin.fxml"));
            Parent root = loader.load();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/ProductCategory.fxml"));
            Parent root = loader.load();
//            ProductCategoryController productCategoryController = loader.getController();
//            productCategoryController.initialize(currentUser.getId());
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