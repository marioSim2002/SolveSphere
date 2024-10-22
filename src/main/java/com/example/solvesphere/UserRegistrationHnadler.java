package com.example.solvesphere;

import com.example.solvesphere.DataBaseUnit.ProblemDAO;
import com.example.solvesphere.DataBaseUnit.ProblemDAOImpl;
import com.example.solvesphere.DataBaseUnit.UserDAO;
import com.example.solvesphere.DataBaseUnit.UserDAOImpl;
import com.example.solvesphere.SecurityUnit.PasswordHasher;
import com.example.solvesphere.UserData.AuthenticationService;
import com.example.solvesphere.UserData.Problem;
import com.example.solvesphere.UserData.User;
import com.example.solvesphere.UserData.UserFactory;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDate;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class UserRegistrationHandler implements Runnable {

    private final Socket clientSocket;

    public UserRegistrationHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

            String command = (String) in.readObject();
            System.out.println("Command received: " + command);

            switch (command.toUpperCase()) {
                case "REGISTER":
                    handleRegistration(in, out);
                    break;
                case "LOGIN":
                    handleLogin(in, out);
                    break;
                case "FETCH_PROBLEMS":
                    handleFetchProblems(out);
                    break;
                default:
                    out.writeObject("Invalid command. Please use REGISTER, LOGIN or FETCH_PROBLEMS.");
                    break;
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

    private void handleFetchProblems(ObjectOutputStream out) {
        try {
            // todo
            // modify method to match filtering handling
            ProblemDAO problemDAO = new ProblemDAOImpl();
            List<Problem> problems = problemDAO.fetchAllProblems();
            out.writeObject(problems);  //send the list of problems to the client
        } catch (Exception e) {
            try {
                System.err.println("Failed to fetch problems: " + e.getMessage());
                out.writeObject(new ArrayList<Problem>());  //send an empty list on error
            } catch (IOException ioException) {
                System.err.println("Error sending error response: " + ioException.getMessage());
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

            out.writeObject("User " + newUser.getUsername() + " registered successfully.");  //success response
            AlertsUnit.showSuccessRegistrationAlert();  //notify the client
        } catch (ClassNotFoundException e) {
            out.writeObject("Error: Unable to read user data.");
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleLogin(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
        //read the object from the input stream
        Object data = in.readObject();

        //check if the received object is indeed a String array
        if (data instanceof String[]) {
            String[] loginData = (String[]) data;
            String username = loginData[0];
            String password = loginData[1];

            System.out.println("Logging in user: " + username); // Debugging

            // retrieve the user from the database
            UserDAO userDAO = new UserDAOImpl();
            User user = userDAO.getUserByUsernameAndPassword(username, password); // validate username and password

            if (user != null) {
                //ProblemDAO problemDAO = new ProblemDAOImpl();
                Map<String, Integer> userInterests = user.getFieldsOfInterest();
                System.out.println(userInterests);
                user.setFieldsOfInterest(userInterests);
                //send the user object
                out.writeObject(user);
            } else {
                out.writeObject("Invalid username or password."); // failure message
            }
        } else {
            // not an array
            out.writeObject("Error: Invalid data format for login.");
            System.out.println("Received invalid data for login: " + data); // Debugging
        }
    }


}