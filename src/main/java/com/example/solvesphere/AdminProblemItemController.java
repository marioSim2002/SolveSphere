package com.example.solvesphere;
import com.example.solvesphere.DataBaseUnit.CommentDAO;
import com.example.solvesphere.UserData.AdminProblem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdminProblemItemController {

    @FXML private Label problemTitle;
    @FXML private Text postedBy;
    @FXML private Text postDate;
    @FXML private Label commentCountTxt;
    @FXML private Button viewDetailsButton;

    private AdminProblem passedProblem;

    public void setProblemData(AdminProblem problem, String adminName) {
        this.passedProblem = problem;
        this.problemTitle.setText(problem.getTitle());
        this.postedBy.setText("Posted by: " + adminName);
        this.postDate.setText(formatDateTime(problem.getCreatedAt()));

//        CommentDAO commentDAO = new CommentDAOImpl();
//        int commentCount = commentDAO.getCommentCountByProblemId(problem.getId());
//        this.commentCountTxt.setText(String.valueOf(commentCount));
    }

    @FXML
    private void onDetailsClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/solvesphere/AdminProblemDetails.fxml"));
            Parent root = loader.load();

            AdminProblemDetailsController controller = loader.getController();
            controller.setAdminProblem(passedProblem);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Problem Details");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm");
        return dateTime.format(formatter);
    }
}
