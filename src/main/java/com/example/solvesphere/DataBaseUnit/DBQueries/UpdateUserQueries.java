package com.example.solvesphere.DataBaseUnit.DBQueries;

public class UpdateUserQueries {
    public static final String UPDATE_USER_DATA_SCRIPT = "UPDATE users SET username = ?, email = ?, country = ?, profile_picture = ? WHERE id = ?";
    public static final String UPDATE_BIRTHDATE_SCRIPT = "UPDATE users SET date_of_birth = ? WHERE id = ?";
    public static final String DELETE_INTEREST_SCRIPT = "DELETE FROM fields_of_interest WHERE user_id = ?";
    public static final String INSERT_INTEREST_SCRIPT ="INSERT INTO fields_of_interest (user_id, interest_name, priority_level)  VALUES (?, ?, ?);";
}
