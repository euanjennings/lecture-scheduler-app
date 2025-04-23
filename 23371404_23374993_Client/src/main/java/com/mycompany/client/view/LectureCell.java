package com.mycompany.client.view;

import com.mycompany.client.controller.LectureController;
import com.mycompany.client.model.Lecture;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;

/**
 * A simple HBox that displays a single lecture and a right-click context menu.
 */
public class LectureCell extends HBox {
    public LectureCell(Lecture lecture, LectureController controller) {
        // Display text
        Label label = new Label(
            lecture.getDate() + " " +
            lecture.getTime() + " | " +
            lecture.getRoomNumber() + " | " +
            lecture.getModuleName()
        );
        getChildren().add(label);

        // Build context menu
        ContextMenu menu = new ContextMenu();

        // Remove item
        MenuItem removeItem = new MenuItem("Remove");
        removeItem.setOnAction(e -> {
            controller.handleRequest(
                "Remove",
                lecture.getDate(),
                lecture.getTime(),
                lecture.getRoomNumber(),
                lecture.getModuleName()
            );
            controller.refreshSchedule();
        });

        // Reschedule item
        MenuItem rescheduleItem = new MenuItem("Reschedule");
        rescheduleItem.setOnAction(e -> controller.openRescheduleView(lecture));

        menu.getItems().addAll(removeItem, rescheduleItem);

        // Show on right-click
        setOnContextMenuRequested((ContextMenuEvent evt) -> {
            menu.show(this, evt.getScreenX(), evt.getScreenY());
            evt.consume();
        });

        // some padding
        setSpacing(10);
    }
}
