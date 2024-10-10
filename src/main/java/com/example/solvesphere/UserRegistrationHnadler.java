package com.example.solvesphere;

import com.example.solvesphere.DataBaseUnit.UserDAO;
import com.example.solvesphere.DataBaseUnit.UserDAOImpl;
import com.example.solvesphere.SecurityUnit.PasswordHasher;
import com.example.solvesphere.UserData.User;
import com.example.solvesphere.UserData.UserFactory;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class UserRegistrationHandler implements Runnable {

    private final Socket clientSocket;

    public UserRegistrationHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

            // Read the command sent by the client
            String command = (String) in.readObject();  // Read command as Object
            System.out.println("Command received: " + command);  // Debugging

            if ("REGISTER".equalsIgnoreCase(command)) {
                handleRegistration(in, out); // Pass ObjectInputStream and ObjectOutputStream for registration
            } else if ("LOGIN".equalsIgnoreCase(command)) {
                handleLogin(in, out); // Pass ObjectInputStream and ObjectOutputStream for login
            } else {
                out.writeObject("Invalid command. Please use REGISTER or LOGIN."); // Send response
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleRegistration(ObjectInputStream in, ObjectOutputStream out) throws IOException {
        try {
            // Read user data
            User newUser = (User) in.readObject();  // Expecting User object

            System.out.println("Registering user: " + newUser.getUsername());  // Debugging
            System.out.println("Email: " + newUser.getEmail());                // Debugging

            UserDAO userDAO = new UserDAOImpl();

            // check if user already exists
            if (userDAO.userExists(newUser.getUsername(), newUser.getEmail())) {
                out.writeObject("Username or email already exists.");
                return; // stop further processing
            }

            PasswordHasher hasher = new PasswordHasher();
            String hashedPassword = hasher.hashPassword(newUser.getPassword());  // Hashing password

            newUser.setPassword(hashedPassword);  // set hashed password back to user object
            userDAO.addUser(newUser);  // add user to the database

            out.writeObject("User " + newUser.getUsername() + " registered successfully.");  // Send success response
            AlertsUnit.showSuccessAlert();  // Notify the client
        } catch (ClassNotFoundException e) {
            out.writeObject("Error: Unable to read user data.");
            e.printStackTrace();
        }
    }

    private void handleLogin(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
        // login process
        String username = (String) in.readObject();
        String password = (String) in.readObject();

        System.out.println("Logging in user: " + username); // Debugging

        // Retrieve the user from the database
        UserDAO userDAO = new UserDAOImpl();
        User user = userDAO.getUserByUsernameAndPassword(username, password); // Validate username and password

        if (user != null) {
            out.writeObject("Login successful!"); // Send success message
        } else {
            out.writeObject("Invalid username or password."); // Send failure message
        }
    }
}
