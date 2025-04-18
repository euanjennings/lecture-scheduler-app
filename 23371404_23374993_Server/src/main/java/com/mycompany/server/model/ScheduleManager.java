package com.mycompany.server.model;

import com.mycompany.server.model.Lecture;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleManager {
    private Map<String, Lecture> schedule; // Store lectures in a HashMap

    public ScheduleManager() {
        schedule = new HashMap<>();
    }

    public String addLecture(Lecture lecture) {
        // Check for scheduling clashes
        if (schedule.containsKey(lecture.getKey())) {
            return "Error: Lecture clash detected.";
        }

        // Add the lecture to the schedule
        schedule.put(lecture.getKey(), lecture);
        return "Lecture added successfully.";
    }

    public String removeLecture(Lecture lecture) {
        if (schedule.remove(lecture.getKey()) != null) {
            return "Lecture removed successfully.";
        } else {
            return "Error: Lecture not found.";
        }
    }

    public List<Lecture> getSchedule() {
        return new ArrayList<>(schedule.values()); // Return a list of all lectures
    }

    public String displaySchedule() {
        if (schedule.isEmpty()) {
            return "No lectures scheduled.";
        }

        StringBuilder sb = new StringBuilder();
        for (Lecture lecture : schedule.values()) {
            sb.append(lecture.getDate()).append(",")
              .append(lecture.getTime()).append(",")
              .append(lecture.getRoomNumber()).append(",")
              .append(lecture.getModuleName()).append(";");
        }
        return sb.toString();
    }
}