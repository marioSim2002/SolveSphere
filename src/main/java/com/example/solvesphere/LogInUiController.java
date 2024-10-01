package com.example.solvesphere.Controllers;

import com.example.solvesphere.AlertsUnit;
import com.example.solvesphere.ServerCommunicator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LogInUiController {
    @FXML
    private Label welcomeText;
    @FXML
    private TextField userNameFld;
    @FXML
    private PasswordField userPassFld;
    @FXML
    private TextField userPassTextFld;
    @FXML
    private CheckBox showPasswordCheck;
    @FXML
    private Hyperlink registerLink;
    @FXML
    private Hyperlink forgotDataLink;
    private final ServerCommunicator serverCommunicator;

    public LogInUiController() {
        serverCommunicator = new ServerCommunicator("localhost", 12345);
    }

    @FXML
    protected void onLogInButtonClick() {
        if (userNameFld.getText().isEmpty() || userPassFld.getText().isEmpty()) {
            AlertsUnit.showInvalidDataAlert();
            return;
        }

        // Gather login data
        String username = userNameFld.getText();
        String password = userPassFld.getText();

        // Send login request to the server
        String response = serverCommunicator.sendLoginRequest(username, password);
        System.out.println(response);
    }
    @FXML
     protected void onRegisterClick(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Register.fxml"));
            Parent root = fxmlLoader.load();
            // new stage
            Stage registerStage = new Stage();
            registerStage.setTitle("Register");
            registerStage.setScene(new Scene(root));
            registerStage.show(); //show

        } catch (IOException e) {e.printStackTrace();}
    }

    @FXML
    protected void togglePassVisibility() {
        if (showPasswordCheck.isSelected()) {
            // show the plain text field and hide the PasswordField
            userPassTextFld.setText(userPassFld.getText());
            userPassTextFld.setVisible(true);
            userPassTextFld.setManaged(true);
            userPassFld.setVisible(false);
            userPassFld.setManaged(false);
        } else {
            // show the PasswordField and hide the plain text field
            userPassFld.setText(userPassTextFld.getText());
            userPassFld.setVisible(true);
            userPassFld.setManaged(true);
            userPassTextFld.setVisible(false);
            userPassTextFld.setManaged(false);
        }
    }
    @FXML
    protected void onForgotCradentialsClick(){

        ///todo if the user forgot password
    }
}