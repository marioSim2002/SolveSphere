package com.example.solvesphere;

import com.example.solvesphere.DataBaseUnit.UserDAO;
import com.example.solvesphere.DataBaseUnit.UserDAOImpl;
import com.example.solvesphere.UserData.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.xml.transform.sax.TemplatesHandler;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class ProfileCardController {
    @FXML
    private VBox profileCard;
    @FXML
    private ImageView profileImage;
    @FXML
    private ImageView activeStatusIcon;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label userInterests;

    private User currentAppUser;
    private long viewedUserID;
    private User viewedUser;
    private final UserDAO userDAO = new UserDAOImpl();

    public void setUserData(User user,User appUser) throws SQLException {
        this.currentAppUser = appUser;
        this.viewedUser = user;
        this.viewedUserID = user.getId();
        usernameLabel.setText(user.getUsername());

        // Convert fields of interest to a readable format
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
            //// green icon if active ////
        if (userDAO.getUserActivityStatus(viewedUserID)) {
            activeStatusIcon.setVisible(true);
            activeStatusIcon.setImage(new Image(Objects.requireNonNull(getClass().getResource("/com/example/solvesphere/Images/onlineIco.png")).toExternalForm()));}
        else {
            activeStatusIcon.setVisible(false);
        }
    }


    @FXML
    public void onMouseEnter() {
        if (profileCard != null) {
            profileCard.setStyle("-fx-background-color: #eaf6ff; -fx-border-color: #3498db; -fx-border-width: 1.5; -fx-padding: 15; -fx-background-radius: 15; -fx-border-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0, 0, 255, 0.3), 10, 0, 0, 5);");
        }
    }

    @FXML
    public void onMouseExit() {
        if (profileCard != null) {
            profileCard.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 1; -fx-padding: 15; -fx-background-radius: 15; -fx-border-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");
        }
    }

    @FXML
    public void onProfileClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("IndividualUserView.fxml"));
            Parent root = loader.load();
            IndividualUserViewController controller = loader.getController();

            //pass User ID when opening the profile
            controller.setUserData(viewedUser,currentAppUser);

            Stage userProfileStage = new Stage();
            userProfileStage.setTitle(viewedUser.getUsername() + " - Profile");
            userProfileStage.setScene(new Scene(root, 500, 600));
            userProfileStage.setResizable(false);
            userProfileStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error opening IndividualUserView.fxml");
        }
    }
}
