package com.ams.campusconnect;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ams.campusconnect.biometric.BiometricUtils;
import com.ams.campusconnect.firebase.Create;
import com.ams.campusconnect.firebase.Delete;
import com.ams.campusconnect.firebase.Read;
import com.ams.campusconnect.firebase.Update;
import com.ams.campusconnect.model.Employee;
import com.ams.campusconnect.model.SaveData;
import com.ams.campusconnect.model.School;
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
    Update update = new Update();
    Delete delete = new Delete();
    Timer timer;
    BiometricUtils biometricManagerHelper;

    MediaPlayer thankyou, alreadyhave;

    private static final String TAG = LogInAttendance.class.getSimpleName();

    DateUtils dateUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        dateUtils = new DateUtils(Attendance.this);

        thankyou = MediaPlayer.create(this, R.raw.thankyou);
        alreadyhave = MediaPlayer.create(this, R.raw.alreadyhave);

        // Initialize Clock
        TextView dateTimeTextView = findViewById(R.id.dateAndTime_TextView);
        timer = new Timer();

        // TODO Update clock every 1s
        TimerTask updateTimeTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> dateUtils.getDateTime((month, day, year, currentTimeIn24Hours, currentTimeIn12Hours) -> dateTimeTextView.setText(String.format("%s/%s/%s %s", month, day, year, currentTimeIn12Hours))));
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

        back.setOnClickListener(view -> {
            Intent intent = new Intent(Attendance.this, LogbookActivity.class);
            startActivity(intent);
        });

        hamburger.setOnClickListener(view -> Toast.makeText(getApplicationContext(), "On going", Toast.LENGTH_SHORT).show());


        read.readRecord(school.getSchoolID() + "/", new Read.OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                // Check if ID exists in the database
                if (!dataSnapshot.exists()) {
                    Log.d("Error", "School ID not found");
                    Toast.makeText(getApplicationContext(), "School ID not found", Toast.LENGTH_SHORT).show();
                } else {
                    school.setIdNumberFeature(dataSnapshot.child("idNumberFeature").getValue(Boolean.class));
                    school.setGpsFeature(dataSnapshot.child("gpsFeature").getValue(Boolean.class));
                    school.setTimeBasedFeature(dataSnapshot.child("timeBasedFeature").getValue(Boolean.class));
                    school.setQrScannerFeature(dataSnapshot.child("qrcodeFeature").getValue(Boolean.class));
                    school.setBiometricFeature(dataSnapshot.child("biometricFeature").getValue(Boolean.class));
//                    school.setFacialRecognitionFeature(dataSnapshot.child("facialRecognitionFeature").getValue(Boolean.class));
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
            dateUtils.getDateTime((month, day, year, currentTimeIn24Hours, currentTimeIn12Hours) -> {
                int hours;
                int minutes;

                String[] parts = currentTimeIn24Hours.split(":");
                hours = Integer.parseInt(parts[0]);
                minutes = Integer.parseInt(parts[1].substring(0, 2)); // remove AM/PM

                Log.d("TAG", "Hours: " + hours + " Minutes: " + minutes);
                Log.d("Time-Based Feature", "timeBasedFeature : " + school.isTimeBasedFeature());

                // Check if Time-Based Button Feature is On/Off
                if (!school.isTimeBasedFeature()) {
                    // Disable the time punch Buttons
                    AMIn.setEnabled(true);
                    AMOut.setEnabled(true);
                    PMIn.setEnabled(true);
                    PMOut.setEnabled(true);

                    AMIn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F9ED69")));
                    AMOut.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F9ED69")));
                    PMIn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F9ED69")));
                    PMOut.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F9ED69")));
                } else {
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
            });
        }


        biometricManagerHelper = new BiometricUtils(this, createBiometricCallback());
        biometricManagerHelper.checkBiometricSupported();

        AMIn.setOnClickListener(view -> {
            save.setAuthenticate("timeAM_In");

            biometricManagerHelper.authenticate(false);
        });

        AMOut.setOnClickListener(view -> {
            save.setAuthenticate("timeAM_Out");

            biometricManagerHelper.authenticate(false);
        });

        PMIn.setOnClickListener(view -> {
            save.setAuthenticate("timePM_In");

            biometricManagerHelper.authenticate(false);
        });

        PMOut.setOnClickListener(view -> {
            save.setAuthenticate("timePM_Out");

            biometricManagerHelper.authenticate(false);
        });

        try {
            if (!employee.getId().isEmpty()) {
                name.setText(String.format("%s, %s", employee.getLastName(), employee.getFirstName()));
                dateUtils.getDateTime((month, day, year, currentTimeIn24Hours, currentTimeIn12Hours) -> {
                    save.setYear(year);
                    save.setMonth(DateUtils.getMonthName(month));
                    save.setDay(String.valueOf(Integer.parseInt(day)));

                    getTimeLogs(save.getYear(), save.getMonth());
                });


            }
        } catch (NullPointerException e) {
            name.setText("Null");
        }
    }

    private BiometricPrompt.AuthenticationCallback createBiometricCallback() {

        String employeeAttendancePath = school.getSchoolID() + "/employee/" + employee.getId();

        return new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(Attendance.this, "Auth error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                // TODO: Check Developer Option

                // TODO: Add time submit here

                Log.d("Check: ", "Outside dateUtils");


                dateUtils.getDateTime((month, day, year, currentTimeIn24Hours, currentTimeIn12Hours) -> {

                    Log.d("Code Block : ", "inside dateUtils");
                    setDateTime(month, day, year);

//                    TODO : Fix Bug time is not registering
                    // Push Time in Database

                    checkEmployeeAttendance(employee, school, save, currentTimeIn12Hours);


                });


//                Toast.makeText(Attendance.this, "Auth succeeded", Toast.LENGTH_SHORT).show();
            }

            private void setDateTime(String month, String day, String year) {
                Log.d("Code Block : ", "inside setDateTime");
                save.setYear(year);
                save.setMonth(DateUtils.getMonthName(month));
                save.setDay(String.valueOf(Integer.parseInt(day)));
            }

            private void checkEmployeeAttendance(Employee employee, School school, SaveData save, String currentTimeIn12Hours) {

                Log.d("Code Block : ", "inside checkEmployeeAttendance");
                // Check if the employee ID exists
                readEmployeeRecord(employeeAttendancePath, save, school, employee, currentTimeIn12Hours);
            }

            private void readEmployeeRecord(String employeeAttendancePath, SaveData save, School school, Employee employee, String currentTimeIn12Hours) {
                Log.d("Code Block : ", "inside readEmployeeRecord");
                read.readRecord(employeeAttendancePath, new Read.OnGetDataListener() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String attendancePath = employeeAttendancePath + "/attendance/" + save.getYear() + "/" + save.getMonth() + "/" + save.getDay();

                            // Check if the employee's attendance for the given date exists
                            readAttendanceRecord(attendancePath, save, employee, school, currentTimeIn12Hours);
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

            private void readAttendanceRecord(String attendancePath, SaveData save, Employee employee, School school, String currentTimeIn12Hours) {
                Log.d("Code Block : ", "inside readAttendanceRecord");
                read.readRecord(attendancePath, new Read.OnGetDataListener() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(save.getAuthenticate()).exists() && !dataSnapshot.child(save.getAuthenticate()).getValue(String.class).equals("")) {
                            Toast.makeText(getApplicationContext(), "Already Have", Toast.LENGTH_SHORT).show();
                            alreadyhave.start();
                        } else {
                            checkTimeIntervals(save, employee, school, currentTimeIn12Hours);

                        }
                    }

                    @Override
                    public void onFailure(DatabaseError databaseError) {
                        // handle error here
                    }
                });
            }

            private void checkTimeIntervals(SaveData save, Employee employee, School school, String currentTimeIn12Hours) {
                Log.d("Code Block : ", "inside checkTimeIntervals");

                String priorAuthenticate = calculatePriorAuthenticate(save.getAuthenticate());

//                Log.d("year : ", save.getYear());
//                Log.d("month : ", save.getMonth());
//                Log.d("day : ", save.getDay());
//                Log.d("priorAuthenticate : ", priorAuthenticate);

                read.readRecord(employeeAttendancePath + "/attendance/" + save.getYear() + "/" + save.getMonth() + "/" + save.getDay() + "/" + priorAuthenticate, new Read.OnGetDataListener() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
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

                        Log.d("priorTime : ", priorTime);

                        int totalMinuteDifference = calculateTimeDifference(currentTimeIn12Hours, priorTime);

                        Log.d("", String.valueOf(totalMinuteDifference));

                        if (totalMinuteDifference <= 15) {
                            Toast.makeText(getApplicationContext(), "Wait 15 minutes", Toast.LENGTH_SHORT).show();
                        } else {
                            getLocationAndCheckIn(employee, school, save, currentTimeIn12Hours);
                        }
                    }

                    @Override
                    public void onFailure(DatabaseError databaseError) {
                        // handle error here
                    }
                });
            }

            private void getLocationAndCheckIn(Employee employee, School school, SaveData save, String currentTimeIn12Hours) {
                Log.d("Code Block : ", "inside getLocationAndCheckIn");

                LocationManager locationManager;
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                if (ContextCompat.checkSelfPermission(Attendance.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(Attendance.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Attendance.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 1, location -> {
                    employee.setLatitude(location.getLatitude());
                    employee.setLongitude(location.getLongitude());

                    if (checkDeveloperOptions()) {
                        // Developer options are enabled, open settings
                        finish();
                        openDeveloperOptionsSettings();
                    }
                    else {
                        // Check employee Coordinate if employee is inside the 4 corners of the campus
                        // Toggle switch to punch time without GPS
                        // Check if currentLocation is not null

                        if (school.isGpsFeature()) {
                            if (isEmployeeWithinCampusBounds(employee, school)) {
                                Toast.makeText(getApplicationContext(), "Thank you", Toast.LENGTH_SHORT).show();

                                // Push Time in Database
                                create.createRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/attendance/" + save.getYear() + "/" + save.getMonth() + "/" + save.getDay() + "/" + save.getAuthenticate(), currentTimeIn12Hours);

                                // Store employee's last known location
                                update.updateRecord(school.getSchoolID() + "/employee/" + employee.getId(), "latitude", employee.getLatitude());
                                update.updateRecord(school.getSchoolID() + "/employee/" + employee.getId(), "longitude", employee.getLongitude());

//                            thankyou.start();

                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Connect to a WIFI for more accurate GPS", Toast.LENGTH_SHORT).show();
                                Toast.makeText(getApplicationContext(), "You are outside the Campus", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Thank you", Toast.LENGTH_SHORT).show();

                            // Push Time in Database
                            create.createRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/attendance/" + save.getYear() + "/" + save.getMonth() + "/" + save.getDay() + "/" + save.getAuthenticate(), currentTimeIn12Hours);
//                        thankyou.start();

                            finish();
                        }
                    }
                });
            }

            private boolean isEmployeeWithinCampusBounds(Employee employee, School school) {
                return (employee.getLatitude() >= school.getLatitudeBottom() &&
                        employee.getLatitude() <= school.getLatitudeTop() &&
                        employee.getLongitude() >= school.getLongitudeLeft() &&
                        employee.getLongitude() <= school.getLongitudeRight());
            }

            private int calculateTimeDifference(String currentTimeIn12Hours, String priorTime) {
                String[] priorTimeParts = priorTime.split(" ")[0].split(":");
                int priorHour = Integer.parseInt(priorTimeParts[0]);
                int priorMinutes = Integer.parseInt(priorTimeParts[1]);

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
                return hourDifference * 60 + minuteDifference;
            }

            private String calculatePriorTime(DataSnapshot dataSnapshot) {
                String priorTime;
                try {
                    priorTime = (dataSnapshot.exists() && dataSnapshot.getValue(String.class) != null) ? dataSnapshot.getValue(String.class) : "00:00 AM";
                } catch (NullPointerException e) {
                    priorTime = "00:00 AM";
                }
                return priorTime;
            }

            private String calculatePriorAuthenticate(String currentAuthenticate) {
                String priorAuthenticate = null;
                switch (currentAuthenticate) {
                    case "timeAM_Out":
                        priorAuthenticate = "timeAM_In";
                        break;
                    case "timePM_In":
                        priorAuthenticate = "timeAM_Out";
                        break;
                    case "timePM_Out":
                        priorAuthenticate = "timePM_In";
                        break;
                }
                return priorAuthenticate;
            }


            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(Attendance.this, "Auth failed", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private boolean checkDeveloperOptions() {
        int developerOptions = Settings.Secure.getInt(getContentResolver(), Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0);
        return developerOptions == 1;
    }

    private void openDeveloperOptionsSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
        startActivity(intent);
        // Display a message informing the user
        Toast.makeText(Attendance.this, "Please turn off Developer Options", Toast.LENGTH_LONG).show();
    }

    private void getTimeLogs(String year, String month) {
        read.readRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/attendance/" + year + "/" + month, new Read.OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                TableLayout table = (TableLayout) findViewById(R.id.dtr_TableLayout);
                table.removeAllViews();

                for (int i = 1; i <= DateUtils.getNumberOfDays(month, year); i++) {

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
                    for (DataSnapshot grandChild : child.getChildren()) {
                        // Log.d("Time", grandChild.getKey() + " : " + grandChild.getValue());

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


    // Destroy Clock
    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}

