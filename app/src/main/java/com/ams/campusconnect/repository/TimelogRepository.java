package com.ams.campusconnect.repository;

import androidx.annotation.NonNull;

import com.ams.campusconnect.model.School;
import com.ams.campusconnect.model.Timelog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TimelogRepository {
    private DatabaseReference databaseReference;

    public TimelogRepository() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public interface OnDataFetchListener{
        void onSuccess(DataSnapshot dataSnapshot);

        void onFailure(DatabaseError databaseError);

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

    // Read School from the database
    public void getTimelogs(int schoolID, String employeeID, String year, String month, String day, final TimelogRepository.OnDataFetchListener listener){
        databaseReference.child(schoolID + "/employee/" + employeeID + "/attendance/" + year + "/" + month +  "/" + day).addListenerForSingleValueEvent(new ValueEventListener() {
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
