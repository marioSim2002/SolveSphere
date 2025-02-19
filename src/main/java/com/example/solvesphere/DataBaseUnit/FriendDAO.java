package com.example.solvesphere.DataBaseUnit;

import com.example.solvesphere.UserData.User;

import java.util.List;
import java.util.Map;

public interface FriendDAO {
    boolean sendFriendRequest(long userId, long friendId);

    boolean acceptFriendRequest(long userId, long friendId);

    boolean removeFriend(long userId, long friendId);

    List<Long> getFriends(long userId);

    boolean areUsersFriends(long userId1, long userId2);

    List<User> getFriendsListAsUsers(long userId);

    boolean markFriendRequestAsSeen(long userId, long friendId);

    void removeGeneralNotification(long userId, String message);

    Map<Long, String> getUnseenFriendRequests(long userId);
}
