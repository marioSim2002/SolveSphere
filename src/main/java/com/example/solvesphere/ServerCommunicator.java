package com.example.solvesphere;

import com.example.solvesphere.UserData.User;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class ServerCommunicator {
    private String serverHost;
    private int serverPort;

    // constructor to initialize host and port
    public ServerCommunicator(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    // General method to send a request and receive a response
    public String sendRequest(String command, Object data) {
        System.out.println("Sending command: " + command);  // Debug message
        try (Socket socket = new Socket(serverHost, serverPort);
             ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream objectIn = new ObjectInputStream(socket.getInputStream())) {

            // Send command to indicate the type of request (e.g., REGISTER, LOGIN)
            objectOut.writeObject(command);
            System.out.println("Sending command: " + command);  // Debug message

            // Send the entire object (User for registration, String[] for login)
            objectOut.writeObject(data);
            System.out.println("Sending data: " + data); // Debugging for user or login data

            objectOut.flush();

            // Read the server's response
            return (String) objectIn.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return "Error: Unable to connect to the server.";
        }
    }


    // Specialized method for sending registration data
    public String sendRegistrationRequest(User user) {
        return sendRequest("REGISTER", user);
    }

    public String sendLoginRequest(String username, String password) {
        return sendRequest("LOGIN", new String[]{username, password});
    }


    // method to send a request to reset the user's password
    public String sendPasswordResetRequest(String username) {
        return sendRequest("RESET_PASSWORD", username);
    }

    // Method to update user profile
    public String sendUpdateProfileRequest(User updatedUser) {
        return sendRequest("UPDATE_PROFILE", updatedUser);
    }
}
