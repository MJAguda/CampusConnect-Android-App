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

    public void getAllSchools(final SchoolRepository.OnDataFetchListener listener){
        schoolService.getAllSchools(listener);
    }

//    public List<School> getAllSchools (){
//        List<School> schools = new ArrayList<>();
//        schoolService.getAllSchools(new SchoolRepository.OnDataFetchListener() {
//            @Override
//            public void onSuccess(DataSnapshot dataSnapshot) {
//                // Get all schools and their fields and set it to School class
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    if (Objects.requireNonNull(snapshot.getKey()).matches("[0-9]+")) {
//                        // get children of school
//                        School school = snapshot.getValue(School.class);
//                        schools.add(school);
//                    }
//                }
//
//                // Traverse List of Schools
//                for (School school : schools) {
//                    Log.d("School", school.getSchoolName());
//                }
//            }
//
//            @Override
//            public void onFailure(DatabaseError databaseError) {
//                Log.e("SchoolController", "Error fetching schools", databaseError.toException());
//            }
//        });
//        return schools;
//    }
}

