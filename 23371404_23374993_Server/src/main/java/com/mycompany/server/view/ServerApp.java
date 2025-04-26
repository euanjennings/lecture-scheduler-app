package com.mycompany.server.view;

import com.mycompany.server.controller.LectureController;
import com.mycompany.server.model.ScheduleManager;
import com.mycompany.server.network.ClientHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApp extends Application {

    private TextArea clientsArea;
    private TextArea requestsArea;
    private ScheduleManager scheduleManager;
    private LectureController controller;

    @Override
    public void start(Stage primaryStage) {
        scheduleManager = new ScheduleManager();
        controller = new LectureController(scheduleManager);

        // GUI Setup
        clientsArea = new TextArea();
        clientsArea.setEditable(false);
        clientsArea.setPromptText("Connected Clients");

        requestsArea = new TextArea();
        requestsArea.setEditable(false);
        requestsArea.setPromptText("Incoming Requests");

        VBox layout = new VBox(10, clientsArea, requestsArea);
        Scene scene = new Scene(layout, 600, 500);

        primaryStage.setTitle("Lecture Scheduler Server");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Start server socket in a new thread
        new Thread(this::startServer).start();
    }

    private void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            appendClientLog("Server started on port 1234");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                String clientInfo = clientSocket.getInetAddress().toString();
                appendClientLog("Client connected: " + clientInfo);

                ClientHandler handler = new ClientHandler(clientSocket, controller, this);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            appendClientLog("Server error: " + e.getMessage());
        }
    }

    public void appendClientLog(String message) {
        Platform.runLater(() -> clientsArea.appendText(message + "\n"));
    }

    public void appendRequestLog(String message) {
        Platform.runLater(() -> requestsArea.appendText(message + "\n"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
