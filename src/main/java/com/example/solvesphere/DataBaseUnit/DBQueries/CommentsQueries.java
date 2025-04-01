package com.example.solvesphere.DataBaseUnit.DBQueries;

public class CommentsQueries {
    public static final String INSERT_COMMENT = "INSERT INTO comments (problem_id, user_id, content, created_at) VALUES (?, ?, ?, ?)";
    public static final String GET_COMMENTS_BY_PROBLEM_ID = "SELECT * FROM comments WHERE problem_id = ?";
   public static final String DELETE_COMMENT = "DELETE FROM comments WHERE id = ?";
   public static final String COUNT_COMMENT = "SELECT COUNT(*) AS count FROM comments WHERE problem_id = ?";
   public static final String GET_ALL_COMMENTS_QUERY = "SELECT id, problem_id, user_id, content, created_at, upvotes, downvotes, is_solution FROM comments";

   public static final String GET_COMMENT_BY_ID = "SELECT * FROM comments WHERE id = ?";

}
