package com.example.solvesphere.UserData;

import com.example.solvesphere.UserData.Problem;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

public class User implements Serializable {
    private long id;
    private String username;
    private String email;
    private String password;
    private LocalDate dateOfBirth;
    private String country;
    private Map<String, Integer> fieldsOfInterest;
    private LocalDate registrationDate;
    private byte[] profilePicture;  // store the image as byte array
    private List<Problem> problems;

    public User(String username, String email, String password, LocalDate dateOfBirth, String country,
                Map<String, Integer> fieldsOfInterest, LocalDate registrationDate, byte[] profilePicture) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.country = country;
        this.fieldsOfInterest = new HashMap<>(fieldsOfInterest);
        this.registrationDate = registrationDate;
        this.profilePicture = profilePicture;
        this.problems = new ArrayList<>();
    }

    public User() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public byte[] getProfilePicture() { return profilePicture; }
    public void setProfilePicture(byte[] profilePicture) { this.profilePicture = profilePicture; }

    // Calculate the user's age based on dateOfBirth

    public int calculateAge() {
        LocalDate currentDate = LocalDate.now(); //current date
        return Period.between(this.dateOfBirth, currentDate).getYears(); // calc age in years
    }
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getCountry() {
        return country;
    }

    public Map<String, Integer> getFieldsOfInterest() {
        return fieldsOfInterest;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public List<Problem> getProblems() {
        return problems;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", country='" + country + '\'' +
                ", fieldsOfInterest=" + fieldsOfInterest +
                ", registrationDate=" + registrationDate +
                ", profilePicture=" + Arrays.toString(profilePicture) +
                '}';
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setFieldsOfInterest(Map<String, Integer> fieldsOfInterest) {
        this.fieldsOfInterest = fieldsOfInterest;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setProblems(List<Problem> problems) {
        this.problems = problems;
    }
}
