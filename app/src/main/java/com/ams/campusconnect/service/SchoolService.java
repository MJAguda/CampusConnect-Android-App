package com.ams.campusconnect.service;

import com.ams.campusconnect.model.School;
import com.ams.campusconnect.model.SchoolModel;
import com.ams.campusconnect.repository.SchoolRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class SchoolService {
    private SchoolRepository schoolRepository;

    public SchoolService(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    public SchoolService() {

    }

    private static List<SchoolModel> schools = new ArrayList<>();
    private static SchoolModel school = new SchoolModel();

    // Add school
    public void addSchool(SchoolModel school) {
        schoolRepository.addSchool(school);
    }

    // Find school by id and Get all fields
//    public void findSchoolById(int schoolID) {
//
////        return new SchoolModel(305113, "Flora National High School", "CATHERINE R. CALUYA PhD", "12345",
////                     "12345", false, true, true, true, true, false,
////                     18.218810, 121.420142, 18.218810, 121.421985, 18.21785994355776, 121.42115711493562);
//
//
//        schoolRepository.findSchoolById(schoolID, new SchoolRepository.OnGetDataListener() {
//
//            @Override
//            public void onSuccess(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    int schoolID = Integer.parseInt(dataSnapshot.child("schoolID").getValue().toString());
//                    String schoolName = dataSnapshot.child("schoolName").getValue().toString();
//                    String schoolHead = dataSnapshot.child("schoolHead").getValue().toString();
//                    String adminUsername = dataSnapshot.child("adminUsername").getValue().toString();
//                    String adminPassword = dataSnapshot.child("adminPassword").getValue().toString();
//                    boolean idNumberFeature = dataSnapshot.child("idNumberFeature").getValue(Boolean.class);
//                    boolean gpsFeature = dataSnapshot.child("gpsFeature").getValue(Boolean.class);
//                    boolean timeBasedFeature = dataSnapshot.child("timeBasedFeature").getValue(Boolean.class);
//                    boolean qrScannerFeature = dataSnapshot.child("qrcodeFeature").getValue(Boolean.class);
//                    boolean fingerPrintScannerFeature = dataSnapshot.child("fingerPrintFeature").getValue(Boolean.class);
//                    boolean facialRecognitionFeature = dataSnapshot.child("facialRecognitionFeature").getValue(Boolean.class);
//                    double latitudeBottom = (double) dataSnapshot.child("latitudeBottom").getValue();
//                    double latitudeTop = (double) dataSnapshot.child("latitudeTop").getValue();
//                    double longitudeLeft = (double) dataSnapshot.child("longitudeLeft").getValue();
//                    double longitudeRight = (double) dataSnapshot.child("longitudeRight").getValue();
//                    double latitudeCenter = (double) dataSnapshot.child("latitudeCenter").getValue();
//                    double longitudeCenter = (double) dataSnapshot.child("longitudeCenter").getValue();
//
//                    School school = School.getInstance();
//
//                    school.setSchoolID(Integer.parseInt(dataSnapshot.child("schoolID").getValue().toString()));
//                    school.setSchoolName(dataSnapshot.child("schoolName").getValue().toString());
//                    school.setSchoolHead(dataSnapshot.child("schoolHead").getValue().toString());
//                    school.setAdminUsername(dataSnapshot.child("adminUsername").getValue().toString());
//                    school.setAdminPassword(dataSnapshot.child("adminPassword").getValue().toString());
//                }
//            }
//
//            @Override
//            public void onFailure(DatabaseError databaseError) {
//                // handle error here
//            }
//        });
//    }
}