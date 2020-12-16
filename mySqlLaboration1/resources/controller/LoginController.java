package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.LoginModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    LoginModel model = new LoginModel();
    private Stage stage;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label loginStatus;
    @FXML
    private Button loginButton;

    public LoginController(){
    }

    public void login(){
        try{
            if(model.tryLogin(usernameField.getText(), passwordField.getText())) {
                loginStatus.setText("Success!");
                stage = (Stage) this.loginButton.getScene().getWindow();
                stage.close();
                initUserDashboard();
            }
            else
                loginStatus.setText("Failed!");
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void disconnectFromDB(){
        model.disconnectDB();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initLabel();
        if(model.isDbConnected())
            System.out.println("Connected to database");
        else
            System.out.println("Can't connect to database");
    }

    private void initLabel(){
        loginStatus.setStyle("-fx-font-weight: bold;");
    }

    //Loads and creates the search window
    private void initUserDashboard(){
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
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
