package com.example.solvesphere;

import com.example.solvesphere.UserData.AdminProblem;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class AdminProblemDetailsController {
    @FXML private Label problemTitleLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label categoryLabel;
    @FXML private Label createdAtLabel;
    @FXML private Label isAgeRestrictedLabel;
    @FXML private Button closeButton;
    private AdminProblem adminProblem;

    public void setAdminProblem(AdminProblem adminProblem) {
        this.adminProblem = adminProblem;

        problemTitleLabel.setText(adminProblem.getTitle());
        descriptionLabel.setText("Description: " + adminProblem.getDescription());
        categoryLabel.setText("Category: " + adminProblem.getCategory());
        createdAtLabel.setText("Created At: " + formatDate(Timestamp.valueOf(adminProblem.getCreatedAt())));
        isAgeRestrictedLabel.setText("Age Restricted: " + (adminProblem.isAgeRestricted() ? "Yes" : "No"));
    }

    private String formatDate(java.sql.Timestamp timestamp) {
        return timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"));
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
