package com.ams.campusconnect.model;

import android.util.Log;

public class School {

    private static School instance; // Lazy Singleton

    private School(){}

    public School(int schoolID, String schoolName, String schoolHead, String adminUsername, String adminPassword, boolean idNumberFeature, boolean gpsFeature, boolean timeBasedFeature, boolean qrScannerFeature, boolean fingerPrintScannerFeature, boolean facialRecognitionFeature, String latitudeBottom, String latitudeTop, String longitudeLeft, String longitudeRight, String latitudeCenter, String longitudeCenter) {
        this.schoolID = schoolID;
        this.schoolName = schoolName;
        this.schoolHead = schoolHead;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
        this.idNumberFeature = idNumberFeature;
        this.gpsFeature = gpsFeature;
        this.timeBasedFeature = timeBasedFeature;
        this.qrScannerFeature = qrScannerFeature;
        this.fingerPrintScannerFeature = fingerPrintScannerFeature;
        this.facialRecognitionFeature = facialRecognitionFeature;
        this.latitudeBottom = latitudeBottom;
        this.latitudeTop = latitudeTop;
        this.longitudeLeft = longitudeLeft;
        this.longitudeRight = longitudeRight;
        this.latitudeCenter = latitudeCenter;
        this.longitudeCenter = longitudeCenter;
    }


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
    private boolean idNumberFeature;
    private boolean gpsFeature;
    private boolean timeBasedFeature;
    private boolean qrScannerFeature;
    private boolean fingerPrintScannerFeature;
    private boolean facialRecognitionFeature;
    private String latitudeBottom;
    private String latitudeTop;
    private String longitudeLeft;
    private String longitudeRight;
    private String latitudeCenter;
    private String longitudeCenter;

    // Setters
    public void setSchoolID(int schoolID){this.schoolID = schoolID;}
    public void setSchoolName(String schoolName){this.schoolName = schoolName;}
    public void setSchoolHead(String schoolHead) {this.schoolHead = schoolHead;}
    public void setAdminUsername(String adminUsername){this.adminUsername = adminUsername;}
    public void setAdminPassword(String adminPassword){this.adminPassword = adminPassword;}
    public void setIdNumberFeature(boolean idNumberFeature){this.idNumberFeature = idNumberFeature;}
    public void setGpsFeature(boolean gpsFeature){this.gpsFeature = gpsFeature;}
    public void setTimeBasedFeature(boolean timeBasedFeature){this.timeBasedFeature = timeBasedFeature;}
    public void setQrScannerFeature(boolean qrScannerFeature){this.qrScannerFeature = qrScannerFeature;}
    public void setFingerPrintScannerFeature(boolean fingerPrintScannerFeature){this.fingerPrintScannerFeature = fingerPrintScannerFeature;}
    public void setFacialRecognitionFeature(boolean facialRecognitionFeature){this.facialRecognitionFeature = facialRecognitionFeature;}
    public void setLatitudeBottom(String latitudeBottom){this.latitudeBottom = latitudeBottom;}
    public void setLatitudeTop(String latitudeTop){this.latitudeTop = latitudeTop;}
    public void setLongitudeLeft(String longitudeLeft){this.longitudeLeft = longitudeLeft;}
    public void setLongitudeRight(String longitudeRight){this.longitudeRight = longitudeRight;}
    public void setLatitudeCenter(String latitudeCenter){this.latitudeCenter = latitudeCenter;}
    public void setLongitudeCenter(String longitudeCenter){this.longitudeCenter = longitudeCenter;}

    // Getters
    public int getSchoolID(){return schoolID;}
    public String getSchoolName(){return schoolName;}
    public String getSchoolHead() {return schoolHead;}
    public String getAdminUsername(){return adminUsername;}
    public String getAdminPassword(){return adminPassword;}
    public boolean isIdNumberFeature(){return idNumberFeature;}
    public boolean isGpsFeature(){return gpsFeature;}
    public boolean isTimeBasedFeature(){return timeBasedFeature;}
    public boolean isQrScannerFeature(){return qrScannerFeature;}
    public boolean isFingerPrintScannerFeature(){return fingerPrintScannerFeature;}
    public boolean isFacialRecognitionFeature(){return facialRecognitionFeature;}
    public String getLatitudeBottom(){return latitudeBottom;}
    public String getLatitudeTop(){return latitudeTop;}
    public String getLongitudeLeft(){return longitudeLeft;}
    public String getLongitudeRight(){return longitudeRight;}
    public String getLatitudeCenter(){return latitudeCenter;}
    public String getLongitudeCenter(){return longitudeCenter;}
}
