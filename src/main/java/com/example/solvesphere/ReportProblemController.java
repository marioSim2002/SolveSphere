package com.example.solvesphere;

import com.example.solvesphere.DataBaseUnit.ReportDAO;
import com.example.solvesphere.DataBaseUnit.ReportDAOImpl;
import com.example.solvesphere.DataBaseUnit.Report;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class ReportProblemController {

    @FXML private RadioButton spamRadio;
    @FXML private RadioButton hateRadio;
    @FXML private RadioButton violenceRadio;
    @FXML private RadioButton otherRadio;
    @FXML private TextArea otherReasonField;
    @FXML private Button submitButton;

    private long problemId;
    private long reporterId;

    public void initialize(long problemId, long reporterId) {
        this.problemId = problemId;
        this.reporterId = reporterId;

        ToggleGroup toggleGroup = new ToggleGroup();
        spamRadio.setToggleGroup(toggleGroup);
        hateRadio.setToggleGroup(toggleGroup);
        violenceRadio.setToggleGroup(toggleGroup);
        otherRadio.setToggleGroup(toggleGroup);

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            submitButton.setDisable(newValue == null);
            otherReasonField.setDisable(!otherRadio.isSelected());
        });
    }

    @FXML
    private void onSubmitReport() {
        String reportReason = getSelectedReason();
        if (reportReason == null || reportReason.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Report");
            alert.setHeaderText("You must select a reason for reporting.");
            alert.showAndWait();
            return;
        }

        ReportDAO reportDAO = new ReportDAOImpl();
        Report report = new Report(0, problemId, reporterId, reportReason, LocalDateTime.now());

        try {
            if (reportDAO.addReport(report)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Report Submitted");
                alert.setHeaderText("Thank you for your feedback.");
                alert.showAndWait();

                closeWindow();
            } else {
                showError("Failed to submit the report.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error submitting the report.");
        }
    }

    private String getSelectedReason() {
        if (spamRadio.isSelected()) return "Spam or misleading";
        if (hateRadio.isSelected()) return "Hate speech or harassment";
        if (violenceRadio.isSelected()) return "Violence or dangerous content";
        if (otherRadio.isSelected()) return otherReasonField.getText().trim();
        return null;
    }

    @FXML
    private void onCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}
