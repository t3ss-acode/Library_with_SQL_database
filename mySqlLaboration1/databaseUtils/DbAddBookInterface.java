package databaseUtils;

import javafx.collections.ObservableList;
import model.objects.Author;
import model.objects.Book;
import model.objects.Category;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public interface DbAddBookInterface {
    void addBook(Book book);
    void addAuthor(Author author);
    void addAuthors();
    ObservableList<Author> searchAuthor(String searchWord) throws DbConnException;

    void addToDB() throws DbInsertException, DbConnException;
}
