package com.example.solvesphere;

import com.example.solvesphere.UserData.Problem;
import com.example.solvesphere.UserData.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ProblemDetailsController {
    @FXML
    private Label problemTitle;
    @FXML
    private TextArea problemDescription;
    @FXML
    private TextField solutionTitleField;
    @FXML
    private TextArea solutionDescriptionField;
    Problem currentProblem; // current post
    User currentUser ; // current user

    public void initData(Problem passedProblem, User currentUser) {
        this.currentProblem = passedProblem;
        this.currentUser = currentUser;
        showData();
    }

    private void showData(){
        try {
            problemTitle.setText(currentProblem.getTitle());
            problemDescription.setText(currentProblem.getDescription());
        }
      catch (NullPointerException exception){
          System.out.println("data trying to access may be null.");
      }
    }
   public void  postComment(ActionEvent actionEvent) {
        try {
            String solutionTitle = solutionTitleField.getText();
            String solutionDescription = solutionDescriptionField.getText();

            // Checking that the fields are not empty
            if (solutionTitle.isEmpty() || solutionDescription.isEmpty()) {
                AlertsUnit.showInvalidDataAlert();
                return;
            }

            // Create a new Solution object
            Solution newSolution = new Solution(
                    0,
                    currentProblem.getId(),
                    currentUser.getId(),
                    solutionTitle,
                    solutionDescription,
                    java.time.LocalDateTime.now(),
                    0 // Default rating (0)
            );

            SolutionDAO solutionDAO = new SolutionDAOImpl();
            solutionDAO.addSolution(newSolution);

            AlertsUnit.successAddSolution();

            // Clearing the fields after adding
            solutionTitleField.clear();
            solutionDescriptionField.clear();
        } catch (Exception e) {
            System.out.println("Error adding solution: " + e.getMessage());
        }
    }
}
