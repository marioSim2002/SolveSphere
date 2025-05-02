package com.example.solvesphere;

import com.example.solvesphere.DataBaseUnit.GlobalPollDAO;
import com.example.solvesphere.DataBaseUnit.GlobalPollDAOImpl;
import com.example.solvesphere.PollsUnit.GlobalPoll;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
public class CreatePollController {
    private GlobalPollsController pollsController;
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
            AlertsUnit.showInvalidDataAlert();
            return;
        }

        GlobalPoll newPoll = new GlobalPoll(question, optionYes, optionNo);
        boolean success = pollDAO.createPoll(newPoll);

        if (success) {
            if (pollsController != null) {
                pollsController.loadAllPolls();
            }
            closeWindow();
        }else {AlertsUnit.showErrorAlert("Error.");}
    }
    public void setPollsController(GlobalPollsController controller) {
        this.pollsController = controller;
    }
    @FXML
    private void cancelPoll() {
        closeWindow();
    }
    private void closeWindow() {
        Stage stage = (Stage) questionField.getScene().getWindow();
        stage.close();
    }
}
