package com.example.solvesphere.DataBaseUnit.DBQueries;

public class FavoritesQueries {
    public static final String ADD_FAVORITE_SCRIPT = "INSERT INTO favorites (user_id, problem_id) VALUES (?, ?)";
    public static final String REMOVE_FAVORITE_SCRIPT = "DELETE FROM favorites WHERE user_id = ? AND problem_id = ?";
    public static final String CHECK_IF_FAV_SCRIPT = "SELECT COUNT(*) FROM favorites WHERE user_id = ? AND problem_id = ?";
    public static final String GET_FAV_POSTS_BY_USER ="SELECT problem_id FROM favorites WHERE user_id = ?";

}
