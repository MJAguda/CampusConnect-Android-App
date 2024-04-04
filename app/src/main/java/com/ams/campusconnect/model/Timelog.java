package com.ams.campusconnect.model;

import java.util.Date;

public class Timelog {

    private int requestorID;
    private int requestorSchoolID;
    private String typeOfTimeLogIssue;
    private Date incorrectTimeLogDate;
    private Date correctTimeLogDate;
    private String reasonForChange;

    // Constructor
    public Timelog(int requestorID, int requestorSchoolID, String typeOfTimeLogIssue, Date incorrectTimeLogDate, Date correctTimeLogDate, String reasonForChange) {
        this.requestorID = requestorID;
        this.requestorSchoolID = requestorSchoolID;
        this.typeOfTimeLogIssue = typeOfTimeLogIssue;
        this.incorrectTimeLogDate = incorrectTimeLogDate;
        this.correctTimeLogDate = correctTimeLogDate;
        this.reasonForChange = reasonForChange;
    }

    // Empty Constructor
    public Timelog() {
    }

    // Getters

    public int getRequestorID() { return requestorID; }

    public int getRequestorSchoolID() { return requestorSchoolID; }

    public String getTypeOfTimeLogIssue() { return typeOfTimeLogIssue; }

    public Date getIncorrectTimeLogDate() { return incorrectTimeLogDate; }

    public Date getCorrectTimeLogDate() { return correctTimeLogDate; }

    public String getReasonForChange() { return reasonForChange; }


    // Setters

    public void setRequestorID(int requestorID) { this.requestorID = requestorID; }

    public void setRequestorSchoolID(int requestorSchoolID) { this.requestorSchoolID = requestorSchoolID; }

    public void setTypeOfTimeLogIssue(String typeOfTimeLogIssue) { this.typeOfTimeLogIssue = typeOfTimeLogIssue; }

    public void setIncorrectTimeLogDate(Date incorrectTimeLogDate) { this.incorrectTimeLogDate = incorrectTimeLogDate; }

    public void setCorrectTimeLogDate(Date correctTimeLogDate) { this.correctTimeLogDate = correctTimeLogDate; }

    public void setReasonForChange(String reasonForChange) { this.reasonForChange = reasonForChange; }
}
