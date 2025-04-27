package com.mycompany.server.controller;

import javafx.concurrent.Task;

public class LectureProcessingService {
    private final LectureController lectureController;

    public LectureProcessingService(LectureController lectureController) {
        this.lectureController = lectureController;
    }

    public String addLecture(String date, String time, String room, String module) {
        return lectureController.addLecture(date, time, room, module);
    }

    public String removeLecture(String date, String time, String room, String module) {
        return lectureController.removeLecture(date, time, room, module);
    }

    public String getSchedule() {
        return lectureController.getSchedule();
    }

    public String shiftToEarlyLectures() {
        return lectureController.shiftToEarlyLectures();
    }

    public Task<String> processEarlyLectures() {
        Task<String> task = new Task<>() {
            @Override
            protected String call() {
                return lectureController.shiftToEarlyLectures();
            }
        };

        task.setOnSucceeded(event -> {
            System.out.println("Early lectures processed successfully.");
        });

        task.setOnFailed(event -> {
            System.err.println("Failed to process early lectures: " + task.getException());
        });

        return task;
    }
}
