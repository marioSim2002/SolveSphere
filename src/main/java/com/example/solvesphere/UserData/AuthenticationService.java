package com.example.solvesphere.UserData;

import com.example.solvesphere.DataBaseUnit.UserDAO;
import com.example.solvesphere.SecurityUnit.PasswordHasher;
import com.example.solvesphere.UserData.User;

public class AuthenticationService {
    private final UserDAO userDAO;

    public AuthenticationService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public String authenticateUser(String username, String password) {
        //check if the user exists
        User user = userDAO.getUserByUsername(username);
        if (user == null) {
            return "User not registered";  // User not found
        }

        //compare password with the stored hashed password
        PasswordHasher hasher = new PasswordHasher();
        if (!hasher.verifyPassword(password, user.getPassword())) {
            return "Wrong password";  // Password mismatch
        }
        //if both match, return success
        return "Login successful";
    }
}
