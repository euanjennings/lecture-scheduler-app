package com.mycompany.server.network;

import com.mycompany.server.controller.LectureController;
import com.mycompany.server.model.ScheduleManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {
    private static final int PORT = 1234;

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);

            // Shared MVC objects
            ScheduleManager sharedSchedule = new ScheduleManager();
            LectureController sharedController = new LectureController(sharedSchedule);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket, sharedController);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ServerMain().startServer();
    }
}
