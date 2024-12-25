package com.example.solvesphere.SecurityUnit;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHasher {

    private final BCryptPasswordEncoder passwordEncoder;

    public PasswordHasher() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // Hash the password for storage
    public String hashPassword(String password) {
        String hashedPassword = passwordEncoder.encode(password);
        System.out.println("Generated Hash: " + hashedPassword);
        return hashedPassword;
    }

    // Verify a password against the stored hash
    public boolean verifyPassword(String password, String hashedPassword) {
        boolean matches = passwordEncoder.matches(password, hashedPassword);
        System.out.println("Password Verification Result: " + matches);
        return matches;
    }
}
