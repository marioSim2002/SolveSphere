package com.example.solvesphere.DataBaseUnit;

import com.example.solvesphere.DataBaseUnit.Report;
import java.sql.SQLException;
import java.util.List;

public interface ReportDAO {
    boolean addReport(Report report) throws SQLException;
}
