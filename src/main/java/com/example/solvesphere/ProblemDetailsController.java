package com.example.solvesphere;

import com.example.solvesphere.DataBaseUnit.CommentDAO;
import com.example.solvesphere.DataBaseUnit.CommentDAOImpl;
import com.example.solvesphere.DataBaseUnit.UserDAO;
import com.example.solvesphere.DataBaseUnit.UserDAOImpl;
import com.example.solvesphere.UserData.Comment;
import com.example.solvesphere.UserData.Problem;
import com.example.solvesphere.UserData.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class ProblemDetailsController {

    @FXML
    private Label problemTitle;

    @FXML
    private TextArea problemDescription;

    @FXML
    private TextField commentField; // Input field for new comments

    @FXML
    private VBox commentListContainer; ///// here ////

    private CommentDAO commentDAO;

    private Problem currentProblem; // Current post
    private User currentUser; // Current user

    public void initData(Problem passedProblem, User currentUser) {
        this.currentProblem = passedProblem;
        this.currentUser = currentUser;
        this.commentDAO = new CommentDAOImpl();
        showData();
        loadComments();
    }

    private void showData() {
        try {
            problemTitle.setText(currentProblem.getTitle());
            problemDescription.setText(currentProblem.getDescription());
        } catch (NullPointerException exception) {
            System.out.println("Data trying to access may be null.");
        }
    }

    private void loadComments() {
        //commentListContainer.getChildren().clear(); // Clear existing comments to reload
        List<Comment> comments = commentDAO.getCommentsByProblemId(currentProblem.getId());
        UserDAO user = new UserDAOImpl();
        for (Comment comment : comments) {
            try {
                // Load the FXML file for a single comment item
                FXMLLoader loader = new FXMLLoader(getClass().getResource("commentItem.fxml"));
                VBox commentItem = loader.load();

                // Get the controller and set the comment data
                CommentItemController controller = loader.getController();
                controller.setCommentData(comment,user.getUserById(comment.getUserId()).getUsername());

                // Add the comment item to the container
                commentListContainer.getChildren().add(commentItem);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void postComment(ActionEvent actionEvent) {
        String content = commentField.getText().trim();

        if (!content.isEmpty()) {
            Comment newComment = new Comment();
            newComment.setProblemId(currentProblem.getId());
            newComment.setUserId(currentUser.getId());
            newComment.setContent(content);

            commentDAO.addComment(newComment); // Add comment to the database via DAO method
            commentField.clear(); // Clear the input field
            loadComments(); // Refresh comments
        }
    }
}