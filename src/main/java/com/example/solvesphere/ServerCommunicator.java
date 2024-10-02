package com.example.solvesphere;

import java.io.*;
import java.net.Socket;

public class ServerCommunicator {

    private String serverHost;
    private int serverPort;

    // constructor to initialize host and port
    public ServerCommunicator(String serverHost, int serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    // general method to send a request and receive a response
    public String sendRequest(String command, Object data) {
        try (Socket socket = new Socket(serverHost, serverPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             ObjectOutputStream objectOut = new ObjectOutputStream(socket.getOutputStream())) {

            // Send the command to indicate the type of request (e.g., REGISTER, LOGIN)
            out.println(command);

            // If there is additional data (like a User object), send it via ObjectOutputStream
            if (data != null) {
                objectOut.writeObject(data);
                objectOut.flush();
            }

            // Read the server's response
            return in.readLine();

        } catch (IOException e) {
            e.printStackTrace();
            return "Error: Unable to connect to the server.";
        }
    }

    // specialized method for sending registration data
    public String sendRegistrationRequest(User user) {
        return sendRequest("REGISTER", user);
    }

    // specialized method for sending login data
    public String sendLoginRequest(String username, String password) {
        return sendRequest("LOGIN", new String[]{username, password});
    }

    // method to send a request to reset the user's password
    public String sendPasswordResetRequest(String username) {
        return sendRequest("RESET_PASSWORD", username);
    }

    //method to update user profile
    public String sendUpdateProfileRequest(User updatedUser) {
        return sendRequest("UPDATE_PROFILE", updatedUser);
    }
}
