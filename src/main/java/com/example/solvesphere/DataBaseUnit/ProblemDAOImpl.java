package com.example.solvesphere.DataBaseUnit;

import com.example.solvesphere.DataBaseUnit.DBQueries.ProblemQueries;
import com.example.solvesphere.UserData.AdminProblem;
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
            stmt.setString(3, "%" + keyword + "%"); // category


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
        List<String> tags = getTagsByProblemId(rs.getInt("id"));
        System.out.println(tags);
        //  boolean isAgeRestricted

        return new Problem(
                rs.getLong("id"),  // Changed from getInt to getLong to match your constructor
                rs.getString("title"),
                rs.getString("description"),
                rs.getInt("user_id"),
                rs.getTimestamp("created_at").toLocalDateTime(),  // Convert SQL Timestamp to LocalDateTime
                rs.getString("category"),
                rs.getBoolean("is_age_restricted")
        );
    }

    @Override
    public List<String> getTagsByProblemId(long problemId) {
        List<String> tags = new ArrayList<>();

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT tag_id FROM problem_tags WHERE problem_id = ?")) {

            stmt.setLong(1, problemId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tags.add(rs.getString("tag_id"));  // Assuming tag_id holds tag names
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
    public List<Problem> getProblemsPostedByUsers(List<Long> userIds) {
        List<Problem> problems = new ArrayList<>();
        if (userIds.isEmpty()) return problems; // Return empty list if no friends

        StringBuilder sql = new StringBuilder("SELECT * FROM problems WHERE user_id IN (");
        sql.append("?,".repeat(userIds.size()));
        sql.setLength(sql.length() - 1); // Remove trailing comma
        sql.append(")");

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < userIds.size(); i++) {
                stmt.setLong(i + 1, userIds.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                problems.add(new Problem(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getLong("user_id"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getString("category"),
                        rs.getBoolean("is_age_restricted")
                ));
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
             PreparedStatement stmt = conn.prepareStatement(ProblemQueries.SELECT_PROBLEMS_IN_USER_COUNTRY)) {

            stmt.setString(1, country.toLowerCase());
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

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean DeleteProblem(long problemId) {
        boolean isDeleted = false;

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(ProblemQueries.DELETE_PROBLEM)) {

            stmt.setLong(1, problemId);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                isDeleted = true;
                System.out.println("Problem with ID " + problemId + " deleted successfully.");
            } else {
                System.out.println("No problem found with ID " + problemId + ".");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return isDeleted;
    }


    @Override
    public Problem getProblemById(long problemId) {
        Problem problem = null;

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(ProblemQueries.GET_PROBLEM_BY_ID)) {

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
                List<String> tags = getTagsByProblemId(problemId);

                problem = new Problem(id, title, description, userId, createdAt, category, isAgeRestricted);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return problem;
    }

    @Override
    public Map<String, Integer> getProblemCategoryCounts() {
        Map<String, Integer> categoryCounts = new HashMap<>();

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(ProblemQueries.GET_CATEGORY_COUNTS_QUERY);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String category = rs.getString("category");
                int count = rs.getInt("count");
                categoryCounts.put(category, count);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching problem category counts", e);
        }

        return categoryCounts;
    }


    @Override
    public List<Problem> findSimilarProblemsByTitleAndDescription(String titleInput, String descInput) throws ClassNotFoundException {
        List<Problem> similarProblems = new ArrayList<>();

        // Start SQL query
        StringBuilder sql = new StringBuilder("SELECT id, user_id, title, description, category, created_at, is_age_restricted FROM problems WHERE 1=1");

        List<String> params = new ArrayList<>();

        if (!titleInput.isEmpty()) {
            sql.append(" AND title LIKE ?");
            params.add("%" + titleInput + "%");  //  LIKE for partial match
        }
        if (!descInput.isEmpty()) {
            sql.append(" AND description LIKE ?");
            params.add("%" + descInput + "%");
        }

        sql.append(" ORDER BY created_at DESC LIMIT 5");

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            // Set parameters dynamically
            for (int i = 0; i < params.size(); i++) {
                stmt.setString(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Problem problem = new Problem(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getInt("user_id"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getString("category"),
                        rs.getBoolean("is_age_restricted"));
                similarProblems.add(problem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return similarProblems;
    }

    @Override
    public List<AdminProblem> getAdminProblems() throws SQLException, ClassNotFoundException {
        List<AdminProblem> problems = new ArrayList<>();
        try (Connection conn = AdminsDBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(ProblemQueries.GET_ADMIN_PROBLEMS);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                problems.add(new AdminProblem(
                        rs.getLong("id"),
                        rs.getInt("admin_id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("category"),
                        rs.getTimestamp("created_at"),
                        rs.getBoolean("is_age_restricted")
                ));
            }
        }
        return problems;
    }
}