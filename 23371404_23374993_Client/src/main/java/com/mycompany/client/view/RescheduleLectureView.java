package com.mycompany.client.view;

import com.mycompany.client.controller.LectureController;
import com.mycompany.client.model.Lecture;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A window that lets you change the details of a lecture.
 */
public class RescheduleLectureView extends Stage {
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public RescheduleLectureView(LectureController controller, Lecture lecture) {
        setTitle("Reschedule Lecture");

        var grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Date
        grid.add(new Label("Date:"), 0, 0);
        DatePicker dp = new DatePicker(LocalDate.parse(lecture.getDate(), DATE_FMT));
        grid.add(dp, 1, 0);

        // Time
        grid.add(new Label("Time (HH:mm):"), 0, 1);
        TextField tfTime = new TextField(lecture.getTime());
        grid.add(tfTime, 1, 1);

        // Room
        grid.add(new Label("Room:"), 0, 2);
        TextField tfRoom = new TextField(lecture.getRoomNumber());
        grid.add(tfRoom, 1, 2);

        // Module
        grid.add(new Label("Module:"), 0, 3);
        TextField tfMod = new TextField(lecture.getModuleName());
        grid.add(tfMod, 1, 3);

        // Submit
        Button btn = new Button("Submit");
        grid.add(btn, 1, 4);

        btn.setOnAction(e -> {
            String newDate = dp.getValue().format(DATE_FMT);
            String newTime = tfTime.getText().trim();
            String newRoom = tfRoom.getText().trim();
            String newMod  = tfMod.getText().trim();

            // 1) Remove old
            controller.handleRequest(
                "Remove",
                lecture.getDate(),
                lecture.getTime(),
                lecture.getRoomNumber(),
                lecture.getModuleName()
            );
            // 2) Add new
            controller.handleRequest(
                "Add",
                newDate,
                newTime,
                newRoom,
                newMod
            );
            controller.refreshSchedule();
            close();
        });

        setScene(new Scene(grid, 350, 220));
    }
}