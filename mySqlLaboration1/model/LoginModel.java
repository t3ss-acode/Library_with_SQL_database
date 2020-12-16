package model;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import databaseUtils.DbConnException;
import databaseUtils.DbConnector;
import org.bson.Document;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginModel{
    private MongoDatabase mongoDatabase;
    private MongoClient mongoClient;
    public LoginModel(){
        try{
            connectToDb();
        }catch(DbConnException ex){
            ex.getMessage();
        }
    }
    /**
     * Connect to the database
     *
     * @throws DbConnException
     */
    private void connectToDb() throws DbConnException{
        mongoClient= DbConnector.getConnection();
        mongoDatabase = mongoClient.getDatabase("db1");
    }

    /**
     * Checks if it's connected to the database
     *
     * @return boolean if it's connected
     */
    public boolean isDbConnected(){
        return this.mongoDatabase != null;
    }

    /**
     * Checks if the given username and password is in the database
     *
     * @param username
     * @param password
     * @return boolean if the parameters are in the database
     */
    public boolean tryLogin(String username, String password) throws MongoException {
        BasicDBObject loginFindQuery = new BasicDBObject();
        loginFindQuery.append("username", username);
        loginFindQuery.append("password", password);

        System.out.println("Connected to " + mongoDatabase.getName());

        MongoCollection<Document> userCollection = mongoDatabase.getCollection("Users");
        int userCount = Math.toIntExact(userCollection.countDocuments(loginFindQuery));
        System.out.println(userCount);
        return userCount != 0;
    }
}
