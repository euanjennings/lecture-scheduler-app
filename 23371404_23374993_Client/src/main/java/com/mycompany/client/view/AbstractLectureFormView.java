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

public abstract class AbstractLectureFormView extends Stage implements ClientView {
    protected final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    protected final LectureController controller;
    protected final ObservableList<String> roomList;
    protected DatePicker datePicker;
    protected TextField timeField;
    protected ComboBox<String> roomBox;
    protected TextField moduleField;
    protected Text errorMessage;

    public AbstractLectureFormView(LectureController controller, ObservableList<String> roomList, String title) {
        this.controller = controller;
        this.roomList = roomList;
        setupUI(title);
    }

    private void setupUI(String title) {
        setTitle(title);

        Label dateLabel = new Label("Date:");
        datePicker = new DatePicker();
        configureDatePicker(datePicker);

        errorMessage = new Text();
        errorMessage.setFill(Color.RED);
        setupDateValidation(datePicker, errorMessage);

        Label timeLabel = new Label("Time:");
        timeField = new TextField();
        timeField.setPromptText("e.g. 14:30");

        Label roomLabel = new Label("Room Code:");
        roomBox = new ComboBox<>(roomList);
        roomBox.setEditable(true);
        roomBox.setPromptText("e.g. CSG001");

        Label moduleLabel = new Label("Module Code:");
        moduleField = new TextField();
        moduleField.setPromptText("e.g. CS4076");

        Button sendButton = new Button("Send Request");
        sendButton.setOnAction(e -> handleSendButton());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
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

        VBox layout = new VBox(10);
        layout.getChildren().add(grid);

        setScene(new Scene(layout, 500, 300));
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

        datePicker.setDayCellFactory(picker -> new DateCell() {
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

    protected abstract void handleSendButton();

    @Override
    public void updateResponseArea(String message) {
        controller.updateResponseArea(message);
    }

    @Override
    public void close() {
        super.close();
    }
}
