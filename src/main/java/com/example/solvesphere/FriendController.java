package com.example.solvesphere;

import com.example.solvesphere.DataBaseUnit.FriendDAO;
import com.example.solvesphere.DataBaseUnit.FriendDAOImpl;
import com.example.solvesphere.UserData.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class FriendController {
    @FXML private Button addFriendButton, acceptFriendButton, removeFriendButton;

    private User currentUser;
    private User selectedUser;
    private final FriendDAO friendDAO = new FriendDAOImpl();

    public void setUsers(User currentUser, User selectedUser) {
        this.currentUser = currentUser;
        this.selectedUser = selectedUser;
    }

    @FXML
    private void sendFriendRequest() {
        if (friendDAO.sendFriendRequest(currentUser.getId(), selectedUser.getId())) {
            showAlert("Friend Request Sent", "Your friend request has been sent!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Could not send friend request.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void acceptFriendRequest() {
        if (friendDAO.acceptFriendRequest(currentUser.getId(), selectedUser.getId())) {
            showAlert("Friend Request Accepted", "You are now friends!", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Could not accept friend request.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void removeFriend() {
        if (friendDAO.removeFriend(currentUser.getId(), selectedUser.getId())) {
            showAlert("Friend Removed", "Friend removed successfully.", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "Could not remove friend.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
