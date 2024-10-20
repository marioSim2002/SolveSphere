package com.example.solvesphere.DataBaseUnit;

import com.example.solvesphere.DBQueries.UserQueries;
import com.example.solvesphere.SecurityUnit.PasswordHasher;
import com.example.solvesphere.UserData.Problem;
import com.example.solvesphere.UserData.User;

import java.sql.*;
import java.util.*;

public class UserDAOImpl implements UserDAO {

    @Override
    public User getUserById(int id) {
        User user = null;
        /// get DB connection
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UserQueries.GET_USER_VIA_ID)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                long userId = rs.getLong("id");
                Map<String, Integer> fieldsOfInterest = fetchFieldsOfInterest(userId);

                user = new User(
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"), // This is the hashed password
                        rs.getDate("date_of_birth").toLocalDate(),
                        rs.getString("country"),
                        fieldsOfInterest,
                        rs.getDate("registration_date").toLocalDate(),
                        rs.getString("profile_picture")
                );
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public User getUserByUsernameAndPassword(String username, String password) {
        User user = null;

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UserQueries.SELECT_USER_BY_USERNAME)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                long userId = rs.getLong("id");
                Map<String, Integer> fieldsOfInterest = fetchFieldsOfInterest(userId);

                user = new User(
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"), // This is the hashed password
                        rs.getDate("date_of_birth").toLocalDate(),
                        rs.getString("country"),
                        fieldsOfInterest,
                        rs.getDate("registration_date").toLocalDate(),
                        rs.getString("profile_picture")
                );

                // Compare the hashed password
                PasswordHasher hasher = new PasswordHasher();
                if (!hasher.verifyPassword(password, user.getPassword())) {
                    user = null; // password does not match, return null
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return user; // return the user if credentials are valid, otherwise null
    }


    @Override
    public User getUserByUserName(String username) {
        return null;
    }


    @Override
    public void addUser(User user) throws SQLException, ClassNotFoundException {
        /// todo
        // implement all methods that related to adding user data.
        long userId = -1;
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UserQueries.INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setDate(4, java.sql.Date.valueOf(user.getDateOfBirth()));
            stmt.setString(5, user.getCountry());
            stmt.setTimestamp(6, java.sql.Timestamp.valueOf(user.getRegistrationDate()));
            stmt.setString(7, user.getProfilePicture());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        userId = rs.getLong(1);  //retrieve the generated ID
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        addUserInterests(userId,user.getFieldsOfInterest());
    }
    @Override
    public boolean userExists(String username, String email) {
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UserQueries.SELECT_USER_BY_USERNAME_AND_EMAIL)) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            ResultSet rs = stmt.executeQuery(); // execute the q

            if (rs.next()) {
                return rs.getInt(1) > 0; // Return true if a user exists
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false; // user does not exist
    }

    @Override
    public User getUserByUsername(String username) {
        User user = null;
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UserQueries.SELECT_USER_BY_USERNAME)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Long id = rs.getLong("id");  // Assuming the ID is stored under the column 'id'
                Map<String, Integer> fieldsOfInterest = fetchFieldsOfInterest(id); // Hypothetical method to fetch interests
                List<Problem> problems = fetchProblems(id); // Hypothetical method to fetch problems

                user = new User(
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getDate("date_of_birth").toLocalDate(),
                        rs.getString("country"),
                        fieldsOfInterest,
                        rs.getDate("registration_date").toLocalDate(),
                        rs.getString("profile_picture")
                );
                user.setProblems(problems); // Assuming you have a setter for problems
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return user;
    }
    private Map<String, Integer> fetchFieldsOfInterest(long userId) throws SQLException, ClassNotFoundException {
        Map<String, Integer> fields = new HashMap<>();
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UserQueries.SELECT_USER_INTEREST)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String field = rs.getString("interest_name");
                int interestLevel = rs.getInt("priority_level");
                fields.put(field, interestLevel);
            }
        }
        return fields;
    }
    private List<Problem> fetchProblems(long userId) throws SQLException, ClassNotFoundException {
        List<Problem> problems = new ArrayList<>();
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM problems WHERE user_id = ?")) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Problem problem = new Problem(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        (int) rs.getLong("user_id"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getString("category"),
                        Arrays.asList(rs.getString("tags").split(",")) // Assuming tags are stored as a comma-separated values
                );
                problems.add(problem);
            }
        }
        return problems;
    }


    @Override
    public void addUserInterests(long userId, Map<String, Integer> interests) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DatabaseConnectionManager.getConnection();
            conn.setAutoCommit(false); // start transaction
            stmt = conn.prepareStatement(UserQueries.INSERT_USER_INTEREST);

            for (Map.Entry<String, Integer> entry : interests.entrySet()) {
                stmt.setLong(1, userId);
                stmt.setString(2, entry.getKey());
                stmt.setInt(3, entry.getValue());
                stmt.executeUpdate();
            }

            conn.commit(); // Commit the transaction
        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback in case of error
                } catch (SQLException e) {
                    System.err.println("Error rolling back transaction");
                    e.printStackTrace();
                }
            }
            throw ex; // Re-throw the exception
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }
}

