package com.ams.campusconnect;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.ams.campusconnect.firebase.Create;
import com.ams.campusconnect.firebase.Read;
import com.ams.campusconnect.gps.GPSCoordinates;
import com.ams.campusconnect.model.Employee;
import com.ams.campusconnect.model.SaveData;
import com.ams.campusconnect.model.School;
import com.ams.campusconnect.qrcode.ScanQR;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


import java.util.Timer;
import java.util.TimerTask;

public class Attendance extends AppCompatActivity {

    SaveData save = SaveData.getInstance();
    School school = School.getInstance();
    Employee employee = Employee.getInstance();
    Create create = new Create();
    Read read = new Read();

    private static final int REQUEST_CODE_SCAN_QR = 1;

    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        // TODO: Scan QR
        Intent intent = new Intent(Attendance.this, ScanQR.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN_QR);


        // TODO: Set Employee model


        DateUtils dateUtils = new DateUtils(Attendance.this);

        // Initialize Clock
        TextView dateTimeTextView = findViewById(R.id.dateAndTime_TextView);
        timer = new Timer();

        // TODO Update clock every 1s
        TimerTask updateTimeTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dateUtils.getDateTime(new DateUtils.VolleyCallBack() {
                            @Override
                            public void onGetDateTime(String month, String day, String year, String currentTimeIn24Hours, String currentTimeIn12Hours) {
                                dateTimeTextView.setText(month + "/" + day + "/" + year + " " + currentTimeIn12Hours);
                            }
                        });

                    }
                });
            }
        };
        timer.scheduleAtFixedRate(updateTimeTask, 0, 1000); // 1000ms = 1s

        // Declare button components
        ImageButton back = findViewById(R.id.backButton_ImageButton);
        ImageButton hamburger = findViewById(R.id.hamburger_ImageButton);
        Button AMIn = findViewById(R.id.AMIn_Button);
        Button AMOut = findViewById(R.id.AMOut_Button);
        Button PMIn = findViewById(R.id.PMIn_Button);
        Button PMOut = findViewById(R.id.PMOut_Button);

        TextView name = findViewById(R.id.name_TextView);

        // Request GPS
        GPSCoordinates gpsCoordinates = new GPSCoordinates(this);
        Location currentLocation = gpsCoordinates.getCurrentLocation();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Attendance.this, MainActivity.class);
                startActivity(intent);
            }
        });

        hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "On going", Toast.LENGTH_SHORT).show();
            }
        });


        read.readRecord(school.getSchoolID() + "/", new Read.OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                // Check if ID exists in the database
                if(!dataSnapshot.exists()){
                    Log.d("Error","School ID not found");
                    Toast.makeText(getApplicationContext(), "School ID not found", Toast.LENGTH_SHORT).show();
                }
                else{
                    school.setIdNumberFeature(dataSnapshot.child("idNumberFeature").getValue(Boolean.class));
                    school.setGpsFeature(dataSnapshot.child("gpsFeature").getValue(Boolean.class));
                    school.setTimeBasedFeature(dataSnapshot.child("timeBasedFeature").getValue(Boolean.class));
                    school.setQrScannerFeature(dataSnapshot.child("qrcodeFeature").getValue(Boolean.class));
                    school.setFingerPrintScannerFeature(dataSnapshot.child("fingerPrintFeature").getValue(Boolean.class));
                    school.setFacialRecognitionFeature(dataSnapshot.child("facialRecognitionFeature").getValue(Boolean.class));
                }
            }

            @Override
            public void onFailure(DatabaseError databaseError) {
                Log.d("Read", "Error: " + databaseError.getMessage());
                Toast.makeText(getApplicationContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Disable the time punch Buttons
        AMIn.setEnabled(false);
        AMOut.setEnabled(false);
        PMIn.setEnabled(false);
        PMOut.setEnabled(false);

        // Greying all disabled button
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AMIn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
            AMOut.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
            PMIn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
            PMOut.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));


            // Enables Button Depending on time
            // Get current time using DateUtils class


            // Parse time into hours and minutes
            dateUtils.getDateTime(new DateUtils.VolleyCallBack() {
                @Override
                public void onGetDateTime(String month, String day, String year, String currentTimeIn24Hours, String currentTimeIn12Hours) {
                    int hours;
                    int minutes;

                    String[] parts = currentTimeIn24Hours.split(":");
                    hours = Integer.parseInt(parts[0]);
                    minutes = Integer.parseInt(parts[1].substring(0, 2)); // remove AM/PM

                    Log.d("TAG", "Hours: " + hours + " Minutes: " + minutes);
                    Log.d("Time-Based Feature", "timeBasedFeature : " + school.isTimeBasedFeature());

                    // Check if Time-Based Button Feature is On/Off
                    if(school.isTimeBasedFeature() == false){
                        // Disable the time punch Buttons
                        AMIn.setEnabled(true);
                        AMOut.setEnabled(true);
                        PMIn.setEnabled(true);
                        PMOut.setEnabled(true);

                        AMIn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F9ED69")));
                        AMOut.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F9ED69")));
                        PMIn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F9ED69")));
                        PMOut.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F9ED69")));
                    }
                    else if(school.isTimeBasedFeature() == true){
                        // Enable AMIn button if current time is before 7:00 AM
                        if (hours < 12 || (hours == 12 && minutes < 1)) {
                            AMIn.setEnabled(true);
                            AMIn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F9ED69")));
                        }
                        // Enable AMOut and PMIn buttons if current time is between 12:00 PM and 2:00 PM
                        else if (hours == 12 && minutes >= 0 || hours == 13 && minutes <= 0) {
                            AMOut.setEnabled(true);
                            PMIn.setEnabled(true);

                            AMOut.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F9ED69")));
                            PMIn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F9ED69")));
                        }
                        // Enable PMOut button if current time is after 5:00 PM
                        else if (hours >= 13 && minutes > 0) {
                            PMOut.setEnabled(true);
                            PMOut.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F9ED69")));
                        }
                    }
                    else {
                        // Add Default
                    }

                }
            });
        }


        AMIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save.setAuthenticate("timeAM_In");
                Intent intent = new Intent(Attendance.this, LogInAttendance.class);
                startActivity(intent);
            }
        });

        AMOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save.setAuthenticate("timeAM_Out");
                Intent intent = new Intent(Attendance.this, LogInAttendance.class);
                startActivity(intent);
            }
        });

        PMIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save.setAuthenticate("timePM_In");
                Intent intent = new Intent(Attendance.this, LogInAttendance.class);
                startActivity(intent);
            }
        });

        PMOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save.setAuthenticate("timePM_Out");
                Intent intent = new Intent(Attendance.this, LogInAttendance.class);
                startActivity(intent);
            }
        });

        try {
            if (!employee.getId().isEmpty()) {
                Read read = new Read();

                // Get Fullname from database
                read.readRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/fullname", new Read.OnGetDataListener() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        name.setText(dataSnapshot.getValue().toString());

                        read.readRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/attendance/" + save.getYear() + "/" + save.getMonth(), new Read.OnGetDataListener() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {

                                TableLayout table = (TableLayout) findViewById(R.id.dtr_TableLayout);
                                table.removeAllViews();

                                for(int i = 1 ; i <= DateUtils.getNumberOfDays(save.getMonth(), save.getYear()) ; i++){

                                    DataSnapshot child = dataSnapshot.child(String.valueOf(i));

                                    // Instance of the row
                                    TableRow row = new TableRow(Attendance.this);

                                    // Add day to the row
                                    TextView day = new TextView(Attendance.this);
                                    day.setText(child.getKey());
                                    //day.setTextColor(Color.BLACK);
                                    day.setTextSize(12);
                                    day.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f);
                                    day.setLayoutParams(params);
                                    day.setBackground(ContextCompat.getDrawable(Attendance.this, R.drawable.cell_shape));

                                    row.addView(day);

                                    // Add time TextView to the row
                                    for(DataSnapshot grandChild : child.getChildren()){
                                        Log.d("Time", grandChild.getKey() + " : " + grandChild.getValue());

                                        // Add time to the row
                                        TextView time = new TextView(Attendance.this);

                                        time.setText(grandChild.getValue().toString());
                                        time.setTextSize(12);
                                        //time.setTextColor(Color.BLACK);
                                        time.setLayoutParams(params);
                                        time.setGravity(Gravity.CENTER);
                                        time.setBackground(ContextCompat.getDrawable(Attendance.this, R.drawable.cell_shape));

                                        row.addView(time);
                                    }
                                    table.addView(row);
                                }
                            }

                            @Override
                            public void onFailure(DatabaseError databaseError) {
                                // handle error here
                            }
                        });
                    }
                    @Override
                    public void onFailure(DatabaseError databaseError) {
                        // handle error here
                    }
                });
            }
        } catch (NullPointerException e) {
            name.setText("Name");
        }
    }


    // Destroy Clock
    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

}

