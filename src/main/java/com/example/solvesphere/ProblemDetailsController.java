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
    public void postComment(ActionEvent actionEvent) {
    }


}
