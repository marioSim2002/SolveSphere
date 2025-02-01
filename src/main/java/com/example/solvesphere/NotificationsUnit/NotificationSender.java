package com.example.solvesphere.NotificationsUnit;

import com.example.solvesphere.DataBaseUnit.DatabaseConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NotificationSender {
    public static void sendNotification(long problemId, long commentId) {
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO notifications (user_id, problem_id, comment_id) " +
                             "SELECT user_id, ?, ? FROM problems WHERE id = ?")) {

            stmt.setLong(1, problemId);
            stmt.setLong(2, commentId);
            stmt.setLong(3, problemId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}
