package com.ams.campusconnect;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ams.campusconnect.controller.SchoolController;
import com.ams.campusconnect.firebase.Delete;
import com.ams.campusconnect.model.School;
import com.ams.campusconnect.repository.SchoolRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SchoolLogIn extends AppCompatActivity {

    School school = new School();
    Delete delete = new Delete();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_log_in);

        // Declare components
        TextView header = findViewById(R.id.headerText_TextView);
        TextView appName = findViewById(R.id.appName_TextView);

        EditText schoolID = findViewById(R.id.schoolID_EditText);

        Button submit = findViewById(R.id.submit_Button);

        submit.setOnClickListener(view -> {
            // Check if school edittext is not empty
            if (schoolID.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Fill all Fields", Toast.LENGTH_SHORT).show();
            } else {
                // Store edittext to schoolID in the School class
//                school.setSchoolID(Integer.parseInt(schoolID.getText().toString()));

                // getBySchoolID
//                getBySchoolID(school.getSchoolID());

                school.setSchoolID(Integer.parseInt(schoolID.getText().toString()));
                SchoolController schoolController = new SchoolController(this);

                // Declare and initialize a ProgressDialog
                ProgressDialog progressDialog = new ProgressDialog(SchoolLogIn.this);
                progressDialog.setMessage("Fetching data...");

                // Show the ProgressDialog
                progressDialog.setCancelable(false);
                progressDialog.show();

                schoolController.getSchool(school.getSchoolID(), new SchoolRepository.OnDataFetchListener() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {

                        // Dismiss the ProgressDialog
                        progressDialog.dismiss();

                        if (!dataSnapshot.exists()) {
                            Toast.makeText(getApplicationContext(), "School ID not found", Toast.LENGTH_SHORT).show();
                        } else {
                            if (dataSnapshot.child("employee").child("attendance").exists()) { // Check if employee/attendance exists if so, delete
                                delete.deleteRecord(school.getSchoolID() + "/employee", "attendance");
                            }

                            // Set School Data from the class
                            school = dataSnapshot.getValue(School.class);

                            Intent intent = new Intent(SchoolLogIn.this, LogbookActivity.class);
                            intent.putExtra("school", school);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(DatabaseError databaseError) {


                        // Dismiss the ProgressDialog
                        progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), "getSchoolDataError : " + databaseError, Toast.LENGTH_SHORT).show();
                        Log.e("getSchoolDataError", databaseError.toString());
                    }
                });


                List<School> schools = new ArrayList<>();
                schoolController.getAllSchools(new SchoolRepository.OnDataFetchListener() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        // Get all schools and their fields and set it to School class
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (Objects.requireNonNull(snapshot.getKey()).matches("[0-9]+")) {
                                // get children of school
                                School school = snapshot.getValue(School.class);
                                schools.add(school);
                            }
                        }
                    }

                    @Override
                    public void onFailure(DatabaseError databaseError) {
                        Log.e("SchoolController", "Error fetching schools", databaseError.toException());
                    }
                });
            }
        });
    }
}