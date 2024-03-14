package com.ams.campusconnect;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
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

import com.ams.campusconnect.controller.SchoolController;
import com.ams.campusconnect.firebase.Create;
import com.ams.campusconnect.firebase.Delete;
import com.ams.campusconnect.firebase.Read;
import com.ams.campusconnect.firebase.Update;
import com.ams.campusconnect.model.Employee;
import com.ams.campusconnect.model.School;
import com.ams.campusconnect.model.SchoolModel;
import com.ams.campusconnect.qrcode.ScanQR;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LogbookActivity extends AppCompatActivity {
    SchoolModel schoolModel;

    Create create = new Create();
    Read read = new Read();
    Update update = new Update();
    Delete delete = new Delete();
    Timer timer;

    TableLayout table;
    DateUtils dateUtils;
    Employee employee = Employee.getInstance();

    private static final int REQUEST_CODE_SCAN_QR = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logbook);

        // Ask user to allow permission for android.permission.CAMERA
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Please allow permission for camera", Toast.LENGTH_SHORT).show();
            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 0);
        }

        // Get the schoolModel from the previous activity
        schoolModel = (SchoolModel) getIntent().getSerializableExtra("schoolModel");

        // Instantiate DateUtils
        dateUtils = new DateUtils(LogbookActivity.this);

        // Declare components
        ImageButton back = findViewById(R.id.backButton_ImageButton);
        TextView schoolName = findViewById(R.id.schoolName_TextView);

        ImageButton home = findViewById(R.id.home_Button);
        ImageButton attendance = findViewById(R.id.attendance_Button);
        ImageButton register = findViewById(R.id.admin_Button);
        ImageButton generate = findViewById(R.id.generate_Button);

        //Find the spinner in the layout
        Spinner monthSpinner = findViewById(R.id.month_Spinner);
        Spinner daySpinner = findViewById(R.id.day_Spinner);
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

        // Create items for day
        ArrayList<String> dayList = new ArrayList<>();
        for (int i = 1; i <= dateUtils.getNumberOfDays((String) monthSpinner.getSelectedItem(), (String) yearSpinner.getSelectedItem()); i++) {
            dayList.add(String.valueOf(i));
        }

        // Create an ArrayAdapter for day spinner
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dayList);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);

        // Create an ArrayList for the year
        ArrayList<String> yearList = new ArrayList<>();
        for (int i = Calendar.getInstance().get(Calendar.YEAR); i >= Calendar.getInstance().get(Calendar.YEAR) - 100; i--) {
            yearList.add(String.valueOf(i));
        }

        // Create an ArrayAdapter for the year spinner
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, yearList);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        Log.d("Month from Spinner : ", monthSpinner.getSelectedItem() + "");
        Log.d("Day from Spinner : ", daySpinner.getSelectedItem() + "");
        Log.d("Year from Spinner : ", yearSpinner.getSelectedItem() + "");

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

            // Selected Listener for monthSpinner

            monthSpinner.setSelection(Integer.parseInt(month) - 1);
            monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    monthSpinner.setSelection(position);
//                    Log.d("MonthSprinner Value : ", (String) monthSpinner.getSelectedItem());

                    displayTimeLogs(monthSpinner.getSelectedItem().toString(), daySpinner.getSelectedItem().toString(), yearSpinner.getSelectedItem().toString());

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            // Selected Listener for daySpinner
            daySpinner.setSelection(Integer.parseInt(day) - 1);
            daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    daySpinner.setSelection(position);

                    displayTimeLogs(monthSpinner.getSelectedItem().toString(), daySpinner.getSelectedItem().toString(), yearSpinner.getSelectedItem().toString());

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            // Selected Listener for yearSpinner

            yearSpinner.setSelection(0);
            yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    yearSpinner.setSelection(position);

                    displayTimeLogs(monthSpinner.getSelectedItem().toString(), daySpinner.getSelectedItem().toString(), yearSpinner.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            timer.scheduleAtFixedRate(updateTimeTask, 0, 1000); // TODO update every 1 second

            //Set school Name
            schoolName.setText(schoolModel.getSchoolName());


            back.setOnClickListener(view -> {
                Intent intent = new Intent(LogbookActivity.this, SchoolLogIn.class);
                intent = intent.putExtra("schoolModel", schoolModel);
                startActivity(intent);
            });

            home.setOnClickListener(view -> {
                Intent intent = new Intent(LogbookActivity.this, LogbookActivity.class);
                intent = intent.putExtra("schoolModel", schoolModel);
                startActivity(intent);
            });

            attendance.setOnClickListener(view -> {
                Intent intent = new Intent(LogbookActivity.this, ScanQR.class);
                intent = intent.putExtra("schoolModel", schoolModel);
                startActivityForResult(intent, REQUEST_CODE_SCAN_QR);
            });

            register.setOnClickListener(view -> {
                Intent intent = new Intent(LogbookActivity.this, AdminLogIn.class);
                intent = intent.putExtra("schoolModel", schoolModel);
                startActivity(intent);
            });

            generate.setOnClickListener(view -> {
                Intent intent = new Intent(LogbookActivity.this, Generate.class);
                intent = intent.putExtra("schoolModel", schoolModel);
                startActivity(intent);
            });
        });
    }

    private void displayTimeLogs(String month, String day, String year) {
        // Get the schoolModel from the previous activity
        schoolModel = (SchoolModel) getIntent().getSerializableExtra("schoolModel");

        read.readRecord(schoolModel.getSchoolID() + "/employee", new Read.OnGetDataListener() {
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
                    TableRow row = new TableRow(LogbookActivity.this);

                    // Add the name to the row
                    TextView name = new TextView(LogbookActivity.this);
                    name.setText(fullName);
                    name.setTextSize(10);
                    name.setGravity(Gravity.TOP);
                    name.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    name.setBackground(ContextCompat.getDrawable(LogbookActivity.this, R.drawable.cell_shape));

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
                            for (DataSnapshot grandChild : child.child("attendance/" + year + "/" + month + "/" + Integer.parseInt(day)).getChildren()) {
                                TextView time = new TextView(LogbookActivity.this);
                                time.setText(grandChild.getValue().toString());
                                time.setTextSize(10);
                                TableRow.LayoutParams timeParams = new TableRow.LayoutParams(0, nameHeight, 0.17f);
                                time.setLayoutParams(timeParams);
                                time.setGravity(Gravity.CENTER);
                                time.setBackground(ContextCompat.getDrawable(LogbookActivity.this, R.drawable.cell_shape));
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
    }

    // Handles Scanned QR Result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Get the schoolModel from the previous activity
        schoolModel = (SchoolModel) getIntent().getSerializableExtra("schoolModel");

        if (requestCode == REQUEST_CODE_SCAN_QR && resultCode == RESULT_OK && data != null) {
            String qrResult = data.getStringExtra("QR_RESULT");
            // Handle the QR code result here

            // Print Toast message
//            Toast.makeText(this, qrResult, Toast.LENGTH_SHORT).show();

            // Verify if employee id exists
            read.readRecord(schoolModel.getSchoolID() + "/employee/" + qrResult, new Read.OnGetDataListener() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        Toast.makeText(getApplicationContext(), "ID Not Found", Toast.LENGTH_SHORT).show();
                    }
                    else {

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

                        try {
                            // Set Employee model
                            employee.setId(id);
                            employee.setFirstName(firstName);
                            employee.setLastName(lastName);
                            employee.setFullName(fullName);
                            employee.setBirthday(birthday);
                            employee.setLatitude(latitude);
                            employee.setLongitude(longitude);
                        } catch (NullPointerException e) {
                            // Handle the case where any of the fields is null
                            // For example, you can show an error message or log the issue
                            // You can also throw a different exception or take other appropriate actions based on your requirements
                            // Here's an example of logging the issue:
                             Toast.makeText(getApplicationContext(), "Error: " + "Employee Field is missing. Contact your administrator", Toast.LENGTH_SHORT).show();
                        }


                        dateUtils.getDateTime((month, day, year, currentTimeIn24Hours, currentTimeIn12Hours) -> {
                            read.readRecord(schoolModel.getSchoolID() + "/employee/" + employee.getId() + "/attendance/" + year + "/" + DateUtils.getMonthName(month), new Read.OnGetDataListener() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.exists()) {
                                        for (int i = 1; i <= DateUtils.getNumberOfDays(DateUtils.getMonthName(month), year); i++) {
                                            if (!dataSnapshot.hasChild(String.valueOf(i))) {
                                                create.createRecord(schoolModel.getSchoolID() + "/employee/" + employee.getId() + "/attendance/" + year + "/" + DateUtils.getMonthName(month) + "/" + i + "/timeAM_In", "");
                                                create.createRecord(schoolModel.getSchoolID() + "/employee/" + employee.getId() + "/attendance/" + year + "/" + DateUtils.getMonthName(month) + "/" + i + "/timeAM_Out", "");
                                                create.createRecord(schoolModel.getSchoolID() + "/employee/" + employee.getId() + "/attendance/" + year + "/" + DateUtils.getMonthName(month) + "/" + i + "/timePM_In", "");
                                                create.createRecord(schoolModel.getSchoolID() + "/employee/" + employee.getId() + "/attendance/" + year + "/" + DateUtils.getMonthName(month) + "/" + i + "/timePM_Out", "");
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

                        Intent intent = new Intent(LogbookActivity.this, Attendance.class);
                        intent = intent.putExtra("schoolModel", schoolModel);
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

