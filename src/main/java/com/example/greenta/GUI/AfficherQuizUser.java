package com.example.greenta.GUI;

import com.example.greenta.Exceptions.UserNotFoundException;
import com.example.greenta.Interfaces.Listener;
import com.example.greenta.Models.Quiz;
import com.example.greenta.Models.User;
import com.example.greenta.Services.ServiceQuestion;
import com.example.greenta.Services.ServiceQuiz;
import com.example.greenta.Services.SessionService;
import com.example.greenta.Services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AfficherQuizUser implements Listener
{
    @FXML
    private GridPane grid;
    @FXML
    private Label charityLabel;

    @FXML
    private Label coursesLabel;

    @FXML
    private Label eventLabel;

    @FXML
    private Label homeLabel;

    @FXML
    private Button profileLabel;

    @FXML
    private Label recruitmentLabel;

    @FXML
    private Label shopLabel;

    private UserService userService = UserService.getInstance();
    SessionService sessionService = SessionService.getInstance();
    private User currentUser;
    @FXML
    public void initialize(int userId) throws UserNotFoundException {
        currentUser = userService.getUserbyID(userId);
        profileLabel.setText(currentUser.getFirstname());
    }
    private int idCour;

    public void setIdCour(int idCour) {
        this.idCour = idCour;
    }

    ServiceQuiz serviceQuiz=new ServiceQuiz();
    void refresh(){
        System.out.println(idCour);
        grid.getChildren().clear();
        List<Quiz> quizList=serviceQuiz.afficherParCour(idCour);
        int row=1;
        int column=0;
        for(Quiz q:quizList){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/greenta/quiz-card-view.fxml"));
            AnchorPane anchorPane = null;
            try {
                anchorPane=fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            QuizCardView controller=fxmlLoader.getController();
            controller.remplireData(q);
            controller.setListener(this);
            if(column==3){
                column=0;
                row++;
            }
            grid.add(anchorPane,column++,row);
            GridPane.setMargin(anchorPane,new Insets(10));

        }
    }
    @FXML
    void retour(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/greenta/afficher-cours-user.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ((Stage) grid.getScene().getWindow()).close();
        Stage stage=new Stage();
        stage.setTitle("Greenta!");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void onVoirQuizClicked(int idCour) {

    }

    @Override
    public void onVoirQuestionClicked(int idQuiz) {
        ServiceQuestion serviceQuestion=new ServiceQuestion();
        if(serviceQuestion.afficherParQuiz(idQuiz).size()==0){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No question found");
            alert.showAndWait();
        }
        else{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/greenta/afficher-question-user.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ((Stage) grid.getScene().getWindow()).close();
            AfficherQuestionUser controller=fxmlLoader.getController();
            controller.setIdQuiz(idQuiz);
            controller.initialQuestion();
            Stage stage=new Stage();
            stage.setTitle("Greenta!");
            stage.setScene(scene);
            stage.show();
        }

    }
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