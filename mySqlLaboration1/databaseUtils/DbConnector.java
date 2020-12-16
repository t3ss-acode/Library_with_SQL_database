package databaseUtils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientException;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.client.MongoDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnector{
    private static MongoDatabase database = null;
    private static MongoClient mongoClient;

    public static MongoClient getConnection() throws DbConnException{
        try{
            if(database == null) {
                String uri = "mongodb+srv://jesrn:Jesrn14697@cluster0-0dnfk.mongodb.net/test?retryWrites=true&w=majority";
                MongoClientURI clientURI = new MongoClientURI(uri);
                mongoClient = new MongoClient(clientURI);
                return mongoClient;
            }
            else
                return mongoClient;
        }catch(MongoException ex){
            throw new DbConnException("Database Connection Failed");
        }
    }

    public static void disconnectDB() throws DbDisconnectException, NullPointerException{
        try{
            mongoClient.close();
        }catch(MongoClientException ex){
            throw new DbDisconnectException("Disconnect to database failed");
        }
    }
}
