/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.client.model;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.util.List;

public class LectureModel {
    private final LectureService lectureService;

    public LectureModel(Socket socket) throws IOException {
        this.lectureService = new LectureService(socket);
    }

    public String sendRequest(String action, String date, String time, String room, String module) {
        return lectureService.sendRequest(action, date, time, room, module);
    }

    public List<Lecture> getLectures(String response) {
        return lectureService.parseLectures(response);
    }

    public boolean validateInput(String action, LocalDate date, String time, String room, String module) {
        if (action == null || action.isEmpty()) return false;
        if (action.equals("Display")) return true;
        if (date == null || room == null) return false;
        if (!time.matches("^([01]\\d|2[0-3]):[0-5]\\d$")) return false;
        if (!module.matches("^[A-Za-z]{2,5}\\d{3,4}$")) return false;
        return true;
    }

    public void closeConnection() {
        lectureService.closeConnection();
    }
}