package com.ams.campusconnect.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.ams.campusconnect.model.School;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SchoolRepository {
    private DatabaseReference databaseReference;

    public SchoolRepository() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    // Add School
    public void addSchool(School school) {
        // Get the reference of the database
        databaseReference = FirebaseDatabase.getInstance().getReference().child(school.getSchoolID() + "/");

        // Add the school to the database
        databaseReference.setValue(school).addOnSuccessListener(aVoid -> {
            // School added successfully

        });
    }
    // // To call addSchool
    // SchoolRepository schoolRepository = new SchoolRepository();
    // schoolRepository.addSchool(school);

    public void readSchool(int schoolID) {
        // Get the reference of the database
        databaseReference = FirebaseDatabase.getInstance().getReference().child(schoolID + "/");

        // Read the school from the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get the school from the database
                School school = dataSnapshot.getValue(School.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error fetching school data: ", databaseError.getMessage());
            }
        });
    }
}