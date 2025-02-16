package com.example.solvesphere;

import com.example.solvesphere.UserData.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.ByteArrayInputStream;
import java.util.Objects;

public class ProfileCardController {

    @FXML
    private ImageView profileImage;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label userInterests;

    public void setUserData(User user) {
        usernameLabel.setText(user.getUsername());

        // Convert fields of interest to a readable format (if applicable)
        if (user.getFieldsOfInterest() != null && !user.getFieldsOfInterest().isEmpty()) {
            userInterests.setText(String.join(", ", user.getFieldsOfInterest().keySet()));
        } else {
            userInterests.setText("No interests provided");
        }

        // Load profile image from byte[]
        if (user.getProfilePicture() != null && user.getProfilePicture().length > 0) {
            ByteArrayInputStream bis = new ByteArrayInputStream(user.getProfilePicture());
            profileImage.setImage(new Image(bis));
        } else {
            profileImage.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource("/com/example/solvesphere/Images/userico.png")).toExternalForm()
            ));
        }
    }

    public void onProfileClick() {
        // TODO: Implement profile click behavior
    }
}
