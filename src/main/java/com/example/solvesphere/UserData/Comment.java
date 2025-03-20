package com.example.solvesphere.UserData;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
public class Comment implements Serializable {
    private long id;
    private long problemId;
    private long userId;
    private String content;
    private Timestamp createdAt;
    private int upvotes;
    private int downvotes;
    private boolean isSolution; // New field to mark the comment as a solution

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProblemId() {
        return problemId;
    }

    public void setProblemId(long problemId) {
        this.problemId = problemId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public void setDownvotes(int downvotes) {
        this.downvotes = downvotes;
    }

    public boolean isSolution() {
        return isSolution;
    }

    public void setSolution(boolean solution) {
        isSolution = solution;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", problemId=" + problemId +
                ", userId=" + userId +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", upvotes=" + upvotes +
                ", downvotes=" + downvotes +
                ", isSolution=" + isSolution +
                '}';
    }
}
