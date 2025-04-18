package com.mycompany.server.network;

import com.mycompany.server.controller.CommandProcessor;
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

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        this.commandProcessor = new CommandProcessor();
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            String clientMessage;
            while (true) {
                clientMessage = in.readLine();
                if (clientMessage == null) {
                    System.out.println("Client disconnected.");
                    break;
                }

                String response = commandProcessor.processCommand(clientMessage);
                out.println(response);
                
                if (response.equals("TERMINATE")) {
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