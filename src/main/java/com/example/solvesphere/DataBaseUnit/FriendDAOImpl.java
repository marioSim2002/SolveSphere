package com.example.solvesphere.DataBaseUnit;

import com.example.solvesphere.DataBaseUnit.DBQueries.FriendsQueries;
import com.example.solvesphere.DataBaseUnit.DBQueries.UserQueries;
import com.example.solvesphere.UserData.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendDAOImpl implements FriendDAO {

    @Override
    public boolean sendFriendRequest(long userId, long friendId) {
        // first, check if the friendship record already exists

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(FriendsQueries.CHECK_IF_RECORD_EXISTS)) {

            checkStmt.setLong(1, userId);
            checkStmt.setLong(2, friendId);
            checkStmt.setLong(3, friendId);
            checkStmt.setLong(4, userId);

            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                String existingStatus = rs.getString("status");

                if ("pending".equals(existingStatus)) {
                    System.out.println("Friend request already sent.");
                    return false; //request already exists
                } else if ("accepted".equals(existingStatus)) {
                    System.out.println("You are already friends!");
                    return false; // Already friends
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        // if no existing record, insert the friend request
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FriendsQueries.INSERT_PENDING_FRIEND)) {

            stmt.setLong(1, userId);
            stmt.setLong(2, friendId);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Friend request sent successfully!");

                // ** Now Add Notification **
//                String notificationSQL = "INSERT INTO notifications (user_id, message) VALUES (?, ?)";
//                try (PreparedStatement notifStmt = conn.prepareStatement(notificationSQL)) {
//                    notifStmt.setLong(1, friendId);
//                    notifStmt.setString(2, "Friend Request from: " + getUsernameById(userId));
//                    notifStmt.executeUpdate();
//                }

                return true;
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    private String getUsernameById(long userId) {
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UserQueries.GET_USERNAME_BY_ID)) {
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

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FriendsQueries.ACCEPT_FRIEND_REQ)) {

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

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FriendsQueries.REMOVE_FRIEND)) {

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

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FriendsQueries.GET_FRIENDS_LIST)) {

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
    public boolean areUsersFriends(long userId1, long userId2) {

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FriendsQueries.CHECK_IF_USERS_FRIENDS)) {

            stmt.setLong(1, userId1);
            stmt.setLong(2, userId2);
            stmt.setLong(3, userId2);
            stmt.setLong(4, userId1);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // If count > 0, users are friends
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public List<User> getFriendsListAsUsers(long userId) {
        List<User> friends = new ArrayList<>();
        String sql = "SELECT u.id, u.username, u.email, u.profile_picture, u.country, u.date_of_birth, u.registration_date " +
                "FROM friends f " +
                "JOIN users u ON (f.user_id = u.id OR f.friend_id = u.id) " +
                "WHERE (f.user_id = ? OR f.friend_id = ?) AND f.status = 'accepted' AND u.id != ?";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            stmt.setLong(2, userId);
            stmt.setLong(3, userId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User friend = new User();
                friend.setId(rs.getLong("id"));
                friend.setUsername(rs.getString("username"));
                friend.setEmail(rs.getString("email"));
                friend.setCountry(rs.getString("country"));
                friend.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
                friend.setRegistrationDate(rs.getDate("registration_date").toLocalDate()); // Added registration date
                friend.setProfilePicture(rs.getBytes("profile_picture")); // Load the image from DB

                friends.add(friend);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return friends;
    }


    @Override
    public boolean markFriendRequestAsSeen(long userId, long friendId) {

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FriendsQueries.MARK_REQUEST_SEEN)) {

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


    @Override
    public List<Long> getFriendIds(long userId) {
        List<Long> friendIds = new ArrayList<>();

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FriendsQueries.GET_FRIENDS_IDS)) {

            stmt.setLong(1, userId);
            stmt.setLong(2, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                long id1 = rs.getLong("user_id");
                long id2 = rs.getLong("friend_id");
                if (id1 != userId) {
                    friendIds.add(id1);
                }
                if (id2 != userId) {
                    friendIds.add(id2);
                }
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return friendIds;
    }
}
