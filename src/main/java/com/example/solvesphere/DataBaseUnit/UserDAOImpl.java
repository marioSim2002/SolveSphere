package com.example.solvesphere.DataBaseUnit;

import com.example.solvesphere.DBQueries.UserQueries;
import com.example.solvesphere.SecurityUnit.PasswordHasher;
import com.example.solvesphere.UserData.Problem;
import com.example.solvesphere.UserData.User;

import java.sql.*;
import java.util.*;

public class UserDAOImpl implements UserDAO {

    @Override
    public User getUserById(long id) {
        User user = null;
        /// get DB connection
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UserQueries.GET_USER_VIA_ID)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getDate("date_of_birth").toLocalDate(),
                        rs.getString("country"),
                        new HashMap<>(), //populate fields of interest
                        rs.getDate("registration_date").toLocalDate(),
                        rs.getString("profile_picture")
                );
                user.setId(rs.getLong("id")); // Set the ID
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
            System.out.println("Fetching user for username: " + username);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                long userId = rs.getLong("id");
                Map<String, Integer> fieldsOfInterest = fetchFieldsOfInterest(userId);
                String storedHashedPassword = rs.getString("password"); // get the hashed password from the database

                user = new User(
                        rs.getString("username"),
                        rs.getString("email"),
                        storedHashedPassword, //use the stored hashed password
                        rs.getDate("date_of_birth").toLocalDate(),
                        rs.getString("country"),
                        fieldsOfInterest,
                        rs.getDate("registration_date").toLocalDate(),
                        rs.getString("profile_picture")
                );

                //*** compare the plain password with the stored hashed password ***//87
                PasswordHasher hasher = new PasswordHasher();
                if (!hasher.verifyPassword(password, storedHashedPassword)) {
                    System.out.println("Password verification failed");
                    user = null; // Password does not match
                } else {
                    System.out.println("Password verification succeeded");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return user; //return the user if credentials are valid, otherwise null
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
                        user.setId(userId); // set to gen ID
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        addUserInterests(userId,user.getFieldsOfInterest());
    }


    @Override
    public void setUserActivityStatus(long userId, boolean isActive) throws SQLException {
        String sql = "UPDATE users SET ACTIVE = ? WHERE id = ?";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, isActive);
            stmt.setLong(2, userId);
            stmt.executeUpdate();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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
                long id = rs.getLong("id");  // Assuming the ID is stored under the column 'id'
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
    @Override
    public Map<String, Integer> fetchFieldsOfInterest(long userId) throws SQLException, ClassNotFoundException {
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
             PreparedStatement stmt = conn.prepareStatement(UserQueries.GET_PROBLEMS_BY_USER_ID)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Problem problem = new Problem(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getInt("user_id"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getString("category"),
                        rs.getBoolean("is_age_restricted"),
                        Arrays.asList(rs.getString("tags").split(","))
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

            conn.commit(); //commit the transaction
        } catch (SQLException ex) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback in case of error
                } catch (SQLException e) {
                    System.err.println("Error rolling back transaction");
                    e.printStackTrace();
                }
            }
            throw ex;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }


    /// once logged in , a method to get user ID for further operations ///
    @Override
    public Long getUserIdByUsernameAndEmail(String username, String email) {
        Long userId = null;
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UserQueries.SELECT_USER_ID_BY_USERNAME_AND_EMAIL)) {

            stmt.setString(1, username);
            stmt.setString(2, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    userId = rs.getLong("id");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Error fetching user ID: " + e.getMessage());
            e.printStackTrace();
        }
        return userId;
    }

}

