package com.example.campusconnect;

public class School {

    private static School instance; // Lazy Singleton

    private School(){}

    public static School getInstance(){
        if(instance == null){
            instance = new School();
        }
        return instance;
    }

    private int schoolID;
    private String schoolName;
    private String schoolHead;
    private String adminUsername;
    private String adminPassword;
    private String latitudeBottom;
    private String latitudeTop;
    private String longitudeLeft;
    private String longitudeRight;

    // Setters
    public void setSchoolID(int schoolID){this.schoolID = schoolID;}
    public void setSchoolName(String schoolName){this.schoolName = schoolName;}
    public void setSchoolHead(String schoolHead) {this.schoolHead = schoolHead;}
    public void setAdminUsername(String adminUsername){this.adminUsername = adminUsername;}
    public void setAdminPassword(String adminPassword){this.adminPassword = adminPassword;}
    public void setLatitudeBottom(String latitudeBottom){this.latitudeBottom = latitudeBottom;}
    public void setLatitudeTop(String latitudeTop){this.latitudeTop = latitudeTop;}
    public void setLongitudeLeft(String longitudeLeft){this.longitudeLeft = longitudeLeft;}
    public void setLongitudeRight(String longitudeRight){this.longitudeRight = longitudeRight;}

    // Getters
    public int getSchoolID(){return schoolID;}
    public String getSchoolName(){return schoolName;}
    public String getSchoolHead() {return schoolHead;}
    public String getAdminUsername(){return adminUsername;}
    public String getAdminPassword(){return adminPassword;}
    public String getLatitudeBottom(){return latitudeBottom;}
    public String getLatitudeTop(){return latitudeTop;}
    public String getLongitudeLeft(){return longitudeLeft;}
    public String getLongitudeRight(){return longitudeRight;}

}
