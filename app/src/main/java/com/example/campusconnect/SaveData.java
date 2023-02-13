package com.example.campusconnect;

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

    // Top and Bottom Latitude && Left and Right Longitude
    private double fnhsTopLat = 18.218810;
    private double fnhsBottonLat = 18.216809;
    private double fnhsLeftLong = 121.420142;
    private double fnhsRightLong = 121.421985;
    private String firstName;
    private String lastName;
    private String fullName;
    private String id;
    private String birthday;
    private String qr;
    private double latitude;
    private double longitude;
    private String month;
    private String day;
    private String year;
    private String timeAM_In;
    private String timeAM_Out;
    private String timePM_In;
    private String timePM_Out;
    private String authenticate; // stores timeAM_In, timeAM_Out, timePM_In, timePM_Out

    // Setters
    public void setAdminUsername(String adminUsername) {this.adminUsername = adminUsername;}
    public void setAdminPassword(String adminPassword) {this.adminPassword = adminPassword;}
    public void setFnhsTopLat(double fnhsTopLat) {this.fnhsTopLat = fnhsTopLat;}
    public void setFnhsBottonLat(double fnhsBottonLat) {this.fnhsBottonLat = fnhsBottonLat;}
    public void setFnhsLeftLong(double fnhsLeftLong) {this.fnhsLeftLong = fnhsLeftLong;}
    public void setFnhsRightLong(double fnhsRightLong) {this.fnhsRightLong = fnhsRightLong;}
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
    public void setMonth(String month){this.month = month;}
    public void setDay(String day){
        this.day = day;
    }
    public void setYear(String year){
        this.year = year;
    }
    public void setTimeAM_In(String timeAM_In){
        this.timeAM_In = timeAM_In;
    }
    public void setTimeAM_Out(String timePM_Out){
        this.timeAM_Out = timeAM_Out;
    }
    public void setTimePM_In(String timePM_In){
        this.timePM_In = timePM_In;
    }
    public void setTimePM_Out(String timePM_Out){
        this.timePM_Out = timePM_Out;
    }
    public void setAuthenticate(String authenticate) {this.authenticate = authenticate;}

    // Getters
    public String getAdminUsername() {return adminUsername;}
    public String getAdminPassword() {return adminPassword;}
    public double getFnhsTopLat() {return fnhsTopLat;}
    public double getFnhsBottonLat() {return fnhsBottonLat;}
    public double getFnhsLeftLong() {return fnhsLeftLong;}
    public double getFnhsRightLong() {return fnhsRightLong;}
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getFullName() {return fullName;}
    public String getId() {return id;}
    public String getBirthday() {return birthday;}
    public String getQr(){return qr;}
    public double getLatitude(){return latitude;}
    public double getLongitude(){return longitude;}
    public String getMonth(){return month;}
    public String getDay(){return day;}
    public String getYear(){return year;}
    public String getTimeAM_In(){return timeAM_In;}
    public String getTimeAM_Out(){return timeAM_Out;}
    public String getTimePM_In(){return timePM_In;}
    public String getTimePM_Out(){return timePM_Out;}
    public String getAuthenticate(){return authenticate;}
}
