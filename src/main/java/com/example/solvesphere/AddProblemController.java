package com.example.solvesphere;

import com.example.solvesphere.DataBaseUnit.ProblemDAO;
import com.example.solvesphere.DataBaseUnit.ProblemDAOImpl;
import com.example.solvesphere.DataBaseUnit.UserDAO;
import com.example.solvesphere.DataBaseUnit.UserDAOImpl;
import com.example.solvesphere.ServerUnit.ServerCommunicator;
import com.example.solvesphere.UserData.Problem;
import com.example.solvesphere.UserData.User;
import com.example.solvesphere.ValidationsUnit.ValidateInputData;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class AddProblemController {

    @FXML
    public CheckBox ageRestrictionCheckbox;
    @FXML
    private TextField titleField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField categoryField;
    @FXML
    private TextField tagsField;
    private User currentUser;


    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @FXML
    private void submitProblem() {
        String title = titleField.getText();
        String description = descriptionField.getText();
        String category = categoryField.getText();
        String tagsText = tagsField.getText();

        String[] inputData = {title, description, category, tagsText};
        if (!ValidateInputData.validTxtData(inputData)) {
            AlertsUnit.showInvalidDataAlert();
            return;
        }

        List<String> tags = Arrays.asList(tagsText.split("\\s*,\\s*"));
        boolean isAgeRestricted = ageRestrictionCheckbox.isSelected();  //age restriction status

        ServerCommunicator communicator = ServerCommunicator.getInstance();
        Long userId = communicator.fetchUserIdByUsernameAndEmail(currentUser.getUsername(), currentUser.getEmail());
        if (userId != null) {
            System.out.println("Fetched User ID: " + userId);
        } else {
            System.out.println("User not found.");
            return;
        }
        System.out.println(isAgeRestricted);
        Problem problem = new Problem(0, title, description, userId, LocalDateTime.now(), category, isAgeRestricted, tags);
        ProblemDAO problemDAO = new ProblemDAOImpl();
        boolean isSuccess = problemDAO.addProblem(problem);
        // TODO , refresh problems list
        if (isSuccess) {
            AlertsUnit.successAddAlert();
            clearFields();
        } else {
            AlertsUnit.showErrorAlert("error occurred.");
        }
    }

    private void clearFields() {
        titleField.clear();
        descriptionField.clear();
        categoryField.clear();
        tagsField.clear();
        ageRestrictionCheckbox.setSelected(false);
    }
}

