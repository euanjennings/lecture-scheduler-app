package com.mycompany.server.controller;

import com.mycompany.server.model.Lecture;
import com.mycompany.server.model.ScheduleManager;
import com.mycompany.server.view.ResponseFormatter;

public class LectureController {
    private final ScheduleManager scheduleManager;
    private final ResponseFormatter responseFormatter;

    public LectureController(ScheduleManager scheduleManager) {
        this.scheduleManager = scheduleManager;
        this.responseFormatter = new ResponseFormatter();
    }

    public String addLecture(String date, String time, String room, String module) {
        try {
            Lecture lecture = new Lecture(date, time, room, module);
            String result = scheduleManager.addLecture(lecture);
            return responseFormatter.formatSuccessMessage(result);
        } catch (Exception e) {
            return responseFormatter.formatErrorMessage(e.getMessage());
        }
    }

    public String removeLecture(String date, String time, String room, String module) {
        try {
            Lecture lecture = new Lecture(date, time, room, module);
            String result = scheduleManager.removeLecture(lecture);
            return responseFormatter.formatSuccessMessage(result);
        } catch (Exception e) {
            return responseFormatter.formatErrorMessage(e.getMessage());
        }
    }

    public String getSchedule() {
        try {
            return responseFormatter.formatSchedule(scheduleManager.getSchedule());
        } catch (Exception e) {
            return responseFormatter.formatErrorMessage(e.getMessage());
        }
    }

    public String shiftToEarlyLectures() {
        try {
            String result = scheduleManager.shiftLecturesToMorning();
            return responseFormatter.formatSuccessMessage(result);
        } catch (Exception e) {
            return responseFormatter.formatErrorMessage(e.getMessage());
        }
    }
}
