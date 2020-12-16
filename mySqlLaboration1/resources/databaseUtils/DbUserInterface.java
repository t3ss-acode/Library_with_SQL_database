package databaseUtils;

import model.objects.Book;
import model.objects.Category;

import java.sql.SQLException;
import java.util.ArrayList;

public interface DbUserInterface {
    void addBook(String ISBN, String title, String description, int nrOfPages, int rating, Category category);
    ArrayList<Book> trySearch(String searchWord, String option) throws SQLException;

}
