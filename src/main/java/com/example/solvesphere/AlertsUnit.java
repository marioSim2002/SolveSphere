package com.example.solvesphere;

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
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Account already Registered");
        a.setContentText("Account already Registered, go back to log-in");
        a.show();
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
