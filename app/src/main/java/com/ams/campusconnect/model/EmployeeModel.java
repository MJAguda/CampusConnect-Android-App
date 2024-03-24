package com.ams.campusconnect.model;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.util.Date;

public class EmployeeModel {
    private int id;
    private String firstName;
    private String lastName;
    private LocalDate birthdate;
    private double latitude;
    private double longitude;

    // Constructor that accepts no field
    public EmployeeModel(){}

    // Constructor that accepts all field
    public EmployeeModel(int id, String firstName, String lastName, LocalDate birthdate, double latitude, double longitude){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters
    public int getId() { return id; }
    public String getLastName() { return lastName; }
    public LocalDate getBirthdate() { return birthdate; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getFirstName() { return firstName; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setBirthdate(LocalDate birthdate) { this.birthdate = birthdate; }
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
                ", birthdate=" + birthdate +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
