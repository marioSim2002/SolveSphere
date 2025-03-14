package com.example.solvesphere;

import com.example.solvesphere.DataBaseUnit.UserDAO;
import com.example.solvesphere.DataBaseUnit.UserDAOImpl;
import com.example.solvesphere.ServerUnit.ServerCommunicator;
import com.example.solvesphere.UserData.SessionManager;
import com.example.solvesphere.UserData.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;

public class LogInUiController {
    @FXML
    public Button logInBt;
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
        serverCommunicator = new ServerCommunicator();
    }

    @FXML
    protected void onLogInButtonClick() throws SQLException {
        if (userNameFld.getText().isEmpty() || getPasswordTxt().isEmpty()) {
            AlertsUnit.showInvalidDataAlert();
            return;
        }
        String username = userNameFld.getText();
        String password = getPasswordTxt();
        if(isBanned(username,password)){
            AlertsUnit.showErrorAlert("User Banned , Contact Support");
            return;
        }
        Object response = serverCommunicator.sendLoginRequest(username, password);
        UserDAO userDAO = new UserDAOImpl();
        // handle the response based on its type
        if (response instanceof User user) {
            String status = userDAO.getUserActivityStatus(extractUserID(user));
            System.out.println("STATUS  "+ status);

            if ("BANNED".equalsIgnoreCase(status)) {
                AlertsUnit.showErrorAlert("Your account has been banned. Please contact support.");
                return;
            }
                SessionManager.setCurrentUser(user);
                transitionToUserDashboard(user);

        } else if (response instanceof String message) {
            responseStatus(message);
        } else {
            AlertsUnit.showErrorAlert("Unexpected response type received.");
        }
    }


    public boolean isBanned(String username, String pass) throws SQLException {
        UserDAO userDAO = new UserDAOImpl();

        long attempterID = extractUserIdByCred(username,pass);
        return userDAO.getUserActivityStatus(attempterID).equalsIgnoreCase("BANNED");
    }


    private void transitionToUserDashboard(User user) {
        try {
            // close the current window
            Stage currentStage = (Stage) logInBt.getScene().getWindow();
            currentStage.close();

            // Load the dashboard
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainDashboard.fxml"));
            Parent root = loader.load();

            Stage dashboardStage = new Stage();
            dashboardStage.setTitle("User Dashboard");
            dashboardStage.setScene(new Scene(root));

            MainDashController controller = loader.getController();
            controller.initUserData(user);
            System.out.println("logging in .," +user.getId());
           // dashboardStage.initStyle(StageStyle.UNDECORATED);
            dashboardStage.setFullScreen(true);
            dashboardStage.show();
            UserDAO userDAO = new UserDAOImpl();
            userDAO.setUserActivityStatus(extractUserID(user),"ACTIVE");
            user.setActive(true);
        } catch (IOException e) {
            e.printStackTrace();
            AlertsUnit.showErrorAlert(e.toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        }

    public void responseStatus(String response) {
        if (response.contains("successful")) {
            AlertsUnit.showSuccessLogInAlert();
        } else if (response.contains("Invalid")) {
            AlertsUnit.showErrorAlert("Incorrect username or password.\nPlease try again.");
        } else {
            System.out.println("response on login click "+response);
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

    public String getPasswordTxt() {
        if (userPassTextFld.isVisible()) {return userPassTextFld.getText();}
        else {return userPassFld.getText();}
    }
    @FXML
    protected void onForgotCradentialsClick(){
        ///todo if the user forgot password
    }

    protected long extractUserID(User user){
        return serverCommunicator.fetchUserIdByUsernameAndEmail(user.getUsername(), user.getEmail());
    }


    protected long extractUserIdByCred(String username ,String pass){

        Long userIdObj = serverCommunicator.fetchUserIdByUsernameAndPassword(username, pass);
        if (userIdObj == null) {
            return 0;
        }
        return userIdObj;
    }
}