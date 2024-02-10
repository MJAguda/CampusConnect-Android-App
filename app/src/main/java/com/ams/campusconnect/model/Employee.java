package com.ams.campusconnect.model;

public class Employee {

    private static Employee instance; // Lazy Singleton

    private Employee(){}

    public static Employee getInstance(){
        if(instance == null){
            instance = new Employee();
        }
        return instance;
    }

    private String firstName;
    private String lastName;
    private String fullName;
    private String id;
    private String birthday;
    private String qr;
    private double latitude;
    private double longitude;
    private String timeAM_In;
    private String timeAM_Out;
    private String timePM_In;
    private String timePM_Out;


    // Setters
    public void setFirstName(String firstName) {this.firstName = firstName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public void setFullName(String fullName) {this.fullName = fullName;}
    public void setId(String id){
        this.id = id;
    }
    public void setBirthday(String birthday) {this.birthday = birthday;}
    public void setQr(String qr){
        this.qr = qr;
    }
    public void setLatitude(double latitude){
        this.latitude = latitude;
    }
    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    // Getters
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getFullName() {return fullName;}
    public String getId() {return id;}
    public String getBirthday() {return birthday;}
    public String getQr(){return qr;}
    public double getLatitude(){return latitude;}
    public double getLongitude(){return longitude;}
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
