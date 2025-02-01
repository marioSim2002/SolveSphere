package com.example.solvesphere;
import com.example.solvesphere.NotificationsUnit.NotificationDAO;
import com.example.solvesphere.NotificationsUnit.NotificationDAOImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.util.List;

public class NotificationsController {

    @FXML
    private ListView<String> notificationList;

    @FXML
    private Button closeButton;

    private final NotificationDAO  notificationDAO = new NotificationDAOImpl();
    private long currentUserId;

    public void initialize(long userId) {
        this.currentUserId = userId;
        loadNotifications();
    }

    private void loadNotifications() {
        List<String> notifications = notificationDAO.getNotifications(currentUserId);
        notificationList.getItems().addAll(notifications);
    }

    @FXML
    private void markAllAsRead() {
        notificationDAO.markNotificationsAsRead(currentUserId);
        notificationList.getItems().clear(); // Clear list after marking as read
    }

    @FXML
    private void onClose() {
        Stage stage = (Stage) notificationList.getScene().getWindow();
        stage.close();
    }
}
