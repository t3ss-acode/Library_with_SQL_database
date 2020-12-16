package databaseUtils;

public class DbConnException extends Exception {
    public DbConnException(){

    }

    public DbConnException(String message){
        super((message));
    }
}
