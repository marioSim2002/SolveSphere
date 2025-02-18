package com.example.solvesphere.NotificationsUnit;

import com.example.solvesphere.DataBaseUnit.DatabaseConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationDAOImpl implements NotificationDAO {


    @Override
    public List<String> getNotifications(long userId) {
        List<String> notifications = new ArrayList<>();
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT c.content, p.title FROM notifications n " +
                             "JOIN comments c ON n.comment_id = c.id " +
                             "JOIN problems p ON n.problem_id = p.id " +
                             "WHERE n.user_id = ? AND n.is_read = FALSE")) {

            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                notifications.add("New comment on '" + rs.getString("title") + "': " + rs.getString("content"));
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return notifications;
    }

    @Override
    public Map<Long, String> getUnseenNotificationsWithProblemTitles(long userId) throws SQLException, ClassNotFoundException {
        Map<Long, String> notifications = new HashMap<>();

        String query = """
        SELECT n.id, p.title
        FROM notifications n
        JOIN problems p ON n.problem_id = p.id
        WHERE n.user_id = ? AND n.is_seen = 0
    """;

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                long notificationId = rs.getLong("id");
                String problemTitle = rs.getString("title");
                notifications.put(notificationId, problemTitle);
            }
        }
        return notifications;
    }


    @Override
    public void markNotificationsAsSeen(List<Long> notificationIds) throws SQLException, ClassNotFoundException {
        if (notificationIds.isEmpty()) return;

        String query = "UPDATE notifications SET is_seen = 1 WHERE id = ?";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            for (Long id : notificationIds) {
                stmt.setLong(1, id);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }



    @Override
    public List<String> getAllNotifications(long userId) {
        List<String> notifications = new ArrayList<>();
        String query = "SELECT problem_id FROM notifications WHERE user_id = ? AND is_read = 0"; // Fetch unread

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                long problemId = rs.getLong("problem_id");
                notifications.add("New comment on Problem ID: " + problemId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return notifications;
    }


    @Override
    public void markNotificationsAsRead(long userId) {
        String updateQuery = "UPDATE notifications SET is_read = 1 WHERE user_id = ?";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setLong(1, userId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeGeneralNotification(long userId, String message) {
        String sql = "DELETE FROM notifications WHERE user_id = ? AND message = ?";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            stmt.setString(2, message);
            stmt.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private String getUsernameById(long userId) {
        String sql = "SELECT username FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("username");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public long getUserIdByUsername(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getLong("id");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return -1; // Return invalid ID if not found
    }

    @Override
    public void removeNotification(long userId, long requesterId, String notificationPrefix) {
        String sql = "DELETE FROM notifications WHERE user_id = ? AND message = ?";
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setString(2, notificationPrefix + getUsernameById(requesterId));
            stmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
