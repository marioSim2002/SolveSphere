package com.example.solvesphere;

import com.example.solvesphere.DataBaseUnit.*;
import com.example.solvesphere.ServerUnit.ServerCommunicator;
import com.example.solvesphere.UserData.Comment;
import com.example.solvesphere.UserData.Problem;
import com.example.solvesphere.UserData.SessionManager;
import com.example.solvesphere.UserData.User;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class CommentItemController {
    @FXML
    private Button markSolutionButton;
    private ProblemDetailsController problemDetailsController; // parent controller //
    @FXML
    private ImageView deleteButton;
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
    @FXML
    private Text solutionIndicator;
    private User currentUser;
    private Comment currentComment;
    private final CommentDAO commentDAO = new CommentDAOImpl();
    private final UserVotesDAO voteDAO = new UserVotesDAOImpl();
    ServerCommunicator serverCommunicator = new ServerCommunicator();

    private final long currentUserId = SessionManager.getCurrentUser().getId();
    boolean isPostOwner;

    public void setProblemDetailsController(ProblemDetailsController parentController) {
        this.problemDetailsController = parentController;
    }

    public void setCommentData(Comment comment, String username, User passedUser, Problem problem) {
        this.currentComment = comment;
        commentText.setText(comment.getContent());
        commentAuthor.setText("By " + username);
        commentDate.setText(comment.getCreatedAt().toString());
        this.currentUser = passedUser;
        initButtons(comment);
        updateVoteCounts(comment);

        isPostOwner = (problem.getUserId() == currentUserId);

        if (comment.getUserId() == currentUserId) {
            deleteButton.setVisible(true);
            deleteButton.setOnMouseClicked(e -> deleteComment());
            Tooltip.install(deleteButton, new Tooltip("Delete your comment"));
        } else {
            deleteButton.setVisible(false);
        }

        // Ensure the "Mark as Solution" button is visible for all users
        markSolutionButton.setVisible(true);

        // Only enable the button for the post owner
        if (isPostOwner) {
            markSolutionButton.setOnMouseClicked(mouseEvent -> markAsSolution());
            markSolutionButton.setDisable(false);
        } else {
            markSolutionButton.setDisable(true);
        }

        System.err.println("Current comment state: " + comment.isSolution());

        //solution indicator if the comment is marked as a solution
        if (comment.isSolution()) {
            solutionIndicator.setVisible(true);
            markSolutionButton.setText("Solution ✓");
            markSolutionButton.setDisable(true);
            markSolutionButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        } else {
            markSolutionButton.setText("Mark as Solution");
            markSolutionButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            solutionIndicator.setVisible(false);
        }
    }

    @FXML
    public void initButtons(Comment comment) {

        if (voteDAO.hasUserVoted(currentUserId, comment.getId())) {
            String voteType = voteDAO.getUserVoteType(currentUserId, comment.getId());
            if ("upvote".equals(voteType)) {
                upvoteButton.setDisable(true);
            } else if ("downvote".equals(voteType)) {
                downvoteButton.setDisable(true);
            }
        }
        upvoteButton.setOnMouseClicked(mouseEvent -> handleUpvote(comment));
        downvoteButton.setOnMouseClicked(mouseEvent -> handleDownvote(comment));
        addHoverEffect(upvoteButton);
        addHoverEffect(downvoteButton);
        addHoverEffect(deleteButton);

        Tooltip.install(upvoteButton, new Tooltip("Up-vote this comment"));
        Tooltip.install(downvoteButton, new Tooltip("Down-vote this comment"));
    }

    @FXML
    public void handleUpvote(Comment comment) {
        String currentVoteType = voteDAO.getUserVoteType(currentUserId, comment.getId());

        if ("upvote".equals(currentVoteType)) {
            System.out.println("User has already upvoted this comment.");
            return;
        }

        voteDAO.recordVote(currentUserId, comment.getId(), "upvote");
        voteDAO.incrementUpvote(comment.getId());
        if ("downvote".equals(currentVoteType)) {
            voteDAO.decrementDownvote(comment.getId());
        }
        updateVoteCounts(comment);
    }

    @FXML
    public void handleDownvote(Comment comment) {
        String currentVoteType = voteDAO.getUserVoteType(currentUserId, comment.getId());

        if ("downvote".equals(currentVoteType)) {
            System.out.println("User has already downvoted this comment.");
            return;
        }

        voteDAO.recordVote(currentUserId, comment.getId(), "downvote");
        voteDAO.incrementDownvote(comment.getId());
        if ("upvote".equals(currentVoteType)) {
            voteDAO.decrementUpvote(comment.getId());
        }
        updateVoteCounts(comment);
    }

    private void updateVoteCounts(Comment comment) {
        Comment updatedComment = commentDAO.getCommentById(comment.getId());
        upvoteCount.setText(String.valueOf(updatedComment.getUpvotes()));
        downvoteCount.setText(String.valueOf(updatedComment.getDownvotes()));
    }

    private void addHoverEffect(ImageView button) {
        ScaleTransition hoverIn = createScaleTransition(button, 1.3);
        ScaleTransition hoverOut = createScaleTransition(button, 1.0);

        button.setOnMouseEntered(e -> hoverIn.playFromStart());
        button.setOnMouseExited(e -> hoverOut.playFromStart());
    }

    private ScaleTransition createScaleTransition(ImageView button, double scale) {
        ScaleTransition transition = new ScaleTransition(Duration.millis(200), button);
        transition.setToX(scale);
        transition.setToY(scale);
        return transition;
    }

    public void deleteComment() {
        long commentId = currentComment.getId();
        Comment comment = commentDAO.getCommentById(commentId);

        if (comment == null) {
            AlertsUnit.commentNotFoundAlert();
            return;
        }

        if (comment.getUserId() == currentUserId) {
            commentDAO.deleteComment(commentId);
            AlertsUnit.commentDeletedSuccessfullyAlert();
            problemDetailsController.loadComments();
        } else {
            AlertsUnit.commentDeletionPermissionDeniedAlert();
        }
    }

    @FXML
    public void markAsSolution() {
        if (currentComment != null) {
            commentDAO.markAsSolution(currentComment.getId(), true);
            // update local state
            currentComment.setSolution(true);
            // update the button for the owner
            markSolutionButton.setText("Solution ✓");
            markSolutionButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            markSolutionButton.setDisable(true);
            //the solution indicator for all users
            solutionIndicator.setVisible(true);
            problemDetailsController.loadComments();
        }
    }
}
