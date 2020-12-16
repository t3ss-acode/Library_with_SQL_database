package databaseUtils;

public interface BooksDBInterface {
    void connectToDb() throws DbConnException;
    void disconnectDB();
}
