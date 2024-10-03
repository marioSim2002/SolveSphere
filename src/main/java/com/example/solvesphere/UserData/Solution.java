package com.example.solvesphere.UserData;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Solution implements Serializable {
    private int id;
    private int problemId;
    private int userId;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private int rating;

    // Constructor
    public Solution(int id, int problemId, int userId, String title, String description, LocalDateTime createdAt, int rating) {
        this.id = id;
        this.problemId = problemId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.rating = rating;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProblemId() { return problemId; }
    public void setProblemId(int problemId) { this.problemId = problemId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getTitle() { return title; }   // Getter for title
    public void setTitle(String title) { this.title = title; }  // Setter for title

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Solution{" +
                "id=" + id +
                ", problemId=" + problemId +
                ", userId=" + userId +
                ", title='" + title + '\'' +  // Include title in the toString method
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}