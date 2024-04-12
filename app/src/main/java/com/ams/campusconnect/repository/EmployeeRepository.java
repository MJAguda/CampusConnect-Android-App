package com.ams.campusconnect.repository;

import com.ams.campusconnect.model.Employee;
import com.ams.campusconnect.model.School;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

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
    public void addEmployee (School school, Employee employee){
        databaseReference = FirebaseDatabase.getInstance().getReference().child( school.getSchoolID() + "/employee/" + employee.getId());
        // Add the employee to the database
        databaseReference.setValue(employee);
    }

    // Read Employee from the database
    // Update Employee
    // Delete Employee

}
