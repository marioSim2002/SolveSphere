package com.example.solvesphere;

import com.example.solvesphere.UserData.Problem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class MainDashController {
    @FXML
    public ImageView addProblemButton;
    @FXML
    private VBox problemListContainer;  // a container in the dashboard to hold the problems

    public void displayProblems(List<Problem> problems) {
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
}
