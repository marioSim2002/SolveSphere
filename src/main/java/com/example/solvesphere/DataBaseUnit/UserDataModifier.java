package com.example.solvesphere.DataBaseUnit;

import com.example.solvesphere.UserData.User;

public interface UserDataModifier {
    boolean updateUserDetails(User user,String newEmail);

    boolean updateUserProfilePicture(long userId, byte[] profilePicture);
}
