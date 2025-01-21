package com.example.solvesphere;

import com.example.solvesphere.DataBaseUnit.UserDataModifier;
import com.example.solvesphere.DataBaseUnit.UserDataModifierImpl;
import com.example.solvesphere.UserData.Problem;
import com.example.solvesphere.UserData.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserDetailsController {
    //    private long id;
//    private String username;
//    private String email;
//    private LocalDate dateOfBirth;
//    private String country;
//    private Map<String, Integer> fieldsOfInterest;  // Interest levels for various fields
//    private LocalDate registrationDate;
    private String profilePicture;
    private List<Problem> problems;  // List of problems associated with the user

    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private DatePicker dateOfBirthPicker;
    @FXML
    private TextField countryField;
    @FXML
    private TextField interestsField;
    @FXML
    private ImageView profilePictureView;
    @FXML
    private Button changePictureButton;

    private User currentUser;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        loadUserDataIntoFields();
    }

    public UserDetailsController() {
    }


    private void loadUserDataIntoFields() {
        if (currentUser != null) {
            usernameField.setText(currentUser.getUsername());
            emailField.setText(currentUser.getEmail());
            dateOfBirthPicker.setValue(currentUser.getDateOfBirth());
            countryField.setText(currentUser.getCountry());
            buildImage(currentUser);
            String formattedInterests = currentUser.getFieldsOfInterest()
                    .entrySet()
                    .stream()
                    .map(Map.Entry::getKey)  // value is priority level
                    .reduce((s1, s2) -> s1 + ", " + s2) //entries with a comma and space
                    .orElse("No interests specified"); //default message if the map is empty
            interestsField.setText(formattedInterests);
        }
    }

    @FXML
    public void onChangePicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Picture");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(changePictureButton.getScene().getWindow());

        if (selectedFile != null) {
            profilePicture = selectedFile.getAbsolutePath();
            profilePictureView.setImage(new Image(selectedFile.toURI().toString()));

            //save the new profile picture to the database
            currentUser.setProfilePicture(profilePicture);
            UserDataModifier userModDAO = new UserDataModifierImpl();
            if (userModDAO.updateUserDetails(currentUser)) {System.out.println("Profile picture updated in the database.");}
            else {System.out.println("Failed to update profile picture in the database.");}
        }
    }

    @FXML
    public void onSaveChanges() {
        UserDataModifier userModDAO = new UserDataModifierImpl();

        currentUser.setUsername(usernameField.getText());
        currentUser.setEmail(emailField.getText());
        currentUser.setDateOfBirth(dateOfBirthPicker.getValue());
        currentUser.setCountry(countryField.getText());

        Map<String, Integer> interestsMap = new HashMap<>();
        for (String interest : interestsField.getText().split(",")) {
            interestsMap.put(interest.trim(), 2);  //default priority value = 2
        }
        currentUser.setFieldsOfInterest(interestsMap);

        boolean updated = userModDAO.updateUserDetails(currentUser);
        if (updated) {
            AlertsUnit.successUserDetailUpdate();
        } else {AlertsUnit.showErrorAlert("Error processing request");}
    }

    private void buildImage(@NotNull User user) {
        String profilePicturePath = user.getProfilePicture();

        if (profilePicturePath != null && !profilePicturePath.isEmpty()) {
            try {
                //convert the string path to an Image
                Image image = new Image(profilePicturePath);
                profilePictureView.setImage(image);
            } catch (IllegalArgumentException e) { // prevent error in loading
                System.out.println("Invalid image path: " + profilePicturePath);
                profilePictureView.setImage(new Image("G:\\My Drive\\solveSphere\\userico.png"));
            }
        } else {
            //default image (null or empty)
            profilePictureView.setImage(new Image("G:\\My Drive\\solveSphere\\userico.png"));
        }
    }
}


