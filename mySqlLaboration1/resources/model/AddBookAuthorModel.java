package model;

import databaseUtils.DbInsertException;
import databaseUtils.DbUserInterface;
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

public class AddBookAuthorModel implements DbUserInterface {
    private LoginModel loginModel = new LoginModel();
    private ArrayList<Author> authorsList = new ArrayList<Author>();
    private Book book;

    public AddBookAuthorModel(){
    }

    @Override
    public void addBook(String ISBN, String title, String description, int nrOfPages, int rating, Category category) {
        book = new Book(title, ISBN, description, nrOfPages, rating, category);
    }

    @Override
    public ArrayList trySearch(String searchWord, String option) {
        return new ArrayList();
    }

    public void addAuthor(String firstname, String lastname, Date dateOfBirth){
        Author author = new Author(firstname, lastname, dateOfBirth);
        authorsList.add(author);
        System.out.println(authorsList.toString());
    }

    public void addAuthors(){
        book.addAuthors(authorsList);
        System.out.println("Authors : " + authorsList.toString());
    }

    public boolean checkValidTitle(String title){
        return !title.trim().isEmpty() && title.length() < 31;
    }

    public boolean checkValidISBN(String ISBN){
        return !ISBN.trim().isEmpty();
    }

    public boolean checkValidPages(String pages){
        if(pages.trim().isEmpty())
            return false;
        return Integer.parseInt(pages) > 0;
    }

    public boolean checkValidFirstname(String firstname){
        return !firstname.trim().isEmpty() && !firstname.matches(".*\\d.*");
    }

    public boolean checkValidLastname(String lastname){
        return !lastname.trim().isEmpty() && !lastname.matches(".*\\d.*");
    }

    public boolean checkValidDate(String dateOfBirth){
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setLenient(false);
        try{
            format.parse(dateOfBirth);
            return true;
        }catch(ParseException ex){
            return false;
        }
    }

    public void addBookToDB() throws DbInsertException, SQLException{
        Connection con = null;
        PreparedStatement insertBook = null;
        PreparedStatement insertAuthor = null;
        PreparedStatement insertAuthorBook = null;

        String insertBookString = "insert INTO Book VALUES(?, ?, ?, ?, ?, ?)";
        String insertAuthorString = "insert INTO Author(firstName, lastName, DOB) VALUES(?, ?, ?)";
        String insertAuthorBookString = "insert INTO Author_Book VALUES(?, ?)";

        try{
            con = loginModel.getConnection();
            con.setAutoCommit(false);
            insertBook = loginModel.getConnection().prepareStatement(insertBookString);
            insertBook.setString(1, book.getIsbn());
            insertBook.setString(2, book.getTitle());
            insertBook.setString(3, book.getDescription());
            insertBook.setInt(4, book.getNrOfPages());
            insertBook.setString(5, book.getCategory());
            insertBook.setInt(6, book.getRating());
            insertBook.executeUpdate();

            for(Author a : authorsList){
                insertAuthor = loginModel.getConnection().prepareStatement(insertAuthorString);
                insertAuthor.setString(1, a.getFirstName());
                insertAuthor.setString(2, a.getLastName());
                insertAuthor.setDate(3, a.getDob());
                insertAuthor.executeUpdate();
            }
        }catch(SQLException e){
            try {
                con.rollback();
            }catch(SQLException excep){
                excep.printStackTrace();
            }
            throw new DbInsertException("Error Inserting Data into DB");
        }finally {
            if(insertBook != null)
                insertBook.close();
            if(insertAuthor != null)
                insertAuthor.close();
            assert con != null;
            con.setAutoCommit(true);
        }
    }
}