package com.example.solvesphere.DataBaseUnit;

import java.security.cert.Certificate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseConnectionManager {

    ///DB connection///
    private static final String URL = "jdbc:mysql://localhost:3306/solvespheredata";
    private static final String USER = "root";
    private static final String PASSWORD = "mario123";

    // Method to establish and return a connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

}