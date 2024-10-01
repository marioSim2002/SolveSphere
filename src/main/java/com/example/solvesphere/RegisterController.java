package com.example.solvesphere.Controllers;

import com.example.solvesphere.AlertsUnit;
import com.example.solvesphere.ServerCommunicator;
import com.example.solvesphere.User;
import com.example.solvesphere.ValidationsUnit.ValidateInputData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.ArrayList;

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
    private ServerCommunicator serverCommunicator;

    public RegisterController() {
        serverCommunicator = new ServerCommunicator("localhost", 12345);  // Initialize server communicator
    }
    @FXML
    public void closeCurrentStage(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


    @FXML
    public void registerUser() {
        // Collect user data
        String username = TxtUsername.getText();
        String email = TxtMail.getText();
        String password = TxtPass.getText();
        LocalDate dateOfBirth = DateOfBirthVal.getValue();
        String country = CountryInput.getValue();
        String fieldOfInterest = fieldOfInterestInput.getText();
        String dateOfBirthString = (dateOfBirth != null) ? dateOfBirth.toString() : "";

        // Validate data before sending to the server
        String[] userDataArr = {username, email, password, dateOfBirthString, country, fieldOfInterest};
        if (!ValidateInputData.validData(userDataArr) || !ValidateInputData.validEmail(email)) {
            AlertsUnit.showInvalidDataAlert();
            return;
        }

        // Create a User object
        User newUser = new User(username, email, password, dateOfBirthString, country, fieldOfInterest);

        // Send the registration request using ServerCommunicator
        String response = serverCommunicator.sendRegistrationRequest(newUser);
        System.out.println("Server response: " + response);
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
}