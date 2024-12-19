package com.example.solvesphere.DataBaseUnit;

import com.example.solvesphere.DBQueries.SolutionQueries;
import com.example.solvesphere.UserData.Solution;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class SolutionDAOImpl implements SolutionDAO {


    public void addSolution(Solution solution) {
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SolutionQueries.INSERT_SOLUTION_SQL)) {
            stmt.setLong(1, solution.getProblemId());
            stmt.setLong(2, solution.getUserId());
            stmt.setString(3, solution.getTitle());
            stmt.setString(4, solution.getDescription());
            stmt.setTimestamp(5, java.sql.Timestamp.valueOf(solution.getCreatedAt()));
            stmt.setInt(6, solution.getRating());
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Failed to add solution: " + e.getMessage());
        }
    }
}

