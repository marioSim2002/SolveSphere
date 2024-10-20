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

            // send command to indicate the type of request (e.g., REGISTER, LOGIN)
            objectOut.writeObject(command);
            System.out.println("Sending command: " + command);  // Debug message

            // send the entire object (User for registration, String[] for login)
            objectOut.writeObject(data);
            System.out.println("Sending data: " + data); // Debugging for user or login data

            objectOut.flush();

            // read the server's response
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

    public Object sendLoginRequest(String username, String password) {
        try (Socket socket = new Socket(serverHost, serverPort);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            // Send login command and credentials
            out.writeObject("LOGIN");
            out.writeObject(new String[]{username, password});
            out.flush();

            // Read the response from the server
            Object response = in.readObject();

            // Handle the response based on its type
            if (response instanceof User) {
                User user = (User) response;
                System.out.println("Login successful for: " + user.getUsername());
                return user;
            } else if (response instanceof String) {
                String message = (String) response;
                System.out.println("Login response: " + message);
                return message;
            } else {
                return "Unexpected response type from server.";
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return "Error: Unable to connect to the server.";
        }
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
