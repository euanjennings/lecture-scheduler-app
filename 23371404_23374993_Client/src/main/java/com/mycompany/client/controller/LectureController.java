package com.mycompany.client.controller;

import com.mycompany.client.model.Lecture;
import com.mycompany.client.model.LectureModel;
import com.mycompany.client.view.ClientMainView;
import com.mycompany.client.view.AddLectureView;
import com.mycompany.client.view.RemoveLectureView;
import com.mycompany.client.view.DisplayLectureView;

import java.time.LocalDate;
import java.util.List;
import javafx.collections.ObservableList;

public class LectureController {
    private final ClientMainView mainView;
    private final LectureModel model;
    private final ObservableList<String> roomList;

    public LectureController(ClientMainView mainView, ObservableList<String> roomList, LectureModel model) {
        this.mainView = mainView;
        this.roomList = roomList;
        this.model = model;
    }

    public void openAddView() {
        new AddLectureView(this, roomList).show();
    }

    public void openRemoveView() {
        new RemoveLectureView(this, roomList).show();
    }

    public void openDisplayView() {
        new DisplayLectureView(this).show();
    }

    public String handleRequest(String action, String date, String time, String room, String module) {
        if (!action.equals("Display")) {
            logRequest(action, date, time, room, module);
        }

        String response = model.sendRequest(action, date, time, room, module);

        if (action.equals("STOP") && response.equals("TERMINATE")) {
            mainView.updateResponseArea("Connection closed.\n");
            model.closeConnection();
        } else if (!action.equals("Display")) {
            mainView.updateResponseArea("[SERVER RESPONSE]\n" + response + "\n");
        }

        return response;
    }

    private void logRequest(String action, String date, String time, String room, String module) {
        String formattedMessage = String.format(
            "[CLIENT REQUEST]\nAction: %s\nDate: %s\nTime: %s\nRoom: %s\nModule: %s\n",
            action, date.isEmpty() ? "N/A" : date, time.isEmpty() ? "N/A" : time,
            room.isEmpty() ? "N/A" : room, module.isEmpty() ? "N/A" : module
        );
        mainView.updateResponseArea(formattedMessage);
    }

    public boolean validateInput(String action, LocalDate date, String time, String room, String module) {
        return model.validateInput(action, date, time, room, module);
    }

    public List<Lecture> getLectures(String response) {
        return model.getLectures(response);
    }

    public void updateResponseArea(String message) {
        mainView.updateResponseArea(message);
    }

    public void requestEarlyShift() {
        String response = handleRequest("Early", "", "", "", "");
        mainView.updateResponseArea(response);
    }
}