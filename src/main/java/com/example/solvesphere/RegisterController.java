package com.example.solvesphere;

import com.example.solvesphere.UserData.User;
import com.example.solvesphere.UserData.UserFactory;
import com.example.solvesphere.ValidationsUnit.ValidateInputData;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RegisterController {
    @FXML
    private TextField TxtUsername;
    @FXML
    private TextField TxtMail;
    @FXML
    private PasswordField TxtPass;
    @FXML
    private DatePicker DateOfBirthVal;
    @FXML
    private ComboBox<String> CountryInput;
    @FXML
    private TextField fieldOfInterestInput;

    @FXML
    private CheckBox showPassCheck;
    @FXML
    private TextField TxtPassVisible;
    @FXML
    private ImageView profileImageView;

    @FXML
    private Hyperlink btChooseImage;

    private String imagePath;
    private final ServerCommunicator serverCommunicator;

    public RegisterController() {
        serverCommunicator = new ServerCommunicator("localhost", 12345);  // initialize server communicator
    }

    @FXML
    public void closeCurrentStage(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


    @FXML
    public void registerUser() {
        // collect user data
        String username = TxtUsername.getText();
        String email = TxtMail.getText();
        String password = getPassword();
        LocalDate dateOfBirth = DateOfBirthVal.getValue();
        String country = CountryInput.getValue();

        // validate data before sending to the server
        String[] userDataArr = {username, email, password, country};
        if (!ValidateInputData.validTxtData(userDataArr) ||
                !ValidateInputData.validEmail(email) ||
                !ValidateInputData.validDate(dateOfBirth)) {
            AlertsUnit.showInvalidDataAlert();
            return;
        }

        User newUser = UserFactory.createUser(username, email, password,
                dateOfBirth, country, getWordsFromFieldOfInterest(),
                LocalDate.now(), getProfileImagePath());

        // run registration in a separate THREAD
        Task<String> registrationTask = new Task<String>() {
            @Override
            protected String call() {
                return serverCommunicator.sendRegistrationRequest(newUser);
            }

            @Override
            protected void succeeded() {
                String response = getValue();
                System.out.println("Server response: " + response); // debugging (check response)

                if (response.contains("User registered successfully")) {
                    AlertsUnit.showSuccessAlert(); // success alert if registration is successful
                    clearInputFields(); // clear input fields on success
                } else if (response.contains("Username or email already exists")) {
                    AlertsUnit.userAlreadyRegistered(); //alert for existing user
                } else {
                    AlertsUnit.showInvalidDataAlert(); //show general error alert
                }
            }

            @Override
            protected void failed() {
                Throwable ex = getException();
                ex.printStackTrace(); // print the exception / debug
                AlertsUnit.showErrorAlert(ex.getMessage()); //show error alert if registration failed
            }
        };

        new Thread(registrationTask).start(); // Start the task in a new thread
    }


    private void clearInputFields() {
            TxtUsername.clear();
            TxtMail.clear();
            TxtPass.clear();
            DateOfBirthVal.setValue(null);
            CountryInput.setValue(null);
            fieldOfInterestInput.clear();
            profileImageView.setImage(null);
    }

    public void togglePassVisibility(ActionEvent actionEvent) {
        if (showPassCheck.isSelected()) {
            // show
            TxtPassVisible.setText(TxtPass.getText());
            TxtPassVisible.setVisible(true);
            TxtPassVisible.setManaged(true);
            TxtPass.setVisible(false);
            TxtPass.setManaged(false);
        } else {
            // mask (UI level)
            TxtPass.setText(TxtPassVisible.getText());
            TxtPass.setVisible(true);
            TxtPass.setManaged(true);
            TxtPassVisible.setVisible(false);
            TxtPassVisible.setManaged(false);
        }
    }

    public Map<String, Integer> getWordsFromFieldOfInterest() {
        final int DEFAULT_PRIORITY = 1;

        String inputText = fieldOfInterestInput.getText();
        Map<String, Integer> wordMap = new HashMap<>();

        String[] words = inputText.split("[ ,]+"); //regex splits on spaces and commas

        for (String word : words) {
            word = word.trim();
            if (!word.isEmpty()) {
                wordMap.put(word, DEFAULT_PRIORITY);
            }
        }
        return wordMap;
    }

    @FXML
    private void chooseProfileImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        File selectedFile = fileChooser.showOpenDialog(btChooseImage.getScene().getWindow());
        if (selectedFile != null) {
            imagePath = selectedFile.getAbsolutePath();
            Image image = new Image(selectedFile.toURI().toString());
            profileImageView.setImage(image);
        }
    }
    public String getPassword() {
        if (TxtPassVisible.isVisible()) {return TxtPassVisible.getText();}
        else {return TxtPass.getText();}
    }

    public String getProfileImagePath() {
        return imagePath;
    }
}
