package com.mycompany.server.view;

import com.mycompany.server.model.Lecture;
import java.util.List;

public class ResponseFormatter {
    
    public String formatSuccessMessage(String message) {
        return "SUCCESS: " + message;
    }
    
    public String formatErrorMessage(String message) {
        return "ERROR: " + message;
    }
    
    public String formatSchedule(List<Lecture> lectures) {
        if (lectures.isEmpty()) {
            return "No lectures scheduled.";
        }
        
        StringBuilder sb = new StringBuilder();
        for (Lecture lecture : lectures) {
            sb.append(lecture.getDate()).append(",")
              .append(lecture.getTime()).append(",")
              .append(lecture.getRoomNumber()).append(",")
              .append(lecture.getModuleName()).append(";");
        }
        return sb.toString();
    }
}