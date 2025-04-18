package com.mycompany.client.model;

public class Lecture {
    private String date;
    private String time;
    private String roomNumber;
    private String moduleName;

    public Lecture(String date, String time, String roomNumber, String moduleName) {
        this.date = date;
        this.time = time;
        this.roomNumber = roomNumber;
        this.moduleName = moduleName;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getKey() {
        return date + "-" + time + "-" + roomNumber; // Unique key for each lecture
    }

    @Override
    public String toString() {
        return "Lecture{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", roomNumber='" + roomNumber + '\'' +
                ", moduleName='" + moduleName + '\'' +
                '}';
    }
}