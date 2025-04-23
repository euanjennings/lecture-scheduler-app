package com.mycompany.server.service;

import com.mycompany.server.controller.LectureController;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LectureProcessingService {
    private final LectureController lectureController;
    private final ExecutorService executor = Executors.newCachedThreadPool();

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

    public String processEarlyLectures() {
        Future<String> future = executor.submit(() -> lectureController.shiftToEarlyLectures());
        try {
            return future.get();
        } catch (Exception e) {
            return "Error processing early lectures: " + e.getMessage();
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}