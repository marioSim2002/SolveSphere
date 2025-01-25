package com.example.solvesphere.ValidationsUnit;

import com.example.solvesphere.DataBaseUnit.CommentDAO;
import com.example.solvesphere.DataBaseUnit.CommentDAOImpl;
import com.example.solvesphere.DataBaseUnit.ProblemDAO;
import com.example.solvesphere.DataBaseUnit.ProblemDAOImpl;
import com.example.solvesphere.MainDashController;
import com.example.solvesphere.UserData.Comment;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Inspector {
    private static final int CHECK_INTERVAL = 60 * 1000; // 1 minute in milliseconds
    private final CommentDAO commentDAO = new CommentDAOImpl();
    private final ProblemDAO problemDAO;
    private final MainDashController mainDashController;

    public Inspector(MainDashController mainDashController) {
        this.problemDAO = new ProblemDAOImpl();
        this.mainDashController = mainDashController;
    }

    public void startInspection() {
        Timer timer = new Timer(true); // Run as daemon
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkAndDeleteDownvotedComments();
                updateMostPostedCategory();
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
}
