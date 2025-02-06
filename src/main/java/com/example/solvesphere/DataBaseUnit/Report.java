package com.example.solvesphere.DataBaseUnit;

import java.time.LocalDateTime;

public class Report {
    private long id;
    private long problemId;
    private long reporterId;
    private String reportReason;
    private LocalDateTime createdAt;

    public Report(long id, long problemId, long reporterId, String reportReason, LocalDateTime createdAt) {
        this.id = id;
        this.problemId = problemId;
        this.reporterId = reporterId;
        this.reportReason = reportReason;
        this.createdAt = createdAt;
    }

    public long getId() { return id; }
    public long getProblemId() { return problemId; }
    public long getReporterId() { return reporterId; }
    public String getReportReason() { return reportReason; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
