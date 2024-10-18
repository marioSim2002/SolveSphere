package com.example.solvesphere;

import com.example.solvesphere.AlertsUnit;
import com.example.solvesphere.DataBaseUnit.UserDAO;
import com.example.solvesphere.DataBaseUnit.UserDAOImpl;
import com.example.solvesphere.ServerCommunicator;
import com.example.solvesphere.ValidationsUnit.ValidateInputData;
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
        String username = userNameFld.getText();
        String password = userPassFld.getText();
        System.out.println(username+" " + password);
        UserDAO userDAO = new UserDAOImpl();
        if (!userDAO.userExists(username,password)){
            AlertsUnit.userNotRegisteredAlert();
            return;
        }
        String response = serverCommunicator.sendLoginRequest(username, password);
        System.out.println("communicator response "+ response); /// debug
        responseStatus(response);

        /// todo
        // modify user alert to match user acc status (error -> wrong data)
        }


    public void responseStatus(String response) {
        if (response.contains("Login successful")) {
            AlertsUnit.showSuccessLogInAlert();
            //  transitionToUserDashboard(); // Placeholder method for navigation to dashboard
        } else if (response.contains("Wrong password")) {
            AlertsUnit.showErrorAlert("Incorrect password. Please try again.");
        } else {
            AlertsUnit.showErrorAlert("Unknown error occurred. Please try again.");
        }
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