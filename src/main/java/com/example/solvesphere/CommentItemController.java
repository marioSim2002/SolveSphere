package com.example.solvesphere;

import com.example.solvesphere.DataBaseUnit.CommentDAO;
import com.example.solvesphere.DataBaseUnit.CommentDAOImpl;
import com.example.solvesphere.DataBaseUnit.UserVotesDAO;
import com.example.solvesphere.DataBaseUnit.UserVotesDAOImpl;
import com.example.solvesphere.ServerUnit.ServerCommunicator;
import com.example.solvesphere.UserData.Comment;
import com.example.solvesphere.UserData.User;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class CommentItemController {

    @FXML
    private Text downvoteCount;
    @FXML
    private Text upvoteCount;
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

    private User currentUser;
    private Comment currentComment;
    private final CommentDAO commentDAO = new CommentDAOImpl();
    private final UserVotesDAO voteDAO = new UserVotesDAOImpl();
    ServerCommunicator serverCommunicator = new ServerCommunicator();

    public void setCommentData(Comment comment, String username, User passedUser) {
        this.currentComment = comment;
        commentText.setText(comment.getContent());
        commentAuthor.setText("By " + username);
        commentDate.setText(comment.getCreatedAt().toString());
        this.currentUser = passedUser;
        //initialize the buttons and update vote counts
        initButtons(comment);
        updateVoteCounts(comment);
    }

    @FXML
    public void initButtons(Comment comment) {
        //check if the user has already voted//
        long currentUserId = serverCommunicator.fetchUserIdByUsernameAndEmail(currentUser.getUsername(),currentUser.getEmail());
        if (voteDAO.hasUserVoted(currentUserId, comment.getId())) {
            String voteType = voteDAO.getUserVoteType(currentUser.getId(), comment.getId());
            if ("upvote".equals(voteType)) {
                upvoteButton.setDisable(true);
            } else if ("downvote".equals(voteType)) {
                downvoteButton.setDisable(true);
            }
        }
        upvoteButton.setOnMouseClicked(mouseEvent -> handleUpvote(comment));
        downvoteButton.setOnMouseClicked(mouseEvent -> handleDownvote(comment));

        // hover effects //
        addHoverEffect(upvoteButton);
        addHoverEffect(downvoteButton);
        // tool tips AKA alt text //
        Tooltip.install(upvoteButton, new Tooltip("Up-vote this comment"));
        Tooltip.install(downvoteButton, new Tooltip("Down-vote this comment"));
    }

    /**
     * handles up-voting for a comment
     * **/
    @FXML
    public void handleUpvote(Comment comment) {
        long currentUserId = serverCommunicator.fetchUserIdByUsernameAndEmail(currentUser.getUsername(),currentUser.getEmail());
        String currentVoteType = voteDAO.getUserVoteType(currentUserId, comment.getId());

        if ("upvote".equals(currentVoteType)) {
            System.out.println("User has already upvoted this comment.");//dg
            return;
        }

        voteDAO.recordVote(currentUserId, comment.getId(), "upvote");
        voteDAO.incrementUpvote(comment.getId());
        if ("downvote".equals(currentVoteType)) {
            voteDAO.decrementDownvote(comment.getId()); // remove the previous downvote
        }
        updateVoteCounts(comment);
    }


    /**
     * handles down-voting for a comment
     * **/
    @FXML
    public void handleDownvote(Comment comment) {
        long currentUserId = serverCommunicator.fetchUserIdByUsernameAndEmail(currentUser.getUsername(),currentUser.getEmail());
        String currentVoteType = voteDAO.getUserVoteType(currentUserId, comment.getId());

        if ("downvote".equals(currentVoteType)) {
            System.out.println("User has already downvoted this comment.");
            return;
        }

        voteDAO.recordVote(currentUserId, comment.getId(), "downvote");
        voteDAO.incrementDownvote(comment.getId());
        if ("upvote".equals(currentVoteType)) {
            voteDAO.decrementUpvote(comment.getId()); // remove the previous upvote
        }
        updateVoteCounts(comment);
    }


    /**
     * counts the current number of upvotes/downvotes of the comment
     * **/
    private void updateVoteCounts(Comment comment) {
        Comment updatedComment = commentDAO.getCommentById(comment.getId());
        //update the UI with the new counts
        upvoteCount.setText(String.valueOf(updatedComment.getUpvotes()));
        downvoteCount.setText(String.valueOf(updatedComment.getDownvotes()));
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
    public void deleteComment(ActionEvent actionEvent) {
        long currentUserId = serverCommunicator.fetchUserIdByUsernameAndEmail(currentUser.getUsername(), currentUser.getEmail());
        long commentId = currentComment.getId();

        Comment comment = commentDAO.getCommentById(commentId);

        if (comment == null) {
            AlertsUnit.commentNotFoundAlert();
            return;
        }

        if (comment.getUserId() == currentUserId) {
            commentDAO.deleteComment(commentId);
            AlertsUnit.commentDeletedSuccessfullyAlert();
        } else {
            AlertsUnit.commentDeletionPermissionDeniedAlert();
        }
    }
}
