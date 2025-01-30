package com.example.solvesphere.ValidationsUnit;

import com.example.solvesphere.DataBaseUnit.*;
import com.example.solvesphere.MainDashController;
import com.example.solvesphere.ProfileTabbedController;
import com.example.solvesphere.UserData.Comment;
import javafx.application.Platform;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Inspector {
    private static final int CHECK_INTERVAL = 60 * 1000; // 1 minute in milliseconds
    private final CommentDAO commentDAO = new CommentDAOImpl();
    private final ProblemDAO problemDAO;
    private final FavoritesDAO favoritesDAO;
    private final MainDashController mainDashController;
    private final ProfileTabbedController profileTabbedController;

    public Inspector(MainDashController mainDashController, ProfileTabbedController profileTabbedController) {
        this.problemDAO = new ProblemDAOImpl();
        this.favoritesDAO = new FavoritesDAOImpl();
        this.mainDashController = mainDashController;
        this.profileTabbedController = profileTabbedController;
    }

    public void startInspection() {
        Timer timer = new Timer(true); // Run as daemon
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkAndDeleteDownvotedComments();
                updateMostPostedCategory();
                updateFavorites(); // קריאה לפונקציה כאן
            }
        }, 0, CHECK_INTERVAL);
    }

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

private void updateFavorites() {
    System.out.println("Checking the comments you favorite...");

    if (isFavoritesPageVisible()) {
        Platform.runLater(() -> profileTabbedController.getFavPosts());
    } else {
        System.out.println("Not on Favorites page, skipping update.");
    }
}
    public boolean isFavoritesPageVisible() {
        return profileTabbedController != null && profileTabbedController.isFavoritesPageVisible();
    }
}
