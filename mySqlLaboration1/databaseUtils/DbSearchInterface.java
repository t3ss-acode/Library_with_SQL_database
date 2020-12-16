package databaseUtils;

import javafx.collections.ObservableList;
import model.objects.Book;
import model.objects.Category;

import java.sql.SQLException;
import java.util.ArrayList;

public interface DbSearchInterface {
    ObservableList<Book> trySearch(String searchWord, String option) throws SQLException, DbConnException;

}
