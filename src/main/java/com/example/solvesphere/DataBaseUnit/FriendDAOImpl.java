package com.example.solvesphere.DataBaseUnit;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendDAOImpl implements FriendDAO {

    @Override
    public boolean sendFriendRequest(long userId, long friendId) {
        // first, check if the friendship record already exists
        String checkQuery = "SELECT status FROM friends WHERE (user_id = ? AND friend_id = ?) OR (user_id = ? AND friend_id = ?)";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {

            checkStmt.setLong(1, userId);
            checkStmt.setLong(2, friendId);
            checkStmt.setLong(3, friendId);
            checkStmt.setLong(4, userId);

            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                String existingStatus = rs.getString("status");

                if ("pending".equals(existingStatus)) {
                    System.out.println("Friend request already sent.");
                    return false; // Request already exists
                } else if ("accepted".equals(existingStatus)) {
                    System.out.println("You are already friends!");
                    return false; // Already friends
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        // If no existing record, insert the friend request
        String sql = "INSERT INTO friends (user_id, friend_id, status) VALUES (?, ?, 'pending')";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            stmt.setLong(2, friendId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Friend request sent successfully!");

                // ** Now Add Notification **
                String notificationSQL = "INSERT INTO notifications (user_id, message) VALUES (?, ?)";
                try (PreparedStatement notifStmt = conn.prepareStatement(notificationSQL)) {
                    notifStmt.setLong(1, friendId);
                    notifStmt.setString(2, "Friend Request from: " + getUsernameById(userId));
                    notifStmt.executeUpdate();
                }

                return true;
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;
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
    public boolean acceptFriendRequest(long userId, long friendId) {
        String sql = "UPDATE friends SET status = 'accepted' WHERE user_id = ? AND friend_id = ? AND status = 'pending'";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, friendId);  // Friend is now accepting
            stmt.setLong(2, userId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean removeFriend(long userId, long friendId) {
        String sql = "DELETE FROM friends WHERE (user_id = ? AND friend_id = ?) OR (user_id = ? AND friend_id = ?)";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            stmt.setLong(2, friendId);
            stmt.setLong(3, friendId);
            stmt.setLong(4, userId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Long> getFriends(long userId) {
        List<Long> friends = new ArrayList<>();
        String sql = "SELECT friend_id FROM friends WHERE user_id = ? AND status = 'accepted' " +
                "UNION SELECT user_id FROM friends WHERE friend_id = ? AND status = 'accepted'";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            stmt.setLong(2, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                friends.add(rs.getLong(1));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return friends;
    }


    @Override
    public boolean markFriendRequestAsSeen(long userId, long friendId) {
        String sql = "UPDATE friends SET is_seen = 1 WHERE user_id = ? AND friend_id = ? AND status = 'pending'";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, friendId);
            stmt.setLong(2, userId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
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


    @Override
    public Map<Long, String> getUnseenFriendRequests(long userId) {
        Map<Long, String> friendRequests = new HashMap<>();
        String sql = "SELECT users.id, users.username FROM friends "
                + "JOIN users ON friends.user_id = users.id "
                + "WHERE friends.friend_id = ? AND friends.status = 'pending' AND friends.seen = 0";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                long requesterId = rs.getLong("id"); // Get sender's ID
                String requesterUsername = rs.getString("username"); // Get sender's username
                friendRequests.put(requesterId, requesterUsername); // Store in the map
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return friendRequests;
    }

}
