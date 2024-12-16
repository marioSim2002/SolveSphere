package com.example.solvesphere;

import com.example.solvesphere.UserData.Comment;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class CommentItemController {

    @FXML
    private Text commentText;
    @FXML
    private Text commentAuthor;
    @FXML
    private Text commentDate;
    @FXML
    private ImageView upvoteButton;
    @FXML
    private ImageView downvoteButton;

    public void setCommentData(Comment comment, String username) {
        commentText.setText(comment.getContent());
        commentAuthor.setText("By " + username);
        commentDate.setText(comment.getCreatedAt().toString());
        //  upvoteButton.setOnMouseClicked(event -> handleUpvote(comment));
       // downvoteButton.setOnMouseClicked(event -> handleDownvote(comment));
    }

    private void handleUpvote(Comment comment) {
        System.out.println("Upvote clicked for comment ID: " + comment.getId());
        // Implement upvote logic
    }

    private void handleDownvote(Comment comment) {
        System.out.println("Downvote clicked for comment ID: " + comment.getId());
        // Implement downvote logic
    }
}
