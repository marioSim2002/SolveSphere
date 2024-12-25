package com.example.solvesphere;

import com.example.solvesphere.DataBaseUnit.CommentDAO;
import com.example.solvesphere.DataBaseUnit.CommentDAOImpl;
import com.example.solvesphere.DataBaseUnit.UserDAO;
import com.example.solvesphere.DataBaseUnit.UserDAOImpl;
import com.example.solvesphere.SecurityUnit.PasswordHasher;
import com.example.solvesphere.ServerUnit.ServerCommunicator;
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
    private ServerCommunicator serverCommunicator;
    private Problem currentProblem; // Current post
    private User currentUser; // Current user
    private Long currentUserId ;
    public void initData(Problem passedProblem, User passedUser) {
        ServerCommunicator serverCommunicator = new ServerCommunicator();
        this.currentProblem = passedProblem;
        this.currentUser = passedUser;
        this.commentDAO = new CommentDAOImpl();
        currentUserId = serverCommunicator.fetchUserIdByUsernameAndEmail(passedUser.getUsername(), passedUser.getEmail());
        System.out.println(currentUserId);
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
                //load the FXML file for a single comment item
                FXMLLoader loader = new FXMLLoader(getClass().getResource("commentItem.fxml"));
                VBox commentItem = loader.load();

                //get the controller and set the comment data
                CommentItemController controller = loader.getController();
                controller.setCommentData(comment,user.getUserById(comment.getUserId()).getUsername(),currentUser);

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
            newComment.setUserId(currentUserId);
            System.out.println("in posting "+currentUserId);
            newComment.setContent(content);

            commentDAO.addComment(newComment); //add comment to the database via DAO method
            commentField.clear();
            loadComments(); // refresh comments
        }
    }
}