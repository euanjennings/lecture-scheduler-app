package com.mycompany.server.controller;

import com.mycompany.server.model.Lecture;
import com.mycompany.server.model.ScheduleManager;
import com.mycompany.server.view.ResponseFormatter;
import java.util.List;

public class LectureController {
    private final ScheduleManager scheduleManager;
    private final ResponseFormatter responseFormatter;
    
    public LectureController() {
        this.scheduleManager = new ScheduleManager();
        this.responseFormatter = new ResponseFormatter();
    }
    
    public String addLecture(String date, String time, String roomNumber, String moduleName) {
        Lecture lecture = new Lecture(date, time, roomNumber, moduleName);
        String result = scheduleManager.addLecture(lecture);
        
        if (result.startsWith("Error")) {
            return responseFormatter.formatErrorMessage(result.substring(7));
        } else {
            return responseFormatter.formatSuccessMessage(result);
        }
    }
    
    public String removeLecture(String date, String time, String roomNumber, String moduleName) {
        Lecture lecture = new Lecture(date, time, roomNumber, moduleName);
        String result = scheduleManager.removeLecture(lecture);
        
        if (result.startsWith("Error")) {
            return responseFormatter.formatErrorMessage(result.substring(7));
        } else {
            return responseFormatter.formatSuccessMessage(result);
        }
    }
    
    public String getSchedule() {
        List<Lecture> lectures = scheduleManager.getSchedule();
        return responseFormatter.formatSchedule(lectures);
    }
}