package com.ams.campusconnect;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ams.campusconnect.biometric.BiometricUtils;
import com.ams.campusconnect.firebase.Create;
import com.ams.campusconnect.firebase.Read;
import com.ams.campusconnect.firebase.Update;
import com.ams.campusconnect.model.Employee;
import com.ams.campusconnect.model.SaveData;
import com.ams.campusconnect.model.School;
import com.ams.campusconnect.qrcode.ScanQR;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.concurrent.Executor;

public class LogInAttendance extends AppCompatActivity {

    SaveData save = SaveData.getInstance();
    School school = School.getInstance();
    Employee employee = Employee.getInstance();
    Create create = new Create();
    Read read = new Read();

    Update update = new Update();
    private static final int REQUEST_CODE_SCAN_QR = 1;
    EditText idNumber;
    Button submit;

    private static final String TAG = LogInAttendance.class.getSimpleName();

    // Instance of scanFingerPrint
    private ImageButton scanBiometric;
    BiometricUtils biometricManagerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_attendance);

        biometricManagerHelper = new BiometricUtils(this, createBiometricCallback());
        biometricManagerHelper.checkBiometricSupported();

        // Instantiate DateUtils
        DateUtils dateUtils = new DateUtils(LogInAttendance.this);

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

        // Declare thankyou sound
        final MediaPlayer thankyou = MediaPlayer.create(this, R.raw.thankyou);
        final MediaPlayer alreadyhave = MediaPlayer.create(this, R.raw.alreadyhave);

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

        // TODO: add submit if Auth Succeed
        dateUtils.getDateTime((month, day, year, currentTimeIn24Hours, currentTimeIn12Hours) -> {

            back.setOnClickListener(view -> {
                Intent intent = new Intent(LogInAttendance.this, Attendance.class);
                startActivity(intent);
            });

            // Set click listener on button to start ScanQR activity
            scanQR.setOnClickListener(view -> {
                Intent intent = new Intent(LogInAttendance.this, ScanQR.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN_QR);
            });

            scanBiometric.setOnClickListener(view -> biometricManagerHelper.authenticate(false));

            submit.setOnClickListener(view -> {
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
                                            switch (save.getAuthenticate()) {
                                                case "timeAM_Out":
                                                    //get timeAM_In and compare it with timeAM_Out
                                                    priorAuthenticate = "timeAM_In";
                                                    break;
                                                case "timePM_In":
                                                    //get timeAMOut and compare it with timePM_In
                                                    priorAuthenticate = "timeAM_Out";
                                                    break;
                                                case "timePM_Out":
                                                    //get timePM_In and compare it with timePM_Out
                                                    priorAuthenticate = "timePM_In";
                                                    break;
                                                default:

                                                    break;
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
                                                        if (dataSnapshot.getValue(String.class).equals(""))
                                                            priorTime = "00:00 AM";
                                                        else {
                                                            priorTime = dataSnapshot.getValue().toString();
                                                        }
                                                    } catch (NullPointerException e) {
                                                        priorTime = "00:00 AM";
                                                    }

                                                    Log.d(TAG, "priorTime Value : " + priorTime);

                                                    // Spit String priorTime into hours and minutes
                                                    String[] priorTimeParts = priorTime.split(" ")[0].split(":");
                                                    int priorHour = Integer.parseInt(priorTimeParts[0]);
                                                    int priorMinutes = Integer.parseInt(priorTimeParts[1]);


                                                    // Spit String currentTime into hours and minutes
                                                    String[] currentTimeParts = currentTimeIn12Hours.split(" ")[0].split(":");
                                                    int currentHour = Integer.parseInt(currentTimeParts[0]);
                                                    int currentMinutes = Integer.parseInt(currentTimeParts[1]);

                                                    if (priorTime.contains("PM")) {
                                                        priorHour += 12;
                                                    }
                                                    if (currentTimeIn12Hours.contains("PM")) {
                                                        currentHour += 12;
                                                    }

                                                    int hourDifference = Math.abs(priorHour - currentHour);
                                                    int minuteDifference = Math.abs(priorMinutes - currentMinutes);
                                                    int totalMinuteDifference = hourDifference * 60 + minuteDifference;
                                                    if (totalMinuteDifference <= 15) {
                                                        Toast.makeText(getApplicationContext(), "Wait 15 minutes", Toast.LENGTH_SHORT).show();
                                                    } else {

                                                        LocationManager locationManager;
                                                        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


                                                        if (ContextCompat.checkSelfPermission(
                                                                LogInAttendance.this,
                                                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                                                &&
                                                                ContextCompat.checkSelfPermission(LogInAttendance.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                                            ActivityCompat.requestPermissions(LogInAttendance.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                                                        }

                                                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 1, location -> {
                                                            employee.setLatitude(location.getLatitude());
                                                            employee.setLongitude(location.getLongitude());

                                                            // Check employee Coordinate if employee is inside the 4 corners of the campus
                                                            // Toggle switch to punch time without GPS
                                                            // Check if currentLocation is not null
                                                            if (school.isGpsFeature()) {
                                                                if (employee.getLatitude() >= Double.parseDouble(school.getLatitudeBottom()) && employee.getLatitude() <= Double.parseDouble(school.getLatitudeTop()) && employee.getLongitude() >= Double.parseDouble(school.getLongitudeLeft()) && employee.getLongitude() <= Double.parseDouble(school.getLongitudeRight())) {
                                                                    Toast.makeText(getApplicationContext(), "Thank you", Toast.LENGTH_SHORT).show();

                                                                    // Push Time in Database
                                                                    create.createRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/attendance/" + save.getYear() + "/" + save.getMonth() + "/" + save.getDay() + "/" + save.getAuthenticate(), currentTimeIn12Hours);

                                                                    // Store employee's last known location
                                                                    update.updateRecord(school.getSchoolID() + "/employee/" + employee.getId(), "latitude", employee.getLatitude());
                                                                    update.updateRecord(school.getSchoolID() + "/employee/" + employee.getId(), "longitude", employee.getLongitude());

                                                                    thankyou.start();
                                                                } else {
                                                                    Toast.makeText(getApplicationContext(), "Connect to a WIFI for more accurate GPS", Toast.LENGTH_SHORT).show();
                                                                    Toast.makeText(getApplicationContext(), "You are outside the Campus", Toast.LENGTH_SHORT).show();
//                                                                        Log.d("Latitude", employee.getLatitude());
//                                                                        Log.d("Longitude", employee.getLongitude());
                                                                }
                                                            } else{
                                                                Toast.makeText(getApplicationContext(), "Thank you", Toast.LENGTH_SHORT).show();

                                                                // Push Time in Database
                                                                create.createRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/attendance/" + save.getYear() + "/" + save.getMonth() + "/" + save.getDay() + "/" + save.getAuthenticate(), currentTimeIn12Hours);
                                                                thankyou.start();
                                                            }
                                                        });
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
            });
        });
    }

    private BiometricPrompt.AuthenticationCallback createBiometricCallback() {
        return new BiometricPrompt.AuthenticationCallback() {
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
        };
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
            Toast.makeText(this, qrResult, Toast.LENGTH_SHORT).show();

            // set the idNumber_TextView
            idNumber.setText(qrResult);

            // Perform click
            submit.performClick();
        }
    }
}

