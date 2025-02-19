package com.example.solvesphere;

import com.almasb.fxgl.net.Server;
import com.example.solvesphere.DataBaseUnit.FriendDAO;
import com.example.solvesphere.DataBaseUnit.FriendDAOImpl;
import com.example.solvesphere.DataBaseUnit.UserDAO;
import com.example.solvesphere.ServerUnit.ServerCommunicator;
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
    private Label dateJoined;
    @FXML
    private Label userAge;
    @FXML
    private ImageView profileImage;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label countryLabel;
    @FXML
    private Label userInterests;
    @FXML
    private Button addFriendButton;
    private User currentAppUser;
    private User goalUser;

    FriendDAO friendDAO = new FriendDAOImpl();

    public void setUserData(User goalUser,User appUser) {
        this.currentAppUser = appUser; // the current user of the application
        this.goalUser = goalUser;
        usernameLabel.setText(goalUser.getUsername());
        countryLabel.setText(goalUser.getCountry());
        userAge.setText(String.valueOf(goalUser.calculateAge()));
        dateJoined.setText(goalUser.getRegistrationDate().toString());
        if(checkFriendsStatus()){addFriendButton.setText("Friends");}
        //convert fields of interest to a readable format
        if (goalUser.getFieldsOfInterest() != null && !goalUser.getFieldsOfInterest().isEmpty()) {
            userInterests.setText(String.join(", ", goalUser.getFieldsOfInterest().keySet()));
        } else {
            userInterests.setText("No interests provided");
        }

        //load profile image if found//
        if (goalUser.getProfilePicture() != null && goalUser.getProfilePicture().length > 0) {
            ByteArrayInputStream bis = new ByteArrayInputStream(goalUser.getProfilePicture());
            profileImage.setImage(new Image(bis));
        } else {
            // set the default image //
            profileImage.setImage(new Image(
                    Objects.requireNonNull(getClass().getResource("/com/example/solvesphere/Images/userico.png")).toExternalForm()
            ));
        }
    }

    @FXML
    private void onSendMessageClick() {
        // TODO: Implement sending a message to the user
        System.out.println("Sending message to " + goalUser.getUsername());
    }

    @FXML
    private void onAddFriendClick() {

        if(checkFriendsStatus()){return;} // if users already friends,return
        /// relevant objs for operations ///
        ServerCommunicator serverCommunicator = new ServerCommunicator();
        FriendDAO friendDAO = new FriendDAOImpl();

        long goalUserID = serverCommunicator.fetchUserIdByUsernameAndEmail(goalUser.getUsername(), goalUser.getEmail());//get the goal user(to add) ID
        long currentAppUserID = serverCommunicator.fetchUserIdByUsernameAndEmail(currentAppUser.getUsername(), currentAppUser.getEmail());
        friendDAO.sendFriendRequest(currentAppUserID,goalUserID);
        addFriendButton.setText("Request Sent");
    }

    private boolean checkFriendsStatus(){
        return friendDAO.areUsersFriends(extractUserID(goalUser),extractUserID(currentAppUser));
    }
    private long extractUserID(User user){
        ServerCommunicator serverCommunicator = new ServerCommunicator();
        return serverCommunicator.fetchUserIdByUsernameAndEmail(user.getUsername(),user.getEmail());
    }
}
