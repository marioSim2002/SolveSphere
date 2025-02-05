package com.example.solvesphere;
import com.example.solvesphere.DataBaseUnit.*;
import com.example.solvesphere.UserData.Problem;
import com.example.solvesphere.UserData.User;
import javafx.event.ActionEvent;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ProblemItemController {
    private ScheduledExecutorService scheduler;

    @FXML
    private Label commentCountTxt;
    @FXML
    private Text postedBy;
    @FXML
    private Text postDate;
    @FXML private Label problemTitle;
    private Problem passedProblem;

    private String owner ;
    private User currentUser; //the current connected use e.g signed in

    public void setProblemData(Problem problem,User passedUser,int commentsCnt,String publisherName) {
        this.problemTitle.setText(problem.getTitle());
        this.postedBy.setText(publisherName);
        this.postDate.setText(formatDateTime(problem.getCreatedAt()));
        this.passedProblem = problem;
        this.currentUser = passedUser;
        this.owner = publisherName;
        this.commentCountTxt.setText(String.valueOf(commentsCnt));

    }

    private void initProblemCommentsCount(){
        CommentDAO commentDAO = new CommentDAOImpl(); //////// throws  .SQLNonTransientConnectionException error
            int count = commentDAO.getCommentCountByProblemId(passedProblem.getId());
            commentCountTxt.setText(String.valueOf(count));
    }

    @FXML
    private void onDetailsClick() {
        if(!validateUserAgeForContentAccess(passedProblem, currentUser.calculateAge())){
            AlertsUnit.userUnderAgeAlert();
            return;
        }
        try {
            //load the FXML for the new screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProblemDetails.fxml"));
            AnchorPane problemDetailsRoot = loader.load();

            ProblemDetailsController controller = loader.getController();
            controller.initData(passedProblem, currentUser,owner);

            //create a NEW stage for the new screen
            Stage newStage = new Stage();
            newStage.setTitle("Problem Details");
            newStage.setScene(new Scene(problemDetailsRoot));
            newStage.show();

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

    public void initialize() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduleCommentCountRefresh();
    }

    private void scheduleCommentCountRefresh() {
        scheduler.scheduleAtFixedRate(() -> {
            if (passedProblem != null) {
                javafx.application.Platform.runLater(this::initProblemCommentsCount);
            }
        }, 0, 8, TimeUnit.SECONDS);
    }

    public void shutdownScheduler() {
        if (scheduler != null) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(8, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
            }
        }
    }
    @FXML
    private void onClose() {
        shutdownScheduler();
    }
}
