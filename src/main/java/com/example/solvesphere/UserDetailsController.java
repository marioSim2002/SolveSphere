package com.example.solvesphere;

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
import java.util.List;
import java.util.Map;

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
    public void onSaveChanges() {
        currentUser.setUsername(usernameField.getText());
        currentUser.setEmail(emailField.getText());
        currentUser.setDateOfBirth(dateOfBirthPicker.getValue());
        currentUser.setCountry(countryField.getText());
        // todo - update DB Logic (to alter data in DB)
        System.out.println("User details updated successfully!");
    }

    @FXML
    public void onChangePicture() {
        //allow the user to select a new profile picture
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Profile Picture");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(changePictureButton.getScene().getWindow());

        if (selectedFile != null) {
            profilePicture = selectedFile.getAbsolutePath();
            // Update profile picture in the view (placeholder logic)
            // profilePictureView.setImage(new Image(new FileInputStream(profilePicture)));
            System.out.println("Profile picture updated: " + profilePicture);
        }
    }

    private void buildImage(@NotNull User user){
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
