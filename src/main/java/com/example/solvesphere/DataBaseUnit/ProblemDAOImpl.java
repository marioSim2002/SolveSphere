package com.example.solvesphere.DataBaseUnit;

import com.example.solvesphere.DBQueries.ProblemQueries;
import com.example.solvesphere.UserData.Problem;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class ProblemDAOImpl implements ProblemDAO {


    @Override
    public List<Problem> getProblemsByUserInterest(Map<String, Integer> userInterests) {
        List<Problem> problems = new ArrayList<>();
        if (userInterests.isEmpty()) {
            return problems;
        }

        List<String> categories = new ArrayList<>(userInterests.keySet());
        String inSql = String.join(",", Collections.nCopies(categories.size(), "?"));
        String sql = "SELECT * FROM problems WHERE category IN (" + inSql + ")";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // set the values for each placeholder
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

            stmt.setString(1, "%" + keyword + "%"); // title
            stmt.setString(2, "%" + keyword + "%"); // desc

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
      //  boolean isAgeRestricted

        return new Problem(
                rs.getLong("id"),  // Changed from getInt to getLong to match your constructor
                rs.getString("title"),
                rs.getString("description"),
                rs.getInt("user_id"),
                rs.getTimestamp("created_at").toLocalDateTime(),  // Convert SQL Timestamp to LocalDateTime
                rs.getString("category"),
                rs.getBoolean("is_age_restricted"),
                tags
        );
    }

    // Helper method to get tags for a problem
    private List<String> getTagsForProblem(int problemId) {
        List<String> tags = new ArrayList<>();
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(ProblemQueries.SELECT_PROBLEM_TAGS)) {
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
         PreparedStatement stmt = conn.prepareStatement(ProblemQueries.INSERT_PROBLEM_SQL)) {

        stmt.setString(1, problem.getTitle());
        stmt.setString(2, problem.getDescription());
        stmt.setLong(3, problem.getUserId());
        stmt.setTimestamp(4, Timestamp.valueOf(problem.getCreatedAt()));
        stmt.setString(5, problem.getCategory());
        stmt.setBoolean(6, problem.isAgeRestricted()); // Correctly bind the isAgeRestricted field
//        String tagsString = String.join(",", problem.getTags());
//        stmt.setString(7, tagsString); // not yet implemented

        int affectedRows = stmt.executeUpdate();
        return affectedRows > 0;
    } catch (SQLException | ClassNotFoundException e) {
        e.printStackTrace();
        return false;
    }
}

    @Override
    public Problem getProblemById(long problemId) {
        Problem problem = null;
        String query = "SELECT * FROM problems WHERE id = ?";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, problemId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Construct a Problem object
                // Adjust column names/types as needed
                long id = rs.getLong("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                long userId = rs.getLong("user_id");
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
                String category = rs.getString("category");
                boolean isAgeRestricted = rs.getBoolean("is_age_restricted");
                List<String> tags = null; // or parse from DB if stored

                problem = new Problem(id, title, description, userId, createdAt, category, isAgeRestricted, tags);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return problem;
    }
}
