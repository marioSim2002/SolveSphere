package com.example.solvesphere;

import com.example.solvesphere.SecurityUnit.PasswordHasher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class UserRegistrationHandler implements Runnable {

    private final Socket clientSocket;

    public UserRegistrationHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            // read the command sent by the client
            String command = in.readLine();

            if ("REGISTER".equalsIgnoreCase(command)) {
                handleRegistration(in, out);
            } else if ("LOGIN".equalsIgnoreCase(command)) {
                handleLogin(in, out);
            } else {
                out.println("Invalid command. Please use REGISTER or LOGIN.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleRegistration(BufferedReader in, PrintWriter out) throws IOException {
        // Read registration data from the client
        String username = in.readLine();
        String email = in.readLine();
        String password = in.readLine();  // Plain-text password received from the client

        // hash pass before storing
        PasswordHasher hasher = new PasswordHasher();
        String hashedPassword = hasher.hashPassword(password);

        System.out.println("Registering user: " + username);
        System.out.println("Email: " + email);
        System.out.println("Plain-Text Password: " + password); // og pass - testing
        System.out.println("Hashed Password: " + hashedPassword); // hashed password for secure storage

        // TODO: Add logic to store the user in a database with the hashed password instead of plain-text

        // confirmation to the client
        out.println("User " + username + " registered successfully.");
    }

    private void handleLogin(BufferedReader in, PrintWriter out) throws IOException {
        // login process
        String username = in.readLine();
        String password = in.readLine();

        System.out.println("Logging in user: " + username);
        System.out.println("Password: " + password);

        // TODO: Implement login DB logic here (e.g., validate username and password against database)

        // dummy validation for demonstration
        if ("testUser".equals(username) && "testPass".equals(password)) {
            out.println("Login successful!");
        } else {
            out.println("Invalid username or password.");
        }
    }
}
