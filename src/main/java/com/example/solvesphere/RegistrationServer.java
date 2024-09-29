package com.example.solvesphere;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class RegistrationServer {

    private static final int PORT = 12345; // The port number

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started, waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected!");

                // Create a new thread for each client
                new Thread(new UserRegistrationHandler(clientSocket)).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


