package model;

import databaseUtils.DbUserInterface;
import model.objects.Author;
import model.objects.Book;
import model.objects.Category;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SearchModel{
    LoginModel loginModel = new LoginModel();
    ArrayList<Book> books = new ArrayList<>();
    ArrayList<Author> authors = new ArrayList<>();

    public SearchModel(){
    }

    //@Override
    public ArrayList<Book> trySearch(String searchWord, String option) throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultset = null;
        String sqlQuery = "";

        switch (option) {
            case "Title":
                sqlQuery = "SELECT * FROM Book where title = ?";
                break;
            case "Author":
                sqlQuery = "SELECT * FROM Book WHERE isbn IN " +
                        "(SELECT isbn FROM Author_Book WHERE idAuthor IN " +
                        "(SELECT idAuthor FROM Author WHERE firstName = ?))";
                break;
            case "ISBN":
                sqlQuery = "SELECT * FROM Book where ISBN = ?";
                break;
            case "Rating":
                sqlQuery = "SELECT * FROM Book where rating = ?";
                break;
            case "Category":
                sqlQuery = "SELECT * FROM Book where category = ?";
                break;
            default:
                System.out.println("Nothing selected");
                sqlQuery = "SELECT * FROM Book Where title = ? AND ISBN = ?";
        }

        // Replaces the question marks with the sent in parameter
        try{
            statement = loginModel.getConnection().prepareStatement(sqlQuery);
            statement.setString(1, searchWord);
            resultset = statement.executeQuery();

            convertToBookObject(resultset);

        }catch(SQLException ex){
            ex.printStackTrace();
        }
        finally {
            assert resultset != null;
            resultset.close();
        }

        return books;
    }

    private ArrayList<Book> convertToBookObject(ResultSet resultset) throws SQLException {
        books.clear();

        String title = null;
        String isbn =  null;
        long isbnNr = 0;
        String description = null;
        int nrOfPages = 0;
        int rating = 0;
        Category category = null;

        while (resultset.next()) {
            title = resultset.getString("title");
            isbnNr = resultset.getLong("ISBN");
            isbn = Long.toString(isbnNr);
            description = resultset.getString("description");
            nrOfPages = resultset.getInt("nrOfPages");
            rating = resultset.getInt("rating");
            category = Category.valueOf(resultset.getString("category"));

            getAuthors(isbn);

            Book book = new Book(title, isbn, description, nrOfPages, rating, category);
            books.add(book);

        }

        return books;
    }

    private void getAuthors(String isbn) throws SQLException {
        ResultSet authorResultSet = null;
        PreparedStatement statement = null;
        String sqlQuery = "SELECT * FROM Author where idAuthor IN " +
                "(SELECT idAuthor FROM Author_Book WHERE ISBN = ?)";

        statement = loginModel.getConnection().prepareStatement(sqlQuery);
        statement.setString(1, isbn);
        authorResultSet = statement.executeQuery();

        convertToAuthorObject(authorResultSet);
    }

    private void convertToAuthorObject(ResultSet resultSet) throws SQLException {
        authors.clear();
        int idAuthor = 0;
        String firstName = null;
        String lastName = null;
        Date dob = null;

        while (resultSet.next()) {
            idAuthor = resultSet.getInt("idAuthor");
            firstName = resultSet.getString("firstName");
            lastName = resultSet.getString("lastName");
            dob = resultSet.getDate("DOB");

            Author author = new Author(firstName, lastName, dob);
            authors.add(author);
        }
    }
}
