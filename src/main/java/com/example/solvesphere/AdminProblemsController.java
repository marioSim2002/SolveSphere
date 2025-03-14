package com.example.solvesphere;

import com.example.solvesphere.DataBaseUnit.ProblemDAO;
import com.example.solvesphere.DataBaseUnit.ProblemDAOImpl;
import com.example.solvesphere.UserData.AdminProblem;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AdminProblemsController {

    @FXML private VBox problemsContainer;
    private final ProblemDAO adminProblemDAO = new ProblemDAOImpl();

    @FXML
    public void initialize() {
        loadAdminProblems();
    }

    private void loadAdminProblems() {
        new Thread(() -> {
            try {
                List<AdminProblem> adminProblems = adminProblemDAO.getAdminProblems();
                Platform.runLater(() -> displayAdminProblems(adminProblems));
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void displayAdminProblems(List<AdminProblem> problems) {
         if (problemsContainer.getChildren()!=null){problemsContainer.getChildren().clear();}
        for (AdminProblem problem : problems) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/solvesphere/AdminProblemItem.fxml")); // FIXED
                Node problemItem = loader.load();

                AdminProblemItemController controller = loader.getController(); // Correct controller
                controller.setProblemData(problem, "Admin");

                problemsContainer.getChildren().add(problemItem);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void handleSearch(KeyEvent keyEvent) {
    }

    public void handleFilter(ActionEvent actionEvent) {
    }
}
