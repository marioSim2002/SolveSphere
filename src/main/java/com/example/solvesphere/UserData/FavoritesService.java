package com.example.solvesphere.UserData;

import com.example.solvesphere.DataBaseUnit.FavoritesDAO;
import com.example.solvesphere.DataBaseUnit.FavoritesDAOImpl;
import com.example.solvesphere.DataBaseUnit.ProblemDAO;
import com.example.solvesphere.DataBaseUnit.ProblemDAOImpl;
import com.example.solvesphere.ProfileTabbedController;

import java.util.ArrayList;
import java.util.List;

public class FavoritesService {
    private final FavoritesDAO favoritesDAO;

    public FavoritesService() {
        this.favoritesDAO = new FavoritesDAOImpl();
    }

    public void addFavorite(long userId, long problemId) {
        if (!favoritesDAO.isFavorite(userId, problemId)) {
            favoritesDAO.addFavorite(userId, problemId);
            System.out.println("Problem added to favorites.");

        } else {
            System.out.println("Problem is already in favorites.");
        }
    }

    public void removeFavorite(long userId, long problemId) {
        if (favoritesDAO.isFavorite(userId, problemId)) {
            favoritesDAO.removeFavorite(userId, problemId);
            System.out.println("Problem removed from favorites.");
        } else {
            System.out.println("Problem is not in favorites.");
        }
    }
}
