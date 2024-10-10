package com.example.solvesphere.UserData;

import java.time.LocalDate;
import java.util.Map;

public class UserFactory {

    public static User createUser(String username, String email, String password,
                                  String dateOfBirth, String country,
                                  Map<String, Integer> fieldsOfInterest,
                                  LocalDate registrationDate, String profilePicture) {
        return new User(username, email, password, dateOfBirth, country,
                fieldsOfInterest, registrationDate, profilePicture);
    }
}


/* factory pattern - helps maintain compatibility for future changes */