package com.example.solvesphere;

import com.example.solvesphere.DataBaseUnit.GlobalPollDAO;
import com.example.solvesphere.DataBaseUnit.GlobalPollDAOImpl;
import com.example.solvesphere.PollsUnit.GlobalPoll;
import com.example.solvesphere.UserData.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class PollItemController {
    @FXML
    private Label pollQuestion;
    @FXML
    private Button option1Button;
    @FXML
    private Button option2Button;
    @FXML
    private ProgressBar option1Bar;
    @FXML
    private ProgressBar option2Bar;
    @FXML
    private Label option1Label;
    @FXML
    private Label option2Label;

    private long currentUserId;
    private GlobalPoll poll;
    private final GlobalPollDAO pollDAO = new GlobalPollDAOImpl();

    public void setPollData(GlobalPoll poll) {
        this.poll = poll;
        this.currentUserId = SessionManager.getCurrentUser().getId();
        if (pollDAO.hasUserVoted(poll.getId(), SessionManager.getCurrentUser().getId())) {
            option1Button.setDisable(true);
            option2Button.setDisable(true);
        }

        pollQuestion.setText(poll.getQuestion());
        option1Label.setText(poll.getOptionYes());
        option2Label.setText(poll.getOptionNo());
        updateUI();
    }

    @FXML
    private void voteOption1() {
        if (pollDAO.recordVote(poll.getId(), currentUserId, true)) {
            poll.setVotesYes(poll.getVotesYes() + 1);
            option1Button.setDisable(true);
            option2Button.setDisable(true);
            updateUI();
        }
    }

    @FXML
    private void voteOption2() {
        if (pollDAO.recordVote(poll.getId(),    currentUserId, false)) {
            poll.setVotesNo(poll.getVotesNo() + 1);
            option1Button.setDisable(true);
            option2Button.setDisable(true);
            updateUI();
        }
    }

    private void updateUI() {
        int totalVotes = poll.getVotesYes() + poll.getVotesNo();

        if (totalVotes == 0) {
            option1Bar.setProgress(0);
            option2Bar.setProgress(0);
            option1Label.setText(poll.getOptionYes() + " (0%)");
            option2Label.setText(poll.getOptionNo() + " (0%)");
            return;
        }
        // yes - A
        // no - B
        double percentA = (double) poll.getVotesYes() / totalVotes;
        double percentB = (double) poll.getVotesNo() / totalVotes;

        option1Bar.setProgress(percentA);
        option2Bar.setProgress(percentB);

        option1Label.setText(String.format("%s (%.0f%%)", poll.getOptionYes(), percentA * 100));
        option2Label.setText(String.format("%s (%.0f%%)", poll.getOptionNo(), percentB * 100));
    }

}
