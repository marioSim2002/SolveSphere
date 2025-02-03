package com.example.solvesphere.ServerUnit;

import com.example.solvesphere.ServerUnit.UserRegistrationHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RegistrationServer {

    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started, waiting for clients...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected!");

                // create a new thread for each client connection
                new Thread(new UserRegistrationHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
