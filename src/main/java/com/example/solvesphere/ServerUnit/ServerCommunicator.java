package com.example.solvesphere.ServerUnit;

import com.example.solvesphere.UserData.Problem;
import com.example.solvesphere.UserData.User;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerCommunicator {
    private String serverHost;
    private int serverPort;
    private static ServerCommunicator instance;

    // private constructor to enforce singleton pattern
    public ServerCommunicator() {
        ConfigLoader configLoader = ConfigLoader.getInstance();
        this.serverHost = configLoader.getProperty("server.host");
        this.serverPort = configLoader.getIntProperty("server.port");

    }

    //singleton getInstance method
    public static ServerCommunicator getInstance() {
        if (instance == null) {
            instance = new ServerCommunicator();
        }
        return instance;
    }

    //general method to send a (login+reg)request and receive a response
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


    public List<Problem> sendFetchProblemsRequest(String command) {
        System.out.println("Sending command to fetch problems: " + command);
        try (Socket socket = new Socket(serverHost, serverPort);
             ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream objectIn = new ObjectInputStream(socket.getInputStream())) {

            objectOut.writeObject(command);
            objectOut.flush();
            Object response = objectIn.readObject();
            if (response instanceof List<?>) {
                return (List<Problem>) response;  // safely cast and return the List
            } else {
                System.out.println("Received incorrect data type: " + response);
                return new ArrayList<>();  // return empty list on type mismatch or error
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    //specialized method for sending registration data
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
            if (response instanceof User user) {
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
    public Long fetchUserIdByUsernameAndEmail(String username, String email) {
        try (Socket socket = new Socket(serverHost, serverPort);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("FETCH_USER_ID");
            out.writeObject(new String[]{username, email});
            out.flush();

            Object response = in.readObject();
            if (response instanceof Long) {
                return (Long) response;
            } else {
                System.err.println("Unexpected response type: " + response);
                return null;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
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
