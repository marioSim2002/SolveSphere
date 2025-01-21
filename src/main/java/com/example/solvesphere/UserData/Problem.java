package com.example.solvesphere.UserData;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Problem implements Serializable {
    @Serial
    private static final long serialVersionUID = 7765231569024119311L;
    private long id;
    private String title;
    private String description;
    private long userId;
    private LocalDateTime createdAt;
    private String category;
    private final boolean isAgeRestricted;
    private List<String> tags;


    public Problem(long id, String title, String description, long userId, LocalDateTime createdAt,String category, Boolean isAgeRestricted,List<String> tags) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.createdAt = createdAt;
        this.category = category;
        this.isAgeRestricted = isAgeRestricted;
        this.tags = tags;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {this.id = id;}

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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
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

    public boolean isAgeRestricted(){return isAgeRestricted;}

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
