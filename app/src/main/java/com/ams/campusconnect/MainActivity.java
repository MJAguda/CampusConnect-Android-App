package com.ams.campusconnect;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.ams.campusconnect.firebase.Create;
import com.ams.campusconnect.firebase.Read;
import com.ams.campusconnect.model.Employee;
import com.ams.campusconnect.model.School;
import com.ams.campusconnect.qrcode.ScanQR;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    School school = School.getInstance();

    Create create = new Create();
    Read read = new Read();
    Timer timer;

    TableLayout table;
    DateUtils dateUtils;
    Employee employee = Employee.getInstance();

    private static final int REQUEST_CODE_SCAN_QR = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate DateUtils
        dateUtils = new DateUtils(MainActivity.this);

        // Declare components
        ImageButton back = findViewById(R.id.backButton_ImageButton);
        TextView schoolName = findViewById(R.id.schoolName_TextView);

        ImageButton home = findViewById(R.id.home_Button);
        ImageButton attendance = findViewById(R.id.attendance_Button);
        ImageButton register = findViewById(R.id.admin_Button);
        ImageButton generate = findViewById(R.id.generate_Button);

        //Find the spinner in the layout
        Spinner monthSpinner = findViewById(R.id.month_Spinner);
        Spinner yearSpinner = findViewById(R.id.year_Spinner);

        // Create an ArrayList for the month
        ArrayList<String> monthList = new ArrayList<>();
        monthList.add("January");
        monthList.add("February");
        monthList.add("March");
        monthList.add("April");
        monthList.add("May");
        monthList.add("June");
        monthList.add("July");
        monthList.add("August");
        monthList.add("September");
        monthList.add("October");
        monthList.add("November");
        monthList.add("December");

        // Create an ArrayAdapter for the month spinner
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, monthList);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

        // Create an ArrayList for the year
        ArrayList<String> yearList = new ArrayList<>();
        for (int i = Calendar.getInstance().get(Calendar.YEAR); i >= Calendar.getInstance().get(Calendar.YEAR) - 100; i--) {
            yearList.add(String.valueOf(i));
        }

        // Create an ArrayAdapter for the year spinner
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, yearList);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);


        TextView dateTimeTextView = findViewById(R.id.dateAndTime_TextView);
        table = (TableLayout) findViewById(R.id.dailyLog_TableLayout);

        dateUtils.getDateTime((month, day, year, currentTimeIn24Hours, currentTimeIn12Hours) -> {
            // Display a Display Date and Time
            timer = new Timer();
            TimerTask updateTimeTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> dateTimeTextView.setText(String.format("%s/%s/%s %s", month, day, year, currentTimeIn12Hours)));
                }
            };

            timer.scheduleAtFixedRate(updateTimeTask, 0, 1000); // TODO update every 1 second

            //Set school Name
            schoolName.setText(school.getSchoolName());

            read.readRecord(school.getSchoolID() + "/employee", new Read.OnGetDataListener() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    table.removeAllViews();

                    // Get all the names from the "employee" node and store them in a list
                    List<String> names = new ArrayList<>();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        String fullName = child.child("fullname").getValue(String.class);
                        if (fullName != null) {
                            names.add(fullName);
                        }
                    }

                    // Sort the names alphabetically
                    Collections.sort(names);

                    // Display the sorted names in the UI
                    for (String fullName : names) {
                        // Create a row for each name
                        TableRow row = new TableRow(MainActivity.this);

                        // Add the name to the row
                        TextView name = new TextView(MainActivity.this);
                        name.setText(fullName);
                        name.setTextSize(10);
                        name.setGravity(Gravity.TOP);
                        name.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                        name.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.cell_shape));

                        // Get the height of the first TextView
                        name.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                        int nameHeight = name.getMeasuredHeight();

                        TableRow.LayoutParams nameParams = new TableRow.LayoutParams(0, nameHeight, 0.32f);
                        name.setLayoutParams(nameParams);
                        row.addView(name);

                        // Add the attendance times to the row
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            String employeeName = child.child("fullname").getValue(String.class);
                            if (employeeName != null && employeeName.equals(fullName)) {
                                for (DataSnapshot grandChild : child.child("attendance/" + year + "/" + dateUtils.getMonthName(month) + "/" + Integer.parseInt(day)).getChildren()) {
                                    TextView time = new TextView(MainActivity.this);
                                    time.setText(grandChild.getValue().toString());
                                    time.setTextSize(10);
                                    TableRow.LayoutParams timeParams = new TableRow.LayoutParams(0, nameHeight, 0.17f);
                                    time.setLayoutParams(timeParams);
                                    time.setGravity(Gravity.CENTER);
                                    time.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.cell_shape));
                                    row.addView(time);
                                }
                            }
                        }

                        table.addView(row);
                    }
                }

                @Override
                public void onFailure(DatabaseError databaseError) {
                    Log.d("Read", "Error: " + databaseError.getMessage());
                    Toast.makeText(getApplicationContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


            back.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity.this, SchoolLogIn.class);
                startActivity(intent);
            });

            home.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            });

            attendance.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity.this, ScanQR.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN_QR);
            });
            register.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity.this, AdminLogIn.class);
                startActivity(intent);
            });

            generate.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity.this, Generate.class);
                startActivity(intent);
            });
        });
    }

    // Handles Scanned QR Result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SCAN_QR && resultCode == RESULT_OK && data != null) {
            String qrResult = data.getStringExtra("QR_RESULT");
            // Handle the QR code result here

            // Print Toast message
//            Toast.makeText(this, qrResult, Toast.LENGTH_SHORT).show();

            // Verify if employee id exists
            read.readRecord(school.getSchoolID() + "/employee/" + qrResult, new Read.OnGetDataListener() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        Toast.makeText(getApplicationContext(), "ID Not Found", Toast.LENGTH_SHORT).show();
                    } else {

                        // Get employee fields from firebase realtime database
                        String id = dataSnapshot.child("id").getValue().toString();
                        String fullName = dataSnapshot.child("fullname").getValue().toString();

                        // Split fullName into firstName and lastName parts
                        String[] nameParts = fullName.split(", ");
                        String lastName = nameParts[0];
                        String firstName = nameParts[1];

                        String birthday = dataSnapshot.child("birthdate").getValue().toString();
                        double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                        double longitude = dataSnapshot.child("longitude").getValue(Double.class);

                        // Set Employee model
                        employee.setId(id);
                        employee.setFirstName(firstName);
                        employee.setLastName(lastName);
                        employee.setFullName(fullName);
                        employee.setBirthday(birthday);
                        employee.setLatitude(latitude);
                        employee.setLongitude(longitude);


                        dateUtils.getDateTime((month, day, year, currentTimeIn24Hours, currentTimeIn12Hours) -> {
                            read.readRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/attendance/" + year + "/" + DateUtils.getMonthName(month), new Read.OnGetDataListener() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.exists()) {
                                        for (int i = 1; i <= DateUtils.getNumberOfDays(DateUtils.getMonthName(month), year) ; i++) {
                                            if (!dataSnapshot.hasChild(String.valueOf(i))) {
                                                create.createRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/attendance/" + year + "/" + DateUtils.getMonthName(month) + "/" + i + "/timeAM_In", "");
                                                create.createRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/attendance/" + year + "/" + DateUtils.getMonthName(month) + "/" + i + "/timeAM_Out", "");
                                                create.createRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/attendance/" + year + "/" + DateUtils.getMonthName(month) + "/" + i + "/timePM_In", "");
                                                create.createRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/attendance/" + year + "/" + DateUtils.getMonthName(month) + "/" + i + "/timePM_Out", "");
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(DatabaseError databaseError) {
                                    // Handle Error Here
                                }
                            });

                        });

                        Intent intent = new Intent(MainActivity.this, Attendance.class);
                        startActivity(intent);

                    }
                }

                @Override
                public void onFailure(DatabaseError databaseError) {
                    // handle error here
                }
            });
        }
    }
}

