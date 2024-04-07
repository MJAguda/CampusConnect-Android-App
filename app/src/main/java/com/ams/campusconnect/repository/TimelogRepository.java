package com.ams.campusconnect.repository;

import com.ams.campusconnect.model.School;
import com.ams.campusconnect.model.Timelog;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TimelogRepository {
    private DatabaseReference databaseReference;

    public TimelogRepository() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    // Add Timelog to the database
    public void addTimelogChangeRequest(Timelog timelog, School school){
        databaseReference = FirebaseDatabase.getInstance().getReference().child( school.getSchoolID() + "/timelogChangeRequest/");
        // Add the timelog to the database
        databaseReference.setValue(timelog);
    }

    // Read Timelog from the database

    // Update Timelog from the database

    // Delete Timelog from the database
}
