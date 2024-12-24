package com.example.solvesphere.DBQueries;

public class CommentsQueries {
    public static final String INSERT_COMMENT = "INSERT INTO comments (problem_id, user_id, content, created_at) VALUES (?, ?, ?, ?)";
    public static final String GET_COMMENTS_BY_PROBLEM_ID = "SELECT * FROM comments WHERE problem_id = ?";
   public static final String DELETE_COMMENT = "DELETE FROM comments WHERE id = ?";
}
