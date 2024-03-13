package com.ams.campusconnect.controller;

import androidx.annotation.NonNull;

import com.ams.campusconnect.model.SchoolModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SchoolController {
    private DatabaseReference databaseReference;

    public SchoolController(int schoolID){
        databaseReference = FirebaseDatabase.getInstance().getReference().child(schoolID + "/");
    }

    public void getSchoolData(final OnDataFetchListener listener){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SchoolModel schoolModel = snapshot.getValue(SchoolModel.class);
                listener.onDataFetched(schoolModel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onError(error.getMessage());
            }
        });
    }

    public interface OnDataFetchListener{
        void onDataFetched(SchoolModel schoolModel);
        void onError(String errorMessage);
    }
}

