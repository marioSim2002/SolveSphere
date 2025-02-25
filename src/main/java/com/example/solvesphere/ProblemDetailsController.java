package com.example.solvesphere;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.example.solvesphere.DataBaseUnit.*;
import com.example.solvesphere.SecurityUnit.PasswordHasher;
import com.example.solvesphere.ServerUnit.ServerCommunicator;
import com.example.solvesphere.UserData.Comment;
import com.example.solvesphere.UserData.FavoritesService;
import com.example.solvesphere.UserData.Problem;
import com.example.solvesphere.UserData.User;
import com.example.solvesphere.ValidationsUnit.ProfanityFilter;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ProblemDetailsController {

    @FXML
    private Label problemCategory;
    @FXML
    private ImageView saveIcon;
    @FXML
    private Button saveButton;
    @FXML
    private Label problemTitle;

    @FXML
    private TextArea problemDescription;

    @FXML
    private TextField commentField; // Input field for new comments

    @FXML
    private VBox commentListContainer;
    private CommentDAO commentDAO;
    private ServerCommunicator serverCommunicator;
    private Problem currentProblem; // current post
    private User currentUser; // current user
    private Long currentUserId;
    private String ownerName;
    private ScheduledExecutorService commentUpdater;
    private FavoritesService favoritesService = new FavoritesService();

    public void initData(Problem passedProblem, User passedUser,String owner) {
        ServerCommunicator serverCommunicator = new ServerCommunicator();
        this.currentProblem = passedProblem;
        this.currentUser = passedUser;
        this.commentDAO = new CommentDAOImpl();
        this.ownerName  = owner;
        this.favoritesService = new FavoritesService();
        currentUserId = serverCommunicator.fetchUserIdByUsernameAndEmail(passedUser.getUsername(), passedUser.getEmail());
        System.out.println(passedUser.getEmail());
        System.out.println(currentUserId);
        showData();
        updateFavoriteUI();
        loadComments();
        startAutoRefreshComments(); //auto-refresh comments

    }

    private void showData() {
        try {

            problemTitle.setText(currentProblem.getTitle());
            problemCategory.setText(currentProblem.getCategory()+" category");
            problemDescription.setText(currentProblem.getDescription());

        } catch (NullPointerException exception) {
            System.out.println("Oops NPE! ,Data trying to access may be null.");
        }
    }


    public void stopAutoRefreshComments() {
        if (commentUpdater != null && !commentUpdater.isShutdown()) {
            commentUpdater.shutdown();
            commentUpdater = null;
        }
    }

    private void startAutoRefreshComments() {
        commentUpdater = Executors.newSingleThreadScheduledExecutor();
        commentUpdater.scheduleAtFixedRate(() -> {
            Platform.runLater(this::loadComments);
        }, 0, 5, TimeUnit.SECONDS); //every 5 seconds
    }
    @FXML
    private void onClose() {
        stopAutoRefreshComments();
        Stage stage = (Stage) problemTitle.getScene().getWindow();
        stage.close();
    }

    public void loadComments() {
        commentListContainer.getChildren().clear();
        List<Comment> comments = commentDAO.getCommentsByProblemId(currentProblem.getId());
        UserDAO userDAO = new UserDAOImpl();
        for (Comment comment : comments) {
            try {
                //load the FXML file for a single comment item
                FXMLLoader loader = new FXMLLoader(getClass().getResource("commentItem.fxml"));
                VBox commentItem = loader.load();

                //get the controller and set the comment data
                CommentItemController controller = loader.getController();
                controller.setProblemDetailsController(this);
                controller.setCommentData(comment, userDAO.getUserById(comment.getUserId()).getUsername(), currentUser,currentProblem);

                //add the comment item to the container
                commentListContainer.getChildren().add(commentItem);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void postComment() {
        String content = commentField.getText().trim();

        if (!content.isEmpty()) {
            //apply the profanity filter
            ProfanityFilter profanityFilter = new ProfanityFilter();
            String filteredComment = profanityFilter.filterText(content);
            Comment newComment = new Comment();
            newComment.setProblemId(currentProblem.getId());
            newComment.setUserId(currentUserId);
            newComment.setContent(filteredComment);
            commentDAO.addComment(newComment);
            commentField.clear();
            loadComments();
        }
    }

    @FXML
    private void onSave() {
        FavoritesDAOImpl favoritesDAO = new FavoritesDAOImpl();
        boolean isFavorite = favoritesDAO.isFavorite(currentUserId, currentProblem.getId());

        if (isFavorite) {
            //remove from favorites
            favoritesService.removeFavorite(currentUserId, currentProblem.getId());
            System.out.println("Problem removed from favorites.");
        } else {
            //add to favorites
            favoritesService.addFavorite(currentUserId, currentProblem.getId());
            System.out.println("Problem added to favorites.");
        }
        updateFavoriteUI();
    }


    private void updateFavoriteUI() {
        FavoritesDAOImpl favoritesDAO = new FavoritesDAOImpl();
        boolean isFavorite = favoritesDAO.isFavorite(currentUserId, currentProblem.getId());

        if (isFavorite) {
            //problem is a favorite
            saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            saveIcon.setImage(new Image(Objects.requireNonNull(getClass().getResource("/com/example/solvesphere/Images/icoFavClicked.png")).toExternalForm()));
        } else {
            //problem is not a favorite
            saveButton.setStyle("-fx-background-color: transparent;");
            saveIcon.setImage(new Image(Objects.requireNonNull(getClass().getResource("/com/example/solvesphere/Images/icoFav.png")).toExternalForm()));
        }
    }
}