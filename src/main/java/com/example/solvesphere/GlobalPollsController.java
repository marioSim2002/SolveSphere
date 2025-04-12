package com.example.solvesphere;

import com.example.solvesphere.DataBaseUnit.GlobalPollDAO;
import com.example.solvesphere.DataBaseUnit.GlobalPollDAOImpl;
import com.example.solvesphere.PollsUnit.GlobalPoll;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class GlobalPollsController {
    @FXML
    private VBox pollsListContainer;


    private final GlobalPollDAO pollDAO = new GlobalPollDAOImpl();

    @FXML
    public void initialize() {
        loadAllPolls();
    }
    @FXML
    private void openPollCreator() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CreatePollView.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Create New Poll");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAllPolls() {
        pollsListContainer.getChildren().clear();
        List<GlobalPoll> polls = pollDAO.getAllPolls();

        for (GlobalPoll poll : polls) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("PollItem.fxml"));
                AnchorPane pollItem = loader.load();

                PollItemController controller = loader.getController();

                    controller.setPollData(poll);

                pollsListContainer.getChildren().add(pollItem);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
