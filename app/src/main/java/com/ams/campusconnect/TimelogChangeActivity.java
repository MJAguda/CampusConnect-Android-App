package com.ams.campusconnect;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.ams.campusconnect.controller.TimelogController;
import com.ams.campusconnect.model.Employee;
import com.ams.campusconnect.model.School;
import com.ams.campusconnect.model.Timelog;
import com.ams.campusconnect.repository.TimelogRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Calendar;

public class TimelogChangeActivity extends AppCompatActivity {

    School school;
    Employee employee = Employee.getInstance();
    DateUtils dateUtils;
    TimelogController timelogController = new TimelogController(this);

    // Declare components
    ImageButton back, hamburgerMenu;
    TextView AMIn_TextView, AMOut_TextView, PMIn_TextView, PMOut_TextView;
    TimePicker correctTimelogDate;
    EditText requestorId, requestorName, reasonForChange;
    Spinner typeOfTimeLogIssue, monthSpinner, daySpinner, yearSpinner;
    Button AMInButton, AMOutButton, PMInButton, PMOutButton, submitButton;

    String timelogSession;

    Timelog timelog;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timelog_change);

        school = (School) getIntent().getSerializableExtra("school");

        dateUtils = new DateUtils(TimelogChangeActivity.this);

        // Initialize components
        initViews();

        // Set Requestor ID and Requestor Name
        setRequestorDetials();

        // Set Type of Timelog Issue Spinner
        setTypeOfTimeLogIssueSpinner();

        // Set Month, Day, and Year Spinners
        setMonthSpinner();
        setDaySpinner();
        setYearSpinner();

        dateUtils.getDateTime((month, day, year, currentTimeIn24Hours, currentTimeIn12Hours) -> {
            // Set timelogs using setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() for monthSpinner
            monthSpinner.setSelection(Integer.parseInt(month) - 1);
            monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    setDaySpinner();
                    monthSpinner.setSelection(position);

                    // Get timelogs based from month, day, and year Spinner
                    getTimelogs();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            // Set timelogs using setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() for daySpinner
            daySpinner.setSelection(Integer.parseInt(day) - 1);
            daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    daySpinner.setSelection(position);

                    // Get timelogs based from month, day, and year Spinner
                    getTimelogs();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            // Set timelogs using setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() for yearSpinner
            yearSpinner.setSelection(0);
            yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                    setDaySpinner();
                    yearSpinner.setSelection(position);

                    // Get timelogs based from month, day, and year Spinner
                    getTimelogs();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        });

        // Back button
        back.setOnClickListener(v -> {
            Intent intent = new Intent(TimelogChangeActivity.this, Attendance.class);
            intent = intent.putExtra("school", school);
            startActivity(intent);
        });

        // Hamburger menu
        hamburgerMenu.setOnClickListener(v -> {
            Toast.makeText(this, "On Going", Toast.LENGTH_SHORT).show();
        });

        // AM In Button onClickListener
        AMInButton.setOnClickListener(v -> {
            timelogSession = "timeAM_In";

            // Change button colors
            AMInButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F9ED69")));
            AMOutButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
            PMInButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
            PMOutButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));

            // set the drawable of AMIn_TextView to cell_shape.xml
            AMIn_TextView.setBackgroundResource(R.drawable.cell_shape);
            AMOut_TextView.setBackgroundResource(0);
            PMIn_TextView.setBackgroundResource(0);
            PMOut_TextView.setBackgroundResource(0);
        });

        // AM Out Button onClickListener
        AMOutButton.setOnClickListener(v -> {
            timelogSession = "timeAM_Out";

            // Change button colors
            AMInButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
            AMOutButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F9ED69")));
            PMInButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
            PMOutButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));

            AMIn_TextView.setBackgroundResource(0);
            AMOut_TextView.setBackgroundResource(R.drawable.cell_shape);
            PMIn_TextView.setBackgroundResource(0);
            PMOut_TextView.setBackgroundResource(0);
        });

        // PM In Button onClickListener
        PMInButton.setOnClickListener(v -> {
            timelogSession = "timePM_In";

            // Change button colors
            AMInButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
            AMOutButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
            PMInButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F9ED69")));
            PMOutButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));

            AMIn_TextView.setBackgroundResource(0);
            AMOut_TextView.setBackgroundResource(0);
            PMIn_TextView.setBackgroundResource(R.drawable.cell_shape);
            PMOut_TextView.setBackgroundResource(0);
        });

        // PM Out Button onClickListener
        PMOutButton.setOnClickListener(v -> {
            timelogSession = "timePM_Out";

            // Change button colors
            AMInButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
            AMOutButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
            PMInButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
            PMOutButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F9ED69")));

            AMIn_TextView.setBackgroundResource(0);
            AMOut_TextView.setBackgroundResource(0);
            PMIn_TextView.setBackgroundResource(0);
            PMOut_TextView.setBackgroundResource(R.drawable.cell_shape);
        });

        // Submit button onClickListener
        submitButton.setOnClickListener(v -> {
//            // Get all Contents
            String requestorID_Value = String.valueOf(requestorId.getText());
            int requestorSchoolID_Value = school.getSchoolID();
            String typeOfTimeLogIssue_Value = typeOfTimeLogIssue.getSelectedItem().toString();

            String month_Value = monthSpinner.getSelectedItem().toString();
            String day_Value = daySpinner.getSelectedItem().toString();
            String year_Value = yearSpinner.getSelectedItem().toString();

            // timeLogSession
            int correctTimelogDate_Hour = correctTimelogDate.getHour();
            int correctTimelogDate_Minute = correctTimelogDate.getMinute();

            String reasonForChange_Value = reasonForChange.getText().toString();

            LocalTime correctTimeLog_LocalTime = LocalTime.of(correctTimelogDate_Hour, correctTimelogDate_Minute);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
            String timeString = correctTimeLog_LocalTime.format(formatter);

            // Error Detector for each components
            // Check if the following are empty or null requestorID_Value, requestorSchoolID_Value, typeOfTimeLogIssue_Value, month_Value, day_Value, year_Value, timelogSession, timeString, reasonForChange_Value
            if (requestorID_Value == null || requestorID_Value.isEmpty() ||
                    typeOfTimeLogIssue_Value == null || typeOfTimeLogIssue_Value.isEmpty() ||
                    month_Value == null || month_Value.isEmpty() ||
                    day_Value == null || day_Value.isEmpty() ||
                    year_Value == null || year_Value.isEmpty() ||
                    timelogSession == null || timelogSession.isEmpty() ||
                    timeString == null || timeString.isEmpty() ||
                    reasonForChange_Value == null || reasonForChange_Value.isEmpty()) {
                Toast.makeText(this, "Please fill up all the fields", Toast.LENGTH_SHORT).show();
                return;
            }
            if(AMIn_TextView.getText().toString().equals("N/A") && timelogSession.equals("timeAM_In")){
                Toast.makeText(this, "Please select date and timelog", Toast.LENGTH_SHORT).show();
                return;
            }

            dateUtils.getDateTime((month, day, year, currentTimeIn24Hours, currentTimeIn12Hours) -> {
                String requestID = requestorID_Value + "_" + year + "_" + month + "_" + day + "_" + currentTimeIn12Hours;
                // Set all contents to timelog object
                timelog = new Timelog(requestID, requestorID_Value, requestorSchoolID_Value, typeOfTimeLogIssue_Value, month_Value, day_Value, year_Value, timelogSession, timeString, reasonForChange_Value);

                // Log timelog
                Log.d("Timelog", timelog.toString());

                // Call addTimelogChangeRequest method from TimelogController
                timelogController.addTimelogChangeRequest(timelog, school);
            });
        });
    }

    private void getTimelogs() {
        timelogController.getTimelogs(school.getSchoolID(), employee.getId(), yearSpinner.getSelectedItem().toString(), monthSpinner.getSelectedItem().toString(), daySpinner.getSelectedItem().toString(), new TimelogRepository.OnDataFetchListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                // Set timelogs in the textview
                if (!dataSnapshot.exists()) {
                    Log.d("Error", "Timelogs not found");
                    Toast.makeText(getApplicationContext(), "Timelogs not found", Toast.LENGTH_SHORT).show();
                    AMIn_TextView.setText("N/A");
                    AMOut_TextView.setText("N/A");
                    PMIn_TextView.setText("N/A");
                    PMOut_TextView.setText("N/A");
                } else {
                    // if getValue.toString is null then set it to N/A
                    AMIn_TextView.setText(dataSnapshot.child("timeAM_In").getValue().toString().isEmpty() ? "N/A" : dataSnapshot.child("timeAM_In").getValue().toString());
                    AMOut_TextView.setText(dataSnapshot.child("timeAM_Out").getValue().toString().isEmpty() ? "N/A" : dataSnapshot.child("timeAM_Out").getValue().toString());
                    PMIn_TextView.setText(dataSnapshot.child("timePM_In").getValue().toString().isEmpty() ? "N/A" : dataSnapshot.child("timePM_In").getValue().toString());
                    PMOut_TextView.setText(dataSnapshot.child("timePM_Out").getValue().toString().isEmpty() ? "N/A" : dataSnapshot.child("timePM_Out").getValue().toString());
                }
            }

            @Override
            public void onFailure(DatabaseError databaseError) {
                Log.d("TimelogChangeActivity", "onFailure: " + databaseError.getMessage());
                Toast.makeText(TimelogChangeActivity.this, "Failed to get timelogs" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setYearSpinner() {
        // Create an ArrayList for the year
        ArrayList<String> yearList = new ArrayList<>();
        for (int i = Calendar.getInstance().get(Calendar.YEAR); i >= Calendar.getInstance().get(Calendar.YEAR) - 100; i--) {
            yearList.add(String.valueOf(i));
        }

        // Create an ArrayAdapter for the year spinner
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, yearList);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);
    }

    private void setDaySpinner() {
        // Create an ArrayList for the day
        ArrayList<String> dayList = new ArrayList<>();
        for (int i = 1; i <= DateUtils.getNumberOfDays((String) monthSpinner.getSelectedItem(), (String) yearSpinner.getSelectedItem()); i++) {
            dayList.add(String.valueOf(i));
        }

        // Create an ArrayAdapter for the day spinner
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dayList);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);
    }

    private void setMonthSpinner() {
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
    }

    private void setTypeOfTimeLogIssueSpinner() {
        String[] timelogIssueType = {"Forgot to Clock In", "Forgot to Clock Out", "Overbreak", "Off Campus Activity", "Incorrect Time Entry", "Misplaced or Lost Credentials"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timelogIssueType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeOfTimeLogIssue.setAdapter(adapter);
    }

    private void setRequestorDetials() {
        requestorId.setText(employee.getId());
        requestorName.setText(String.format("%s, %s", employee.getLastName(), employee.getFirstName()));
    }

    private void initViews() {
        back = findViewById(R.id.backButton_ImageButton);
        hamburgerMenu = findViewById(R.id.hamburger_ImageButton);
        requestorId = findViewById(R.id.requestorId_EditText);
        requestorName = findViewById(R.id.requestorName_EditText);
        typeOfTimeLogIssue = findViewById(R.id.typeOfTimelogIssue_Spinner);
        monthSpinner = findViewById(R.id.month_Spinner);
        daySpinner = findViewById(R.id.day_Spinner);
        yearSpinner = findViewById(R.id.year_Spinner);
        AMInButton = findViewById(R.id.AMIn_Button);
        AMOutButton = findViewById(R.id.AMOut_Button);
        PMInButton = findViewById(R.id.PMIn_Button);
        PMOutButton = findViewById(R.id.PMOut_Button);
        AMIn_TextView = findViewById(R.id.AMIn_TextView);
        AMOut_TextView = findViewById(R.id.AMOut_TextView);
        PMIn_TextView = findViewById(R.id.PMIn_TextView);
        PMOut_TextView = findViewById(R.id.PMOut_TextView);
        correctTimelogDate = findViewById(R.id.correctTimelog_TimePicker);
        reasonForChange = findViewById(R.id.reasonForChange_EditText);
        submitButton = findViewById(R.id.submitTimelogChangeRequest_Button);
    }
}