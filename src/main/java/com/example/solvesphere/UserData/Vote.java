package com.example.solvesphere.UserData;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Vote implements Serializable {
    private int id;                  // vote ID
    private int userId;              // ID of the user who cast the vote
    private int problemId;           // associated problem ID (optional)
    private int solutionId;          // associated solution ID (optional)
    private String voteType;         // vote type ("upvote" or "downvote")

    public Vote(int id, int userId, int problemId, int solutionId, String voteType) {
        this.id = id;
        this.userId = userId;
        this.problemId = problemId;
        this.solutionId = solutionId;
        this.voteType = voteType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProblemId() {
        return problemId;
    }

    public void setProblemId(int problemId) {
        this.problemId = problemId;
    }

    public int getSolutionId() {
        return solutionId;
    }

    public void setSolutionId(int solutionId) {
        this.solutionId = solutionId;
    }

    public String getVoteType() {
        return voteType;
    }

    public void setVoteType(String voteType) {
        this.voteType = voteType;
    }


    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", userId=" + userId +
                ", problemId=" + problemId +
                ", solutionId=" + solutionId +
                ", voteType='" + voteType + '\'' +
                '}';
    }
}
