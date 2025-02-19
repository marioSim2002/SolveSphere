package com.example.solvesphere.ValidationsUnit;

import com.example.solvesphere.Algos.SuggestionsAlgo;
import com.example.solvesphere.DataBaseUnit.*;
import com.example.solvesphere.MainDashController;
import com.example.solvesphere.NotificationsController;
import com.example.solvesphere.NotificationsUnit.NotificationDAO;
import com.example.solvesphere.NotificationsUnit.NotificationDAOImpl;
import com.example.solvesphere.ServerUnit.ServerCommunicator;
import com.example.solvesphere.UserData.Comment;
import com.example.solvesphere.UserData.User;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.util.*;

public class Inspector {
    private static final int CHECK_INTERVAL = 20 * 1000; //each 20 sec, inspect
    private final CommentDAO commentDAO = new CommentDAOImpl();
    private final ProblemDAO problemDAO;
    private final MainDashController mainDashController;
    private final NotificationsController notificationsController;
    private final User user;
    private final NotificationDAO notificationDAO = new NotificationDAOImpl();

    public Inspector(MainDashController mainDashController, NotificationsController notificationsController, User user) {
        this.problemDAO = new ProblemDAOImpl();
        this.mainDashController = mainDashController;
        this.notificationsController = notificationsController;
        this.user = user;
    }

    public void startInspection() {
        Timer timer = new Timer(true); // Run as daemon thread
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkAndDeleteDownvotedComments();
                updateMostPostedCategory();
                checkForNewComments();
                checkForNewFriendRequests();
                mainDashController.setProfileImage(buildImage(user)); // update profile image
            }
        }, 0, CHECK_INTERVAL);
    }

    private void checkForNewComments() {
        try {
            Map<Long, String> newNotifications = notificationDAO.getUnseenNotificationsWithProblemTitles(extractUserID());

            if (!newNotifications.isEmpty()) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("New Comments on Your Posts");
                    alert.setHeaderText("You have new comments!");

                    //format the list of posts with new comments
                    StringBuilder content = new StringBuilder("New comments on:\n");
                    for (String title : newNotifications.values()) {
                        content.append("• ").append(title).append("\n");
                    }

                    alert.setContentText(content.toString());
                    alert.showAndWait();
                });

                //mark notifications as seen after displaying
                notificationDAO.markNotificationsAsSeen(new ArrayList<>(newNotifications.keySet()));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Image buildImage(@NotNull User user) {
        byte[] profilePictureData = user.getProfilePicture();

        if (profilePictureData != null && profilePictureData.length > 0) {
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(profilePictureData);
                return new Image(bis);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid image data");
                return getDefaultProfileImage();
            }
        } else {
            return getDefaultProfileImage();
        }
    }

    private Image getDefaultProfileImage() {
        return new Image(Objects.requireNonNull(getClass().getResource("/com/example/solvesphere/Images/userico.png")).toExternalForm());
    }

    private long extractUserID() {
        ServerCommunicator serverCommunicator = new ServerCommunicator();
        return serverCommunicator.fetchUserIdByUsernameAndEmail(user.getUsername(), user.getEmail());
    }

    private void checkForNewFriendRequests() {
        FriendDAO friendDAO = new FriendDAOImpl();
        Map<Long, String> newFriendRequests = friendDAO.getUnseenFriendRequests(extractUserID());

        if (!newFriendRequests.isEmpty()) {
            // update UI dynamically
            Platform.runLater(notificationsController::loadFriendRequests);
        }
    }

    /**
     * checks all comments in the database and deletes those with more than 40 downvotes.
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
