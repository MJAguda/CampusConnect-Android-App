package com.ams.campusconnect.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Timelog {
    private String requestID;
    private String requestorID;
    private int requestorSchoolID;
    private String typeOfTimeLogIssue;
    private String month, day, year;
    private String timelogSession;
    private String correctTimelog;
    private String reasonForChange;

    public Timelog(String requestID, String requestorID, int requestorSchoolID, String typeOfTimeLogIssue, String month, String day, String year, String timelogSession, String correctTimelog, String reasonForChange) {
        this.requestID = requestID;
        this.requestorID = requestorID;
        this.requestorSchoolID = requestorSchoolID;
        this.typeOfTimeLogIssue = typeOfTimeLogIssue;
        this.month = month;
        this.day = day;
        this.year = year;
        this.timelogSession = timelogSession;
        this.correctTimelog = correctTimelog;
        this.reasonForChange = reasonForChange;
    }

    public Timelog(){}

    // Getters
    public String getRequestID() { return requestID; }
    public String getRequestorID() { return requestorID; }
    public int getRequestorSchoolID() { return requestorSchoolID; }
    public String getTypeOfTimeLogIssue() { return typeOfTimeLogIssue; }
    public String getMonth() { return month; }
    public String getDay() { return day; }
    public String getYear() { return year; }
    public String getTimelogSession() { return timelogSession; }
    public String getCorrectTimelog() { return correctTimelog; }
    public String getReasonForChange() { return reasonForChange; }

    // Setters
    public void setRequestID(String requestID) { this.requestID = requestID; }
    public void setRequestorID(String requestorID) { this.requestorID = requestorID; }
    public void setRequestorSchoolID(int requestorSchoolID) { this.requestorSchoolID = requestorSchoolID; }
    public void setTypeOfTimeLogIssue(String typeOfTimeLogIssue) { this.typeOfTimeLogIssue = typeOfTimeLogIssue; }
    public void setMonth(String month) { this.month = month; }
    public void setDay(String day) { this.day = day; }
    public void setYear(String year) { this.year = year; }
    public void setTimelogSession(String timelogSession) { this.timelogSession = timelogSession; }
    public void setCorrectTimelog(String correctTimelog) { this.correctTimelog = correctTimelog; }
    public void setReasonForChange(String reasonForChange) { this.reasonForChange = reasonForChange; }

    @Override
    public String toString() {
        return "Timelog{" +
                "requestorID='" + requestorID + '\'' +
                ", requestorSchoolID=" + requestorSchoolID +
                ", typeOfTimeLogIssue='" + typeOfTimeLogIssue + '\'' +
                ", month='" + month + '\'' +
                ", day='" + day + '\'' +
                ", year='" + year + '\'' +
                ", timelogSession='" + timelogSession + '\'' +
                ", correctTimelog='" + correctTimelog + '\'' +
                ", reasonForChange='" + reasonForChange + '\'' +
                '}';
    }
}
