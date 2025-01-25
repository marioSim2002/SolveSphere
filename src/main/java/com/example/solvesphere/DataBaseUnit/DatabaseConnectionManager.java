package com.example.solvesphere.DataBaseUnit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseConnectionManager {

    ///DB connection///
    //// DONT PUSH NEW URL/USER/PASS ////
    private static final String URL = "jdbc:mysql://localhost:3306/solvespheredata";
    private static final String USER = "root";
    private static final String PASSWORD = "mario123";

    //establish and return a connection
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");  // register MySQL driver
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}