package com.mycompany.client.view;

import com.mycompany.client.controller.LectureController;
import com.mycompany.client.model.LectureModel;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class ClientMainView extends Application implements ClientView {
    private LectureController controller;
    private TextArea responseArea;
    private Socket socket;

    private final ObservableList<String> roomList = FXCollections.observableArrayList(
        Arrays.asList("A0049", "A0050", "A0060A", "A0060B", "A0060C", "A1050", "A1051", "A1052", "A1053", "A1054", "A1055",
                      "A1089", "A1090", "A1095", "A2005", "A2011", "A2012", "A3009", "A3009A", "AD0034", "AD2010", "AM038", "AM061",
                      "B0005", "B0006", "B0010", "B0013", "B0027", "B0029", "B0030", "B1005", "B1005A", "B1023", "B2005", "B2006",
                      "B2011", "B2041", "B2043", "B3005", "B3022", "B3053", "BM015", "BM037", "C0043", "C0052", "C0078", "C0079",
                      "C1058", "C1059", "C1060", "C1061", "C1062", "C1063", "C2062", "CG022", "CG024", "CG025", "CG029", "CG053",
                      "CG054", "CG055", "CG057", "CG058", "CG059", "CM010", "CM083A", "CM083B", "CM085", "CS1044", "CS1045", "CS2044",
                      "CS2046", "CS3004B", "CS3005A", "CS3005B", "CSG001", "CSG025", "D1050", "DG016", "E1022", "E1023", "EG002", "EG003",
                      "EG004", "EG006", "EG010", "EM009", "EM010", "ER0013", "ER2011", "ER2029A", "ERB001", "ERB006", "ERB007", "ERB008",
                      "ERO008", "ERO009", "FB003S1", "FB003S2", "FB003S3", "FB009", "FB012", "FB028", "FG042", "FG061", "GEMS0016",
                      "GEMS1023", "HS1006", "HS1014", "HS1022", "HS1023", "HS1026", "HS1031", "HS1032", "HS1035", "HS2005", "HS2009",
                      "HS2012", "HSG008", "HSG008A", "HSG021", "HSG022", "HSG023", "HSG024", "HSG025", "HSG030", "HSG031", "HSG037",
                      "KB111", "KB112", "KB118", "KB119", "KB121A", "KB121B", "KB122", "KBG10", "KBG11", "KBG12", "KBG13", "KBG14",
                      "KBG15", "LB015", "LC1002", "LC1003", "LC1016", "LCB002", "LCB003", "LCB009", "LCB009A", "LCB010", "LCB015",
                      "LCO002", "LCO003", "LCO017", "LG011", "P1003", "P1004", "P1005", "P1006", "P1007", "P1029", "P1031", "P1033",
                      "PG008", "PG039", "PG040", "PG050", "PG053", "S114", "S115", "S116", "S117", "S119", "S201", "S204", "S205",
                      "S206", "S207", "SG15", "SG16", "SG17", "SG18", "SG19", "SG20", "SG21", "SG21A", "SR1014A", "SR1014B", "SR1017",
                      "SR1020", "SR2027", "SR2028", "SR2029", "SR2030", "SR2031", "SR2032", "SR2044", "SR2046", "SR2047", "SR2057",
                      "SR2058", "SR2064", "SR2065", "SR2071", "SR3004A", "SR3004B", "SR3005A", "SR3005B", "SR3006", "SR3007", "SR3008",
                      "SR4111")
    );

    @Override
    public void start(Stage primaryStage) {
        try {
            socket = new Socket("localhost", 1234);
            LectureModel model = new LectureModel(socket);
            controller = new LectureController(this, roomList, model);
            setupUI(primaryStage);
        } catch (IOException e) {
            System.err.println("Error connecting to the server: " + e.getMessage());
        }
    }

    private void setupUI(Stage primaryStage) {
        Button addButton = new Button("Add");
        Button removeButton = new Button("Remove");
        Button displayButton = new Button("Display");
        Button earlyButton = new Button("Early Lectures");
        Button otherButton = new Button("Other");
        Button stopButton = new Button("Stop");

        responseArea = new TextArea();
        responseArea.setEditable(false);

        VBox mainLayout = new VBox(10);
        mainLayout.getChildren().addAll(
            addButton, removeButton, displayButton, earlyButton,
            otherButton, stopButton, responseArea
        );

        addButton.setOnAction(e -> controller.openAddView());
        removeButton.setOnAction(e -> controller.openRemoveView());
        displayButton.setOnAction(e -> controller.openDisplayView());
        earlyButton.setOnAction(e -> controller.requestEarlyShift());
        otherButton.setOnAction(e -> controller.handleRequest("Something", "", "", "", ""));
        stopButton.setOnAction(e -> controller.handleRequest("STOP", "", "", "", ""));

        Scene mainScene = new Scene(mainLayout, 600, 400);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Lecture Scheduler Client");
        primaryStage.show();
    }

    @Override
    public void updateResponseArea(String message) {
        responseArea.appendText(message + "\n");
    }

    @Override
    public void close() {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing socket: " + e.getMessage());
        }
    }

    @Override
    public void show() {}

    public static void main(String[] args) {
        launch(args);
    }
}
