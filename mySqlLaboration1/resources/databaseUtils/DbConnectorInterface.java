package databaseUtils;

public interface DbConnectorInterface {
    void connectToDb() throws DbConnException;
    void disconnectDB();
}
