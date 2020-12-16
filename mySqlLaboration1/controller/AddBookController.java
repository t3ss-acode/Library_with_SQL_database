package controller;

import databaseUtils.DbAddBookInterface;
import databaseUtils.DbConnException;
import databaseUtils.DbInsertException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.AddBookAuthorModel;
import model.objects.Author;
import model.objects.Book;
import model.objects.Category;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

public class AddBookController implements Initializable {
    private DbAddBookInterface addBookAuthorModel = new AddBookAuthorModel();
    private ObservableList<Author> authorList = FXCollections.observableArrayList();

    @FXML
    public TextField titleTextfield;
    @FXML
    public TextField isbnTextfield;
    @FXML
    public TextField pagesTextfield;
    @FXML
    public TextField searchAuthor;

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
    @FXML
    public Button submitBookButton;
    @FXML
    public Button searchButton;

    @FXML
    public VBox searchAuthorVbox;
    @FXML
    public VBox addAuthorVbox;
    @FXML
    public VBox addBookVbox;

    @FXML
    public TableView<Author> searchTable;
    @FXML
    public TableColumn<Author, String> firstNameColumn;
    @FXML
    public TableColumn<Author, String> lastNameColumn;
    @FXML
    public TableColumn<Author, Date> dobColumn;

    private String cssLayoutDisable = "-fx-border-color: red;\n" +
            "-fx-border-insets: 5;\n" +
            "-fx-border-width: 3;\n" +
            "-fx-border-style: dashed;\n";
    private String cssLayoutEnable = "-fx-border-color: green;\n" +
            "-fx-border-insets: 5;\n" +
            "-fx-border-width: 3;\n" +
            "-fx-border-style: dashed;\n";

    public AddBookController() throws DbConnException {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        categoryBox.getItems().setAll(Category.values());
        categoryBox.getSelectionModel().selectFirst();

        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        dobColumn.setCellValueFactory(new PropertyValueFactory<>("dob"));

        enableAddBook();
        disableAddAuthor();
        disableSearchAuthor();
    }

    public void addBook(){
        if(checkValidBookData()) {
            Book book = new Book(titleTextfield.getText(), isbnTextfield.getText(), descriptionTextfield.getText(), Integer.parseInt(pagesTextfield.getText()), Integer.parseInt(ratingTextfield.getText()), (Category) categoryBox.getValue());
            addBookAuthorModel.addBook(book);
            disableAddBook();
            enableSearchAuthor();
        }
    }

    // Load search results into temp list and let user select on of them to be added into the current authorlist
    public void searchExistingAuthor(){
        authorList.clear();

        new Thread() {
            public void run(){
                try {
                    authorList.addAll(addBookAuthorModel.searchAuthor(searchAuthor.getText()));
                } catch (DbConnException ex){
                    //initConnectionAlert();
                    System.out.println();
                }

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(authorList.isEmpty())
                            System.out.println();
                        else
                            searchTable.setItems(authorList);
                    }
                });
            }
        }.start();
    }

    public void addNewAuthor(){
        if(checkValidAuthorData()){
            Author author = new Author(firstnameTextfield.getText(), lastnameTextfield.getText(), Date.valueOf(dateOfBirthTextfield.getText()));
            addBookAuthorModel.addAuthor(author);
            moreAuthorsButton.setDisable(false);
            doneButton.setDisable(false);
            submitButton.setDisable(true);
        }
        else {
            moreAuthorsButton.setDisable(true);
            doneButton.setDisable(true);
        }
    }

    public void addAuthorsToBookAndDB(){
        addBookAuthorModel.addAuthors();

        new Thread(() -> {
            try {
                addBookAuthorModel.addToDB();
            }catch(DbInsertException | DbConnException ex){
                resetAuthor();
                resetBookInfo();
                initInsertAlert();
            }

            Platform.runLater(() -> {
                Stage dashboardStage = (Stage) firstnameTextfield.getScene().getWindow();
                dashboardStage.close();
            });
        }).start();
    }

    public void initAddNewAuthor(){
        enableAddAuthor();
        disableSearchAuthor();
    }

    // Take user click in table view and convert it into a place in the temp array of search results.
    public void addExistingAuthor(){
        Author selectedAuthor = searchTable.getSelectionModel().getSelectedItem();
        addBookAuthorModel.addAuthor(selectedAuthor);
    }

    private void enableAddBook(){
        addBookVbox.setDisable(false);
        addBookVbox.setStyle(cssLayoutEnable);
    }

    private void disableAddBook(){
        addBookVbox.setDisable(true);
        addBookVbox.setStyle(cssLayoutDisable);
    }

    private void enableAddAuthor(){
        addAuthorVbox.setDisable(false);
        addAuthorVbox.setStyle(cssLayoutEnable);
    }

    private void disableAddAuthor(){
        addAuthorVbox.setDisable(true);
        addAuthorVbox.setStyle(cssLayoutDisable);
    }

    private void enableSearchAuthor(){
        searchAuthorVbox.setDisable(false);
        searchAuthorVbox.setStyle(cssLayoutEnable);
    }

    private void disableSearchAuthor(){
        searchAuthorVbox.setDisable(true);
        searchAuthorVbox.setStyle(cssLayoutDisable);
    }

    private boolean checkValidBookData(){
        boolean flag = true;
        if(Book.checkValidTitle(titleTextfield.getText()))
            titleCircle.setFill(Color.LIMEGREEN);
        else {
            titleCircle.setFill(Color.RED);
            flag = false;
        }
        if(Book.checkValidISBN(isbnTextfield.getText()))
            isbnCircle.setFill(Color.LIMEGREEN);
        else {
            isbnCircle.setFill(Color.RED);
            flag = false;
        }
        if(Book.checkValidPages(pagesTextfield.getText()))
            pagesCircle.setFill(Color.LIMEGREEN);
        else {
            pagesCircle.setFill(Color.RED);
            flag = false;
        }
        if(Book.checkValidRating(ratingTextfield.getText()))
            ratingCircle.setFill(Color.LIMEGREEN);
        else{
            ratingCircle.setFill(Color.RED);
            flag = false;
        }
        return flag;
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
        submitButton.setDisable(false);
    }

    private void resetBookInfo(){
        titleTextfield.clear();
        isbnTextfield.clear();
        descriptionTextfield.clear();
        pagesTextfield.clear();
        ratingTextfield.clear();
        submitBookButton.setDisable(false);
        titleCircle.setFill(Color.WHITE);
        isbnCircle.setFill(Color.WHITE);
        pagesCircle.setFill(Color.WHITE);
        ratingCircle.setFill(Color.WHITE);
    }

    private boolean checkValidAuthorData(){
        boolean flag = true;
        if(Author.checkValidFirstname(firstnameTextfield.getText()))
            firstnameCircle.setFill(Color.LIMEGREEN);
        else {
            firstnameCircle.setFill(Color.RED);
            flag = false;
        }
        if(Author.checkValidLastname(lastnameTextfield.getText()))
            lastnameCircle.setFill(Color.LIMEGREEN);
        else {
            lastnameCircle.setFill(Color.RED);
            flag = false;
        }
        if(Author.checkValidDate(dateOfBirthTextfield.getText()))
            dateOfBirthCircle.setFill(Color.LIMEGREEN);
        else{
            dateOfBirthCircle.setFill(Color.RED);
            flag = false;
        }
        return flag;
    }

    private void initInsertAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Book Already Exists!");
        alert.showAndWait();
    }
}
