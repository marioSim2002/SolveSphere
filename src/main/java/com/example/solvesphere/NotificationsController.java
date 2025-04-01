package com.example.solvesphere;

import com.example.solvesphere.NotificationsUnit.NotificationDAO;
import com.example.solvesphere.NotificationsUnit.NotificationDAOImpl;
import com.example.solvesphere.DataBaseUnit.FriendDAO;
import com.example.solvesphere.DataBaseUnit.FriendDAOImpl;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.util.List;
import java.util.Map;

public class NotificationsController {

    @FXML
    private ListView<HBox> notificationList;

    @FXML
    private Button closeButton;

    private final NotificationDAO notificationDAO = new NotificationDAOImpl();
    private final FriendDAO friendDAO = new FriendDAOImpl();
    private long currentUserId;

    public void initialize(long userId) {
        this.currentUserId = userId;
        loadNotifications();
    }

    private void loadNotifications() {
        notificationList.getItems().clear(); // Clear existing notifications
        loadFriendRequests(); // Load friend requests separately
        loadGeneralNotifications(); // Load general notifications separately
    }
    public void loadFriendRequests() {
        if (notificationList!=null){ notificationList.getItems().clear();} // clear previous entries\ prevent duplicates

        Map<Long, String> friendRequests = friendDAO.getUnseenFriendRequests(currentUserId);

        for (Map.Entry<Long, String> entry : friendRequests.entrySet()) {
            long requesterId = entry.getKey();
            String requesterUsername = entry.getValue();

            HBox notificationItem = createFriendRequestItem(requesterUsername, requesterId);
            notificationList.getItems().add(notificationItem);
        }
    }

    private void loadGeneralNotifications() {
        List<String> notifications = notificationDAO.getNotifications(currentUserId);

        for (String notification : notifications) {
            if (!notification.startsWith("Friend Request from: ")) {
                HBox normalNotification = createGeneralNotificationItem(notification);
                notificationList.getItems().add(normalNotification);
            }
        }
    }

    // friend request item with buttons //
    private HBox createFriendRequestItem(String username, long userId) {
        Label label = new Label("Friend request from: " + username);
        Button acceptButton = new Button("✅ Accept");
        Button rejectButton = new Button("❌ Reject");

        acceptButton.setOnAction(event -> {
            acceptFriendRequest(userId);
            notificationList.getItems().removeIf(item -> item.getChildren().contains(label)); // remove from UI
        });

        rejectButton.setOnAction(event -> {
            rejectFriendRequest(userId);
            notificationList.getItems().removeIf(item -> item.getChildren().contains(label)); // remove from UI
        });

        return new HBox(15, label, acceptButton, rejectButton);
    }


    // normal notification item (not friend request) //
    private HBox createGeneralNotificationItem(String notification) {
        Label label = new Label(notification);
        Button dismissButton = new Button("🗑 Dismiss");

        dismissButton.setOnAction(event -> {
            notificationDAO.removeGeneralNotification(currentUserId, notification);
            notificationList.getItems().removeIf(item -> item.getChildren().contains(label)); //remove from UI
        });

        return new HBox(10, label, dismissButton);
    }

    private void acceptFriendRequest(long requesterId) {
        if (friendDAO.acceptFriendRequest(currentUserId, requesterId)) {
            notificationDAO.removeNotification(currentUserId, requesterId, "Friend Request from: ");
            showAlert("Friend Request Accepted", "You are now friends!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Could not accept the friend request.", Alert.AlertType.ERROR);
        }
    }

    private void rejectFriendRequest(long requesterId) {
        notificationDAO.removeNotification(currentUserId, requesterId, "Friend Request from: ");
        showAlert("Friend Request Rejected", "The friend request has been rejected.", Alert.AlertType.INFORMATION);
    }

    @FXML
    private void markAllAsRead() {
        notificationDAO.markNotificationsAsRead(currentUserId);
        notificationList.getItems().clear();
    }

    @FXML
    private void onClose() {
        Stage stage = (Stage) notificationList.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
