package com.example.solvesphere.NotificationsUnit;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface NotificationDAO {
    List<String> getNotifications(long userId);

    Map<Long, String> getUnseenNotificationsWithProblemTitles(long userId) throws SQLException, ClassNotFoundException;
    void markNotificationsAsSeen(List<Long> notificationIds) throws SQLException, ClassNotFoundException;

    long getUserIdByUsername(String username);
    void removeNotification(long userId, long requesterId, String notificationPrefix);
    List<String> getAllNotifications(long userId);

    void markNotificationsAsRead(long userId);

    void removeGeneralNotification(long currentUserId, String notification);
}
