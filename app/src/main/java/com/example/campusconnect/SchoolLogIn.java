package com.example.campusconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.Timer;
import java.util.TimerTask;

public class SchoolLogIn extends AppCompatActivity {

    SaveData save = SaveData.getInstance();
    School school = School.getInstance();
    Read read = new Read();

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
                                // Fetch data and store it in School Class
                                school.setSchoolID(Integer.parseInt(dataSnapshot.child("schoolID").getValue().toString()));
                                school.setSchoolName(dataSnapshot.child("schoolName").getValue().toString());
                                school.setSchoolHead(dataSnapshot.child("schoolHead").getValue().toString());
                                school.setAdminUsername(dataSnapshot.child("adminUsername").getValue().toString());
                                school.setAdminPassword(dataSnapshot.child("adminPassword").getValue().toString());
                                school.setGpsFeature(dataSnapshot.child("gpsFeature").getValue(Boolean.class));
                                school.setIdNumberFeature(dataSnapshot.child("idNumberFeature").getValue(Boolean.class));
                                school.setQrScannerFeature(dataSnapshot.child("qrcodeFeature").getValue(Boolean.class));
                                school.setFingerPrintScannerFeature(dataSnapshot.child("fingerPrintFeature").getValue(Boolean.class));
                                school.setFacialRecognitionFeature(dataSnapshot.child("facialRecognitionFeature").getValue(Boolean.class));
                                school.setLatitudeBottom(dataSnapshot.child("latitudeBottom").getValue().toString());
                                school.setLatitudeTop(dataSnapshot.child("latitudeTop").getValue().toString());
                                school.setLongitudeLeft(dataSnapshot.child("longitudeLeft").getValue().toString());
                                school.setLongitudeRight(dataSnapshot.child("longitudeRight").getValue().toString());

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