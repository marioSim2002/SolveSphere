package com.example.solvesphere;

import com.example.solvesphere.DataBaseUnit.*;
import com.example.solvesphere.ServerUnit.ServerCommunicator;
import com.example.solvesphere.UserData.FavoritesService;
import com.example.solvesphere.UserData.Problem;
import com.example.solvesphere.UserData.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileTabbedController {
    @FXML
    private Label postCountLabel;
    @FXML
    private Label favoriteCountLabel;
    @FXML
    private PieChart favoritesPieChart;
    @FXML
    private BarChart<String, Number> postBarChart;
    @FXML
    private Label usernameLabel;
    @FXML
    private Button changePictureButton;
    @FXML
    private VBox favoriteProblemListContainer;

    // VBox container where we will display the custom items (ProblemItem.fxml)
    @FXML private VBox problemListContainer;

    private User currentUser;

    public void initialize(User user) {
        this.currentUser = user;
        usernameLabel.setText("Welcome "+currentUser.getUsername());
        loadAllPosts();
        getFavPosts();
        initializeCharts();
    }

    private void loadAllPosts() {
        List<Problem> userPosts = getUserPosts();
        postCountLabel.setText(String.valueOf(userPosts.size()));
        displayProblems(userPosts,problemListContainer);
    }
    private void getFavPosts() {
        // 1) Fetch current user ID
        ServerCommunicator serverCommunicator = new ServerCommunicator();
        long currentUserId = serverCommunicator.fetchUserIdByUsernameAndEmail(
                currentUser.getUsername(),
                currentUser.getEmail()
        );

        // 2) Get favorite problem IDs
        FavoritesDAO favoritesDAO = new FavoritesDAOImpl();
        List<Long> favoriteProblemIDs = favoritesDAO.getFavoriteProblemsByUser(currentUserId);

        // 3) Convert those IDs into Problem objects
        ProblemDAO problemDAO = new ProblemDAOImpl();
        List<Problem> favoriteProblems = new ArrayList<>();

        for (Long problemId : favoriteProblemIDs) {
            Problem p = problemDAO.getProblemById(problemId);
            if (p != null) {
                favoriteProblems.add(p);
            }
        }

        favoriteCountLabel.setText(String.valueOf(favoriteProblems.size()));//tmp

        displayProblems(favoriteProblems, favoriteProblemListContainer);
    }


    private List<Problem> getUserPosts() {
        ServerCommunicator serverCommunicator = new ServerCommunicator();
        ProblemDAO problemDAO = new ProblemDAOImpl();
        long currentUserId = serverCommunicator.fetchUserIdByUsernameAndEmail(currentUser.getUsername(), currentUser.getEmail());
        return problemDAO.getProblemsPostedByCurrentUser(currentUserId);
    }

    public void displayProblems(List<Problem> problems, VBox targetContainer) {
        UserDAO userDAO = new UserDAOImpl();
        targetContainer.getChildren().clear(); // Clear any existing items

        for (Problem problem : problems) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ProblemItem.fxml"));
                VBox problemItem = loader.load();
                ProblemItemController controller = loader.getController();

                User problemUser = userDAO.getUserById(problem.getUserId());
                CommentDAO commentDAO = new CommentDAOImpl();
                int count = commentDAO.getCommentCountByProblemId(problem.getId());

                controller.setProblemData(problem, problemUser.getUsername(), currentUser, count);

                // add the item to the chosen container
                targetContainer.getChildren().add(problemItem);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initializeCharts() {
        List<Problem> userPosts = getUserPosts();

        Map<String, Integer> postCategoryCounts = new HashMap<>();
        for (Problem problem : userPosts) {
            postCategoryCounts.put(problem.getCategory(),
                    postCategoryCounts.getOrDefault(problem.getCategory(), 0) + 1);
        }
        postBarChart.getData().clear();
        BarChart.Series<String, Number> postSeries = new BarChart.Series<>();
        postSeries.setName("User Contributions");
        for (Map.Entry<String, Integer> entry : postCategoryCounts.entrySet()) {
            postSeries.getData().add(new BarChart.Data<>(entry.getKey(), entry.getValue()));
        }
        postBarChart.getData().add(postSeries);

        //fetch favorite problem data
        FavoritesDAO favoritesDAO = new FavoritesDAOImpl();
        long currentUserId = new ServerCommunicator().fetchUserIdByUsernameAndEmail(currentUser.getUsername(), currentUser.getEmail());

        List<Long> favoriteProblemIDs = favoritesDAO.getFavoriteProblemsByUser(currentUserId);
        ProblemDAO problemDAO = new ProblemDAOImpl();

        Map<String, Integer> favoriteCategoryCounts = new HashMap<>();
        for (Long problemId : favoriteProblemIDs) {
            Problem p = problemDAO.getProblemById(problemId);
            if (p != null) {
                favoriteCategoryCounts.put(p.getCategory(),
                        favoriteCategoryCounts.getOrDefault(p.getCategory(), 0) + 1);
            }
        }

        favoritesPieChart.getData().clear();
        for (Map.Entry<String, Integer> entry : favoriteCategoryCounts.entrySet()) {
            favoritesPieChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
    }

}
