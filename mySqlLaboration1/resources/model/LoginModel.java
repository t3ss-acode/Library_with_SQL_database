package model;

import databaseUtils.DbConnectorInterface;
import databaseUtils.DbConnException;
import databaseUtils.DbConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginModel implements DbConnectorInterface {
    public Connection connection;

    public Connection getConnection() { return connection; }

    public LoginModel(){
        try{
            connectToDb();
        }catch(DbConnException ex) {
            ex.getMessage();
        }
    }

    @Override
    public void connectToDb() throws DbConnException{
        this.connection = DbConnector.getConnection();
    }

    public boolean isDbConnected(){
        return this.connection != null;
    }

    public void disconnectDB(){
        try{
            connection.close();
            System.out.println("disconnected");
        }catch(SQLException e){
            System.out.println("Disconnect failed");
        }
    }

    public boolean tryLogin(String username, String password) throws Exception{
        PreparedStatement statement = null;
        ResultSet resultset = null;
        String sqlQuery = "SELECT * FROM User where userName = ? and password = ?";

        // Replaces the question marks with the sent in username and password
        try{
            statement = this.connection.prepareStatement(sqlQuery);
            statement.setString(1, username);
            statement.setString(2, password);

            resultset = statement.executeQuery();

            return resultset.next();
        }catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }
        finally {
            assert statement != null;
            statement.close();
            assert resultset != null;
            resultset.close();
        }
    }
}
