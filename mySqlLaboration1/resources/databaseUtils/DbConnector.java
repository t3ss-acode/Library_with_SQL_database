package databaseUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnector {
    private static final String username = "stduser";
    private static final String password = "kth";
    private static final String conn = "jdbc:mysql://81.234.103.217:8457/Laboration1?serverTimezone = UTC";

    public static Connection getConnection() throws DbConnException{
        try{
            return DriverManager.getConnection(conn, username, password);
        }catch(SQLException ex){
            throw new DbConnException("Database Connection Failed");
        }
    }
}
