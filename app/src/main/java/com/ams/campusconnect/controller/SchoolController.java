package com.ams.campusconnect.controller;

import android.content.Context;

import com.ams.campusconnect.model.School;
import com.ams.campusconnect.repository.SchoolRepository;

public class SchoolController {
    private SchoolRepository schoolRepository;
    private Context context;

    public SchoolController(Context context){
        this.context = context;
        this.schoolRepository = new SchoolRepository();
    }

    public void addSchool(School school) {
        schoolRepository.addSchool(school);
    }

    public void getSchoolData(int schoolID, final SchoolRepository.OnDataFetchListener listener){
        schoolRepository.getSchoolData(schoolID, listener);
    }

    public void updateSchool(School school){
        schoolRepository.updateSchool(school);
    }

    public void deleteSchool(int schoolID){
        schoolRepository.deleteSchool(schoolID);
    }

    public void getAllSchoolIDs(final SchoolRepository.OnDataFetchListener listener){
        schoolRepository.getAllSchoolIDs(listener);
    }

    public void getAllSchools(final SchoolRepository.OnDataFetchListener listener){
        schoolRepository.findAll(listener);
    }
}

