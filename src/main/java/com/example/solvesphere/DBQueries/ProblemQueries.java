package com.example.solvesphere.DBQueries;

public class ProblemQueries {
    public static final String SELECT_PROBLEMS_BY_INTEREST = "SELECT * FROM problems WHERE category IN (?)";
    public static final String SELECT_PROBLEM_BY_ID = "SELECT * FROM problems WHERE id = ?";
    public static final String SEARCH_PROBLEMS = "SELECT * FROM problems WHERE title LIKE ? OR description LIKE ?";

    public static final String FETCH_PROBLEMS_NO_INTEREST ="SELECT p.id, p.title, p.description, p.category, p.created_at, " +
            "GROUP_CONCAT(t.name) AS tags " +
            "FROM problems p " +
            "LEFT JOIN problem_tags pt ON p.id = pt.problem_id " +
            "LEFT JOIN tags t ON t.id = pt.tag_id " +
            "GROUP BY p.id";
}
