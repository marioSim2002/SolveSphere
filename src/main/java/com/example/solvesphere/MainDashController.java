package com.example.solvesphere;
import com.example.solvesphere.DataBaseUnit.*;
import com.example.solvesphere.ServerUnit.ServerCommunicator;
import com.example.solvesphere.UserData.Problem;
import com.example.solvesphere.UserData.SessionManager;
import com.example.solvesphere.UserData.User;
import com.example.solvesphere.ValidationsUnit.Inspector;
import javafx.application.Platform;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.util.List;

public class MainDashController {
    private ScheduledExecutorService problemUpdater;

    private Stage profileStage;
    private Stage settingsStage;
    private Stage globalStatsStage;
    @FXML
    private ImageView notificationImg;
    @FXML
    private Label mostPostedCategoryLabel;
    @FXML
    private ImageView profileImg;
    @FXML
    private VBox problemListContainer;
    @FXML
    private ComboBox<String> filterOptions;
    @FXML
    private TextField searchField;
    private User currentUser;  //connected user e.g current user
    @FXML
    private TextField chatInputField;
    @FXML
    private ListView<String> chatListView;
    private final String fetch_problems_cmd = "FETCH_PROBLEMS";
   private long currentUserId ;

    public void initUserData(User user) {
        System.out.println("SM TEST : "+SessionManager.getCurrentUser().getId());
        this.currentUser = user;
        this.currentUserId = SessionManager.getCurrentUser().getId();
        NotificationsController notificationsController = new NotificationsController();
        buildImage(currentUser);
        fetchAndDisplayProblems();
        startAutoRefreshProblems();  // method to start auto-refreshing problems
        initializeChat();
        profileImg.setImage(buildImage(user));
        Inspector inspector = new Inspector(this,notificationsController,currentUser);
        inspector.startInspection();
    }

    private void stopAutoRefresh() {
        if (problemUpdater != null && !problemUpdater.isShutdown()) {
            problemUpdater.shutdown();
            problemUpdater = null;
        }
    }

    private void startAutoRefreshProblems() {
        problemUpdater = Executors.newSingleThreadScheduledExecutor();
        problemUpdater.scheduleAtFixedRate(() -> {
            Platform.runLater(this::fetchAndDisplayProblems);
        }, 0, 5, TimeUnit.SECONDS); // every 5 seconds
    }

    // updates UI when called //
    private void envokeAllProblemsDisplay() {
        ServerCommunicator serverCommunicator = new ServerCommunicator();
        List<Problem> allProblems = serverCommunicator.sendFetchProblemsRequest(fetch_problems_cmd);
        Platform.runLater(() -> {
            System.out.println("Updating UI with new problem/s data.");
            displayProblems(allProblems);
        });
    }


    public void displayProblems(List<Problem> problems) {
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
                        CommentDAO commentDAO = new CommentDAOImpl();
                        int count = commentDAO.getCommentCountByProblemId(problem.getId());
                        controller.setProblemData(problem,currentUser,count,problemPublisherName);
                        System.out.println("comments count for problem id "+problem.getId()+" is "+count);
                        problemListContainer.getChildren().add(problemItem);

                    } catch (IOException e) {e.printStackTrace();}
                }
        }


    public void fetchAndDisplayProblems() {
        ServerCommunicator serverCommunicator = new ServerCommunicator();
        List<Problem> allProblems = serverCommunicator.sendFetchProblemsRequest(fetch_problems_cmd);
        Platform.runLater(() -> displayProblems(allProblems));  // ensure UI updates are done on the JavaFX Application Thread
    }

    public boolean checkCurrentUserAgainstPublisher(String emailA , String emailB){
        System.out.println(emailA+" " + emailB); //debug
        return emailB.equals(emailA);}

    @FXML
    private void handleFilterSelection() {
        String selectedFilter = filterOptions.getValue();
        System.out.println("Selected Filter: " + selectedFilter);
        applyFilter(selectedFilter);
    }

    // sends request and builds data according to filter
    private void applyFilter(String filter) {
        if (currentUser == null) {
            return;
        }

        ProblemDAO problemDAO = new ProblemDAOImpl();
        FriendDAO friendDAO = new FriendDAOImpl();
        long userId = SessionManager.getCurrentUser().getId();
        stopAutoRefresh(); // stop auto-refreshing the problem list

        List<Problem> problems = new ArrayList<>(); //an empty list if no case is matched

        switch (filter) {
            case "No Filter":
                envokeAllProblemsDisplay();
                return; // No need to continue execution

            case "Interests Related":
                problems = problemDAO.getProblemsByUserInterest(currentUser.getFieldsOfInterest());
                break;

            case "In Your Country":
                problems = problemDAO.getProblemsByCountry(currentUser.getCountry());
                System.out.println(currentUser.getCountry());
                break;

            case "Posted By You":
                problems = problemDAO.getProblemsPostedByCurrentUser(userId);
                break;

            case "Posted By Friends":
                List<Long> friendIds = friendDAO.getFriendIds(userId); // fetch friend IDs
                problems = problemDAO.getProblemsPostedByUsers(friendIds); // fetch problems posted by friends
                break;

            default:
                System.out.println("Unknown filter option: " + filter);
                return; // exit the method if the filter is invalid
        }

        displayProblems(problems); // display the filtered problems
    }


    public void addProblem() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addProblem.fxml"));
            HBox addProblemRoot = loader.load();  // change VBox to HBox

            //new problem scene
            Scene scene = new Scene(addProblemRoot);
            Stage stage = new Stage();
            stage.setTitle("Add New Problem");
            stage.setScene(scene);
            stage.setResizable(false);
           AddProblemController controller = loader.getController();
           controller.initialize(currentUser);
            stage.setOnHidden(event -> fetchAndDisplayProblems());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void searchForInput() {
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            envokeAllProblemsDisplay();
            return;
        }
        ProblemDAO problemDAO = new ProblemDAOImpl();

        // searching for suitable problems via query
        List<Problem> matchingProblems = problemDAO.searchProblems(searchText);
        if (matchingProblems.isEmpty()) {
            System.out.println("No problems were found according to the search: " + searchText);
            problemListContainer.getChildren().clear(); //clearing the problem list - prevents duplicates

        } else {
            // presenting the appropriate problems
            displayProblems(matchingProblems);
        }
    }

    // method converts the path(string) to an image
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

    public void setProfileImage(Image image){
            this.profileImg.setImage(image);
    }

    private Image getDefaultProfileImage() {
        return new Image(Objects.requireNonNull(getClass().getResource("/com/example/solvesphere/Images/userico.png")).toExternalForm());
    }



    private void initializeChat() {
        ScheduledExecutorService chatUpdater = Executors.newSingleThreadScheduledExecutor();
        chatUpdater.scheduleAtFixedRate(this::updateChat, 0, 1, TimeUnit.SECONDS);
    }

    //send message request to the server and update UIO
    @FXML
    private void sendMessage() {
        String message = chatInputField.getText().trim();
        if (!message.isEmpty()) {
            ServerCommunicator communicator = ServerCommunicator.getInstance();
            String response = communicator.sendChatMessage(currentUser.getUsername(), message);
            System.out.println(response);
            chatInputField.clear();
        }
    }

    private void updateChat() {
        ServerCommunicator communicator = ServerCommunicator.getInstance();
        List<String> chatMessages = communicator.fetchChatMessages();

        Platform.runLater(() -> {
            chatListView.getItems().clear();
            chatListView.getItems().addAll(chatMessages);
        });
    }


    @FXML
    public void onProfileClick() {
        try {
            if (profileStage != null && profileStage.isShowing()) {
                profileStage.toFront();  // if already open ,bring the existing window to the front
                return;
            }
            // else open
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ProfileDetails.fxml"));
            Parent root = loader.load();

            ProfileTabbedController controller = loader.getController();
            controller.initialize(currentUser);

            //create a new stage and store reference
            profileStage = new Stage();
            profileStage.setTitle("User Profile");
            profileStage.setScene(new Scene(root, 800, 600));
            profileStage.show();

            //add listener to detect when the stage is closed
            profileStage.setOnCloseRequest(event -> profileStage = null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /* 1 checks all opened stages
     * 2 close all open stages
     * 3 terminate running tasks
     * 4 send user to login page  */
    public void onLogoutClick() throws SQLException {
        if (problemUpdater != null && !problemUpdater.isShutdown()) {
            problemUpdater.shutdown();
        }
        Stage[] openStages = {profileStage, settingsStage, globalStatsStage};

        for (int i = 0; i < openStages.length; i++) {
            if (openStages[i] != null) {
                openStages[i].close();
                openStages[i] = null; //clear reference to avoid memory leaks
            }
        }
        Stage stage = (Stage) searchField.getScene().getWindow();  //the current window
        stage.close();
        UserDAO userDAO = new UserDAOImpl();
        userDAO.setUserActivityStatus(currentUserId,"INACTIVE");//update on DB
        currentUser.setActive(false);//update on app
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = loader.load();
            Stage loginStage = new Stage();
            loginStage.setTitle("SolveSphere - Login");
            loginStage.setScene(new Scene(root, 600, 400));
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void onSettingsClick() {
        try {
            if (settingsStage != null && settingsStage.isShowing()) {
                settingsStage.toFront();
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserDetails.fxml"));
            Parent root = loader.load();
            UserDetailsController controller = loader.getController();
            controller.setCurrentUser(currentUser);

            settingsStage = new Stage();
            settingsStage.setTitle("Profile Page");
            settingsStage.setScene(new Scene(root));
            settingsStage.show();

            settingsStage.setOnCloseRequest(event -> settingsStage = null);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading ProfilePage.fxml");
        }
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

    public void updateMostPostedCategoryLabel(String mostPostedCategory, int maxCount) {
        Platform.runLater(() -> {
            mostPostedCategoryLabel.setText("Trending topics \uD83D\uDD25 : " + mostPostedCategory + " (" + maxCount + " posts)");
        });
    }

    @FXML
    public void onViewGlobalStatsClick() {

        if (globalStatsStage == null || !globalStatsStage.isShowing()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("GlobalStats.fxml"));
                Parent root = loader.load();

                globalStatsStage = new Stage();
                globalStatsStage.setTitle("Detailed Statistics");
                globalStatsStage.setScene(new Scene(root, 800, 600));
                globalStatsStage.show();

                globalStatsStage.setOnCloseRequest(event -> globalStatsStage = null);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            globalStatsStage.toFront();
        }
    }

    @FXML
    private void onNotificationClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NotificationView.fxml"));

            Parent root = loader.load();

            NotificationsController controller = loader.getController();
            controller.initialize(currentUserId); //pass current user ID

            Stage notificationStage = new Stage();
            notificationStage.initModality(Modality.APPLICATION_MODAL); //stays on top
            notificationStage.setTitle("Notifications");
            notificationStage.setScene(new Scene(root, 340, 420));
            notificationStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onPeopleClick() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PeoplePage.fxml"));
        Parent root = loader.load();

        PeoplePageController controller = loader.getController();
        controller.initialize(currentUserId,currentUser); //pass current user ID
        Stage discoverPeopleSt = new Stage();
        discoverPeopleSt.setTitle("Discover people");
        discoverPeopleSt.setScene(new Scene(root, 650, 650));
        discoverPeopleSt.show();
    }

    public void onAdminProblemsClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminProblem.fxml"));

            Parent root = loader.load();

            AdminProblemsController controller = loader.getController();
            controller.initialize(); // current user id to pass.

            Stage notificationStage = new Stage();
            notificationStage.initModality(Modality.APPLICATION_MODAL); //stays on top
            notificationStage.setTitle("Admin problems");
            notificationStage.setScene(new Scene(root, 340, 420));
            notificationStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onPollsClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GlobalPolls.fxml"));
            Parent root = loader.load();

            Stage pollStage = new Stage();
            pollStage.initModality(Modality.APPLICATION_MODAL);
            pollStage.setTitle("Global Anonymous Polls");
            pollStage.setScene(new Scene(root, 340, 420));
            pollStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
