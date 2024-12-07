package com.example.solvesphere.TestUnit;

import com.example.solvesphere.DataBaseUnit.DatabaseConnectionManager;
import com.example.solvesphere.DataBaseUnit.UserDAO;
import com.example.solvesphere.UserData.User;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class DataBaseTests {

    private UserDAO userDAO;
    @Test
    public void testDataBaseConnection() throws SQLException, ClassNotFoundException {
        Connection conn = DatabaseConnectionManager.getConnection();
        assert !conn.isClosed() : "Connection should be open!";

    }



}