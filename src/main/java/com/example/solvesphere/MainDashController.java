package com.example.solvesphere;

import com.example.solvesphere.DataBaseUnit.ProblemDAO;
import com.example.solvesphere.DataBaseUnit.ProblemDAOImpl;
import com.example.solvesphere.DataBaseUnit.UserDAO;
import com.example.solvesphere.DataBaseUnit.UserDAOImpl;
import com.example.solvesphere.ServerUnit.ServerCommunicator;
import com.example.solvesphere.UserData.Problem;
import com.example.solvesphere.UserData.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class MainDashController {
    @FXML
    private VBox problemListContainer;
    @FXML
    private ComboBox<String> filterOptions;


    private User currentUser;  //connected user e.g current user

    public void initUserData(User user) {
        this.currentUser = user;
        envokeAllProblemsDisplay();
    }

    private void envokeAllProblemsDisplay() {
        // sendRequest now returns an Object that could be a List<Problem>
        ServerCommunicator serverCommunicator = new ServerCommunicator();
        List<Problem> allProblems = serverCommunicator.sendFetchProblemsRequest("FETCH_PROBLEMS");
        displayProblems(allProblems);
        }


    private void displayProblems(List<Problem> problems) {
        UserDAO userDAO = new UserDAOImpl();
        problemListContainer.getChildren().clear(); //clear existing problems
        for (Problem problem : problems) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("ProblemItem.fxml"));
                    VBox problemItem = loader.load();

                    ProblemItemController controller = loader.getController();
                    User problemUser = userDAO.getUserById(problem.getUserId()); ///fixed

                    //use the provided method to check if the current user posted this problem
                    boolean isCurrentUserThePublisher = checkCurrentUserAgainstPublisher(problemUser.getEmail(), currentUser.getEmail());
                    String problemPublisherName = isCurrentUserThePublisher ? "You" : problemUser.getUsername();

                    controller.setProblemData(problem,problemPublisherName);
                    problemListContainer.getChildren().add(problemItem);
                } catch (IOException e) {e.printStackTrace();}
            }
    }
    public boolean checkCurrentUserAgainstPublisher(String emailA , String emailB){
        System.out.println(emailA+" " + emailB);
        return emailB.equals(emailA);}

    @FXML
    private void handleFilterSelection() {
        String selectedFilter = filterOptions.getValue();
        System.out.println("Selected Filter: " + selectedFilter);
        applyFilter(selectedFilter);
    }

    private void applyFilter(String filter) {
        ProblemDAO problemDAO = new ProblemDAOImpl();
        UserDAO userDAO = new UserDAOImpl();;
        switch (filter) {
            case "No Filter":
                envokeAllProblemsDisplay();
                break;
            case "Interests Related":
                    List<Problem> problems = problemDAO.getProblemsByUserInterest(currentUser.getFieldsOfInterest());
                    displayProblems(problems);
                break;
            case "In Your Country":
                if (currentUser != null) {
                    List<Problem> problemsByCountry = problemDAO.getProblemsByCountry(currentUser.getCountry());
                    displayProblems(problemsByCountry);
                }
            case "Posted By You":
                assert currentUser != null;
                long userId = userDAO.getUserIdByUsernameAndEmail(currentUser.getUsername(),currentUser.getEmail());
                problems = problemDAO.getProblemsPostedByCurrentUser(userId);
                displayProblems(problems);
                break;
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

    @FXML
    private void onHoverEnter(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #555555; -fx-text-fill: white;-fx-font-size:15;");
    }

    @FXML
    private void onHoverExit(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: white;-fx-font-size:14;");
    }

}
