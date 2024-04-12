package com.ams.campusconnect.model;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class EmployeeModel implements Serializable {
    private String id;
    private String firstName;
    private String lastName;
    private String month;
    private int day;
    private int year;
    private double latitude;
    private double longitude;

    // Constructor that accepts no field
    public EmployeeModel(){}

    // Constructor that accepts all field
    public EmployeeModel(String id, String firstName, String lastName, String month, int day, int year, double latitude, double longitude){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.month = month;
        this.day = day;
        this.year = year;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters
    public String getId() { return id; }
    public String getLastName() { return lastName; }
    public String getMonth() { return month; }
    public int getDay() { return day; }
    public int getYear() { return year; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getFirstName() { return firstName; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setMonth(String month) { this.month = month; }
    public void setDay(int day) { this.day = day; }
    public void setYear(int year) { this.year = year; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    // toString

    @NonNull
    @Override
    public String toString() {
        return "EmployeeModel{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", month='" + month + '\'' +
                ", day='" + day + '\'' +
                ", year='" + year + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
