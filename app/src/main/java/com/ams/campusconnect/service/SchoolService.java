package com.ams.campusconnect.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.ams.campusconnect.controller.SchoolController;
import com.ams.campusconnect.model.School;
import com.ams.campusconnect.repository.SchoolRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class SchoolService {
    private SchoolRepository schoolRepository;
    private Context context;

    public SchoolService(Context context) {
        this.schoolRepository = new SchoolRepository();
        this.context = context;
    }

    // Create/Add Employee
    public void addSchool(School school) {
        schoolRepository.addSchool(school);
        Toast.makeText(context, "School Added Successfully", Toast.LENGTH_SHORT).show();
    }

    public void getSchoolData(int schoolID, final SchoolRepository.OnDataFetchListener listener){
        schoolRepository.getSchoolData(schoolID, listener);
    }

    // Update School
    public void updateSchool(School school){
        schoolRepository.updateSchool(school);
        Toast.makeText(context, "School Updated Successfully", Toast.LENGTH_SHORT).show();
    }

    //Delete School
    public void deleteSchool(int schoolID){
        schoolRepository.deleteSchool(schoolID);
        Toast.makeText(context, "School Deleted Successfully", Toast.LENGTH_SHORT).show();
    }


    // Get all school IDs
    public void getAllSchoolIDs(final SchoolRepository.OnDataFetchListener listener){
        schoolRepository.getAllSchoolIDs(listener);
    }

    public void getAllSchools(final SchoolRepository.OnDataFetchListener listener){
        schoolRepository.findAll(listener);
    }
}