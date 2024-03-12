package com.ams.campusconnect.model;

public class SchoolModel {
    private int schoolID;
    private String schoolName;
    private String schoolHead;
    private String adminPassword;
    private String adminUsername;
    private boolean gpsFeature;
    private boolean idNumberFeature;
    private boolean qrcodeFeature;
    private boolean biometricFeature;
    private boolean timeBasedFeature;
    private double latitudeTop;
    private double longitudeLeft;
    private double latitudeBottom;
    private double longitudeRight;
    private double latitudeCenter;
    private double longitudeCenter;

    // Constructor
    // Constructor that accepts all fields
    public SchoolModel(int schoolID, String schoolName, String schoolHead, String adminPassword, String adminUsername, boolean gpsFeature, boolean idNumberFeature, boolean qrcodeFeature, boolean biometricFeature, boolean timeBasedFeature, double latitudeTop, double longitudeLeft, double latitudeBottom, double longitudeRight, double latitudeCenter, double longitudeCenter) {
        this.schoolID = schoolID;
        this.schoolName = schoolName;
        this.schoolHead = schoolHead;
        this.adminPassword = adminPassword;
        this.adminUsername = adminUsername;
        this.gpsFeature = gpsFeature;
        this.idNumberFeature = idNumberFeature;
        this.qrcodeFeature = qrcodeFeature;
        this.biometricFeature = biometricFeature;
        this.timeBasedFeature = timeBasedFeature;
        this.latitudeTop = latitudeTop;
        this.longitudeLeft = longitudeLeft;
        this.latitudeBottom = latitudeBottom;
        this.longitudeRight = longitudeRight;
        this.latitudeCenter = latitudeCenter;
        this.longitudeCenter = longitudeCenter;
    }

    // Constructor that accepts no field
    public SchoolModel() {
    }


    // Getters
    public int getSchoolID() {
        return schoolID;
    }
    public String getSchoolName() {
        return schoolName;
    }
    public String getSchoolHead() {
        return schoolHead;
    }
    public String getAdminPassword() {
        return adminPassword;
    }
    public String getAdminUsername() {
        return adminUsername;
    }
    public boolean isGpsFeature() {
        return gpsFeature;
    }
    public boolean isIdNumberFeature() {
        return idNumberFeature;
    }
    public boolean isQrcodeFeature() {
        return qrcodeFeature;
    }
    public boolean isBiometricFeature() {
        return biometricFeature;
    }
    public boolean isTimeBasedFeature() {
        return timeBasedFeature;
    }
    public double getLatitudeTop() {
        return latitudeTop;
    }
    public double getLongitudeLeft() {
        return longitudeLeft;
    }
    public double getLatitudeBottom() {
        return latitudeBottom;
    }
    public double getLongitudeRight() {
        return longitudeRight;
    }
    public double getLatitudeCenter() {
        return latitudeCenter;
    }
    public double getLongitudeCenter() {
        return longitudeCenter;
    }
    // Setters
    public void setSchoolID(int schoolID) {
        this.schoolID = schoolID;
    }
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
    public void setSchoolHead(String schoolHead) {
        this.schoolHead = schoolHead;
    }
    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }
    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }
    public void setGpsFeature(boolean gpsFeature) {
        this.gpsFeature = gpsFeature;
    }
    public void setIdNumberFeature(boolean idNumberFeature) { this.idNumberFeature = idNumberFeature; }
    public void setQrcodeFeature(boolean qrcodeFeature) {
        this.qrcodeFeature = qrcodeFeature;
    }
    public void setBiometricFeature(boolean biometricFeature) { this.biometricFeature = biometricFeature; }
    public void setTimeBasedFeature(boolean timeBasedFeature) { this.timeBasedFeature = timeBasedFeature; }
    public void setLatitudeTop(double latitudeTop) {
        this.latitudeTop = latitudeTop;
    }
    public void setLongitudeLeft(double longitudeLeft) {
        this.longitudeLeft = longitudeLeft;
    }
    public void setLatitudeBottom(double latitudeBottom) {
        this.latitudeBottom = latitudeBottom;
    }
    public void setLongitudeRight(double longitudeRight) {
        this.longitudeRight = longitudeRight;
    }
    public void setLatitudeCenter(double latitudeCenter) {
        this.latitudeCenter = latitudeCenter;
    }
    public void setLongitudeCenter(double longitudeCenter) { this.longitudeCenter = longitudeCenter; }

    // toString

    @Override
    public String toString() {
        return "SchoolModel{" +
                "schoolID=" + schoolID +
                ", schoolName='" + schoolName + '\'' +
                ", schoolHead='" + schoolHead + '\'' +
                ", adminPassword='" + adminPassword + '\'' +
                ", adminUsername='" + adminUsername + '\'' +
                ", gpsFeature=" + gpsFeature +
                ", idNumberFeature=" + idNumberFeature +
                ", qrcodeFeature=" + qrcodeFeature +
                ", biometricFeature=" + biometricFeature +
                ", timeBasedFeature=" + timeBasedFeature +
                ", latitudeTop=" + latitudeTop +
                ", longitudeLeft=" + longitudeLeft +
                ", latitudeBottom=" + latitudeBottom +
                ", longitudeRight=" + longitudeRight +
                ", latitudeCenter=" + latitudeCenter +
                ", longitudeCenter=" + longitudeCenter +
                '}';
    }

//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (obj == null || getClass() != obj.getClass()) {
//            return false;
//        }
//        SchoolModel other = (SchoolModel) obj;
//        return this.schoolID == other.schoolID &&
//                this.schoolName.equals(other.schoolName) &&
//                this.schoolHead.equals(other.schoolHead) &&
//                this.adminPassword.equals(other.adminPassword) &&
//                this.adminUsername.equals(other.adminUsername) &&
//                this.gpsFeature == other.gpsFeature &&
//                this.idNumberFeature == other.idNumberFeature &&
//                this.qrcodeFeature == other.qrcodeFeature &&
//                this.facialRecognitionFeature == other.facialRecognitionFeature &&
//                this.fingerPrintFeature == other.fingerPrintFeature &&
//                this.timeBasedFeature == other.timeBasedFeature &&
//                this.latitudeTop == other.latitudeTop &&
//                this.longitudeLeft == other.longitudeLeft &&
//                this.latitudeBottom == other.latitudeBottom &&
//                this.longitudeRight == other.longitudeRight &&
//                this.latitudeCenter == other.latitudeCenter &&
//                this.longitudeCenter == other.longitudeCenter;
//    }
}
