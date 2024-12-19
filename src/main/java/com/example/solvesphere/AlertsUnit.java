package com.example.solvesphere;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

public abstract class AlertsUnit {
    private static void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            FontIcon ico_exclamation = new FontIcon("fas-exclamation-triangle");
            ico_exclamation.setIconColor(Color.RED);
            ico_exclamation.setIconSize(20);
            alert.setGraphic(ico_exclamation);
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
        showAlert(Alert.AlertType.INFORMATION, "Invalid Data!", "At least one of your information is incorrect or empty.");
    }

    public static void successAddAlert() {
        showAlert(Alert.AlertType.INFORMATION, "Success", "Your problem has been posted\nothers can interact with it and suggest solutions.");
    }
     public static void successAddSolution() {
        showAlert(Alert.AlertType.INFORMATION, "Success", "Your solution has been successfully added.");
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
