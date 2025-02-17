package com.example.solvesphere;

import com.example.solvesphere.UserData.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.util.Objects;

public class IndividualUserViewController {

    @FXML
    private ImageView profileImage;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label countryLabel;
    @FXML
    private Label userInterests;
    @FXML
    private Button addFriendButton;

    private User currentUser;

    public void setUserData(User user) {
        this.currentUser = user;
        usernameLabel.setText(user.getUsername());
        emailLabel.setText("Email: " + user.getEmail());
        countryLabel.setText("Country: " + user.getCountry());

        //convert fields of interest to a readable format
        if (user.getFieldsOfInterest() != null && !user.getFieldsOfInterest().isEmpty()) {
            userInterests.setText(String.join(", ", user.getFieldsOfInterest().keySet()));
        } else {
            userInterests.setText("No interests provided");
        }

        //load profile image from byte[]
        if (user.getProfilePicture() != null && user.getProfilePicture().length > 0) {
            ByteArrayInputStream bis = new ByteArrayInputStream(user.getProfilePicture());
            profileImage.setImage(new Image(bis));
        } else {
            profileImage.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource("/com/example/solvesphere/Images/userico.png")).toExternalForm()
            ));
        }
    }

    @FXML
    private void onSendMessageClick() {
        // TODO: Implement sending a message to the user
        System.out.println("Sending message to " + currentUser.getUsername());
    }

    @FXML
    private void onAddFriendClick() {
        // TODO: Implement adding the user as a friend
        System.out.println("Adding " + currentUser.getUsername() + " as a friend.");
    }
}
