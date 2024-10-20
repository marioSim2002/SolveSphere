package com.example.solvesphere.DBQueries;

public class ProblemQueries {
    public static final String SELECT_PROBLEMS_BY_INTEREST = "SELECT * FROM problems WHERE interest IN (?)";
    public static final String SELECT_PROBLEM_BY_ID = "SELECT * FROM problems WHERE id = ?";
    public static final String SEARCH_PROBLEMS = "SELECT * FROM problems WHERE title LIKE ? OR description LIKE ?";
}
