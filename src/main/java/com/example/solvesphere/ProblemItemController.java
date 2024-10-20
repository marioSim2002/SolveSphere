package com.example.solvesphere;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProblemItemController {
    @FXML private Label problemTitle;
    @FXML private Label problemDescription;

    public void setProblemData(String title, String description) {
        this.problemTitle.setText(title);
        this.problemDescription.setText(description);
    }

    @FXML
    private void onDetailsClick() {
        // logic to show details
    }
}
