package com.example.solvesphere.DataBaseUnit;

import com.example.solvesphere.DBQueries.ProblemQueries;
import com.example.solvesphere.UserData.Problem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ProblemDAOImpl implements ProblemDAO {


    @Override
    public List<Problem> getProblemsByUserInterest(Map<String, Integer> userInterests) {
        List<Problem> problems = new ArrayList<>();
        if (userInterests.isEmpty()) {
            return problems;  // Return empty if no interests
        }

        // extract the keys from the map to use as categories
        List<String> categories = new ArrayList<>(userInterests.keySet());
        String inSql = String.join(",", Collections.nCopies(categories.size(), "?"));
        String sql = "SELECT * FROM problems WHERE category IN (" + inSql + ")";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the values for each placeholder
            int index = 1;
            for (String category : categories) {
                stmt.setString(index++, category);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                problems.add(mapResultSetToProblem(rs));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return problems;
    }


    @Override
    public List<Problem> searchProblems(String keyword) {
        List<Problem> problems = new ArrayList<>();
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(ProblemQueries.SEARCH_PROBLEMS)) {

            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                problems.add(mapResultSetToProblem(rs));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return problems;
    }

    //include fetching tags associated with each problem
    private Problem mapResultSetToProblem(ResultSet rs) throws SQLException {
        List<String> tags = getTagsForProblem(rs.getInt("id"));
        boolean isAgeRestricted = rs.getBoolean("is_age_restricted");  // Fetch the boolean value from ResultSet

        return new Problem(
                rs.getLong("id"),  // Changed from getInt to getLong to match your constructor
                rs.getString("title"),
                rs.getString("description"),
                rs.getInt("user_id"),
                rs.getTimestamp("created_at").toLocalDateTime(),  // Convert SQL Timestamp to LocalDateTime
                rs.getString("category"),
                isAgeRestricted,  // Add this to match your constructor
                tags
        );
    }

    // Helper method to get tags for a problem
    private List<String> getTagsForProblem(int problemId) {
        List<String> tags = new ArrayList<>();
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(ProblemQueries.SELECT_PROBLEM_BY_ID)) {

            stmt.setInt(1, problemId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tags.add(rs.getString("tag_name"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return tags;
    }

    @Override
    public List<Problem> fetchAllProblems() {
            List<Problem> problems = new ArrayList<>();
            try (Connection conn = DatabaseConnectionManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(ProblemQueries.SELECT_ALL_PROBLEMS)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    problems.add(mapResultSetToProblem(rs));
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return problems;
    }

    @Override
    public List<Problem> getProblemsPostedByCurrentUser(long userId) {
        List<Problem> problems = new ArrayList<>();
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(ProblemQueries.SELECT_PROBLEM_BY_USER_ID)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                problems.add(mapResultSetToProblem(rs));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return problems;
    }

    @Override
    public List<Problem> getProblemsByCountry(String country) {
        List<Problem> problems = new ArrayList<>();
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(ProblemQueries.SELECT_PROBLEMS_IN_USER_COUNTRY)){

            stmt.setString(1, country);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                problems.add(mapResultSetToProblem(rs));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return problems;
    }
    
@Override
    public boolean addProblem(Problem problem) {
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(ProblemQueries.SELECT_INSERT_PROBLEM_SQL)) {

            // Set values for the placeholders in the SQL statement
            stmt.setString(1, problem.getTitle());
            stmt.setString(2, problem.getDescription());
            stmt.setLong(3, problem.getUserId());
            stmt.setString(4, problem.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            stmt.setString(5, problem.getCategory());

            // Join tags list into a comma-separated string for storage
            String tagsString = String.join(",", problem.getTags());
            stmt.setString(6, tagsString);

            // Execute update and return true if insertion was successful
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
