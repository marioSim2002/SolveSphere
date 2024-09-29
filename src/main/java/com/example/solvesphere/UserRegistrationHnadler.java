package com.example.solvesphere;

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

    // thread
    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            // Read the user's registration data from the client
            String username = in.readLine();
            String email = in.readLine();
            String password = in.readLine();

            System.out.println("Registering user: " + username);
            System.out.println("Email: " + email);
            System.out.println("Password: " + password); // Normally, you'd hash this

            //add logic to store the user in a database, validate the data, etc.
            // Send confirmation back to the client

            out.println("User " + username + " registered successfully.");

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
}