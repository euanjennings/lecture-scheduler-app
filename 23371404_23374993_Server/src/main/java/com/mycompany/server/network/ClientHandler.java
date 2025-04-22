package com.mycompany.server.network;

import com.mycompany.server.controller.CommandProcessor;
import com.mycompany.server.controller.LectureController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final CommandProcessor commandProcessor;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket socket, LectureController controller) {
        this.clientSocket = socket;
        this.commandProcessor = new CommandProcessor(controller); // Injected
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            String clientMessage;
            while ((clientMessage = in.readLine()) != null) {
                String response = commandProcessor.processCommand(clientMessage);
                out.println(response);

                if ("TERMINATE".equals(response)) {
                    System.out.println("Client requested termination.");
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Client handler error: " + e.getMessage());
        } finally {
            try {
                System.out.println("Closing client connection...");
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
            }
        }
    }
}
