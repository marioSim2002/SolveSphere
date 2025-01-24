package com.example.solvesphere;

import com.example.solvesphere.DataBaseUnit.*;
import com.example.solvesphere.SecurityUnit.PasswordHasher;
import com.example.solvesphere.ServerUnit.ServerCommunicator;
import com.example.solvesphere.UserData.Comment;
import com.example.solvesphere.UserData.FavoritesService;
import com.example.solvesphere.UserData.Problem;
import com.example.solvesphere.UserData.User;
import com.example.solvesphere.ValidationsUnit.ProfanityFilter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ProblemDetailsController {

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
    private Problem currentProblem; // Current post
    private User currentUser; // Current user
    private Long currentUserId;
    private FavoritesService favoritesService = new FavoritesService();
    private boolean isSaved = false;

    public void initData(Problem passedProblem, User passedUser) {
        ServerCommunicator serverCommunicator = new ServerCommunicator();
        this.currentProblem = passedProblem;
        this.currentUser = passedUser;
        this.commentDAO = new CommentDAOImpl();
        this.favoritesService = new FavoritesService();
        currentUserId = serverCommunicator.fetchUserIdByUsernameAndEmail(passedUser.getUsername(), passedUser.getEmail());
        System.out.println(currentUserId);
        showData();
        updateFavoriteUI();
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
    public void postComment(ActionEvent actionEvent) {
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