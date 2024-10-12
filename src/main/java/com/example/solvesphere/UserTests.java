package com.example.solvesphere.TestUnit;

import com.example.solvesphere.DataBaseUnit.DatabaseConnectionManager;
import com.example.solvesphere.UserRegistrationHandler;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.SQLException;
public class UserTests {

    @Test
    public void testUserRegisteration(){
        UserRegistrationHandler userRegistrationHandler = new UserRegistrationHandler();
    }
}
