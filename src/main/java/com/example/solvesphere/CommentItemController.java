package com.example.solvesphere;

import com.example.solvesphere.UserData.Comment;
import com.example.solvesphere.UserData.User;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class CommentItemController {

    @FXML
    private Text commentText;
    @FXML
    private Text commentAuthor;
    @FXML
    private Text commentDate;
    @FXML
    private ImageView upvoteButton;
    private User currentUser;
    @FXML
    private ImageView downvoteButton;


    public void setCommentData(Comment comment, String username, User passedUser) {
        commentText.setText(comment.getContent());
        commentAuthor.setText("By " + username);
        commentDate.setText(comment.getCreatedAt().toString());
        this.currentUser = passedUser;
        initButtons(comment);

    }
    @FXML
    public void initButtons(Comment comment) {
        // Initialize button click listeners
        upvoteButton.setOnMouseClicked(mouseEvent -> handleUpvote(comment));
        downvoteButton.setOnMouseClicked(mouseEvent -> handleDownvote(comment));

        // Add hover effects for upvote and downvote buttons
        addHoverEffect(upvoteButton);
        addHoverEffect(downvoteButton);

        // Add tooltips
        Tooltip.install(upvoteButton, new Tooltip("Up-vote this comment"));
        Tooltip.install(downvoteButton, new Tooltip("Down-vote this comment"));
    }

    /**
     * Adds a scaling hover effect to an ImageView.
     */
    private void addHoverEffect(ImageView button) {
        ScaleTransition hoverIn = createScaleTransition(button, 1.3);
        ScaleTransition hoverOut = createScaleTransition(button, 1.0);

        button.setOnMouseEntered(e -> hoverIn.playFromStart());
        button.setOnMouseExited(e -> hoverOut.playFromStart());
    }

    /**
     * Creates a scale transition for a given ImageView.
     */
    private ScaleTransition createScaleTransition(ImageView button, double scale) {
        ScaleTransition transition = new ScaleTransition(Duration.millis(200), button);
        transition.setToX(scale);
        transition.setToY(scale);
        return transition;
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
