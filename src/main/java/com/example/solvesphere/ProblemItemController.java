package com.example.solvesphere;
import com.example.solvesphere.DataBaseUnit.ProblemDAO;
import com.example.solvesphere.DataBaseUnit.ProblemDAOImpl;
import com.example.solvesphere.DataBaseUnit.UserDAO;
import com.example.solvesphere.DataBaseUnit.UserDAOImpl;
import com.example.solvesphere.UserData.Problem;
import com.example.solvesphere.UserData.User;
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
    private Problem passedProblem;
    private User currentUser; //the current connected use e.g signed in

    public void setProblemData(Problem problem,String publisher,User passedUser) {
        this.problemTitle.setText(problem.getTitle());
        this.postedBy.setText(publisher);
        this.postDate.setText(formatDateTime(problem.getCreatedAt()));
        this.passedProblem = problem;
        this.currentUser = passedUser;

    }

    @FXML
    private void onDetailsClick() {
        if(!validateUserAgeForContentAccess(passedProblem, currentUser.calculateAge())){
            System.out.println("im here");
            AlertsUnit.userUnderAgeAlert();
            return;
        }
        //todo ,
        // show problem details (open problem)
    }


    //utility method to format date-time in ui-friendly manner
    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm");
        return dateTime.format(formatter);
    }

    public boolean validateUserAgeForContentAccess(Problem clickedProblem,int currentUserAge){
        return !clickedProblem.isAgeRestricted() || currentUserAge >= 18;
    }
}
