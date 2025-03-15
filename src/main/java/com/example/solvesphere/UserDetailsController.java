package com.example.solvesphere;

import com.example.solvesphere.DataBaseUnit.UserDataModifier;
import com.example.solvesphere.DataBaseUnit.UserDataModifierImpl;
import com.example.solvesphere.UserData.Problem;
import com.example.solvesphere.UserData.User;
import com.example.solvesphere.ValidationsUnit.ValidateInputData;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserDetailsController {

    private byte[] profilePicture;

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

    public UserDetailsController() {}

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
                    .map(Map.Entry::getKey) // Only store the interest name
                    .reduce((s1, s2) -> s1 + ", " + s2) // Combine entries with a comma and space
                    .orElse("No interests specified");
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
            profilePicture = convertImageToByteArray(selectedFile);
            profilePictureView.setImage(new Image(selectedFile.toURI().toString()));

            //save the new profile picture to the database
            currentUser.setProfilePicture(profilePicture);
            UserDataModifier userModDAO = new UserDataModifierImpl();
            if (userModDAO.updateUserProfilePicture(currentUser.getId(), profilePicture)) {
                System.out.println("Profile picture updated in the database.");
            } else {
                System.out.println("Failed to update profile picture in the database.");
            }
        }
    }

    @FXML
    public void onSaveChanges() {
        // Validate basic input credentials
        String[] inputData = {usernameField.getText(), countryField.getText(), interestsField.getText()};
        if (!ValidateInputData.validTxtData(inputData) || !ValidateInputData.validEmail(emailField.getText())) {
            AlertsUnit.showInvalidDataAlert();
            return;
        }

        UserDataModifier userModDAO = new UserDataModifierImpl();

        currentUser.setUsername(usernameField.getText());
        currentUser.setDateOfBirth(dateOfBirthPicker.getValue());
        currentUser.setCountry(countryField.getText().toLowerCase());

        Map<String, Integer> interestsMap = new HashMap<>();
        for (String interest : interestsField.getText().split(",")) {
            interestsMap.put(interest.trim(), 2); // Default priority value = 2
        }
        currentUser.setFieldsOfInterest(interestsMap);

        boolean updated = userModDAO.updateUserDetails(currentUser, emailField.getText());
        if (updated) {
            AlertsUnit.successUserDetailUpdate();
        } else {
            AlertsUnit.showErrorAlert("Error processing request");
        }
    }

    private void buildImage(@NotNull User user) {
        byte[] profilePictureData = user.getProfilePicture();

        if (profilePictureData != null && profilePictureData.length > 0) {
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(profilePictureData);
                profilePictureView.setImage(new Image(bis));
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid image data");
                setDefaultProfilePicture();
            }
        } else {
            setDefaultProfilePicture();
        }
    }

    private void setDefaultProfilePicture() {
        profilePictureView.setImage(new Image(
                getClass().getResource("/com/example/solvesphere/Images/userico.png").toExternalForm()
        ));
    }

    private byte[] convertImageToByteArray(File imageFile) {
        try (FileInputStream fis = new FileInputStream(imageFile);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
