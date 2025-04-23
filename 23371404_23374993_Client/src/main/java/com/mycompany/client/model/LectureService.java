package com.mycompany.client.model;

import com.mycompany.client.model.Lecture;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class LectureService {
    private final Socket socket; // Maintain the socket connection
    private final PrintWriter out;
    private final BufferedReader in;

    public LectureService(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public String sendRequest(String action, String date, String time, String room, String module) {
        try {
            // Format the request message
            String request = String.format("%s,%s,%s,%s,%s", action, date, time, room, module);
            out.println(request); // Send the request to the server

            // Read and return the server's response
            return in.readLine();
        } catch (IOException e) {
            System.err.println("Error communicating with the server: " + e.getMessage());
            return "Error: Unable to connect to the server.";
        }
    }

    public List<Lecture> parseLectures(String response) {
        List<Lecture> lectures = new ArrayList<>();
        if (response == null || response.isEmpty() || response.equals("No lectures scheduled.")) {
            return lectures; // Return empty list if no lectures are scheduled
        }

        String[] entries = response.split(";");
        for (String entry : entries) {
            String[] fields = entry.split(",");
            if (fields.length == 4) { // Ensure correct format
                String date = fields[0].trim();
                String time = fields[1].trim();
                String room = fields[2].trim();
                String module = fields[3].trim();
                lectures.add(new Lecture(date, time, room, module));
            }
        }
        return lectures;
    }

    public void closeConnection() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close(); // Properly close the connection when needed
            }
        } catch (IOException e) {
            System.err.println("Error closing socket: " + e.getMessage());
        }
    }
}