package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.LoginModel;
import model.SearchModel;
import model.SearchOptions;
import model.objects.Book;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SearchController implements Initializable {
    private LoginModel loginModel = new LoginModel();
    private SearchModel searchModel = new SearchModel();
    private ArrayList<Book> books = new ArrayList<>();

    @FXML
    public TextField searchWord;
    @FXML
    public ComboBox searchOptions;
    @FXML
    public VBox textArea;

    private Stage userStage;


    public SearchController(){
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchOptions.getItems().setAll(SearchOptions.values());
        searchOptions.getSelectionModel().selectFirst();

        if(loginModel.isDbConnected())
            System.out.println("Still connected to database");
        else
            System.out.println("Can't connect to database");
    }


    public void initAddBook(){
        try {
            userStage = new Stage();
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

    //Tråd
    public void search(){
        textArea.getChildren().clear();
        books.clear();

        try{
            if(books.addAll(searchModel.trySearch(searchWord.getText(), searchOptions.getValue().toString()))) {
                System.out.println("Search successful");
            }
            else
                System.out.println("No result");
        }catch(Exception ex){
            ex.printStackTrace();
        }

        for(Book book: books) {
            textArea.getChildren().add(new Text(book.toString()));
        }
    }
}
