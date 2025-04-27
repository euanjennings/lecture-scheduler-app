package com.mycompany.client.view;

import com.mycompany.client.controller.LectureController;
import javafx.collections.ObservableList;
import java.time.LocalDate;

public class AddLectureView extends AbstractLectureFormView {

    public AddLectureView(LectureController controller, ObservableList<String> roomList) {
        super(controller, roomList, "Add Lecture");
    }

    @Override
    protected void handleSendButton() {
        LocalDate date = datePicker.getValue();
        String time = timeField.getText().trim();
        String room = roomBox.getValue() != null ? roomBox.getValue().toUpperCase() : "";
        String module = moduleField.getText().trim().toUpperCase();

        if (date == null || date.isBefore(LocalDate.now()) ||
            date.getDayOfWeek() == java.time.DayOfWeek.SATURDAY ||
            date.getDayOfWeek() == java.time.DayOfWeek.SUNDAY) {
            errorMessage.setText("Enter a valid date");
            return;
        }

        if (!controller.validateInput("Add", date, time, room, module)) {
            controller.updateResponseArea("Error: Invalid input! Please check your format.\n");
            return;
        }

        controller.handleRequest("Add", date.toString(), time, room, module);
        this.close();
    }
}
