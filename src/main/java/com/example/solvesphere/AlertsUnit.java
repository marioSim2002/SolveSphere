package com.example.solvesphere;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public abstract class AlertsUnit {

    public static void showInvalidDataAlert(){
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Invalid Data!");
        a.setResizable(false);
        a.setContentText("At least one of your info aren't correct.");
        a.show();
    }

    public static void userAlreadyRegistered(){
            Alert a = new Alert(Alert.AlertType.WARNING);
            a.setTitle("Registration Warning");
            a.setHeaderText("User already exists");
            a.setContentText("The username or email you entered is already registered.");
            a.showAndWait();

    }

    public static void showSuccessAlert(){
        Alert a  = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Success");
        a.setContentText("Registered Successfully!");
        a.show();
    }

    public static void showErrorAlert(String response) {

        Alert a  = new Alert(Alert.AlertType.ERROR);
        a.setTitle("Error Occurred");
        a.setContentText("Error connecting user!/n"+response);
        a.show();
    }
}
