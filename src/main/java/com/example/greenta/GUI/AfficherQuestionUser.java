package com.example.greenta.GUI;

import com.example.greenta.Exceptions.UserNotFoundException;
import com.example.greenta.Models.Epreuve;
import com.example.greenta.Models.Question;
import com.example.greenta.Models.User;
import com.example.greenta.Services.ServiceEpreuve;
import com.example.greenta.Services.ServiceQuestion;
import com.example.greenta.Services.SessionService;
import com.example.greenta.Services.UserService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

import javafx.scene.Node;
import javafx.scene.layout.FlowPane; // Importation de FlowPane

import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class AfficherQuestionUser
{
    @FXML
    private Pane pane;
    @FXML
    private Label timer;
    @FXML
    private FlowPane progressPane; // Déclaration de progressPane
    ServiceQuestion serviceQuestion=new ServiceQuestion();
    private Timeline timeline;
    private int timeSeconds;
    int i=0;
    private int idQuiz;
    public static int noteTotal=0;
    List<Question> list;
    @FXML
    private Button profileLabel;
    private final UserService userService = UserService.getInstance();
    private SessionService sessionService = SessionService.getInstance();
    private User currentUser;
    public void setIdQuiz(int idQuiz) {
        this.idQuiz = idQuiz;
    }


    @FXML
    void precedent(ActionEvent event) {
        if(i>0){
            i--;
            afficherQuestion(list.get(i));
            updateProgressTypography(i);
        }

    }
    ServiceEpreuve serviceEpreuve=new ServiceEpreuve();


    @FXML
    void suivant(ActionEvent event) {
        if(i<list.size()-1){
            i++;
            afficherQuestion(list.get(i));
            updateProgressTypography(i);
        }
        else{
            timeline.stop();
            Epreuve epreuve=new Epreuve(idQuiz,LocalDateTime.now(),noteTotal,true);
            serviceEpreuve.ajouter(epreuve);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/greenta/epreuve-reponse.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            EpreuveReponse controller=fxmlLoader.getController();
            controller.setEpreuve(epreuve);
            controller.report();
            ((Stage) pane.getScene().getWindow()).close();
            Stage stage=new Stage();
            stage.setTitle("Greenta!");
            stage.setScene(scene);
            stage.show();

        }

    }

    @FXML
    void initialize(int userId) throws UserNotFoundException {
        currentUser = userService.getUserbyID(userId);
        //profileLabel.setText(currentUser.getFirstname());
        timeSeconds=60;
        initialQuestion();
        if(timeline!=null){
            timeline.stop();
        }
        timeline=new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(new KeyFrame(
                Duration.seconds(1),
                e->{
                    timeSeconds--;
                    timer.setText(timeToString(timeSeconds));
                    if(timeSeconds<=0){
                        timeline.stop();
                        timer.setText("END");
                        Epreuve epreuve=new Epreuve(idQuiz,LocalDateTime.now(),0,true);
                        serviceEpreuve.ajouter(epreuve);
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/greenta/epreuve-reponse.fxml"));
                        Scene scene = null;
                        try {
                            scene = new Scene(fxmlLoader.load());
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        EpreuveReponse controller=fxmlLoader.getController();
                        controller.setEpreuve(epreuve);
                        controller.report();
                        ((Stage) pane.getScene().getWindow()).close();
                        Stage stage=new Stage();
                        stage.setTitle("Greenta!");
                        stage.setScene(scene);
                        stage.show();
                    }

                }
        ));
        timeline.playFromStart();

        updateProgressTypography(0); // Met à jour la typographie pour la première question
    }
    String timeToString(int timeSeconds){
        int min=timeSeconds/60;
        int sec=timeSeconds%60;
        return String.format("%02d:%02d",min,sec);
    }

    void initialQuestion(){
        list=serviceQuestion.afficherParQuiz(idQuiz);
        if(list.size()!=0){

            afficherQuestion(list.get(i));



         //   ----------------------------------------------------------------
            /*// Obtention du nombre total de questions à partir de la liste de questions
            int totalQuestions = list.size();

            // Ajout des marqueurs de progression textuels dans progressPane
            for (int i = 0; i < totalQuestions; i++) {
                Label progressLabel = new Label(Integer.toString(i +1));
                progressLabel.setStyle("-fx-padding: 10px;");
                progressPane.getChildren().add(progressLabel);
            }*/
        }

    }


    private void updateProgressTypography(int currentQuestionIndex) {
        // Efface tous les marqueurs de progression précédents
        progressPane.getChildren().clear();
        // Obtention du nombre total de questions à partir de la liste de questions
        int totalQuestions = list.size();
        // Parcours de tous les marqueurs de progression
        for (int i =0 ; i < totalQuestions; i++) {
            Label progressLabel = new Label(Integer.toString(i + 1));
            progressLabel.setStyle("-fx-padding: 10px; -fx-font-size: 14px;"); // Définir la taille de la police
            // Appliquer un style différent à la question actuelle
            if (i == currentQuestionIndex) {
                progressLabel.setStyle("-fx-padding: 10px; -fx-font-size: 27px; -fx-font-weight: bold; -fx-text-fill: #ff0000;"); // Par exemple, en gras et rouge
            } else {
                progressLabel.setStyle("-fx-padding: 10px; -fx-font-size: 22px; -fx-text-fill: #000000;"); // Par exemple, en noir
            }

            progressPane.getChildren().add(progressLabel);
        }
    }


    void afficherQuestion(Question question) {
        pane.getChildren().clear();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/greenta/question-card-view.fxml"));
        AnchorPane anchorPane = null;
        try {
            anchorPane=fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        QuestionCardView controller=fxmlLoader.getController();
        controller.remplireData(question);
        pane.getChildren().add(anchorPane);
    }
    /*void refresh(){
        grid.getChildren().clear();
        List<Question> questions=serviceQuestion.afficher();
        int row=1;
        int column=0;
        for(Question q:questions){
            FXMLLoader fxmlLoader = new FXMLLoader(FXMain.class.getResource("question-card-view.fxml"));
            AnchorPane anchorPane = null;
            try {
                anchorPane=fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            QuestionCardView controller=fxmlLoader.getController();
            controller.remplireData(q);
            if(column==1){
                column=0;
                row++;
            }
            grid.add(anchorPane,column++,row);
            GridPane.setMargin(anchorPane,new Insets(10));

        }
    }*/
    @FXML
    void charityButton(MouseEvent event) throws UserNotFoundException {
        User user = userService.getUserbyEmail(currentUser.getEmail());
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/ListCharity.fxml"));
            Parent root = loader.load();
            ListCharityController listCharityController = loader.getController();
            listCharityController.initialize(user.getId());
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/afficher-cours-user.fxml"));
            Parent root = loader.load();
            AfficherCoursUser afficherCoursUser = loader.getController();
            afficherCoursUser.initialize(user.getId());
            Scene scene = new Scene(root, 800, 600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void eventButton(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/EventFront.fxml"));
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
    void homeButton(MouseEvent event) throws UserNotFoundException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/FrontHome.fxml"));
            Parent root = loader.load();
            FrontHomeController frontHomeController = loader.getController();
            frontHomeController.initialize(currentUser.getId());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/App.fxml"));
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
    void shopButton(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/Product.fxml"));
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
    void backOffice(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/BackOffice.fxml"));
            Parent root = loader.load();
            BackOfficeController backOfficeController = loader.getController();
            backOfficeController.initialize(currentUser.getId());
            Scene scene = new Scene(root, 800, 600);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException | UserNotFoundException e) {
            e.printStackTrace();
        }

    }
    @FXML
    void donation(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/greenta/AddDonation.fxml"));
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
}