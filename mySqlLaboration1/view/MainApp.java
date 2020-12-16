package view;

import controller.LoginController;
import databaseUtils.DbConnector;
import databaseUtils.DbDisconnectException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Book Database");
        primaryStage.setResizable(false);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            try{
                DbConnector.disconnectDB();
            }catch (DbDisconnectException | NullPointerException ex){
                System.out.println("Disconnect failed");
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
