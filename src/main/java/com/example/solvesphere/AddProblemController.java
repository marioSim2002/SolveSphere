package com.example.solvesphere;

import com.example.solvesphere.DataBaseUnit.*;
import com.example.solvesphere.UserData.Problem;
import com.example.solvesphere.UserData.SessionManager;
import com.example.solvesphere.UserData.User;
import com.example.solvesphere.ValidationsUnit.ValidateInputData;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.Node;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class AddProblemController {

    private User currentUser;
    @FXML private TextField titleField;
    @FXML private TextArea descriptionField;
    @FXML private TextField categoryField;
    @FXML private TextField tagsField;
    @FXML private CheckBox ageRestrictionCheckbox;
    @FXML private VBox similarProblemsListView; // will hold ProblemItem components

    private final ProblemDAO problemDAO = new ProblemDAOImpl();

    @FXML
    public void initialize(User currentUser) {
        this.currentUser = currentUser;
        //attach listener to titleField to fetch similar problems dynamically
        titleField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                searchSimilarProblems(newValue, descriptionField.getText());
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        descriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                searchSimilarProblems(titleField.getText(), newValue);
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Fetch and display similar problems dynamically based on title and description.
     */
    private void searchSimilarProblems(String titleInput, String descInput) throws SQLException, ClassNotFoundException {
        titleInput = titleInput.trim();
        descInput = descInput.trim();

        //at least one meaningful input exists
        if (titleInput.isEmpty() && descInput.isEmpty()) {
            similarProblemsListView.getChildren().clear();
            return;
        }

        List<Problem> similarProblems = problemDAO.findSimilarProblemsByTitleAndDescription(titleInput, descInput);

        Platform.runLater(() -> {
            similarProblemsListView.getChildren().clear();
            for (Problem problem : similarProblems) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("ProblemItem.fxml"));
                    Node problemItem = loader.load();
                    ProblemItemController controller = loader.getController();
                    controller.setProblemData(problem, currentUser, getProblemCommentCount(problem), getProblemPublisherName(problem));
                    similarProblemsListView.getChildren().add(problemItem);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public int getProblemCommentCount(Problem problem) {
        CommentDAO commentDAO = new CommentDAOImpl();
        return commentDAO.getCommentCountByProblemId(problem.getId());
    }

    public String getProblemPublisherName(Problem problem) {
        UserDAO userDAO = new UserDAOImpl();
        User user = userDAO.getUserById(problem.getUserId());
        return user.getUsername();
    }

    @FXML
    private void submitProblem() {
        String title = titleField.getText();
        String description = descriptionField.getText();
        String category = categoryField.getText();
        String tagsText = tagsField.getText();

        // Validate input
        String[] inputData = {title, description, category, tagsText};
        if (!ValidateInputData.validTxtData(inputData)) {
            AlertsUnit.showInvalidDataAlert();
            return;
        }

        boolean isAgeRestricted = ageRestrictionCheckbox.isSelected();
        List<String> tags = List.of(tagsText.split("\\s*,\\s*"));

        long currentUserId = SessionManager.getCurrentUser().getId();
        Problem problem = new Problem(0, title, description, currentUserId, LocalDateTime.now(), category, isAgeRestricted);
        boolean isSuccess = problemDAO.addProblem(problem);

        if (isSuccess) {
            AlertsUnit.successAddAlert();
            clearFields();
        } else {
            AlertsUnit.showErrorAlert("An error occurred.");
        }
    }

    private void clearFields() {
        titleField.clear();
        descriptionField.clear();
        categoryField.clear();
        tagsField.clear();
        ageRestrictionCheckbox.setSelected(false);
        similarProblemsListView.getChildren().clear();
    }
}
