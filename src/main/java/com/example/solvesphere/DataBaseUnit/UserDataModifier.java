package com.example.solvesphere.DataBaseUnit;

import com.example.solvesphere.UserData.User;


/*
UserDataModifier , is responsible for modifying/altering existing user personal data in thr DB
*/
public interface UserDataModifier {
    boolean updateUserDetails(User user,String newEmail);
    boolean updateUserProfilePicture(long userId, byte[] profilePicture);
}
