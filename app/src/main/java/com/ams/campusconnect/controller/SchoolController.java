package com.ams.campusconnect.controller;

import android.content.Context;

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

public class SchoolController {
    private DatabaseReference databaseReference;
    private SchoolService schoolService;
    private Context context;

    public SchoolController(Context context){
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.context = context;
        this.schoolService = new SchoolService(context);
    }

    public void addSchool(School school) {
        schoolService.addSchool(school);
    }

    public void getSchoolData(int schoolID, final SchoolRepository.OnDataFetchListener listener){
        schoolService.getSchoolData(schoolID, listener);
    }

    public void updateSchool(School school){
        schoolService.updateSchool(school);
    }

    public void deleteSchool(int schoolID){
        schoolService.deleteSchool(schoolID);
    }

    public void getAllSchoolIDs(final SchoolRepository.OnDataFetchListener listener){
        schoolService.getAllSchoolIDs(listener);
    }
}

