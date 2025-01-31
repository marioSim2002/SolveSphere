package com.example.solvesphere.ValidationsUnit;

import com.example.solvesphere.Algos.SuggestionsAlgo;
import com.example.solvesphere.DataBaseUnit.CommentDAO;
import com.example.solvesphere.DataBaseUnit.CommentDAOImpl;
import com.example.solvesphere.DataBaseUnit.ProblemDAO;
import com.example.solvesphere.DataBaseUnit.ProblemDAOImpl;
import com.example.solvesphere.MainDashController;
import com.example.solvesphere.ServerUnit.ServerCommunicator;
import com.example.solvesphere.UserData.Comment;
import com.example.solvesphere.UserData.User;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Inspector {
    private static final int CHECK_INTERVAL = 50 * 1000; // 50 sec in milliseconds
    private final CommentDAO commentDAO = new CommentDAOImpl();
    private final ProblemDAO problemDAO;
    private final MainDashController mainDashController;
    private final User user;
    SuggestionsAlgo suggestionsAlgo;
    public Inspector(MainDashController mainDashController, User user) {
        this.problemDAO = new ProblemDAOImpl();
        this.mainDashController = mainDashController;
        this.user = user;
        suggestionsAlgo =  new SuggestionsAlgo();
    }

    public void startInspection() {
        Timer timer = new Timer(true); //run as daemon
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkAndDeleteDownvotedComments();
                updateMostPostedCategory();
                suggestionsAlgo.updateInterestPriorityBasedOnFavorites(extractUserID());
            }
        }, 0, CHECK_INTERVAL);
    }

    /**
     * Checks all comments in the database and deletes those with more than 40 downvotes.
     */
    private void checkAndDeleteDownvotedComments() {
        System.out.println("Checking for comments with excessive downvotes...");
        List<Comment> allComments = commentDAO.getAllComments();

        for (Comment comment : allComments) {
            if (comment.getDownvotes() > 40) {
                commentDAO.deleteComment(comment.getId());
                System.out.println("Deleted comment ID: " + comment.getId() + " due to excessive downvotes.");
            }
        }
    }

    /**
     * Checks the most posted category in the database and updates it in the dashboard.
     */
    private void updateMostPostedCategory() {
        System.out.println("Checking the most posted category...");
        Map<String, Integer> categoryCounts = problemDAO.getProblemCategoryCounts();

        String mostPostedCategory = null;
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : categoryCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostPostedCategory = entry.getKey();
            }
        }

        if (mostPostedCategory != null) {
            System.out.println("Most posted category: " + mostPostedCategory + " (" + maxCount + " posts)");
            mainDashController.updateMostPostedCategoryLabel(mostPostedCategory, maxCount);
        }
    }
    private long extractUserID(){
        ServerCommunicator serverCommunicator = new ServerCommunicator();
        return serverCommunicator.fetchUserIdByUsernameAndEmail(user.getUsername(), user.getEmail());
    }
}
