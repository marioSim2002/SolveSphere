package com.example.solvesphere.Algos;

import com.example.solvesphere.DataBaseUnit.*;
import com.example.solvesphere.ServerUnit.ServerCommunicator;
import com.example.solvesphere.UserData.Problem;
import com.example.solvesphere.UserData.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SuggestionsAlgo {
    /*
     * class to improve suggestions to user :
     * combine user interests with favorites posts to adjust priority levels foreach topic
     * STEPS :
     * 1. check user interests topics
     * 2. check user favorites topics
     * 3. if same topic found in both adjust priority
     * */
    public void updateInterestPriorityBasedOnFavorites(long userId) {
        try {
            UserDAO userDAO = new UserDAOImpl();
            FavoritesDAO favoritesDAO = new FavoritesDAOImpl();
            ProblemDAO problemDAO = new ProblemDAOImpl();

            //fetch user interests (Map<String, Integer>) and favorite problems (List<Problem>)
            Map<String, Integer> interests = userDAO.fetchFieldsOfInterest(userId);
            List<Long> favProblemIds = favoritesDAO.getFavoriteProblemsByUser(userId);
            List<Problem> favProblems = new ArrayList<>();

            for (Long id : favProblemIds) {
                favProblems.add(problemDAO.getProblemById(id));
            }

            // loop through interests and favorite problems
            for (Map.Entry<String, Integer> entry : interests.entrySet()) {
                String interestCategory = entry.getKey();
                int currentPriority = entry.getValue();

                for (Problem problem : favProblems) {
                    if (problem.getCategory().equals(interestCategory)) {
                        // Increase priority by 2 if category matches a favorite problem
                        interests.put(interestCategory, currentPriority + 2);
                    }
                }
            }

            // update the new interest priorities in the database
            userDAO.updateUserInterests(userId, interests);
            System.out.println("INTERESTS UPDATED!");

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
