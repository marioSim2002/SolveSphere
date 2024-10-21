package com.example.solvesphere;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProblemItemController {
    @FXML
    private Text postedBy;
    @FXML
    private Text postDate;
    @FXML private Label problemTitle;
    @FXML private Label problemDescription;

    public void setProblemData(String title, String description, String publisher, LocalDateTime date) {
        this.problemTitle.setText(title);
        this.problemDescription.setText(description);
        this.postedBy.setText(publisher);
        this.postDate.setText(formatDateTime(date));
    }

    @FXML
    private void onDetailsClick() {
        // logic to show details
    }


    //utility method to format date-time in ui-friendly manner
    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm");
        return dateTime.format(formatter);
    }
}
