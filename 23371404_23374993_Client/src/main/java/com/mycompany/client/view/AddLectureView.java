package com.mycompany.client.view;

import com.mycompany.client.controller.LectureController;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.collections.ObservableList;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddLectureView extends Stage implements ClientView {
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final LectureController controller;

    public AddLectureView(LectureController controller, ObservableList<String> roomList) {
        this.controller = controller;
        
        // Initialize the UI components
        setupUI(roomList);
    }

    private void setupUI(ObservableList<String> roomList) {
        setTitle("Add Event");

        // Date Picker components
        Label dateLabel = new Label("Date:");
        DatePicker datePicker = new DatePicker();
        configureDatePicker(datePicker);

        // Error message for invalid date
        Text errorMessage = new Text();
        errorMessage.setFill(Color.RED);

        // Add date validation listener
        setupDateValidation(datePicker, errorMessage);

        // Time Input components
        Label timeLabel = new Label("Time:");
        TextField timeField = new TextField();
        timeField.setPromptText("e.g. 14:00");

        // Room Selection components
        Label roomLabel = new Label("Room Code:");
        ComboBox<String> roomBox = new ComboBox<>(roomList);
        roomBox.setEditable(true);
        roomBox.setPromptText("e.g. CSG001");

        // Module Input components
        Label moduleLabel = new Label("Module Code:");
        TextField moduleField = new TextField();
        moduleField.setPromptText("e.g. CS4076");

        // Send Request Button
        Button sendButton = new Button("Send Request");

        // Layout setup
        GridPane grid = setupGridLayout(dateLabel, datePicker, errorMessage, 
                                       timeLabel, timeField, roomLabel, roomBox, 
                                       moduleLabel, moduleField, sendButton);

        VBox addLayout = new VBox(10);
        addLayout.getChildren().add(grid);

        // Set up button event handler
        setupButtonAction(sendButton, datePicker, timeField, roomBox, moduleField, errorMessage);

        // Display the window
        setScene(new Scene(addLayout, 500, 300));
    }

    private void configureDatePicker(DatePicker datePicker) {
        datePicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return (date != null) ? dateFormatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return (string != null && !string.isEmpty()) ? LocalDate.parse(string, dateFormatter) : null;
            }
        });

        // Disable past dates, Saturdays, and Sundays
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                // Disable past dates
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #cccccc;");
                }

                // Disable weekends
                if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    setDisable(true);
                    setStyle("-fx-background-color: #cccccc;");
                }
            }
        });

        datePicker.setValue(LocalDate.now());
    }

    private void setupDateValidation(DatePicker datePicker, Text errorMessage) {
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isBefore(LocalDate.now()) || 
                newValue.getDayOfWeek() == DayOfWeek.SATURDAY || 
                newValue.getDayOfWeek() == DayOfWeek.SUNDAY) {
                errorMessage.setText("Enter a valid date");
            } else {
                errorMessage.setText("");
            }
        });
    }

    private GridPane setupGridLayout(Label dateLabel, DatePicker datePicker, Text errorMessage,
                                   Label timeLabel, TextField timeField, 
                                   Label roomLabel, ComboBox<String> roomBox,
                                   Label moduleLabel, TextField moduleField,
                                   Button sendButton) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        // Add components to grid
        grid.add(dateLabel, 0, 0);
        grid.add(datePicker, 1, 0);
        grid.add(errorMessage, 2, 0);
        grid.add(timeLabel, 0, 1);
        grid.add(timeField, 1, 1);
        grid.add(roomLabel, 0, 2);
        grid.add(roomBox, 1, 2);
        grid.add(moduleLabel, 0, 3);
        grid.add(moduleField, 1, 3);
        grid.add(sendButton, 1, 4);
        
        return grid;
    }

    private void setupButtonAction(Button sendButton, DatePicker datePicker, 
                                 TextField timeField, ComboBox<String> roomBox, 
                                 TextField moduleField, Text errorMessage) {
        sendButton.setOnAction(e -> {
            LocalDate date = datePicker.getValue();
            String time = timeField.getText().trim();
            String room = roomBox.getValue();
            String module = moduleField.getText().trim();

            // Convert room and module to uppercase if not null
            if (room != null) {
                room = room.toUpperCase();
            }
            if (module != null) {
                module = module.toUpperCase();
            }

            // Validate date before sending the request
            if (date == null || date.isBefore(LocalDate.now()) || 
                date.getDayOfWeek() == DayOfWeek.SATURDAY || 
                date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                errorMessage.setText("Enter a valid date");
                return;
            }

            // Validate input through controller
            if (!controller.validateInput("Add", date, time, room, module)) {
                controller.updateResponseArea("Error: Invalid input! Please check your format.\n");
                return;
            }

            // Send request through controller
            String response = controller.handleRequest("Add", date.toString(), time, room, module);
            
            // Close window after successful request
            this.close();
        });
    }

    @Override
    public void updateResponseArea(String message) {
        // Forward to the controller which will update the main view
        controller.updateResponseArea(message);
    }

    @Override
    public void close() {
        super.close();
    }
}