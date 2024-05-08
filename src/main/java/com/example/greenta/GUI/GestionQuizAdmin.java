package com.example.greenta.GUI;

import com.example.greenta.Exceptions.UserNotFoundException;
import com.example.greenta.Models.Quiz;
import com.example.greenta.Models.User;
import com.example.greenta.Services.ServiceQuiz;
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
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class GestionQuizAdmin {

    @FXML
    private TextField tftitre;

    @FXML
    private TextField tfduree;

    @FXML
    private TextField tfnote;

    @FXML
    private TableColumn<Quiz, Integer> tcnbqst;

    @FXML
    private TableView<Quiz> tvquiz;

    @FXML
    private TableColumn<Quiz, String> tctitre;

    @FXML
    private TableColumn<Quiz, Integer> tccour;

    @FXML
    private TableColumn<Quiz, Integer> tcduree;

    @FXML
    private TableColumn<Quiz, Integer> tcnote;

    @FXML
    private TableColumn<Quiz, LocalDateTime> tcdate;

    @FXML
    private TableColumn<Quiz, Void> tcquestion;
    @FXML
    private TextField tfnbqst;
    ServiceQuiz serviceQuiz=new ServiceQuiz();
    public static int idQuiz;
    @FXML
    private Button profileLabel;
    private final UserService userService = UserService.getInstance();
    private SessionService sessionService = SessionService.getInstance();
    private User currentUser;
    @FXML
    void initialize(int userId) throws UserNotFoundException {
        currentUser = userService.getUserbyID(userId);
        profileLabel.setText(currentUser.getFirstname());
        refresh();
    }
    @FXML
    void ajouter(ActionEvent event) {
        Quiz quiz=new Quiz();
        quiz.setDuree(Integer.valueOf(tfduree.getText()));
        quiz.setNbrq(Integer.valueOf(tfnbqst.getText()));
        quiz.setNote(Integer.valueOf(tfnote.getText()));
        quiz.setTitre(tftitre.getText());
        quiz.setCreated_at(LocalDateTime.now());
        quiz.setCourid_id(GestionCourAdmin.idCour);
        serviceQuiz.ajouter(quiz);
        refresh();

    }

    @FXML
    void modifier(ActionEvent event) {
        Quiz quiz=tvquiz.getSelectionModel().getSelectedItem();
        if(quiz!=null){
            quiz.setDuree(Integer.valueOf(tfduree.getText()));
            quiz.setNbrq(Integer.valueOf(tfnbqst.getText()));
            quiz.setNote(Integer.valueOf(tfnote.getText()));
            quiz.setTitre(tftitre.getText());
            quiz.setCreated_at(LocalDateTime.now());
            serviceQuiz.modifier(quiz);
            refresh();
        }
    }

    @FXML
    void supprimer(ActionEvent event) {
        Quiz quiz=tvquiz.getSelectionModel().getSelectedItem();
        if(quiz!=null){
            serviceQuiz.supprimer(quiz.getId());
            refresh();
        }
    }
    void refresh(){
        List<Quiz> list=serviceQuiz.afficherParCour(GestionCourAdmin.idCour);
        tvquiz.getItems().setAll(list);
        tctitre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitre()));
        tcduree.setCellValueFactory(new PropertyValueFactory<>("duree"));
        tcnbqst.setCellValueFactory(new PropertyValueFactory<>("nbrq"));
        tccour.setCellValueFactory(new PropertyValueFactory<>("courid_id"));
        tcdate.setCellValueFactory(new PropertyValueFactory<>("created_at"));
        tcnote.setCellValueFactory(new PropertyValueFactory<>("note"));
        tcquestion.setCellFactory(c->{
            return new TableCell<Quiz,Void>() {
                private final Button addQuiz = new Button("Add question");

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if(empty){
                        setGraphic(null);
                    }
                    else{
                        setGraphic(addQuiz);
                        addQuiz.setOnAction(event->{
                            Quiz quiz=getTableView().getItems().get(getIndex());
                            idQuiz=quiz.getId();
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/greenta/gestion-question-admin.fxml"));
                            Scene scene = null;
                            try {
                                User user = userService.getUserbyEmail(currentUser.getEmail());
                                scene = new Scene(fxmlLoader.load());
                                GestionQuestionAdmin gestionQuestionAdmin = fxmlLoader.getController();
                                gestionQuestionAdmin.initialize(user.getId());
                            } catch (UserNotFoundException| IOException e) {
                                throw new RuntimeException(e);
                            }
                            ((Stage) tfduree.getScene().getWindow()).close();
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
    @FXML
    void retour(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gestion-cour-admin.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ((Stage) tfduree.getScene().getWindow()).close();
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
