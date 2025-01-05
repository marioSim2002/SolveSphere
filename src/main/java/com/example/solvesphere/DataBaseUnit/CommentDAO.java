package com.example.solvesphere.DataBaseUnit;

import com.example.solvesphere.UserData.Comment;

import java.util.List;

public interface CommentDAO {
    void addComment(Comment comment);  // add a new comment

    List<Comment> getCommentsByProblemId(long problemId);  // Retrieve comments for a problem
    void deleteComment(long commentId);  // Delete a specific comment
    int getCommentCountByProblemId(long problemId);  // Get comment count for a problem

    Comment getCommentById(long id);
}
