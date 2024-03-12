package com.ams.campusconnect.model;

public class School {

    private static School instance; // Lazy Singleton

    private School(){}

    public School(int schoolID, String schoolName, String schoolHead, String adminUsername, String adminPassword, boolean idNumberFeature, boolean gpsFeature, boolean timeBasedFeature, boolean qrScannerFeature, boolean biometricFeature, boolean facialRecognitionFeature, double latitudeBottom, double latitudeTop, double longitudeLeft, double longitudeRight, double latitudeCenter, double longitudeCenter) {
        this.schoolID = schoolID;
        this.schoolName = schoolName;
        this.schoolHead = schoolHead;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
        this.idNumberFeature = idNumberFeature;
        this.gpsFeature = gpsFeature;
        this.timeBasedFeature = timeBasedFeature;
        this.qrScannerFeature = qrScannerFeature;
        this.biometricFeature = biometricFeature;
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
    private boolean biometricFeature;
    private double latitudeBottom;
    private double latitudeTop;
    private double longitudeLeft;
    private double longitudeRight;
    private double latitudeCenter;
    private double longitudeCenter;

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
    public void setBiometricFeature(boolean biometricFeature){this.biometricFeature = biometricFeature;}
    public void setLatitudeBottom(double latitudeBottom){this.latitudeBottom = latitudeBottom;}
    public void setLatitudeTop(double latitudeTop){this.latitudeTop = latitudeTop;}
    public void setLongitudeLeft(double longitudeLeft){this.longitudeLeft = longitudeLeft;}
    public void setLongitudeRight(double longitudeRight){this.longitudeRight = longitudeRight;}
    public void setLatitudeCenter(double latitudeCenter){this.latitudeCenter = latitudeCenter;}
    public void setLongitudeCenter(double longitudeCenter){this.longitudeCenter = longitudeCenter;}

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
    public boolean isBiometricFeature(){return biometricFeature;}
    public double getLatitudeBottom(){return latitudeBottom;}
    public double getLatitudeTop(){return latitudeTop;}
    public double getLongitudeLeft(){return longitudeLeft;}
    public double getLongitudeRight(){return longitudeRight;}
    public double getLatitudeCenter(){return latitudeCenter;}
    public double getLongitudeCenter(){return longitudeCenter;}
}
