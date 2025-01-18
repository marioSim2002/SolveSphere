package com.example.solvesphere.DataBaseUnit;

import com.example.solvesphere.DBQueries.FavoritesQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FavoritesDAOImpl implements FavoritesDAO {

    @Override
    public void addFavorite(long userId, long problemId) {
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FavoritesQueries.ADD_FAVORITE_SCRIPT)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, problemId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeFavorite(long userId, long problemId) {
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FavoritesQueries.REMOVE_FAVORITE_SCRIPT)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, problemId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isFavorite(long userId, long problemId) {
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FavoritesQueries.CHECK_IF_FAV_SCRIPT)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, problemId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public List<Long> getFavoriteProblemsByUser(long userId) {
        List<Long> favoriteProblems = new ArrayList<>();
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(FavoritesQueries.GET_FAV_POSTS_BY_USER)) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                favoriteProblems.add(rs.getLong("problem_id"));
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return favoriteProblems;
    }
}
