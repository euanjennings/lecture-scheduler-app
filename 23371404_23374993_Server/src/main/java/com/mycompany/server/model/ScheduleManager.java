// ScheduleManager.java
package com.mycompany.server.model;

import java.time.LocalTime;
import java.util.*;

public class ScheduleManager {
    private final Map<String, Lecture> schedule = new HashMap<>();

    public synchronized String addLecture(Lecture lecture) {
        if (schedule.containsKey(lecture.getKey())) {
            return "Error: Lecture clash detected.";
        }
        schedule.put(lecture.getKey(), lecture);
        return "Lecture added successfully.";
    }

    public synchronized String removeLecture(Lecture lecture) {
        return (schedule.remove(lecture.getKey()) != null)
                ? "Lecture removed successfully."
                : "Error: Lecture not found.";
    }

    public synchronized List<Lecture> getSchedule() {
        return new ArrayList<>(schedule.values());
    }

    public String shiftLecturesToMorning() {
        Map<String, List<Lecture>> byDay = new HashMap<>();
        List<Thread> threads = new ArrayList<>();

        synchronized (this) {
            for (Lecture lecture : schedule.values()) {
                String date = lecture.getDate();
                byDay.putIfAbsent(date, new ArrayList<>());
                byDay.get(date).add(lecture);
            }
        }

        for (String date : byDay.keySet()) {
            Thread t = new Thread(() -> shiftDayLectures(date, byDay.get(date)));
            threads.add(t);
            t.start();
        }

        for (Thread t : threads) {
            try { t.join(); } catch (InterruptedException ignored) {}
        }

        return "Lectures rescheduled to earlier time slots where available.";
    }

    private synchronized void shiftDayLectures(String date, List<Lecture> lectures) {
        Set<String> usedTimes = new HashSet<>();
        for (Lecture l : lectures) {
            usedTimes.add(l.getTime());
        }

        List<Lecture> moved = new ArrayList<>();
        for (Lecture l : lectures) {
            String oldKey = l.getKey();
            int originalHour = Integer.parseInt(l.getTime().split(":")[0]);
            int newHour = 9;

            while (newHour < originalHour) {
                String newTime = String.format("%02d:00", newHour);
                String newKey = date + "-" + newTime + "-" + l.getRoomNumber();

                if (!schedule.containsKey(newKey) && !usedTimes.contains(newTime)) {
                    schedule.remove(oldKey);
                    Lecture newLecture = new Lecture(date, newTime, l.getRoomNumber(), l.getModuleName());
                    schedule.put(newKey, newLecture);
                    usedTimes.add(newTime);
                    moved.add(newLecture);
                    break;
                }
                newHour++;
            }
        }
    }
}
