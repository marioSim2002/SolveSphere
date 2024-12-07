package com.example.solvesphere.UserData;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Serializable {
    private long id = -1; // ID initialized as -1 to indicate it's not set initially
    private String username;
    private String email;
    private String password;
    private LocalDate dateOfBirth;
    private String country;
    private Map<String, Integer> fieldsOfInterest;  // Interest levels for various fields
    private LocalDate registrationDate;
    private String profilePicture;
    private List<Problem> problems;  //list of problems associated with the user

    public User(String username, String email, String password, LocalDate dateOfBirth, String country,
                Map<String, Integer> fieldsOfInterest, LocalDate registrationDate, String profilePicture) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.country = country;
        this.fieldsOfInterest = new HashMap<>(fieldsOfInterest);
        this.registrationDate = registrationDate;
        this.profilePicture = profilePicture;
        this.problems = new ArrayList<>();  // Initialize the problems list
    }

    // getters and setters, including for the new ID field
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public List<Problem> getProblems() {
        return problems;  // getter for the problems list
    }

    public void addProblem(Problem problem) {
        this.problems.add(problem);  // Method to add a problem to the user's list
    }

    public void removeProblem(Problem problem) {
        this.problems.remove(problem);  // Method to remove a problem from the user's list
    }

    public void setProblems(List<Problem> problems) {
        this.problems = problems;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public Map<String, Integer> getFieldsOfInterest() { return fieldsOfInterest; }
    public void setFieldsOfInterest(Map<String, Integer> fieldsOfInterest) {
        this.fieldsOfInterest = new HashMap<>(fieldsOfInterest);
    }

    public LocalDateTime getRegistrationDate() { return registrationDate.atStartOfDay(); }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

    // Calculate the user's age based on dateOfBirth
    public int calculateAge() {
        LocalDate currentDate = LocalDate.now(); //current date
        return Period.between(this.dateOfBirth, currentDate).getYears(); // calc age in years
    }

    public boolean isUnderage() {
        return calculateAge() < 18;  // s et age threshold to 18
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
