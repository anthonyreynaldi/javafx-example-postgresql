package org.example.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection db;

    public static Connection getConnection() throws SQLException {
        String dbName = "hr";
        String user = "postgres";
        String password = "admin";
        String url = "jdbc:postgresql://localhost:5432/" + dbName;

        try{
            db = DriverManager.getConnection(url, user, password);
        }catch (Exception e){
            e.printStackTrace();
        }

        return db;
    }
}
