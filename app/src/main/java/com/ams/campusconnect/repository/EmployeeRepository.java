package com.ams.campusconnect.repository;

import androidx.annotation.NonNull;

import com.ams.campusconnect.model.EmployeeModel;
import com.ams.campusconnect.model.School;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class EmployeeRepository {
    private DatabaseReference databaseReference;

    public EmployeeRepository(int schoolID) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child(schoolID + "/employee/");
    }

    public interface OnDataFetchListener{
        void onSuccess(DataSnapshot dataSnapshot);
        void onFailure(DatabaseError databaseError);
    }

    // Create/Add Employee to the database
    public void addEmployee (School school, EmployeeModel employee){
        databaseReference = FirebaseDatabase.getInstance().getReference().child( school.getSchoolID() + "/employee/" + employee.getId());
        // Add the employee to the database
        databaseReference.setValue(employee);
    }

    // Read Employee from the database
    public void getEmployee (School school, String employeeID, final EmployeeRepository.OnDataFetchListener listener){
        databaseReference.child(employeeID).addListenerForSingleValueEvent(new ValueEventListener() {
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

    // Update Employee
    // Delete Employee

}
