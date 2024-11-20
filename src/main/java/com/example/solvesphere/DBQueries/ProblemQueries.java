package com.example.solvesphere.DBQueries;

public class ProblemQueries {
    public static final String SELECT_PROBLEM_BY_ID = "SELECT * FROM problems WHERE id = ?";
    public static final String SEARCH_PROBLEMS = "SELECT * FROM problems WHERE title LIKE ? OR description LIKE ?";
    public static final String SELECT_ALL_PROBLEMS ="SELECT * FROM problems";

    public static final String SELECT_PROBLEM_BY_USER_ID = "SELECT * FROM problems WHERE user_id = ?";

    public static final String SELECT_PROBLEMS_IN_USER_COUNTRY = "SELECT p.*\n" +
            "FROM problems p\n" +
            "JOIN users u ON p.user_id = u.id\n" +
            "WHERE u.country = ?;\n";

     public static final String SELECT_INSERT_PROBLEM_SQL =
            "INSERT INTO problems (title, description, user_id, created_at, category, tags) VALUES (?, ?, ?, ?, ?, ?)";
}
