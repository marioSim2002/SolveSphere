package com.example.solvesphere.UserData;

import java.sql.Timestamp;

public class Comment {
    private long id;
    private long problemId;
    private long userId;
    private String content;
    private Timestamp createdAt;

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getProblemId() { return problemId; }
    public void setProblemId(long problemId) { this.problemId = problemId; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
