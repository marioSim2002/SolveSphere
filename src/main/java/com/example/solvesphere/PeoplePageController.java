package com.example.solvesphere;

import com.example.solvesphere.DataBaseUnit.UserDAO;
import com.example.solvesphere.DataBaseUnit.UserDAOImpl;
import com.example.solvesphere.UserData.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class PeoplePageController {

    private Long currentUserID;
    private User currentAppUser;
    @FXML private TextField searchField;
    @FXML private GridPane peopleGrid;

    private final UserDAO userDAO = new UserDAOImpl();

    @FXML
    public void initialize(long currentUserID,User currentAppUser) {
        this.currentUserID = currentUserID;
        this.currentAppUser = currentAppUser;
        try {
            loadPeople(); // load all users initially
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onSearchPeople() {
        String keyword = searchField.getText().trim();
        searchUsers(keyword);
    }

    private void loadPeople() throws SQLException, ClassNotFoundException {
        List<User> users = userDAO.getAllUsers(); // fetch all users
        populateUserGrid(users);
    }

    private void searchUsers(String keyword) {
        try {
            List<User> users;
            if (keyword.isEmpty()) {
                users = userDAO.getAllUsers(); // if empty, load all users
            } else {
                users = userDAO.searchUsers(keyword); //search by keyword
            }
            populateUserGrid(users);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void populateUserGrid(List<User> users) {
        peopleGrid.getChildren().clear(); // Clear existing users
        peopleGrid.getColumnConstraints().clear(); // Clear previous column constraints
        peopleGrid.getRowConstraints().clear(); // Clear previous row constraints

        int columns = 3; // Number of columns per row
        int row = 0, col = 0;

        for (User user : users) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ProfileCard.fxml"));
                VBox profileCard = loader.load();
                ProfileCardController controller = loader.getController();

                controller.setUserData(user,currentAppUser);

                //ensure card fills available space
                profileCard.setMaxWidth(Double.MAX_VALUE);
                GridPane.setFillWidth(profileCard, true);

                // Add card to grid
                peopleGrid.add(profileCard, col, row);

                // Define column constraints for equal spacing
                if (peopleGrid.getColumnConstraints().size() < columns) {
                    ColumnConstraints colConstraints = new ColumnConstraints();
                    colConstraints.setPercentWidth(100.0 / columns);
                    colConstraints.setFillWidth(true);
                    peopleGrid.getColumnConstraints().add(colConstraints);
                }

                // Move to next column, reset if full
                col++;
                if (col >= columns) {
                    col = 0;
                    row++;
                }

            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
