package com.mycompany.server.controller;

import com.mycompany.server.exception.IncorrectActionException;
import com.mycompany.server.view.ResponseFormatter;

public class CommandProcessor {
    private final LectureController lectureController;
    private final ResponseFormatter responseFormatter;

    public CommandProcessor(LectureController lectureController) {
        this.lectureController = lectureController;
        this.responseFormatter = new ResponseFormatter();
    }

    public String processCommand(String command) {
        String[] parts = command.split(",");
        if (parts.length < 1) {
            return responseFormatter.formatErrorMessage("Invalid request format");
        }

        String action = parts[0].trim();
        try {
            return switch (action) {
                case "Add" -> lectureController.addLecture(parts[1], parts[2], parts[3], parts[4]);
                case "Remove" -> lectureController.removeLecture(parts[1], parts[2], parts[3], parts[4]);
                case "Display" -> lectureController.getSchedule();
                case "Early" -> lectureController.shiftToEarlyLectures();
                case "STOP" -> "TERMINATE";
                default -> throw new IncorrectActionException("Invalid action: " + action);
            };
        } catch (IncorrectActionException e) {
            return responseFormatter.formatErrorMessage(e.getMessage());
        } catch (Exception e) {
            return responseFormatter.formatErrorMessage("Server error: " + e.getMessage());
        }
    }
}
