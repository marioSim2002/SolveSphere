package com.example.solvesphere;

import com.example.solvesphere.DataBaseUnit.FriendDAO;
import com.example.solvesphere.DataBaseUnit.FriendDAOImpl;
import com.example.solvesphere.UserData.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Objects;

public class FriendItemController {
    @FXML private Button deleteFriend;
    @FXML private ImageView profileImage;
    @FXML private Label usernameLabel;
    @FXML private Button viewProfileButton;
    private ProfileTabbedController profileTabbedController;

    private User friend;
    private User currentUser;

    public void setFriendData(User friend, User currentUser) {
        this.friend = friend;
        this.currentUser = currentUser;
        usernameLabel.setText(friend.getUsername());

        // Load profile image from byte[]
        if (friend.getProfilePicture() != null && friend.getProfilePicture().length > 0) {
            ByteArrayInputStream bis = new ByteArrayInputStream(friend.getProfilePicture());
            profileImage.setImage(new Image(bis));
        } else {
            profileImage.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource("/com/example/solvesphere/Images/userico.png")).toExternalForm()
            ));
        }
    }

    @FXML
    private void onViewProfileClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("IndividualUserView.fxml"));
            Parent friendDetailsRoot = loader.load();
            IndividualUserViewController controller = loader.getController();
            controller.setUserData(friend, currentUser);

            Stage stage = new Stage();
            stage.setTitle(friend.getUsername() + " - Profile");
            stage.setScene(new Scene(friendDetailsRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onDeleteFriendClick() {
        FriendDAO friendDAO = new FriendDAOImpl();
        if (friendDAO.removeFriend(currentUser.getId(), friend.getId())) {
            profileTabbedController.loadFriendsList();
            AlertsUnit.successFriendDeletionAlert(friend.getUsername());

        }
    }
}
