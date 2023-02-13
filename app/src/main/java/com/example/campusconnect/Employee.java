package com.example.campusconnect;

public class Employee {
    private int day;
    private String timeAM_In;
    private String timeAM_Out;
    private String timePM_In;
    private String timePM_Out;

    public Employee(int day, String timeAM_In, String timeAM_Out, String timePM_In, String timePM_Out) {
        this.day = day;
        this.timeAM_In = timeAM_In;
        this.timeAM_Out = timeAM_Out;
        this.timePM_In = timePM_In;
        this.timePM_Out = timePM_Out;
    }

    public int getDay() {
        return day;
    }

    public String getTimeAM_In() {
        return timeAM_In;
    }

    public String getTimeAM_Out() {
        return timeAM_Out;
    }

    public String getTimePM_In() {
        return timePM_In;
    }

    public String getTimePM_Out() {
        return timePM_Out;
    }
}
