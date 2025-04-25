package com.mycompany.server.service;

import com.mycompany.server.controller.LectureController;
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

    // Synchronous processing of early lectures
    public String shiftToEarlyLectures() {
        return lectureController.shiftToEarlyLectures();  // Performs the shift and returns updated schedule
    }

    // Optional: still support background task, if needed elsewhere
    public Task<String> processEarlyLectures() {
        Task<String> task = new Task<>() {
            @Override
            protected String call() {
                return lectureController.shiftToEarlyLectures();
            }
        };

        task.setOnSucceeded(event -> {
            System.out.println("Early lectures processed successfully!");
        });

        task.setOnFailed(event -> {
            System.err.println("Error processing early lectures: " + task.getException().getMessage());
        });

        return task;
    }

    public void shutdown() {
       
    }
}
