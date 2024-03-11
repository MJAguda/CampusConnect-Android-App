package com.ams.campusconnect.repository;

import com.ams.campusconnect.model.SchoolModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SchoolRepository {
    private DatabaseReference databaseReference;

    public SchoolRepository() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public SchoolModel findSchoolById(int schoolID) {
        SchoolModel school = new SchoolModel(305113, "Flora National High School", "CATHERINE R. CALUYA PhD", "12345",
                "12345", false, true, true, true, true, false,
                18.218810, 121.420142, 18.218810, 121.421985, 18.21785994355776, 121.42115711493562);
        return school;
    }

    public interface SchoolDataCallback {
        void onDataReceived(SchoolModel school);
        void onError(String errorMessage);
    }

    // find the school by id
    public void findSchoolById(int schoolID, final SchoolDataCallback callback) {
        databaseReference.child(String.valueOf(schoolID)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    SchoolModel school = dataSnapshot.getValue(SchoolModel.class);
                    callback.onDataReceived(school);
                } else {
                    callback.onError("School with ID : " + schoolID + " not found");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());
            }
        });
    }
}
