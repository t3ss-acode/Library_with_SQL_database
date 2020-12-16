package databaseUtils;

public class DbDisconnectException extends Exception {
    public DbDisconnectException(){

    }

    public DbDisconnectException(String message){
        super((message));
    }
}
