package com.ams.campusconnect.model;

public class SaveData {

    private static SaveData instance; // Lazy Singleton

    private SaveData(){}

    public static SaveData getInstance(){
        if(instance == null){
            instance = new SaveData();
        }
        return instance;
    }


    private String adminUsername = "administrator";
    private String adminPassword = "123";


    private String month;
    private String day;
    private String year;

    private String authenticate; // stores timeAM_In, timeAM_Out, timePM_In, timePM_Out

    // Setters
    public void setAdminUsername(String adminUsername) {this.adminUsername = adminUsername;}
    public void setAdminPassword(String adminPassword) {this.adminPassword = adminPassword;}


    public void setMonth(String month){this.month = month;}
    public void setDay(String day){
        this.day = day;
    }
    public void setYear(String year){
        this.year = year;
    }

    public void setAuthenticate(String authenticate) {this.authenticate = authenticate;}

    // Getters
    public String getAdminUsername() {return adminUsername;}
    public String getAdminPassword() {return adminPassword;}

    public String getMonth(){return month;}
    public String getDay(){return day;}
    public String getYear(){return year;}

    public String getAuthenticate(){return authenticate;}
}