package com.ams.campusconnect;

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
import com.ams.campusconnect.firebase.Read;
import com.ams.campusconnect.model.School;
import com.ams.campusconnect.model.SchoolModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class SchoolLogIn extends AppCompatActivity {

    SchoolModel schoolModel = new SchoolModel();
    Read read = new Read();
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

                schoolModel.setSchoolID(Integer.parseInt(schoolID.getText().toString()));
                SchoolController schoolController = new SchoolController(schoolModel.getSchoolID());

                schoolController.getSchoolData(new SchoolController.OnDataFetchListener() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            Toast.makeText(getApplicationContext(), "School ID not found", Toast.LENGTH_SHORT).show();
                        } else {
                            if (dataSnapshot.child("employee").child("attendance").exists()) { // Check if employee/attendance exists if so, delete
                                delete.deleteRecord(schoolModel.getSchoolID() + "/employee", "attendance");
                            }

                            schoolModel.setSchoolID(Integer.parseInt(dataSnapshot.child("schoolID").getValue().toString()));
                            schoolModel.setSchoolName(dataSnapshot.child("schoolName").getValue().toString());
                            schoolModel.setSchoolHead(dataSnapshot.child("schoolHead").getValue().toString());
                            schoolModel.setAdminUsername(dataSnapshot.child("adminUsername").getValue().toString());
                            schoolModel.setAdminPassword(dataSnapshot.child("adminPassword").getValue().toString());
                            schoolModel.setIdNumberFeature(dataSnapshot.child("idNumberFeature").getValue(Boolean.class));
                            schoolModel.setGpsFeature(dataSnapshot.child("gpsFeature").getValue(Boolean.class));
                            schoolModel.setTimeBasedFeature(dataSnapshot.child("timeBasedFeature").getValue(Boolean.class));
                            schoolModel.setQrcodeFeature(dataSnapshot.child("qrcodeFeature").getValue(Boolean.class));
                            schoolModel.setBiometricFeature(dataSnapshot.child("biometricFeature").getValue(Boolean.class));
                            schoolModel.setLatitudeBottom(Double.parseDouble(dataSnapshot.child("latitudeBottom").getValue().toString()));
                            schoolModel.setLatitudeTop(Double.parseDouble(dataSnapshot.child("latitudeTop").getValue().toString()));
                            schoolModel.setLongitudeLeft(Double.parseDouble(dataSnapshot.child("longitudeLeft").getValue().toString()));
                            schoolModel.setLongitudeRight(Double.parseDouble(dataSnapshot.child("longitudeRight").getValue().toString()));
                            schoolModel.setLatitudeCenter(Double.parseDouble(dataSnapshot.child("latitudeCenter").getValue().toString()));
                            schoolModel.setLongitudeCenter(Double.parseDouble(dataSnapshot.child("longitudeCenter").getValue().toString()));

                            Intent intent = new Intent(SchoolLogIn.this, LogbookActivity.class);
                            intent.putExtra("schoolModel", schoolModel);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "getSchoolDataError : " + databaseError, Toast.LENGTH_SHORT).show();
                        Log.e("getSchoolDataError", databaseError.toString());
                    }
                });
            }
        });
    }
}