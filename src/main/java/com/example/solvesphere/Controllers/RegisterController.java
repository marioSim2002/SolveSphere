package com.example.solvesphere;

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


    @FXML
    public void closeCurrentStage(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void registerUser() {

        // todo - implement method to check if user already found (already registered/in database)
        String username = TxtUsername.getText();
        String email = TxtMail.getText();
        String password = TxtPass.getText();
        LocalDate dateOfBirth = DateOfBirthVal.getValue();
        String country = CountryInput.getValue();
        String fieldOfInterest = fieldOfInterestInput.getText();
        String dateOfBirthString = (dateOfBirth != null) ? dateOfBirth.toString() : "";
        String[] userDataArr = {username, email, password, dateOfBirthString, country, fieldOfInterest};

        if (!ValidateInputData.validData(userDataArr) || !ValidateInputData.validEmail(email)) {
            AlertsUnit.showInvalidDataAlert();
            return ;
        }

        // send registration data to the server and read the response
        try (Socket socket = new Socket("localhost", 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            //todo , create user class
            // send registration details to the server
            out.println(username);
            out.println(email);
            out.println(password);
            // read the server's response
            String response = in.readLine();
            System.out.println("Server response: " + response);

            // optionally, display the server's response in the UI (e.g., a label)

        } catch (IOException e) {
            e.printStackTrace();
        }
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