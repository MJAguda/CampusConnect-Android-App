package com.ams.campusconnect.controller;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.ams.campusconnect.SchoolAdmin;
import com.ams.campusconnect.model.School;
import com.ams.campusconnect.repository.SchoolRepository;
import com.ams.campusconnect.service.SchoolService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

