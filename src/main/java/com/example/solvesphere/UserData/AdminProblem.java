package com.example.solvesphere.UserData;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class AdminProblem {
    private long id;
    private int adminId; // Admin ID (int because it's from the 'admin' table)
    private String title;
    private String description;
    private String category;
    private Timestamp createdAt;
    private boolean isAgeRestricted;

    // Constructor
    public AdminProblem(long id, int adminId, String title, String description, String category, Timestamp createdAt, boolean isAgeRestricted) {
        this.id = id;
        this.adminId = adminId;
        this.title = title;
        this.description = description;
        this.category = category;
        this.createdAt = createdAt;
        this.isAgeRestricted = isAgeRestricted;
    }

    // Getters
    public long getId() {
        return id;
    }

    public int getAdminId() {
        return adminId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt.toLocalDateTime();
    }

    public boolean isAgeRestricted() {
        return isAgeRestricted;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setAgeRestricted(boolean ageRestricted) {
        isAgeRestricted = ageRestricted;
    }

    @Override
    public String toString() {
        return "AdminProblem{" +
                "id=" + id +
                ", adminId=" + adminId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", createdAt=" + createdAt +
                ", isAgeRestricted=" + isAgeRestricted +
                '}';
    }
}
