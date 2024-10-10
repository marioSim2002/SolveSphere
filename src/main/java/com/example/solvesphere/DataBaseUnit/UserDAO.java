package com.example.solvesphere.DataBaseUnit;

import com.example.solvesphere.UserData.User;

public interface UserDAO {
    User getUserById(int id);   // Fetch a user by ID

    User getUserByUsernameAndPassword(String username, String password);
    void addUser(User user);    // add a new user to the database

    boolean userExists(String username, String email); // check if the user already exists

}
