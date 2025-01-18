package com.example.solvesphere.DataBaseUnit;

import java.util.List;

public interface FavoritesDAO {
    void addFavorite(long userId, long problemId);
    void removeFavorite(long userId, long problemId);
    boolean isFavorite(long userId, long problemId);
    List<Long> getFavoriteProblemsByUser(long userId);
}
