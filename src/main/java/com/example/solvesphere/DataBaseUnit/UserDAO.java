package com.example.solvesphere.DataBaseUnit;

import com.example.solvesphere.UserData.Problem;
import com.example.solvesphere.UserData.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface UserDAO {
    User getUserById(long id);   // Fetch a user by ID
    User getUserByUsernameAndPassword(String username, String password);
    void addUser(User user) throws SQLException, ClassNotFoundException;    // add a new user to the database
    void setUserActivityStatus(long userId, String status) throws SQLException;
    String getUserActivityStatus(long userId) throws SQLException;
    boolean userExists(String username, String email); // check if the user already exists
    // user by username only
    User getUserByUsername(String username);
    Map<String, Integer> fetchFieldsOfInterest(long userId) throws SQLException, ClassNotFoundException;
    void addUserInterests(long userId, Map<String, Integer> interests) throws SQLException, ClassNotFoundException;

    Long getUserIdByUsernameAndEmail(String username, String email);

    List<User> searchUsers(String keyword) throws SQLException, ClassNotFoundException;

    List<User> getAllUsers() throws SQLException, ClassNotFoundException;

    Long getUserIdByUsernameAndPassword(String username, String password);
}
