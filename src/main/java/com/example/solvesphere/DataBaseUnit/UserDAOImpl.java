package com.example.solvesphere.DataBaseUnit;

import com.example.solvesphere.DBQueries.UserQueries;
import com.example.solvesphere.SecurityUnit.PasswordHasher;
import com.example.solvesphere.UserData.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl implements UserDAO {

    @Override
    public User getUserById(int id) {
        User user = null;
        /// get DB connection
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UserQueries.GET_USER_VIA_ID)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user = new User(
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"), // this is the hashed password
                        rs.getString("date_of_birth"),
                        rs.getString("country"),
                        null,  // handle fields_of_interest separately
                        rs.getDate("registration_date").toLocalDate(),
                        rs.getString("profile_picture")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public User getUserByUsernameAndPassword(String username, String password) {
        User user = null;
        String sql = UserQueries.SELECT_USER_BY_USERNAME_AND_PASSWORD;

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                //retrieve the user details
                user = new User(
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"), // this is the hashed password
                        rs.getString("date_of_birth"),
                        rs.getString("country"),
                        null,  // handle fields_of_interest separately
                        rs.getDate("registration_date").toLocalDate(),
                        rs.getString("profile_picture")
                );

                // Compare the hashed password
                PasswordHasher hasher = new PasswordHasher();
                if (!hasher.verifyPassword(password, user.getPassword())) {
                    user = null; // password does not match, return null
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user; // Return the user if credentials are valid, otherwise null
    }

    @Override
    public void addUser(User user) {
        String sql = UserQueries.INSERT_USER; // Use the insert query from UserQueries
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getDateOfBirth());
            stmt.setString(5, user.getCountry());
            stmt.setDate(6, java.sql.Date.valueOf(user.getRegistrationDate())); // Convert LocalDate to java.sql.Date
            stmt.setString(7, user.getProfilePicture());

            stmt.executeUpdate(); // execute the insert
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean userExists(String username, String email) {
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UserQueries.SELECT_USER_BY_USERNAME_AND_EMAIL)) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            ResultSet rs = stmt.executeQuery(); // execute the q

            if (rs.next()) {
                return rs.getInt(1) > 0; // Return true if a user exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // user does not exist
    }
}

