package com.mycompany.server.controller;

import com.mycompany.server.model.Lecture;
import com.mycompany.server.model.ScheduleManager;
import com.mycompany.server.view.ResponseFormatter;

import java.util.List;

public class LectureController {
    private final ScheduleManager scheduleManager;
    private final ResponseFormatter responseFormatter;

    public LectureController(ScheduleManager scheduleManager) {
        this.scheduleManager = scheduleManager;
        this.responseFormatter = new ResponseFormatter();
    }

    public String addLecture(String date, String time, String roomNumber, String moduleName) {
        Lecture lecture = new Lecture(date, time, roomNumber, moduleName);
        String result = scheduleManager.addLecture(lecture);

        return result.startsWith("Error")
                ? responseFormatter.formatErrorMessage(result.substring(7))
                : responseFormatter.formatSuccessMessage(result);
    }

    public String removeLecture(String date, String time, String roomNumber, String moduleName) {
        Lecture lecture = new Lecture(date, time, roomNumber, moduleName);
        String result = scheduleManager.removeLecture(lecture);

        return result.startsWith("Error")
                ? responseFormatter.formatErrorMessage(result.substring(7))
                : responseFormatter.formatSuccessMessage(result);
    }

    public String getSchedule() {
        List<Lecture> lectures = scheduleManager.getSchedule();
        return responseFormatter.formatSchedule(lectures);
    }

    public String shiftToEarlyLectures() {
        String result = scheduleManager.shiftLecturesToMorning();
        return responseFormatter.formatSuccessMessage(result);
    }
}
