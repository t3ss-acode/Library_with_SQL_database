package controller;

import com.mongodb.MongoException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.LoginModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private LoginModel model = new LoginModel();
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label loginStatus;
    @FXML
    private Button loginButton;

    public LoginController() {
    }

    public void login() {
        try {
            if (model.tryLogin(usernameField.getText(), passwordField.getText())) {
                Stage stage = (Stage) this.loginButton.getScene().getWindow();
                stage.close();
                initUserDashboard();
            } else
                initLoginAlert();
        } catch (MongoException ex) {
            initDBConnError();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initLabel();
        if (model.isDbConnected())
            System.out.println("Connected to database");
        else
            System.out.println("Can't connect to database");
    }

    private void initLabel() {
        loginStatus.setStyle("-fx-font-weight: bold;");
    }

    //Loads and creates the search window
    private void initUserDashboard() {
        try {
            Stage userStage = new Stage();

            //Loads the dashboard screen from SceneBuilder
            FXMLLoader loader = new FXMLLoader();
            //a SearchController is created with this line
            Pane root = loader.load(getClass().getResource("/view/search.fxml").openStream());

            Scene scene = new Scene(root);
            userStage.setScene(scene);
            userStage.setTitle("User Dashboard");
            userStage.setResizable(false);
            userStage.show();
        } catch (IOException e) {
            initUDWindowError();
        }
    }

    private void initLoginAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Wrong Login Credentials");
        alert.showAndWait();
    }

    private void initUDWindowError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("User Dashboard could not open!");
        alert.showAndWait();
    }

    private void initDBConnError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Connection to database failed");
        alert.showAndWait();
    }
}

