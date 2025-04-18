package com.mycompany.client.view;

import com.mycompany.client.controller.LectureController;
import com.mycompany.client.model.Lecture;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DisplayLectureView extends Stage implements ClientView {
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final LectureController controller;
    private GridPane grid;
    private DatePicker datePicker;

    public DisplayLectureView(LectureController controller) {
        this.controller = controller;
        
        setupUI();
        updateDisplay(LocalDate.now());
    }

    private void setupUI() {
        setTitle("Display Events");

        datePicker = new DatePicker(LocalDate.now());
        configureDatePicker();

        grid = new GridPane();
        configureGridPane();

        VBox displayLayout = new VBox(10);
        displayLayout.getChildren().addAll(datePicker, grid);

        setScene(new Scene(displayLayout, 1000, 600));
    }

    private void configureDatePicker() {
        datePicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? dateFormatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return string != null && !string.isEmpty() ? LocalDate.parse(string, dateFormatter) : null;
            }
        });

        datePicker.setDayCellFactory(new Callback<>() {
            @Override
            public DateCell call(final DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        if (date.isBefore(LocalDate.now()) ||
                            date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                            date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                            setDisable(true);
                            setStyle("-fx-background-color: #cccccc;");
                        }
                    }
                };
            }
        });

        datePicker.setOnAction(e -> updateDisplay(datePicker.getValue()));
    }

    private void configureGridPane() {
        grid.setHgap(2);
        grid.setVgap(2);
        grid.setPadding(new Insets(10));
        grid.setStyle("-fx-background-color: #f5f5f5;");
    }

    private void updateDisplay(LocalDate selectedDate) {
        grid.getChildren().clear();

        LocalDate startOfWeek = selectedDate.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(4);

        String response = controller.handleRequest("Display", "", "", "", "");
        List<Lecture> lectures = controller.getLectures(response);

        setupGridHeaders(startOfWeek);
        setupTimeRows();
        setupEmptyPlaceholders();
        populateLectures(lectures, startOfWeek, endOfWeek);
    }

    private void setupGridHeaders(LocalDate startOfWeek) {
        String[] days = {"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        for (int i = 0; i < days.length; i++) {
            String headerText = i == 0 ? days[i] :
                days[i] + " (" + startOfWeek.plusDays(i - 1).format(dateFormatter) + ")";

            Label dayLabel = new Label(headerText);
            dayLabel.setStyle("-fx-font-weight: bold; -fx-alignment: center; -fx-padding: 5px;");
            dayLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            dayLabel.setAlignment(Pos.CENTER);
            GridPane.setHgrow(dayLabel, Priority.ALWAYS);
            grid.add(dayLabel, i, 0);
        }
    }

    private void setupTimeRows() {
        for (int hour = 9; hour <= 18; hour++) {
            Label timeLabel = new Label(String.format("%02d:00", hour));
            timeLabel.setStyle("-fx-font-weight: bold; -fx-alignment: center;");
            timeLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            timeLabel.setAlignment(Pos.CENTER);
            grid.add(timeLabel, 0, hour - 8);
        }
    }

    private void setupEmptyPlaceholders() {
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 10; j++) {
                VBox placeholder = new VBox();
                placeholder.setStyle("-fx-border-color: lightgray; -fx-border-width: 1; -fx-background-color: white;");
                placeholder.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                placeholder.setMinSize(120, 50);
                placeholder.setAlignment(Pos.CENTER);
                grid.add(placeholder, i, j);
            }
        }
    }

    private void populateLectures(List<Lecture> lectures, LocalDate startOfWeek, LocalDate endOfWeek) {
        Map<String, String> moduleColors = new HashMap<>();
        String[] colors = {"#F4A261", "#E76F51", "#2A9D8F", "#E9C46A", "#264653", "#9B5DE5", "#00A896", "#FFD166"};
        int colorIndex = 0;

        for (Lecture lecture : lectures) {
            try {
                LocalDate date = LocalDate.parse(lecture.getDate(), dateFormatter);
                if (date.isBefore(startOfWeek) || date.isAfter(endOfWeek)) continue;

                String time = lecture.getTime();
                int startHour = Integer.parseInt(time.split(":")[0]);
                String day = date.getDayOfWeek().toString();

                int dayColumn;
                switch (day) {
                    case "MONDAY": dayColumn = 1; break;
                    case "TUESDAY": dayColumn = 2; break;
                    case "WEDNESDAY": dayColumn = 3; break;
                    case "THURSDAY": dayColumn = 4; break;
                    case "FRIDAY": dayColumn = 5; break;
                    default: dayColumn = -1; break;
                }
                
                if (dayColumn == -1) continue;

                int row = startHour - 8;

                String module = lecture.getModuleName();
                moduleColors.putIfAbsent(module, colors[colorIndex++ % colors.length]);
                String eventColor = moduleColors.get(module);

                VBox eventBox = createEventBox(lecture, eventColor);
                grid.getChildren().removeIf(node ->
                        GridPane.getColumnIndex(node) == dayColumn && GridPane.getRowIndex(node) == row);
                grid.add(eventBox, dayColumn, row);
            } catch (Exception e) {
                System.err.println("Error parsing lecture: " + lecture + " - " + e.getMessage());
            }
        }
    }

    private VBox createEventBox(Lecture lecture, String eventColor) {
        VBox eventBox = new VBox(3);
        eventBox.setStyle("-fx-background-color: " + eventColor + "; " +
                "-fx-border-color: black; -fx-border-width: 1; -fx-padding: 5; " +
                "-fx-alignment: center;");

        eventBox.getChildren().addAll(
                createStyledLabel(lecture.getModuleName(), "white", "bold"),
                createStyledLabel(lecture.getTime(), "white", "normal"),
                createStyledLabel(lecture.getRoomNumber(), "white", "normal")
        );

        eventBox.setMinSize(120, 50);
        GridPane.setFillWidth(eventBox, true);
        GridPane.setFillHeight(eventBox, true);

        eventBox.setOnMouseClicked(e -> {
            boolean confirmed = showConfirmationDialog(lecture);
            if (confirmed) {
                String response = controller.handleRequest(
                        "Remove",
                        lecture.getDate(),
                        lecture.getTime(),
                        lecture.getRoomNumber(),
                        lecture.getModuleName()
                );
                controller.updateResponseArea(response);
                updateDisplay(LocalDate.parse(lecture.getDate()));
            }
        });

        return eventBox;
    }

    private Label createStyledLabel(String text, String textColor, String fontWeight) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: " + textColor + "; -fx-font-weight: " + fontWeight + "; -fx-padding: 3px;");
        label.setAlignment(Pos.CENTER);
        label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        return label;
    }

    private boolean showConfirmationDialog(Lecture lecture) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Remove Lecture");
        alert.setHeaderText("Are you sure you want to remove this lecture?");
        alert.setContentText(String.format("Module: %s\nDate: %s\nTime: %s\nRoom: %s",
                lecture.getModuleName(), lecture.getDate(), lecture.getTime(), lecture.getRoomNumber()));

        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }

    @Override
    public void updateResponseArea(String message) {
        controller.updateResponseArea(message);
    }

    @Override
    public void close() {
        super.close();
    }
}
