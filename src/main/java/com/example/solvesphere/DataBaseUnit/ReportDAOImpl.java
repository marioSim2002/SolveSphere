package com.example.solvesphere.DataBaseUnit;

import java.sql.*;

public class ReportDAOImpl implements ReportDAO {

    @Override
    public boolean addReport(Report report) throws SQLException {
        String sql = "INSERT INTO reports (problem_id, reporter_id, report_reason) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, report.getProblemId());
            stmt.setLong(2, report.getReporterId());
            stmt.setString(3, report.getReportReason());
            return stmt.executeUpdate() > 0;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private Report mapResultSetToReport(ResultSet rs) throws SQLException {
        return new Report(
                rs.getLong("id"),
                rs.getLong("problem_id"),
                rs.getLong("reporter_id"),
                rs.getString("report_reason"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
