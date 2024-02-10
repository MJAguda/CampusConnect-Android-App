package com.ams.campusconnect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ams.campusconnect.firebase.Create;
import com.ams.campusconnect.firebase.Read;
import com.ams.campusconnect.gps.GPSCoordinates;
import com.ams.campusconnect.model.Employee;
import com.ams.campusconnect.model.SaveData;
import com.ams.campusconnect.model.School;
import com.ams.campusconnect.qrcode.ScanQR;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.concurrent.Executor;

import com.ams.campusconnect.biometric.BiometricManagerWrapper;

public class LogInAttendance extends AppCompatActivity {

    SaveData save = SaveData.getInstance();
    School school = School.getInstance();
    Employee employee = Employee.getInstance();
    Create create = new Create();
    Read read = new Read();
    private static final int REQUEST_CODE_SCAN_QR = 1;
    EditText idNumber;
    Button submit;


    private static final String TAG = LogInAttendance.class.getSimpleName();

    // Instance of scanFingerPrint
    private ImageButton scanBiometric;

    private BiometricManagerWrapper biometricManagerWrapper;
    private TextView txinfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_attendance);

        // Instantiate DateUtils
        DateUtils dateUtils = new DateUtils(LogInAttendance.this);

        // biometricManagerWrapper
        biometricManagerWrapper = new BiometricManagerWrapper(this);

        // Declare Components
        idNumber = findViewById(R.id.id_EditText);
        ImageButton scanQR = findViewById(R.id.scanQR_ImageButton);
        scanBiometric = findViewById(R.id.scanFingerPrint_ImageButton);
        Button submit = findViewById(R.id.submit_Button);
        ImageButton back = findViewById(R.id.backButton_ImageButton);

        // Hide all components
        idNumber.setVisibility(school.isIdNumberFeature() ? View.VISIBLE : View.GONE);
        scanQR.setVisibility(school.isQrScannerFeature() ? View.VISIBLE : View.GONE);
        scanBiometric.setVisibility(school.isFingerPrintScannerFeature() ? View.VISIBLE : View.GONE);

        submit.setVisibility(school.isIdNumberFeature() ? View.VISIBLE : View.GONE);

        // Declare and Initialized locationManager
        LocationManager locationManager;
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        // Declare thankyou sound
        final MediaPlayer thankyou = MediaPlayer.create(this, R.raw.thankyou);
        final MediaPlayer alreadyhave = MediaPlayer.create(this, R.raw.alreadyhave);

        // Checkbiometric Feature
        checkBiometricSupported();

        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(LogInAttendance.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(LogInAttendance.this, "Auth error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(LogInAttendance.this, "Auth succeeded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(LogInAttendance.this, "Auth failed", Toast.LENGTH_SHORT).show();
            }
        });

        dateUtils.getDateTime(new DateUtils.VolleyCallBack() {
            @Override
            public void onGetDateTime(String month, String day, String year, String currentTimeIn24Hours, String currentTimeIn12Hours) {

                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(LogInAttendance.this, Attendance.class);
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

                scanBiometric.setOnClickListener(this::onBiometricButtonClick);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Authenticate
                        employee.setId(idNumber.getText().toString());

                        save.setYear(year);
                        save.setMonth(DateUtils.getMonthName(month));
                        save.setDay(String.valueOf(Integer.parseInt(day)));



                        // Check id if exist Log in Using ID Number
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
                                                } else {
                                                    //Time must have 15 minutes interval
                                                    String priorAuthenticate = null;
                                                    if (save.getAuthenticate().equals("timeAM_Out")) {
                                                        //get timeAM_In and compare it with timeAM_Out
                                                        priorAuthenticate = "timeAM_In";
                                                    } else if (save.getAuthenticate().equals("timePM_In")) {
                                                        //get timeAMOut and compare it with timePM_In
                                                        priorAuthenticate = "timeAM_Out";
                                                    } else if (save.getAuthenticate().equals("timePM_Out")) {
                                                        //get timePM_In and compare it with timePM_Out
                                                        priorAuthenticate = "timePM_In";
                                                    } else {

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
                                                                } else {
                                                                    priorTime = dataSnapshot.getValue().toString();
                                                                }
                                                            } catch (NullPointerException e) {
                                                                priorTime = "00:00 AM";
                                                            }
                                                            String currentTime = currentTimeIn12Hours;

                                                            Log.d(TAG, "priorTime Value : " + priorTime);

                                                            // Spit String priorTime into hours and minutes
                                                            String[] priorTimeParts = priorTime.split(" ")[0].split(":");
                                                            int priorHour = Integer.parseInt(priorTimeParts[0]);
                                                            int priorMinutes = Integer.parseInt(priorTimeParts[1]);


                                                            // Spit String currentTime into hours and minutes
                                                            String[] currentTimeParts = currentTime.split(" ")[0].split(":");
                                                            int currentHour = Integer.parseInt(currentTimeParts[0]);
                                                            int currentMinutes = Integer.parseInt(currentTimeParts[1]);

                                                            if (priorTime.contains("PM")) {
                                                                priorHour += 12;
                                                            }
                                                            if (currentTime.contains("PM")) {
                                                                currentHour += 12;
                                                            }

                                                            int hourDifference = Math.abs(priorHour - currentHour);
                                                            int minuteDifference = Math.abs(priorMinutes - currentMinutes);
                                                            int totalMinuteDifference = hourDifference * 60 + minuteDifference;
                                                            if (totalMinuteDifference <= 15) {
                                                                Toast.makeText(getApplicationContext(), "Wait 15 minutes", Toast.LENGTH_SHORT).show();
                                                            } else {

                                                                // Get the current location of phone through GPS
                                                                GPSCoordinates gpsCoordinates = new GPSCoordinates(LogInAttendance.this);
                                                                Location currentLocation = gpsCoordinates.getCurrentLocation();

                                                                // Check employee Coordinate if employee is inside the 4 corners of the campus
                                                                // Toggle switch to punch time without GPS
                                                                // Check if currentLocation is not null
                                                                if (school.isGpsFeature() == true && currentLocation != null) {

                                                                    employee.setLatitude(currentLocation.getLatitude());
                                                                    employee.setLongitude(currentLocation.getLongitude());

                                                                    if (employee.getLatitude() >= Double.parseDouble(school.getLatitudeBottom()) && employee.getLatitude() <= Double.parseDouble(school.getLatitudeTop()) && employee.getLongitude() >= Double.parseDouble(school.getLongitudeLeft()) && employee.getLongitude() <= Double.parseDouble(school.getLongitudeRight())) {
                                                                        Toast.makeText(getApplicationContext(), "Thank you", Toast.LENGTH_SHORT).show();

                                                                        // Push Time in Database
                                                                        create.createRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/attendance/" + save.getYear() + "/" + save.getMonth() + "/" + save.getDay() + "/" + save.getAuthenticate(), currentTimeIn12Hours);
                                                                        thankyou.start();
                                                                    } else {
                                                                        Toast.makeText(getApplicationContext(), "You are outside the Campus. Connect to School WIFI", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                } else if (school.isGpsFeature() == false) {
                                                                    Toast.makeText(getApplicationContext(), "Thank you", Toast.LENGTH_SHORT).show();

                                                                    // Push Time in Database
                                                                    create.createRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/attendance/" + save.getYear() + "/" + save.getMonth() + "/" + save.getDay() + "/" + save.getAuthenticate(), currentTimeIn12Hours);
                                                                    thankyou.start();
                                                                }
                                                                else {
                                                                    Toast.makeText(getApplicationContext(), "No location data available", Toast.LENGTH_SHORT).show();
                                                                    Log.d("Location", "No location data available.");
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(DatabaseError databaseError) {
                                                            Toast.makeText(getApplicationContext(), "Read Error", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            } catch (NullPointerException e) {

                                                // Create time cells for each day
                                                read.readRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/attendance/" + save.getYear() + "/" + save.getMonth(), new Read.OnGetDataListener() {
                                                    @Override
                                                    public void onSuccess(DataSnapshot dataSnapshot) {
                                                        for (int i = 1; i <= DateUtils.getNumberOfDays(save.getMonth(), save.getYear()); i++) {
                                                            if (!dataSnapshot.hasChild(String.valueOf(i))) {
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
                                                //Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_LONG).show();

                                                submit.performClick();
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
                    }
                });
            }

            // TODO: add submit if Auth Succeed
            private void onBiometricButtonClick(View view) {
                biometricManagerWrapper.authenticate(false);
            }
        });
    }

    // Handles Scanned QR Result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        idNumber = findViewById(R.id.id_EditText);
        submit = findViewById(R.id.submit_Button);

        if (requestCode == REQUEST_CODE_SCAN_QR && resultCode == RESULT_OK && data != null) {
            String qrResult = data.getStringExtra("QR_RESULT");
            // Handle the QR code result here

            // Print Toast message
            Toast.makeText(this, ""+ qrResult, Toast.LENGTH_SHORT).show();

            // set the idNumber_TextView
            idNumber.setText(qrResult);

            // Perform click
            submit.performClick();
        }
    }



    // Check the state of biometric feature
    private void checkBiometricSupported() {
        String info = biometricManagerWrapper.checkBiometricSupported();
        txinfo = findViewById(R.id.tx_info);
        txinfo.setText(info);
    }

    public void enableButton(boolean enable){
        scanBiometric.setEnabled(enable);
    }

    public void enableButton(boolean enable, boolean enroll){
        enableButton(enable);
        if(!enroll) return;
        Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
        enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                BiometricManager.Authenticators.BIOMETRIC_STRONG
                        | BiometricManager.Authenticators.BIOMETRIC_WEAK);
        startActivity(enrollIntent);
    }
}
