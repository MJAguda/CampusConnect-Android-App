package com.ams.campusconnect.service;

import android.util.Log;

import com.ams.campusconnect.model.School;
import com.ams.campusconnect.repository.SchoolRepository;

import java.util.ArrayList;
import java.util.List;

public class SchoolService {
    private SchoolRepository schoolRepository;

    public SchoolService(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    public SchoolService() {

    }

    private static List<School> schools = new ArrayList<>();
    private static School school = new School();

    // Add school
    public void addSchool(School school) {
        schoolRepository.addSchool(school);
    }

    public void fetchSchoolFromFireBase(School school) {
        Log.d("School ID : ", school.getSchoolID() + "");
        Log.d("School Name : ", school.getSchoolName() + "");
    }
}