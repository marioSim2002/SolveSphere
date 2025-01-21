package com.example.solvesphere.DataBaseUnit;

import com.example.solvesphere.DBQueries.UpdateUserQueries;
import com.example.solvesphere.ServerUnit.ServerCommunicator;
import com.example.solvesphere.UserData.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class UserDataModifierImpl implements UserDataModifier {
    public boolean updateUserDetails(User user) {
        ServerCommunicator serverCommunicator = new ServerCommunicator();
         long userId = serverCommunicator.fetchUserIdByUsernameAndEmail(user.getUsername(),user.getEmail());
        try (Connection conn = DatabaseConnectionManager.getConnection()) {
            conn.setAutoCommit(false);  // Start transaction

            try {
                // update basic user details
                try (PreparedStatement stmt = conn.prepareStatement(UpdateUserQueries.UPDATE_USER_DATA_SCRIPT)) {
                    stmt.setString(1, user.getUsername());
                    stmt.setString(2, user.getEmail());
                    stmt.setString(3, user.getCountry());
                    stmt.setString(4, user.getProfilePicture());
                    stmt.setLong(5, userId);
                    stmt.executeUpdate();
                }

                // update date of birth
                try (PreparedStatement stmt = conn.prepareStatement(UpdateUserQueries.UPDATE_BIRTHDATE_SCRIPT)) {
                    stmt.setDate(1, java.sql.Date.valueOf(user.getDateOfBirth()));
                    stmt.setLong(2,userId);
                    stmt.executeUpdate();
                }

                // clear old interests from the many-to-many relationship table
                try (PreparedStatement stmt = conn.prepareStatement(UpdateUserQueries.DELETE_INTEREST_SCRIPT)) {
                    stmt.setLong(1, userId);
                    stmt.executeUpdate();
                }
                // insert new interests with their priority levels
                try (PreparedStatement stmt = conn.prepareStatement(UpdateUserQueries.INSERT_INTEREST_SCRIPT)) {
                    for (Map.Entry<String, Integer> entry : user.getFieldsOfInterest().entrySet()) {
                        stmt.setLong(1, userId);
                        stmt.setString(2, entry.getKey());  // Interest name
                        stmt.setInt(3, entry.getValue());  // Priority level
                        stmt.addBatch();
                    }
                    stmt.executeBatch();  //execute
                }

                conn.commit();  // commit transaction
                return true;
            } catch (SQLException e) {
                conn.rollback();  // rollback on failure
                e.printStackTrace();
                return false;
            } finally {
                conn.setAutoCommit(true);  // restore default behavior
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}

