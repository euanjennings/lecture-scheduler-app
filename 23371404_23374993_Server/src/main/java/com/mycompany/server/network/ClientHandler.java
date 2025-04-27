package com.mycompany.server.network;

import com.mycompany.server.controller.CommandProcessor;
import com.mycompany.server.controller.LectureController;
import com.mycompany.server.controller.LectureProcessingService;
import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final CommandProcessor commandProcessor;
    private final LectureProcessingService lectureProcessingService;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket socket, LectureController controller, LectureProcessingService service) {
        this.clientSocket = socket;
        this.commandProcessor = new CommandProcessor(controller);
        this.lectureProcessingService = service;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            String clientMessage;
            while ((clientMessage = in.readLine()) != null) {
                System.out.println("Received: " + clientMessage);

                String[] parts = clientMessage.split(",");
                String action = parts[0].trim();

                if ("Early".equalsIgnoreCase(action)) {
                    System.out.println("Starting background early lectures task...");

                    Task<String> earlyTask = lectureProcessingService.processEarlyLectures();

                    earlyTask.setOnSucceeded(event -> {
                        String result = earlyTask.getValue();
                        out.println(result);
                    });

                    earlyTask.setOnFailed(event -> {
                        out.println("ERROR: Failed to process early lectures.");
                    });

                    new Thread(earlyTask).start();
                } else {
                    String response = commandProcessor.processCommand(clientMessage);
                    out.println(response);

                    if ("TERMINATE".equals(response)) {
                        System.out.println("Client requested termination.");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Client handler error: " + e.getMessage());
        } finally {
            try {
                try (clientSocket) {
                    System.out.println("Closing client connection...");
                }
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }
}
