package com.example.solvesphere.DBQueries;

public class SolutionQueries {

    public static final String INSERT_SOLUTION_SQL =
            "INSERT INTO solutions (problem_id, user_id, title, description, created_at, rating) VALUES (?, ?, ?, ?, ?, ?)";
}

