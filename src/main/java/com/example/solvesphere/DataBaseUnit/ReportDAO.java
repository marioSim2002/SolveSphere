package com.example.solvesphere.DataBaseUnit;

import com.example.solvesphere.DataBaseUnit.Report;
import java.sql.SQLException;
import java.util.List;

public interface ReportDAO {
    boolean addReport(Report report) throws SQLException;
    List<Report> getPendingReports() throws SQLException;
    boolean updateReportStatus(long reportId, String status) throws SQLException, ClassNotFoundException;
}
