package com.ams.campusconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ams.campusconnect.model.School;
import com.ams.campusconnect.model.SaveData;
import com.ams.campusconnect.firebase.Read;
import com.ams.campusconnect.firebase.Delete;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class SchoolLogIn extends AppCompatActivity {

    SaveData save = SaveData.getInstance();
    School school = School.getInstance();
    Read read = new Read();
    Delete delete = new Delete();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_log_in);

        //Analog Clock
        AnalogClock analogClock = findViewById(R.id.analog_clock);

        // Declare components
        TextView header = findViewById(R.id.headerText_TextView);
        TextView appName = findViewById(R.id.appName_TextView);

        EditText schoolID = findViewById(R.id.schoolID_EditText);

        Button submit = findViewById(R.id.submit_Button);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if school edittext is not empty
                if (schoolID.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Fill all Fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Store edittext to schoolID in the School class
                    school.setSchoolID(Integer.parseInt(schoolID.getText().toString()));

                    // getBySchoolID
                    getBySchoolID(school.getSchoolID());
                }
            }
        });
    }

    private void getBySchoolID(int schoolID) {

        read.readRecord(schoolID + "/", new Read.OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Toast.makeText(getApplicationContext(), "School ID not found", Toast.LENGTH_SHORT).show();
                } else {
                    if (dataSnapshot.child("employee").child("attendance").exists()) { // Check if employee/attendance exists if so, delete
                        delete.deleteRecord(school.getSchoolID() + "/employee", "attendance");
                    }

                    int schoolID = Integer.parseInt(dataSnapshot.child("schoolID").getValue().toString());
                    String schoolName = dataSnapshot.child("schoolName").getValue().toString();
                    String schoolHead = dataSnapshot.child("schoolHead").getValue().toString();
                    String adminUsername = dataSnapshot.child("adminUsername").getValue().toString();
                    String adminPassword = dataSnapshot.child("adminPassword").getValue().toString();
                    boolean idNumberFeature = dataSnapshot.child("idNumberFeature").getValue(Boolean.class);
                    boolean gpsFeature = dataSnapshot.child("gpsFeature").getValue(Boolean.class);
                    boolean timeBasedFeature = dataSnapshot.child("timeBasedFeature").getValue(Boolean.class);
                    boolean qrScannerFeature = dataSnapshot.child("qrcodeFeature").getValue(Boolean.class);
                    boolean fingerPrintScannerFeature = dataSnapshot.child("fingerPrintFeature").getValue(Boolean.class);
                    boolean facialRecognitionFeature = dataSnapshot.child("facialRecognitionFeature").getValue(Boolean.class);
                    String latitudeBottom = dataSnapshot.child("latitudeBottom").getValue().toString();
                    String latitudeTop = dataSnapshot.child("latitudeTop").getValue().toString();
                    String longitudeLeft = dataSnapshot.child("longitudeLeft").getValue().toString();
                    String longitudeRight = dataSnapshot.child("longitudeRight").getValue().toString();
                    String latitudeCenter = dataSnapshot.child("latitudeCenter").getValue().toString();
                    String longitudeCenter = dataSnapshot.child("longitudeCenter").getValue().toString();

//                            School school1 = new School(schoolID,
//                                    schoolName,
//                                    schoolHead,
//                                    adminUsername,
//                                    adminPassword,
//                                    idNumberFeature,
//                                    gpsFeature,
//                                    timeBasedFeature,
//                                    qrScannerFeature,
//                                    fingerPrintScannerFeature,
//                                    facialRecognitionFeature,
//                                    latitudeBottom,
//                                    latitudeTop,
//                                    longitudeLeft,
//                                    longitudeRight,
//                                    latitudeCenter,
//                                    longitudeCenter);

                    // Fetch data and store it in School Class
                    school.setSchoolID(schoolID);
                    school.setSchoolName(schoolName);
                    school.setSchoolHead(schoolHead);
                    school.setAdminUsername(adminUsername);
                    school.setAdminPassword(adminPassword);
                    school.setIdNumberFeature(idNumberFeature);
                    school.setGpsFeature(gpsFeature);
                    school.setTimeBasedFeature(timeBasedFeature);
                    school.setQrScannerFeature(qrScannerFeature);
                    school.setFingerPrintScannerFeature(fingerPrintScannerFeature);
                    school.setFacialRecognitionFeature(facialRecognitionFeature);
                    school.setLatitudeBottom(latitudeBottom);
                    school.setLatitudeTop(latitudeTop);
                    school.setLongitudeLeft(longitudeLeft);
                    school.setLongitudeRight(longitudeRight);
                    school.setLatitudeCenter(latitudeCenter);
                    school.setLongitudeCenter(longitudeCenter);

                    // Toast.makeText(getApplicationContext(), "GPS : " + school.getGpsFeature() + "QR : " + school.isQrScannerFeature() + "Biometric : " + school.isFingerPrintScannerFeature() + "Facial : " + school.isFacialRecognitionFeature(), Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(SchoolLogIn.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Read Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
}