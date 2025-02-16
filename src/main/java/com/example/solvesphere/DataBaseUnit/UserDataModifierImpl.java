package com.example.solvesphere.DataBaseUnit;

import com.example.solvesphere.DBQueries.UpdateUserQueries;
import com.example.solvesphere.ServerUnit.ServerCommunicator;
import com.example.solvesphere.UserData.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class UserDataModifierImpl implements UserDataModifier {

    @Override
    public boolean updateUserDetails(User user, String newEmail) {
        ServerCommunicator serverCommunicator = new ServerCommunicator();
        System.out.println("UPDATE USER DETAILS " + user.getUsername() + " " + user.getEmail());
        long userId = serverCommunicator.fetchUserIdByUsernameAndEmail(user.getUsername(), user.getEmail());

        try (Connection conn = DatabaseConnectionManager.getConnection()) {
            conn.setAutoCommit(false);  // Start transaction

            try {
                // Update basic user details (including profile picture as BLOB)
                try (PreparedStatement stmt = conn.prepareStatement(UpdateUserQueries.UPDATE_USER_DATA_SCRIPT)) {
                    stmt.setString(1, user.getUsername());
                    stmt.setString(2, newEmail);
                    stmt.setString(3, user.getCountry());

                    // Store the profile picture as a byte array (BLOB)
                    if (user.getProfilePicture() != null && user.getProfilePicture().length > 0) {
                        stmt.setBytes(4, user.getProfilePicture());
                    } else {
                        stmt.setNull(4, java.sql.Types.BLOB);
                    }

                    stmt.setLong(5, userId);
                    stmt.executeUpdate();
                }

                // Update date of birth
                try (PreparedStatement stmt = conn.prepareStatement(UpdateUserQueries.UPDATE_BIRTHDATE_SCRIPT)) {
                    stmt.setDate(1, java.sql.Date.valueOf(user.getDateOfBirth()));
                    stmt.setLong(2, userId);
                    stmt.executeUpdate();
                }

                // Clear old interests from the many-to-many relationship table
                try (PreparedStatement stmt = conn.prepareStatement(UpdateUserQueries.DELETE_INTEREST_SCRIPT)) {
                    stmt.setLong(1, userId);
                    stmt.executeUpdate();
                }

                // Insert new interests with their priority levels
                try (PreparedStatement stmt = conn.prepareStatement(UpdateUserQueries.INSERT_INTEREST_SCRIPT)) {
                    for (Map.Entry<String, Integer> entry : user.getFieldsOfInterest().entrySet()) {
                        stmt.setLong(1, userId);
                        stmt.setString(2, entry.getKey());  // Interest name
                        stmt.setInt(3, entry.getValue());  // Priority level
                        stmt.addBatch();
                    }
                    stmt.executeBatch();  // Execute batch insert
                }

                conn.commit();  // Commit transaction
                return true;
            } catch (SQLException e) {
                conn.rollback();  // Rollback on failure
                e.printStackTrace();
                return false;
            } finally {
                conn.setAutoCommit(true);  // Restore default behavior
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateUserProfilePicture(long userId, byte[] profilePicture) {
        String sql = "UPDATE users SET profile_picture = ? WHERE id = ?";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (profilePicture != null && profilePicture.length > 0) {
                stmt.setBytes(1, profilePicture);
            } else {
                stmt.setNull(1, java.sql.Types.BLOB);
            }

            stmt.setLong(2, userId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
