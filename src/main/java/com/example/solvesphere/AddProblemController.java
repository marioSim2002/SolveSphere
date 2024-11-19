package com.example.solvesphere;

import com.example.solvesphere.DataBaseUnit.ProblemDAO;
import com.example.solvesphere.DataBaseUnit.ProblemDAOImpl;
import com.example.solvesphere.UserData.Problem;
import com.example.solvesphere.UserData.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class AddProblemController {

    @FXML
    private TextField titleField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField categoryField;
    @FXML
    private TextField tagsField;

    private User currentUser;  // מחזיק את כל פרטי המשתמש

    // מתודה לאתחול פרטי המשתמש
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @FXML
    private void submitProblem() {
        String title = titleField.getText();
        String description = descriptionField.getText();
        String category = categoryField.getText();
        String tagsText = tagsField.getText();

        // בדיקה שכל השדות מלאים
        if (title.isEmpty() || description.isEmpty() || category.isEmpty() || tagsText.isEmpty()) {
            showAlert("All fields are required!", AlertType.WARNING);
            return;
        }

        // פיצול התגים לפי פסיקים והסרת רווחים מיותרים
        List<String> tags = Arrays.asList(tagsText.split("\\s*,\\s*"));

        // יצירת אובייקט Problem חדש
        Problem problem = new Problem(0, title, description, currentUser.getId(), LocalDateTime.now(), category, tags);

        // הוספת הבעיה למסד הנתונים
        ProblemDAO problemDAO = new ProblemDAOImpl();
        boolean isSuccess = problemDAO.addProblem(problem);

        // הודעה על הצלחה או כישלון
        if (isSuccess) {
            showAlert("Problem added successfully!", AlertType.INFORMATION);
            clearFields();
        } else {
            showAlert("Failed to add problem. Please try again.", AlertType.ERROR);
        }
    }

    private void clearFields() {
        titleField.clear();
        descriptionField.clear();
        categoryField.clear();
        tagsField.clear();
    }

    private void showAlert(String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

