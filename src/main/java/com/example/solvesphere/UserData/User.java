package com.example.solvesphere;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {
    private String username;
    private String email;
    private String password;
    private String dateOfBirth;  // Stores date of birth as String in "YYYY-MM-DD" format
    private String country;
    private Map<String, Integer> fieldsOfInterest;  // Map to store interest levels for various fields
    private LocalDate registrationDate;  // Date of registration
    private String profilePicture;  // URL or path to profile picture

    // Constructor
    public User(String username, String email, String password, String dateOfBirth, String country,
                Map<String, Integer> fieldsOfInterest,
                LocalDate registrationDate, String profilePicture) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.country = country;
        this.fieldsOfInterest = new HashMap<>(fieldsOfInterest);  // Initialize fieldsOfInterest
        this.registrationDate = registrationDate;  // Set registration date
        this.profilePicture = profilePicture;  // Set profile picture URL
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public Map<String, Integer> getFieldsOfInterest() { return fieldsOfInterest; }
    public void setFieldsOfInterest(Map<String, Integer> fieldsOfInterest) {
        this.fieldsOfInterest = new HashMap<>(fieldsOfInterest);
    }

    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

    // Calculate the user's age based on dateOfBirth
    public int calculateAge() {
        LocalDate birthDate = LocalDate.parse(this.dateOfBirth);  // Convert the String date to LocalDate
        LocalDate currentDate = LocalDate.now();                  // Get the current date
        return Period.between(birthDate, currentDate).getYears(); // Calculate age in years
    }

    // Determine if the user is underaged (e.g., under 18 years old)
    public boolean isUnderage() {
        return calculateAge() < 18;  // Set age threshold to 18
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", country='" + country + '\'' +
                ", fieldsOfInterest=" + fieldsOfInterest +
                ", registrationDate=" + registrationDate +
                ", profilePicture='" + profilePicture + '\'' +
                '}';
    }
}
