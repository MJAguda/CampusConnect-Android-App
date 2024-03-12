package com.ams.campusconnect.service;

import android.util.Log;

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

    public void fetchSchoolFromFireBase(SchoolModel schoolModel) {
        Log.d("School ID : ", schoolModel.getSchoolID() + "");
        Log.d("School Name : ", schoolModel.getSchoolName() + "");
    }
}