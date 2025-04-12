package com.example.solvesphere;

import com.example.solvesphere.DataBaseUnit.GlobalPollDAO;
import com.example.solvesphere.DataBaseUnit.GlobalPollDAOImpl;
import com.example.solvesphere.PollsUnit.GlobalPoll;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class CreatePollController {

    @FXML private TextField questionField;
    @FXML private TextField optionYesField;
    @FXML private TextField optionNoField;

    private final GlobalPollDAO pollDAO = new GlobalPollDAOImpl();

    @FXML
    private void submitPoll() {
        String question = questionField.getText().trim();
        String optionYes = optionYesField.getText().trim();
        String optionNo = optionNoField.getText().trim();

        if (question.isEmpty() || optionYes.isEmpty() || optionNo.isEmpty()) {
            showAlert("All fields must be filled.");
            return;
        }

        GlobalPoll newPoll = new GlobalPoll(question, optionYes, optionNo);
        boolean success = pollDAO.createPoll(newPoll);

        if (success) {
            closeWindow();
        } else {
            showAlert("Failed to create poll.");
        }
    }

    @FXML
    private void cancelPoll() {
        closeWindow();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Poll Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) questionField.getScene().getWindow();
        stage.close();
    }
}
