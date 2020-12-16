package controller;

/*
    Search Option kan göras om till en enum istället för String,
 */

import databaseUtils.DbConnException;
import databaseUtils.DbConnector;
import databaseUtils.DbSearchInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.LoginModel;
import model.SearchModel;
import model.SearchOptions;
import model.objects.Author;
import model.objects.Book;
import model.objects.Category;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SearchController implements Initializable {
    private DbSearchInterface searchModel = new SearchModel();
    private ObservableList<Book> books = FXCollections.observableArrayList();

    @FXML
    public TextField searchWord;
    @FXML
    public ComboBox searchOptions;
    @FXML
    public TableView<Book> tableView;
    @FXML
    public TableColumn<Book, String> ISBNColumn;
    @FXML
    public TableColumn<Book, String> titleColumn;
    @FXML
    public TableColumn<Book, String> descriptionColumn;
    @FXML
    public TableColumn<Book, Integer> pagesColumn;
    @FXML
    public TableColumn<Book, Integer> ratingColumn;
    @FXML
    public TableColumn<Book, Category> categoryColumn;
    @FXML
    public TableColumn<Book, ArrayList<Author>> firstNameColumn;

    public SearchController(){
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchOptions.getItems().setAll(SearchOptions.values());
        searchOptions.getSelectionModel().selectFirst();

        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        ISBNColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        pagesColumn.setCellValueFactory(new PropertyValueFactory<>("nrOfPages"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("authorArrayList"));
    }


    public void initAddBook(){
        try {
            Stage userStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Pane root = loader.load(getClass().getResource("/view/addBook.fxml").openStream());
            Scene scene = new Scene(root);
            userStage.setScene(scene);
            userStage.setTitle("Add Book");
            userStage.setResizable(false);
            userStage.initModality(Modality.APPLICATION_MODAL);
            userStage.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void search(){
        books.clear();
        new Thread() {
            public void run() {
                try {
                    books.addAll(searchModel.trySearch(searchWord.getText(), searchOptions.getValue().toString()));
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (DbConnException ex){
                    initConnectionAlert();
                }

                javafx.application.Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(books.isEmpty())
                            initNoResultAlert();
                        else
                            tableView.setItems(books);
                    }
                });
            }
        }.start();
    }


    private void initNoResultAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("No Result!");
        alert.showAndWait();
    }

    private void initConnectionAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Database Connection Error");
        alert.showAndWait();
    }
}
