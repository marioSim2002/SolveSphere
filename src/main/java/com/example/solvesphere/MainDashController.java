package com.example.solvesphere;

import com.example.solvesphere.DataBaseUnit.ProblemDAO;
import com.example.solvesphere.DataBaseUnit.ProblemDAOImpl;
import com.example.solvesphere.DataBaseUnit.UserDAO;
import com.example.solvesphere.DataBaseUnit.UserDAOImpl;
import com.example.solvesphere.UserData.Problem;
import com.example.solvesphere.UserData.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MainDashController {
    @FXML
    public ImageView addProblemButton;
    @FXML
    private VBox problemListContainer;  // a container in the dashboard to hold the problems

    public void initUserData(User user) {
        ProblemDAO problemDAO = new ProblemDAOImpl();
        Map<String,Integer> interests = user.getFieldsOfInterest();
        List<Problem> userProblems = problemDAO.getProblemsByUserInterest(interests);
        user.setProblems(userProblems);
        displayProblems(user.getProblems());
    }

    public void displayProblems(List<Problem> problems) {
        System.out.println(problems);
        for (Problem problem : problems) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ProblemItem.fxml"));
                VBox problemItem = loader.load();

                // Set problem data
                ProblemItemController controller = loader.getController();
                controller.setProblemData(problem.getTitle(), problem.getDescription());

                problemListContainer.getChildren().add(problemItem);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void addProblem() {
    }

    @FXML
    public void searchForInput() {
    }

    @FXML
    public void onHomeClick() {
    }

    @FXML
    public void onProfileClick() {
    }

    public void onLogoutClick(ActionEvent actionEvent) {
    }



    public void onSettingsClick(ActionEvent actionEvent) {
    }

    public void onFilterClick(ActionEvent actionEvent) {
    }
}
