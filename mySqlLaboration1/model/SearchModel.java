package model;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import databaseUtils.DbConnException;
import databaseUtils.DbConnector;
import databaseUtils.DbSearchInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.objects.Author;
import model.objects.Book;
import model.objects.Category;
import org.bson.Document;

import java.util.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SearchModel extends Thread implements DbSearchInterface {
    private ObservableList<Book> books = FXCollections.observableArrayList();
    private ArrayList<Author> authors = new ArrayList<>();


    public SearchModel(){
    }


    /*
        db.books.insert({
        isbn: '1111111111111',
        title: 'lord',
        pages: '323',
        description: 'blo blo blo',
        category: 'horror',
        rating: '4',
        authors: [{firstName: 'joe', lastName: 'smith', dob: new Date("1944-04-18")},
        {firstName: 'billy', lastName: 'bob', dob: new Date("1967-11-12")}]
        })

        db.test.insert({title: 'bla', date: new Date("1990-09-09")})
        new Date("<YYYY-mm-dd>")
 */


    /**
     * Checks if there are books in the database that contain the given search word in the given search category
     *
     * @param searchWord
     * @param option
     * @return An observableList with all the books that contain the search word for that search category
     */
    //@Override
    public ObservableList<Book> trySearch(String searchWord, String option) throws DbConnException {
        BasicDBObject whereQuery = new BasicDBObject();
        Document pop = new Document();

        MongoClient client = DbConnector.getConnection();
        MongoDatabase database = client.getDatabase("db1");
        MongoCollection<Document> collection = database.getCollection("books");

        pop.append("$regex", ".*" + Pattern.quote(searchWord) + ".*");

        switch (option) {
            case "Title":
                whereQuery.put("title", pop);
                break;
            case "Author":
                whereQuery.put("authors.firstName", pop);
                break;
            case "ISBN":
                whereQuery.put("isbn", pop);
                break;
            case "Rating":
                whereQuery.put("rating", searchWord);
                break;
            case "Category":
                whereQuery.put("category", pop);
                break;
            default:
                System.out.println("Nothing selected");
                whereQuery.put("title", " ");
        }

        try{
            FindIterable<Document> cursor = collection.find(whereQuery);
            convertToBookObject(cursor);
        }catch(MongoException ex){
            throw new DbConnException("Database Connection Failed");
        }

        return books;
    }

    private void convertToBookObject(FindIterable<Document> cursor) throws DbConnException {
        books.clear();

        String title = null;
        String isbn =  null;
        String description = null;
        int nrOfPages = 0;
        int rating = 0;
        Category category = null;

        String firstName = null;
        String lastName = null;
        Date dob = null;


        for (Document docs : cursor) {
            authors.clear();

            title = docs.getString("title");
            isbn = docs.getString("isbn");
            description = docs.getString("desc");
            nrOfPages = docs.getInteger("pages").intValue();
            rating = docs.getInteger("rating").intValue();
            category = Category.valueOf(docs.getString("category"));

            //Get the books authors
            List<Document> booksAuthors = (List<Document>) docs.get("authors");
            for (Document authorDocs : booksAuthors) {
                firstName = authorDocs.getString("firstName");
                lastName = authorDocs.getString("lastName");
                dob = authorDocs.getDate("dob");

                Author author = new Author(firstName, lastName, dob);
                authors.add(author);
            }

            //Make the book object and add it to the list
            Book book = new Book(title, isbn, description, nrOfPages, rating, category);
            book.addAuthors(authors);
            books.add(book);
        }
    }

    private void getAuthors(String isbn) throws DbConnException {
    }

    private void convertToAuthorObject(ResultSet resultSet) {
    }
}
