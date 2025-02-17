package com.example.solvesphere.UserData;

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
    private byte[] profilePicture;  //store the image as a byte array
    private boolean active;  //represents if the user is currently active
    private List<Problem> problems;

    public User(String username, String email, String password, LocalDate dateOfBirth, String country,
                Map<String, Integer> fieldsOfInterest, LocalDate registrationDate, byte[] profilePicture, boolean active) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.country = country;
        this.fieldsOfInterest = new HashMap<>(fieldsOfInterest);
        this.registrationDate = registrationDate;
        this.profilePicture = profilePicture;
        this.active = active;
        this.problems = new ArrayList<>();
    }

    public User() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public byte[] getProfilePicture() { return profilePicture; }
    public void setProfilePicture(byte[] profilePicture) { this.profilePicture = profilePicture; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public int calculateAge() {
        LocalDate currentDate = LocalDate.now();
        return Period.between(this.dateOfBirth, currentDate).getYears();
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
        this.fieldsOfInterest = fieldsOfInterest;
    }

    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }

    public List<Problem> getProblems() { return problems; }
    public void setProblems(List<Problem> problems) { this.problems = problems; }

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
                ", active=" + active +
                '}';
    }
}
