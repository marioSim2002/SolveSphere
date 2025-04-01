package com.example.solvesphere.DataBaseUnit.DBQueries;

public class FriendsQueries {

    public static final String CHECK_IF_RECORD_EXISTS = "SELECT status FROM friends WHERE (user_id = ? AND friend_id = ?) OR (user_id = ? AND friend_id = ?)";
    //public static final String INSERT_PENDING_FRIEND = "SELECT status FROM friends WHERE (user_id = ? AND friend_id = ?) OR (user_id = ? AND friend_id = ?)";
    public static final String INSERT_PENDING_FRIEND =
            "INSERT INTO friends (user_id, friend_id, status, requested_at, seen) " +
                    "VALUES (?, ?, 'pending', NOW(), 0)";
    public static final String ACCEPT_FRIEND_REQ = "UPDATE friends SET status = 'accepted' WHERE user_id = ? AND friend_id = ? AND status = 'pending'";
    public static final String REMOVE_FRIEND = "SELECT username FROM users WHERE id = ?";
    public static final String MARK_REQUEST_SEEN = "UPDATE friends SET seen = 1 WHERE user_id = ? AND friend_id = ?";
    public static final String GET_FRIENDS_IDS = "SELECT user_id, friend_id FROM friends WHERE (user_id = ? OR friend_id = ?) AND status = 'accepted'";

    public static final String CHECK_IF_USERS_FRIENDS = "SELECT COUNT(*) FROM friends " +
            "WHERE ((user_id = ? AND friend_id = ?) OR (user_id = ? AND friend_id = ?)) " +
            "AND status = 'accepted'";

    public static final String GET_FRIENDS_LIST = "SELECT friend_id FROM friends WHERE user_id = ? AND status = 'accepted' " +
            "UNION SELECT user_id FROM friends WHERE friend_id = ? AND status = 'accepted'";

}
