package com.mycompany.server.controller;

import com.mycompany.server.exception.IncorrectActionException;
import com.mycompany.server.service.LectureProcessingService;
import com.mycompany.server.view.ResponseFormatter;

public class CommandProcessor {
    private final LectureProcessingService processingService;
    private final ResponseFormatter responseFormatter;

    public CommandProcessor(LectureController lectureController) {
        this.processingService = new LectureProcessingService(lectureController);
        this.responseFormatter = new ResponseFormatter();
    }

    public String processCommand(String command) {
        String[] parts = command.split(",");
        if (parts.length < 1) {
            return responseFormatter.formatErrorMessage("Invalid request format");
        }

        String action = parts[0].trim();
        try {
            switch (action) {
                case "Add":
                    if (parts.length < 5) throw new IncorrectActionException("Add requires 4 parameters");
                    return processingService.addLecture(parts[1], parts[2], parts[3], parts[4]);
                
                case "Remove":
                    if (parts.length < 5) throw new IncorrectActionException("Remove requires 4 parameters");
                    return processingService.removeLecture(parts[1], parts[2], parts[3], parts[4]);
                
                case "Display":
                    return processingService.getSchedule();
                
                case "Early":
                    return processingService.processEarlyLectures();
                
                case "STOP":
                    processingService.shutdown();
                    return "TERMINATE";
                
                default:
                    throw new IncorrectActionException("Invalid action: " + action);
            }
        } catch (Exception e) {
            return responseFormatter.formatErrorMessage(
                e instanceof IncorrectActionException ? e.getMessage() : "Server error: " + e.getMessage()
            );
        }
    }
}