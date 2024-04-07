package com.ams.campusconnect.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Timelog {
    private int requestorID;
    private int requestorSchoolID;
    private String typeOfTimeLogIssue;
    private LocalDate date;
    private String timelogSession;
    private LocalTime correctTimelog; // This is in 07:00 AM format (HH:mm a)
    private String reasonForChange;

    public Timelog(int requestorID, int requestorSchoolID, String typeOfTimeLogIssue, LocalDate date, String timelogSession, LocalTime correctTimelog, String reasonForChange) {
        this.requestorID = requestorID;
        this.requestorSchoolID = requestorSchoolID;
        this.typeOfTimeLogIssue = typeOfTimeLogIssue;
        this.date = date;
        this.timelogSession = timelogSession;
        this.correctTimelog = correctTimelog;
        this.reasonForChange = reasonForChange;
    }

    public Timelog(){}

    public int getRequestorID() { return requestorID; }

    public int getRequestorSchoolID() { return requestorSchoolID; }

    public String getTypeOfTimeLogIssue() { return typeOfTimeLogIssue; }

    public LocalDate getDate() { return date; }

    public String getTimelogSession() { return timelogSession; }

    public LocalTime getCorrectTimelog() { return correctTimelog; }

    public String getReasonForChange() { return reasonForChange; }

    public void setRequestorID(int requestorID) { this.requestorID = requestorID; }

    public void setRequestorSchoolID(int requestorSchoolID) { this.requestorSchoolID = requestorSchoolID; }

    public void setTypeOfTimeLogIssue(String typeOfTimeLogIssue) { this.typeOfTimeLogIssue = typeOfTimeLogIssue; }

    public void setDate(LocalDate date) { this.date = date; }

    public void setTimelogSession(String timelogSession) { this.timelogSession = timelogSession; }

    public void setCorrectTimelog(LocalTime correctTimelog) { this.correctTimelog = correctTimelog; }

    public void setReasonForChange(String reasonForChange) { this.reasonForChange = reasonForChange; }

    @Override
    public String toString() {
        return "Timelog{" +
                "requestorID=" + requestorID +
                ", requestorSchoolID=" + requestorSchoolID +
                ", typeOfTimeLogIssue='" + typeOfTimeLogIssue + '\'' +
                ", date=" + date +
                ", timelogSession=" + timelogSession +
                ", correctTimelog=" + correctTimelog +
                ", reasonForChange='" + reasonForChange + '\'' +
                '}';
    }
}
