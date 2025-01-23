package com.example.solvesphere.DataBaseUnit;

import com.example.solvesphere.UserData.Comment;

import java.util.List;

public interface CommentDAO {
    void addComment(Comment comment);  // add a new comment

    List<Comment> getCommentsByProblemId(long problemId);  // Retrieve comments for a problem
    void deleteComment(long commentId);  // delete a specific comment
    int getCommentCountByProblemId(long problemId);  //get comment count for a problem

    Comment getCommentById(long id);

    void markAsSolution(long commentId,boolean req);

    Comment getSolutionForProblem(long problemId);
}
