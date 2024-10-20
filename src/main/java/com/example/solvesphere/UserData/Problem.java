package com.example.solvesphere.UserData;

import java.io.Serializable;
import java.time.LocalDateTime;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Problem implements Serializable {
    private int id;
    private String title;
    private String description;
    private int userId;
    private LocalDateTime createdAt;
    private String category;
    private List<String> tags;


    public Problem(int id, String title, String description, int userId, LocalDateTime createdAt,String category, List<String> tags) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.createdAt = createdAt;
        this.category = category;
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
