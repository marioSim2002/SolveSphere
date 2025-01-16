package com.example.solvesphere.DataBaseUnit;

import com.example.solvesphere.DBQueries.CommentsQueries;
import com.example.solvesphere.DBQueries.VoteQueries;
import com.example.solvesphere.UserData.Comment;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentDAOImpl implements CommentDAO {

    private final Connection connection;

    public CommentDAOImpl() {
        try {
            this.connection = DatabaseConnectionManager.getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to establish database connection", e);
        }
    }

    //add a new comment
    @Override
    public void addComment(Comment comment) {
        try (PreparedStatement stmt = connection.prepareStatement(CommentsQueries.INSERT_COMMENT)) {
            stmt.setLong(1, comment.getProblemId());
            stmt.setLong(2, comment.getUserId()); ////
            stmt.setString(3, comment.getContent());
            stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now())); //created at
            System.out.println(comment.getProblemId());
            System.out.println( );
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Comment getCommentById(long commentId) {
        Comment comment = null;
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM comments WHERE id = ?")) {
            stmt.setLong(1, commentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                comment = new Comment();
                comment.setId(rs.getLong("id"));
                comment.setProblemId(rs.getLong("problem_id"));
                comment.setUserId(rs.getLong("user_id"));
                comment.setContent(rs.getString("content"));
                comment.setCreatedAt(Timestamp.valueOf(rs.getTimestamp("created_at").toLocalDateTime()));
                comment.setUpvotes(rs.getInt("upvotes")); // Fetch upvotes
                comment.setDownvotes(rs.getInt("downvotes")); // Fetch downvotes
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return comment;
    }

    //get comments by problem ID
    @Override
    public List<Comment> getCommentsByProblemId(long problemId) {
        List<Comment> comments = new ArrayList<>();

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CommentsQueries.GET_COMMENTS_BY_PROBLEM_ID)) {
            stmt.setLong(1, problemId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Comment comment = new Comment();
                    comment.setId(rs.getLong("id"));
                    comment.setProblemId(rs.getLong("problem_id"));
                    comment.setUserId(rs.getLong("user_id"));
                    comment.setContent(rs.getString("content"));
                    comment.setCreatedAt(Timestamp.valueOf(rs.getTimestamp("created_at").toLocalDateTime()));
                    comment.setUpvotes(rs.getInt("upvotes")); // Fetch upvotes
                    comment.setDownvotes(rs.getInt("downvotes")); // Fetch downvotes
                    comments.add(comment);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching comments for problem ID: " + problemId, e);
        }
        return comments;
    }

    //delete a comment by its ID
    @Override
    public void deleteComment(long commentId) {
        try (PreparedStatement stmt = connection.prepareStatement(CommentsQueries.DELETE_COMMENT)) {
            stmt.setLong(1, commentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCommentCountByProblemId(long problemId) {
        try (PreparedStatement stmt = connection.prepareStatement(CommentsQueries.COUNT_COMMENT)) {
            stmt.setLong(1, problemId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; //return 0 if there's an error or no data found
    }
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
