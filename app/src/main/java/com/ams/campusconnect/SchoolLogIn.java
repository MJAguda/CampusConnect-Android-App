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
                if(schoolID.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Fill all Fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    // Store edittext to schoolID in the School class
                    school.setSchoolID(Integer.parseInt(schoolID.getText().toString()));

                    read.readRecord( school.getSchoolID() + "/", new Read.OnGetDataListener() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.exists()){
                                Toast.makeText(getApplicationContext(), "School ID not found", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                if(dataSnapshot.child("employee").child("attendance").exists()){ // Check if employee/attendance exists if so, delete
                                    delete.deleteRecord(school.getSchoolID() + "/employee", "attendance");
                                }

                                // Fetch data and store it in School Class
                                school.setSchoolID(Integer.parseInt(dataSnapshot.child("schoolID").getValue().toString()));
                                school.setSchoolName(dataSnapshot.child("schoolName").getValue().toString());
                                school.setSchoolHead(dataSnapshot.child("schoolHead").getValue().toString());
                                school.setAdminUsername(dataSnapshot.child("adminUsername").getValue().toString());
                                school.setAdminPassword(dataSnapshot.child("adminPassword").getValue().toString());
                                school.setIdNumberFeature(dataSnapshot.child("idNumberFeature").getValue(Boolean.class));
                                school.setGpsFeature(dataSnapshot.child("gpsFeature").getValue(Boolean.class));
                                school.setTimeBasedFeature(dataSnapshot.child("timeBasedFeature").getValue(Boolean.class));
                                school.setQrScannerFeature(dataSnapshot.child("qrcodeFeature").getValue(Boolean.class));
                                school.setFingerPrintScannerFeature(dataSnapshot.child("fingerPrintFeature").getValue(Boolean.class));
                                school.setFacialRecognitionFeature(dataSnapshot.child("facialRecognitionFeature").getValue(Boolean.class));
                                school.setLatitudeBottom(dataSnapshot.child("latitudeBottom").getValue().toString());
                                school.setLatitudeTop(dataSnapshot.child("latitudeTop").getValue().toString());
                                school.setLongitudeLeft(dataSnapshot.child("longitudeLeft").getValue().toString());
                                school.setLongitudeRight(dataSnapshot.child("longitudeRight").getValue().toString());
                                school.setLatitudeCenter(dataSnapshot.child("latitudeCenter").getValue().toString());
                                school.setLongitudeCenter(dataSnapshot.child("longitudeCenter").getValue().toString());

                                //Toast.makeText(getApplicationContext(), "GPS : " + school.getGpsFeature() + "QR : " + school.isQrScannerFeature() + "Biometric : " + school.isFingerPrintScannerFeature() + "Facial : " + school.isFacialRecognitionFeature(), Toast.LENGTH_LONG).show();

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
        });
    }
}