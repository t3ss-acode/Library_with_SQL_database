package controller;

import databaseUtils.DbInsertException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.LoginModel;
import model.AddBookAuthorModel;
import model.objects.Book;
import model.objects.Category;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddBookController implements Initializable {
    LoginModel loginModel = new LoginModel();
    AddBookAuthorModel addBookAuthorModel = new AddBookAuthorModel();

    @FXML
    public TextField titleTextfield;
    @FXML
    public TextField isbnTextfield;
    @FXML
    public TextField pagesTextfield;

    @FXML
    public Circle titleCircle;
    @FXML
    public Circle isbnCircle;
    @FXML
    public Circle pagesCircle;

    @FXML
    public TextArea descriptionTextfield;

    @FXML
    public ComboBox categoryBox;

    @FXML
    public TextField firstnameTextfield;
    @FXML
    public TextField lastnameTextfield;
    @FXML
    public TextField dateOfBirthTextfield;
    @FXML
    public TextField ratingTextfield;

    @FXML
    public Circle firstnameCircle;
    @FXML
    public Circle lastnameCircle;
    @FXML
    public Circle dateOfBirthCircle;
    @FXML
    public Circle ratingCircle;

    @FXML
    public Button moreAuthorsButton;
    @FXML
    public Button doneButton;
    @FXML
    public Button submitButton;

    public AddBookController(){
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        categoryBox.getItems().setAll(Category.values());
        categoryBox.getSelectionModel().selectFirst();

        if(loginModel.isDbConnected())
            System.out.println("Still connected to database");
        else
            System.out.println("Can't connect to database");
    }

    //Tråd
    public void addBook(){
        if(checkValidBookData()) {
            addBookAuthorModel.addBook(isbnTextfield.getText(), titleTextfield.getText(), descriptionTextfield.getText(), Integer.parseInt(pagesTextfield.getText()), Integer.parseInt(ratingTextfield.getText()), (Category) categoryBox.getValue());
            initAddAuthor();
        }
    }

    private void initAddAuthor(){
        firstnameTextfield.setDisable(false);
        lastnameTextfield.setDisable(false);
        dateOfBirthTextfield.setDisable(false);
        submitButton.setDisable(false);
    }

    private boolean checkValidBookData(){
        boolean flag = true;
        if(addBookAuthorModel.checkValidTitle(titleTextfield.getText()))
            titleCircle.setFill(Color.LIMEGREEN);
        else {
            titleCircle.setFill(Color.RED);
            flag = false;
        }
        if(addBookAuthorModel.checkValidISBN(isbnTextfield.getText()))
            isbnCircle.setFill(Color.LIMEGREEN);
        else {
            isbnCircle.setFill(Color.RED);
            flag = false;
        }
        if(addBookAuthorModel.checkValidPages(pagesTextfield.getText()))
            pagesCircle.setFill(Color.LIMEGREEN);
        else {
            pagesCircle.setFill(Color.RED);
            flag = false;
        }
        return flag;
    }

    public void addAuthor(){
        if(checkValidAuthorData()) {
            addBookAuthorModel.addAuthor(firstnameTextfield.getText(), lastnameTextfield.getText(), Date.valueOf(dateOfBirthTextfield.getText()));
            moreAuthorsButton.setDisable(false);
            doneButton.setDisable(false);
        }
        else {
            moreAuthorsButton.setDisable(true);
            doneButton.setDisable(true);
        }
    }

    public void addAuthorsToBookAndDB(){
        addBookAuthorModel.addAuthors();
        try {
            addBookAuthorModel.addBookToDB();
        }catch(DbInsertException | SQLException ex){
            resetAuthor();
            resetBookInfo();
            // Send alert to user here
        }
    }

    public void resetAuthor(){
        firstnameTextfield.clear();
        lastnameTextfield.clear();
        dateOfBirthTextfield.clear();
        firstnameCircle.setFill(Color.WHITE);
        lastnameCircle.setFill(Color.WHITE);
        dateOfBirthCircle.setFill(Color.WHITE);
        moreAuthorsButton.setDisable(true);
        doneButton.setDisable(true);
    }

    public void resetBookInfo(){
        titleTextfield.clear();
        isbnTextfield.clear();
        descriptionTextfield.clear();
        pagesTextfield.clear();
        titleCircle.setFill(Color.WHITE);
        isbnCircle.setFill(Color.WHITE);
        pagesCircle.setFill(Color.WHITE);
    }

    private boolean checkValidAuthorData(){
        boolean flag = true;
        if(addBookAuthorModel.checkValidFirstname(firstnameTextfield.getText()))
            firstnameCircle.setFill(Color.GREEN);
        else {
            firstnameCircle.setFill(Color.RED);
            flag = false;
        }
        if(addBookAuthorModel.checkValidLastname(lastnameTextfield.getText()))
            lastnameCircle.setFill(Color.LIMEGREEN);
        else {
            lastnameCircle.setFill(Color.RED);
            flag = false;
        }
        if(addBookAuthorModel.checkValidDate(dateOfBirthTextfield.getText()))
            dateOfBirthCircle.setFill(Color.LIMEGREEN);
        else{
            dateOfBirthCircle.setFill(Color.RED);
            flag = false;
        }
        return flag;
    }
}
