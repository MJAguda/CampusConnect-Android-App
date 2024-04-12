package com.ams.campusconnect.repository;

import androidx.annotation.NonNull;

import com.ams.campusconnect.controller.SchoolController;
import com.ams.campusconnect.model.School;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class SchoolRepository {
    private DatabaseReference databaseReference;

    public SchoolRepository() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public interface OnDataFetchListener{
        void onSuccess(DataSnapshot dataSnapshot);

        void onFailure(DatabaseError databaseError);

    }

    // Create/Add School to the database
    public void addSchool(School school) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child(school.getSchoolID() + "/");
        // Add the school to the database
        databaseReference.setValue(school);
    }

    // Read School from the database
    public void getSchool(int schoolID, final SchoolRepository.OnDataFetchListener listener){
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

    // Update School from the database
    public void updateSchool(School school){
        // Reference to the school in the database
        databaseReference = FirebaseDatabase.getInstance().getReference().child(school.getSchoolID() + "/");

        // Set the values for each child that needs to be updated only
        databaseReference.child("schoolName").setValue(school.getSchoolName());
        databaseReference.child("schoolHead").setValue(school.getSchoolHead());
        databaseReference.child("adminUsername").setValue(school.getAdminUsername());
        databaseReference.child("adminPassword").setValue(school.getAdminPassword());
        databaseReference.child("idNumberFeature").setValue(school.isIdNumberFeature());
        databaseReference.child("gpsFeature").setValue(school.isGpsFeature());
        databaseReference.child("qrcodeFeature").setValue(school.isQrcodeFeature());
        databaseReference.child("timeBasedFeature").setValue(school.isTimeBasedFeature());
        databaseReference.child("biometricFeature").setValue(school.isBiometricFeature());
        databaseReference.child("latitudeBottom").setValue(school.getLatitudeBottom());
        databaseReference.child("latitudeTop").setValue(school.getLatitudeTop());
        databaseReference.child("longitudeLeft").setValue(school.getLongitudeLeft());
        databaseReference.child("longitudeRight").setValue(school.getLongitudeRight());
        databaseReference.child("latitudeCenter").setValue((school.getLatitudeBottom() + school.getLatitudeTop() ) / 2);
        databaseReference.child("longitudeCenter").setValue((school.getLongitudeLeft() + school.getLongitudeRight()) / 2);
    }

    // Delete School from the database
    public void deleteSchool(int schoolID){
        databaseReference = FirebaseDatabase.getInstance().getReference().child(schoolID + "/");
        databaseReference.removeValue();
    }

    // Get all school ids from the database
    public void getAllSchoolIDs(final SchoolRepository.OnDataFetchListener listener){
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

    public void findAll(final SchoolRepository.OnDataFetchListener listener){
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

}