package com.example.solvesphere.UserData;

import java.io.Serializable;

public class FieldOfInterest implements Serializable {
    private int id;                  // ID of the field of interest
    private int userId;              // associated user ID
    private String interestName;     // name of the interest (e.g., "Technology")
    private int priorityLevel;       // priority level (1-5)

    // Constructor
    public FieldOfInterest(int id, int userId, String interestName, int priorityLevel) {
        this.id = id;
        this.userId = userId;
        this.interestName = interestName;
        this.priorityLevel = priorityLevel;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getInterestName() { return interestName; }
    public void setInterestName(String interestName) { this.interestName = interestName; }

    public int getPriorityLevel() { return priorityLevel; }
    public void setPriorityLevel(int priorityLevel) { this.priorityLevel = priorityLevel; }

    @Override
    public String toString() {
        return "FieldOfInterest{" +
                "id=" + id +
                ", userId=" + userId +
                ", interestName='" + interestName + '\'' +
                ", priorityLevel=" + priorityLevel +
                '}';
    }
}
