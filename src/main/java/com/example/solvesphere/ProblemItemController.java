package com.example.solvesphere;
import com.example.solvesphere.DataBaseUnit.ProblemDAO;
import com.example.solvesphere.DataBaseUnit.ProblemDAOImpl;
import com.example.solvesphere.DataBaseUnit.UserDAO;
import com.example.solvesphere.DataBaseUnit.UserDAOImpl;
import com.example.solvesphere.UserData.Problem;
import com.example.solvesphere.UserData.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
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
            AlertsUnit.userUnderAgeAlert();
            return;
        }
        try {
            // Load the FXML for the new screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProblemDetails.fxml"));
            AnchorPane problemDetailsRoot = loader.load();

            ProblemDetailsController controller = loader.getController();
            controller.initData(passedProblem, currentUser);

            //create a NEW stage for the new screen
            Stage newStage = new Stage();
            newStage.setTitle("Problem Details");
            newStage.setScene(new Scene(problemDetailsRoot));
            newStage.show();

            // Do NOT close or overwrite the main dash:
            // No calls to 'stage.close()' or 'stage.setScene(...)' on the main dash
            // So the main dash remains open.


        }  catch (IOException e) {throw new RuntimeException(e);}
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
