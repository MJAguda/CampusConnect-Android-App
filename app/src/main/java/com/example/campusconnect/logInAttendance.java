package com.example.campusconnect;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class logInAttendance extends AppCompatActivity {

    SaveData save = SaveData.getInstance();
    School school = School.getInstance();
    Create create = new Create();
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_attendance);

        EditText id = findViewById(R.id.id_EditText);
        Button submit = findViewById(R.id.submit_Button);

        // Declare and Initialized locationManager
        LocationManager locationManager;
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        // Declare thankyou sound
        final MediaPlayer thankyou = MediaPlayer.create(this, R.raw.thankyou);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Authenticate
                save.setId(id.getText().toString());

                save.setYear(String.valueOf(calendar.get(Calendar.YEAR)));
                save.setMonth(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()));
                save.setDay(String.valueOf(calendar. get(Calendar.DAY_OF_MONTH)));

                Date dt = new Date();
                SimpleDateFormat dateFormat;
                dateFormat = new SimpleDateFormat("hh:mm a");

                // TODO Scan QR and Compare it with ID Number

                // Check id if exist Log in Using ID Number
                Read read = new Read();
                read.readRecord(school.getSchoolID() + "/employee/" + save.getId(), new Read.OnGetDataListener() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            // Check if Time already exists
                            read.readRecord(school.getSchoolID() + "/employee/" + save.getId() + "/attendance/" + save.getYear() + "/" + save.getMonth() + "/" + save.getDay(), new Read.OnGetDataListener() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {

                                    try {
                                    // check if dataSnapshot exists but not equal to ""
                                        if (!dataSnapshot.child(save.getAuthenticate()).getValue().equals("")) {
                                            Toast.makeText(getApplicationContext(), "Already Have", Toast.LENGTH_SHORT).show();
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
                                            read.readRecord(school.getSchoolID() + "/employee/" + save.getId() + "/attendance/" + save.getYear() + "/" + save.getMonth() + "/" + save.getDay() + "/" + priorAuthenticate, new Read.OnGetDataListener() {
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
                                                    String currentTime = dateFormat.format(dt);

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
                                                        GPSCoordinates gpsCoordinates = new GPSCoordinates(logInAttendance.this);
                                                        Location currentLocation = gpsCoordinates.getCurrentLocation();

                                                        if (currentLocation != null) {
                                                            save.setLatitude(currentLocation.getLatitude());
                                                            save.setLongitude(currentLocation.getLongitude());

                                                            // Check employee Coordinate if employee is inside the 4 corners of the campus
                                                            if(save.getLatitude() >= save.getFnhsBottonLat() && save.getLatitude() <= save.getFnhsTopLat() && save.getLongitude() >= save.getFnhsLeftLong() && save.getLongitude() <= save.getFnhsRightLong()){
                                                                Toast.makeText(getApplicationContext(), "Thank you", Toast.LENGTH_SHORT).show();

                                                                // Push Time in Database
                                                                create.createRecord(school.getSchoolID() + "/employee/" + save.getId() + "/attendance/" + save.getYear() + "/" + save.getMonth() + "/" + save.getDay() + "/" + save.getAuthenticate(), dateFormat.format(dt));
                                                                thankyou.start();
                                                            }
                                                            else{
                                                                Toast.makeText(getApplicationContext(), "You are outside the Campus", Toast.LENGTH_SHORT).show();
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
                                        read.readRecord(school.getSchoolID() + "/employee/" + save.getId() + "/attendance/" + save.getYear() + "/" + save.getMonth(), new Read.OnGetDataListener() {
                                            @Override
                                            public void onSuccess(DataSnapshot dataSnapshot) {
                                                for(int i = 1 ; i <= DateUtils.getNumberOfDays(save.getMonth(), save.getYear()); i++){
                                                    if(!dataSnapshot.hasChild(String.valueOf(i))){
                                                        create.createRecord(school.getSchoolID() + "/employee/" + save.getId() + "/attendance/" + save.getYear() + "/" + save.getMonth() + "/" + i + "/timeAM_In", "");
                                                        create.createRecord(school.getSchoolID() + "/employee/" + save.getId() + "/attendance/" + save.getYear() + "/" + save.getMonth() + "/" + i + "/timeAM_Out", "");
                                                        create.createRecord(school.getSchoolID() + "/employee/" + save.getId() + "/attendance/" + save.getYear() + "/" + save.getMonth() + "/" + i + "/timePM_In", "");
                                                        create.createRecord(school.getSchoolID() + "/employee/" + save.getId() + "/attendance/" + save.getYear() + "/" + save.getMonth() + "/" + i + "/timePM_Out", "");
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

                                    Intent intent = new Intent(logInAttendance.this, Attendance.class);
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




                // TODO Log in using QR
                // TODO Log in using Facial Recognition
                // TODO Log in using Biometric

            }
        });
    }
}