package com.mycompany.server.network;

import com.mycompany.server.controller.CommandProcessor;
import com.mycompany.server.controller.LectureController;
import com.mycompany.server.view.ServerApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final CommandProcessor commandProcessor;
    private final ServerApp serverApp;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket socket, LectureController controller, ServerApp serverApp) {
        this.clientSocket = socket;
        this.commandProcessor = new CommandProcessor(controller);
        this.serverApp = serverApp;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            String clientMessage;
            while ((clientMessage = in.readLine()) != null) {
                serverApp.appendRequestLog("Request from " + clientSocket.getInetAddress() + ": " + clientMessage);

                String response = commandProcessor.processCommand(clientMessage);
                out.println(response);

                if ("TERMINATE".equals(response)) {
                    serverApp.appendClientLog("Client disconnected: " + clientSocket.getInetAddress());
                    break;
                }
            }
        } catch (IOException e) {
            serverApp.appendClientLog("Client handler error: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                serverApp.appendClientLog("Error closing client socket: " + e.getMessage());
            }
        }
    }
}
