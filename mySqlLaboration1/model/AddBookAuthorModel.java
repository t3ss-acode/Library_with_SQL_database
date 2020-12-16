package model;

import com.mongodb.*;
import com.mongodb.MongoClient;
import com.mongodb.client.*;
import databaseUtils.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.objects.Author;
import model.objects.Book;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class AddBookAuthorModel implements DbAddBookInterface {
    private ArrayList<Author> authorsList = new ArrayList<>();
    private ObservableList<Author> existingAuthors = FXCollections.observableArrayList();
    private Book book;
    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> authorCollection;
    private MongoCollection<Document> bookCollection;

    public AddBookAuthorModel() throws DbConnException{
        client = DbConnector.getConnection();
        database = client.getDatabase("db1");
        authorCollection = database.getCollection("authors");
        bookCollection = database.getCollection("books");
    }
    /**
     * Takes the given parameters and makes a Book object
     */
    @Override
        public void addBook(Book book){
        this.book = book;
    }

    /**
     * Takes the given parameters and makes an Author object
     */
    public void addAuthor(Author author){
        authorsList.add(author);
        System.out.println(authorsList.toString());
    }

    /**
     * Add the arrayList of Authors to the Book object
     */
    public void addAuthors(){
        book.addAuthors(authorsList);
        System.out.println("Authors : " + authorsList.toString());
    }

    /**
     * Checks if the given title a valid title
     * @param title
     * @return boolean value for if the title uphold the requirements
     */

    /**
     * Adds the book into the database
     */
    public void addToDB(){
        ClientSession clientSession = null;
        try{
            clientSession = client.startSession();
            clientSession.startTransaction();
            addBookToDB(clientSession);
            addAuthorToDB(clientSession);
            clientSession.commitTransaction();
        }catch(DbInsertException | MongoException ex){
            assert clientSession != null;
            clientSession.abortTransaction();
        }finally {
            assert clientSession != null;
            clientSession.close();
        }
    }

    private void addBookToDB(ClientSession clientSession) throws DbInsertException {
        BasicDBObject isbnQuery = new BasicDBObject();
        isbnQuery.append("isbn", book.getIsbn());

        if(Math.toIntExact(bookCollection.countDocuments(isbnQuery)) != 0){
            throw new DbInsertException("Book already exists");
        }

        BasicDBObject authorObj;
        Document doc = new Document();
        doc.append("isbn", book.getIsbn());
        doc.append("title", book.getTitle());
        doc.append("pages", book.getNrOfPages());
        doc.append("desc", book.getDescription());
        doc.append("category", book.getCategory());
        doc.append("rating", book.getRating());

        List<BasicDBObject> listAuthors = new ArrayList<>();
        for(Author a : authorsList){
            authorObj = new BasicDBObject();
            authorObj.append("firstName", a.getFirstName());
            authorObj.append("lastName", a.getLastName());
            authorObj.append("dob", a.getDob());
            listAuthors.add(authorObj);
        }
        doc.append("authors", listAuthors);
        bookCollection.insertOne(doc);
    }

    private void addAuthorToDB(ClientSession clientSession){
        BasicDBObject authorQuery;
        Document doc;

        for(Author a : authorsList){
            authorQuery = new BasicDBObject();
            authorQuery.append("firstName", a.getFirstName());
            authorQuery.append("lastName", a.getLastName());
            authorQuery.append("dob", a.getDob());

            if(Math.toIntExact(authorCollection.countDocuments(authorQuery)) == 0){
                doc = new Document();
                doc.append("firstName", a.getFirstName());
                doc.append("lastName", a.getLastName());
                doc.append("dob", a.getDob());
                authorCollection.insertOne(doc);
            }
        }
    }

    // Inte klar..
    public ObservableList<Author> searchAuthor(String searchWord) throws DbConnException{
        BasicDBObject whereQuery = new BasicDBObject();
        Document pop = new Document();

        MongoCollection<Document> collection = database.getCollection("authors");

        pop.append("$regex", ".*" + Pattern.quote(searchWord) + ".*");
        whereQuery.put("firstName", pop);
        try{
            //Cursor points to the first document that fulfills the query
            FindIterable<Document> cursor = collection.find(whereQuery);

            convertToAuthorObject(cursor);

        }catch(MongoException ex){
            throw new DbConnException("Database Connection Failed");
        }
        return existingAuthors;
    }

    private void convertToAuthorObject(FindIterable<Document> cursor) {
        existingAuthors.clear();

        String firstName = null;
        String lastName = null;
        Date dob = null;

        for(Document docs : cursor) {
            firstName = docs.getString("firstName");
            lastName = docs.getString("lastName");
            dob = docs.getDate("dob");

            Author author = new Author(firstName, lastName, dob);
            existingAuthors.add(author);
        }

    }

}