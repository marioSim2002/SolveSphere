package com.example.solvesphere;

import com.example.solvesphere.DataBaseUnit.UserDAO;
import com.example.solvesphere.DataBaseUnit.UserDAOImpl;
import com.example.solvesphere.UserData.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class PeoplePageController {

    private Long currentUserID;
    @FXML
    private TextField searchField;
    @FXML
    private GridPane peopleGrid;

    private final UserDAO userDAO = new UserDAOImpl();

    @FXML
    public void initialize(long currentUserID) {
        this.currentUserID = currentUserID;
        loadPeople(""); // Load all users initially
    }

    @FXML
    public void onSearchPeople() {
        String keyword = searchField.getText().trim();
        loadPeople(keyword);
    }

    private void loadPeople(String keyword) {
        List<User> users = userDAO.searchUsers(keyword); // Search for users

        peopleGrid.getChildren().clear(); // Clear existing users
        int col = 0, row = 0;

        for (User user : users) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ProfileCard.fxml"));
                VBox profileCard = loader.load();
                ProfileCardController controller = loader.getController();
                controller.setUserData(user);

                peopleGrid.add(profileCard, col, row);
                col++;
                if (col == 3) { // 3 users per row
                    col = 0;
                    row++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onProfileClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProfileDetails.fxml"));
            Parent root = loader.load();

            Stage profileStage = new Stage();
            profileStage.setTitle("User Profile");
            profileStage.setScene(new Scene(root, 600, 400));
            profileStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
