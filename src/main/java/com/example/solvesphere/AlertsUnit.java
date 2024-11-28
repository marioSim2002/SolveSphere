package com.example.solvesphere;

import javafx.application.Platform;
import javafx.scene.control.Alert;
//example
public abstract class AlertsUnit {
    private static void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(headerText);
            alert.setContentText(contentText);
            alert.setResizable(false);
            alert.showAndWait();
        });
    }

    private static void showAlert(Alert.AlertType alertType, String title, String contentText) {
        showAlert(alertType, title, null, contentText);
    }

    // Specific alerts using the generic method
    public static void showInvalidDataAlert() {
        showAlert(Alert.AlertType.INFORMATION, "Invalid Data!", "At least one of your info isn't correct.");
    }

    public static void successAddAlert() {
        showAlert(Alert.AlertType.INFORMATION, "Success", "Your problem has been posted\nothers can interact with it and suggest solutions.");
    }


    public static void userAlreadyRegistered() {
        showAlert(Alert.AlertType.WARNING, "Registration Warning", "User already exists", "The username or email you entered is already registered.");
    }

    public static void showSuccessRegistrationAlert() {
        showAlert(Alert.AlertType.INFORMATION, "Success", "Registered Successfully!");
    }

    public static void showSuccessLogInAlert() {
        showAlert(Alert.AlertType.INFORMATION, "Success", "User logged in successfully!");
    }

    public static void showErrorAlert(String response) {
        showAlert(Alert.AlertType.ERROR, "Error Occurred", "Error connecting user!\n" + response);
    }

    public static void userNotRegisteredAlert() {
        showAlert(Alert.AlertType.WARNING, "User not registered", "Please register before attempting login!");
    }

    public static void userUnderAgeAlert() {
        showAlert(Alert.AlertType.WARNING, "Access Denied: Age-Restricted Content", "This content has age restrictions and is not available for viewing.");
    }
}
