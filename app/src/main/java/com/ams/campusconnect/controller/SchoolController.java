package com.ams.campusconnect.controller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.ams.campusconnect.model.School;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SchoolController {
    private DatabaseReference databaseReference;

    public SchoolController(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void addSchool(School school) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child(school.getSchoolID() + "/");

        // Add the school to the database
        databaseReference.setValue(school).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // School added successfully

            }
        });
    }
    // // To call addSchool
    // SchoolController schoolController = new SchoolController(schoolModel);
    // schoolController.addSchool(school);

    public void getSchoolData(int schoolID, final OnDataFetchListener listener){
        databaseReference.child(String.valueOf(schoolID)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure(databaseError);
            }
        });
    }

    public interface OnDataFetchListener{
        void onSuccess(DataSnapshot dataSnapshot);

        void onFailure(DatabaseError databaseError);
    }

    public void updateSchool(School school){
        // Reference to the school in the database
        databaseReference = FirebaseDatabase.getInstance().getReference().child(school.getSchoolID() + "/");

        // Set the values for each child that needs to be updated only
        databaseReference.child("schoolName").setValue(school.getSchoolName());
        databaseReference.child("schoolHead").setValue(school.getSchoolHead());
        databaseReference.child("adminUsername").setValue(school.getAdminUsername());
        databaseReference.child("adminPassword").setValue(school.getAdminPassword());
        databaseReference.child("latitudeBottom").setValue(school.getLatitudeBottom());
        databaseReference.child("latitudeTop").setValue(school.getLatitudeTop());
        databaseReference.child("longitudeLeft").setValue(school.getLongitudeLeft());
        databaseReference.child("longitudeRight").setValue(school.getLongitudeRight());
    }

    public void deleteSchool(int schoolID){
        databaseReference = FirebaseDatabase.getInstance().getReference().child(schoolID + "/");
        databaseReference.removeValue();
    }

    public void getAllSchoolIDs(final OnDataFetchListener listener){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure(databaseError);
            }
        });
    }

    public void updateSchoolData(School school){
        databaseReference = FirebaseDatabase.getInstance().getReference().child(school.getSchoolID() + "/");
        databaseReference.setValue(school);

    }
}

