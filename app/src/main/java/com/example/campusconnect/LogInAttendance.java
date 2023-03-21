package com.example.campusconnect;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LogInAttendance extends AppCompatActivity {

    SaveData save = SaveData.getInstance();
    School school = School.getInstance();
    Employee employee = Employee.getInstance();
    Create create = new Create();
    private static final int REQUEST_CODE_SCAN_QR = 1;
    EditText id;

    // Instance of scanFingerPrint
    private static final String TAG = LogInAttendance.class.getSimpleName();
    private ScanFingerPrint scanFingerPrint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_attendance);

        // TODO Add VPN restriction

        // Declare Components
        id = findViewById(R.id.id_EditText);
        ImageButton scanQR = findViewById(R.id.scanQR_ImageButton);
        ImageButton scanFinger = findViewById(R.id.scanFingerPrint_ImageButton);
        ImageButton scanFacial = findViewById(R.id.scanFacial_ImageButton);
        Button submit = findViewById(R.id.submit_Button);
        ImageButton back = findViewById(R.id.backButton_ImageButton);

        // Declare and Initialized locationManager
        LocationManager locationManager;
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        // Declare thankyou sound
        final MediaPlayer thankyou = MediaPlayer.create(this, R.raw.thankyou);
        final MediaPlayer alreadyhave = MediaPlayer.create(this, R.raw.alreadyhave);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (LogInAttendance.this, Attendance.class);
                startActivity(intent);
            }
        });

        // Set click listener on button to start ScanQR activity
        scanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInAttendance.this, ScanQR.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN_QR);
            }
        });

        scanFinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
                        ScanFingerPrint scanFingerPrint = new ScanFingerPrint(LogInAttendance.this);
                        scanFingerPrint.startScan();
                    } else {
                        Toast.makeText(getApplicationContext(), "Your phone is not equipped with fingerprint scanner", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Fingerprint authentication is not supported in your device", Toast.LENGTH_SHORT).show();
                }
            }
        });

        scanFacial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Authenticate
                employee.setId(id.getText().toString());

                save.setYear(DateUtils.getCurrentYear());
                save.setMonth(DateUtils.getMonthName(DateUtils.getCurrentMonth()));
                save.setDay(DateUtils.getCurrentDay());

                // Check id if exist Log in Using ID Number
                Read read = new Read();
                read.readRecord(school.getSchoolID() + "/employee/" + employee.getId(), new Read.OnGetDataListener() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            // Check if Time already exists
                            read.readRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/attendance/" + save.getYear() + "/" + save.getMonth() + "/" + save.getDay(), new Read.OnGetDataListener() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {

                                    try {
                                    // check if dataSnapshot exists but not equal to ""
                                        if (!dataSnapshot.child(save.getAuthenticate()).getValue().equals("")) {
                                            Toast.makeText(getApplicationContext(), "Already Have", Toast.LENGTH_SHORT).show();
                                            alreadyhave.start();
                                        }
                                        else{
                                            //Time must have 15 minutes interval
                                            String priorAuthenticate = null;
                                            if (save.getAuthenticate().equals("timeAM_Out")){
                                                //get timeAM_In and compare it with timeAM_Out
                                                priorAuthenticate = "timeAM_In";
                                            }
                                            else if(save.getAuthenticate().equals("timePM_In")){
                                                //get timeAMOut and compare it with timePM_In
                                                priorAuthenticate = "timeAM_Out";
                                            }
                                            else if(save.getAuthenticate().equals("timePM_Out")){
                                                //get timePM_In and compare it with timePM_Out
                                                priorAuthenticate = "timePM_In";
                                            }
                                            else{

                                            }

                                            Log.d(TAG, "priorAuthenticate Value : " + priorAuthenticate);

                                            // get priorAuthenticate time and compare it with getAuthenticate
                                            Read read = new Read();
                                            read.readRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/attendance/" + save.getYear() + "/" + save.getMonth() + "/" + save.getDay() + "/" + priorAuthenticate, new Read.OnGetDataListener() {
                                                @Override
                                                public void onSuccess(DataSnapshot dataSnapshot) {

                                                    // Check if priorAuthenticate is not empty
                                                    String priorTime;

                                                    try {
                                                        if (dataSnapshot.getValue(String.class).equals("")) {
                                                            priorTime = "00:00 AM";
                                                        }
                                                        else{
                                                            priorTime = dataSnapshot.getValue().toString();
                                                        }
                                                    }
                                                    catch (NullPointerException e){
                                                        priorTime = "00:00 AM";
                                                    }
                                                    String currentTime = DateUtils.getCurrentTime();

                                                    Log.d(TAG, "priorTime Value : " + priorTime);

                                                    // Spit String priorTime into hours and minutes
                                                    String[] priorTimeParts = priorTime.split(" ")[0].split(":");
                                                    int priorHour = Integer.parseInt(priorTimeParts[0]);
                                                    int priorMinutes = Integer.parseInt(priorTimeParts[1]);


                                                    // Spit String currentTime into hours and minutes
                                                    String[] currentTimeParts = currentTime.split(" ")[0].split(":");
                                                    int currentHour = Integer.parseInt(currentTimeParts[0]);
                                                    int currentMinutes = Integer.parseInt(currentTimeParts[1]);

                                                    if(priorTime.contains("PM")){
                                                        priorHour += 12;
                                                    }
                                                    if(currentTime.contains("PM")){
                                                        currentHour += 12;
                                                    }

                                                    int hourDifference = Math.abs(priorHour - currentHour);
                                                    int minuteDifference = Math.abs(priorMinutes - currentMinutes);
                                                    int totalMinuteDifference = hourDifference*60 + minuteDifference;
                                                    if(totalMinuteDifference <= 15){
                                                        Toast.makeText(getApplicationContext(), "Wait 15 minutes", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else{

                                                        // Get the current location of phone through GPS
                                                        GPSCoordinates gpsCoordinates = new GPSCoordinates(LogInAttendance.this);
                                                        Location currentLocation = gpsCoordinates.getCurrentLocation();

                                                        if (currentLocation != null) {
                                                            employee.setLatitude(currentLocation.getLatitude());
                                                            employee.setLongitude(currentLocation.getLongitude());

                                                            // Check employee Coordinate if employee is inside the 4 corners of the campus
                                                            if(employee.getLatitude() >= Double.parseDouble(school.getLatitudeBottom()) && employee.getLatitude() <= Double.parseDouble(school.getLatitudeTop()) && employee.getLongitude() >= Double.parseDouble(school.getLongitudeLeft()) && employee.getLongitude() <= Double.parseDouble(school.getLongitudeRight())){
                                                                Toast.makeText(getApplicationContext(), "Thank you", Toast.LENGTH_SHORT).show();

                                                                // Push Time in Database
                                                                create.createRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/attendance/" + save.getYear() + "/" + save.getMonth() + "/" + save.getDay() + "/" + save.getAuthenticate(), DateUtils.getCurrentTime());
                                                                thankyou.start();
                                                            }
                                                            else{
                                                                Toast.makeText(getApplicationContext(), "You are outside the Campus. Connect to School WIFI", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } else {
                                                            Toast.makeText(getApplicationContext(), "No location data available", Toast.LENGTH_SHORT).show();
                                                            Log.d("Location", "No location data available.");
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    }
                                    catch (NullPointerException e){

                                        // Create time cells for each day
                                        read.readRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/attendance/" + save.getYear() + "/" + save.getMonth(), new Read.OnGetDataListener() {
                                            @Override
                                            public void onSuccess(DataSnapshot dataSnapshot) {
                                                for(int i = 1 ; i <= DateUtils.getNumberOfDays(save.getMonth(), save.getYear()); i++){
                                                    if(!dataSnapshot.hasChild(String.valueOf(i))){
                                                        create.createRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/attendance/" + save.getYear() + "/" + save.getMonth() + "/" + i + "/timeAM_In", "");
                                                        create.createRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/attendance/" + save.getYear() + "/" + save.getMonth() + "/" + i + "/timeAM_Out", "");
                                                        create.createRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/attendance/" + save.getYear() + "/" + save.getMonth() + "/" + i + "/timePM_In", "");
                                                        create.createRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/attendance/" + save.getYear() + "/" + save.getMonth() + "/" + i + "/timePM_Out", "");
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(DatabaseError databaseError) {
                                                // handle error here
                                            }
                                        });

                                        Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_LONG).show();
                                    }

                                    Intent intent = new Intent(LogInAttendance.this, Attendance.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void onFailure(DatabaseError databaseError) {
                                    // handle error here
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "ID Not Found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(DatabaseError databaseError) {
                        // handle error here
                    }
                });

                // TODO Log in using Facial Recognition
                // TODO Log in using Biometric

            }
        });
    }

    // Handles Scanned QR Result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        id = findViewById(R.id.id_EditText);

        if (requestCode == REQUEST_CODE_SCAN_QR && resultCode == RESULT_OK && data != null) {
            String qrResult = data.getStringExtra("QR_RESULT");
            // Handle the QR code result here
            Toast.makeText(this, "QR code result: " + qrResult, Toast.LENGTH_SHORT).show();
            id.setText(qrResult);
        }
    }
}

