package com.ams.campusconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ams.campusconnect.model.Employee;
import com.ams.campusconnect.model.School;
import com.ams.campusconnect.model.Timelog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class TimelogChangeActivity extends AppCompatActivity {

    School school;
    Employee employee = Employee.getInstance();
    DateUtils dateUtils;

    // Declare components
    ImageButton back, hamburgerMenu;
    TextView AMIn_TextView, AMOut_TextView, PMIn_TextView, PMOut_TextView;
    EditText requestorId, requestorName, correctTimelogDate, reasonForChange;
    Spinner typeOfTimeLogIssue, monthSpinner, daySpinner, yearSpinner;
    Button AMInButton, AMOutButton, PMInButton, PMOutButton, submitButton;
    TextView AMIn, AMOut, PMIn, PMOut;

    String timelogSession;

    Timelog timelog = new Timelog();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timelog_change);

        school = (School) getIntent().getSerializableExtra("school");

        dateUtils = new DateUtils(TimelogChangeActivity.this);

        // Initialize components
        initViews();

        // Set Requestor ID and Requestor Name
        setRequstorDetials();

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
                    monthSpinner.setSelection(position);

                    // set timelogs in the textview
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

                    // Set timelogs in the textview
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
                    yearSpinner.setSelection(position);

                    // Set timelogs in the textview
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
        });

        // AM Out Button onClickListener
        AMOutButton.setOnClickListener(v -> {
            timelogSession = "timeAM_Out";

            // Change button colors
            AMInButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
            AMOutButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F9ED69")));
            PMInButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
            PMOutButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
        });

        // PM In Button onClickListener
        PMInButton.setOnClickListener(v -> {
            timelogSession = "timePM_In";

            // Change button colors
            AMInButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
            AMOutButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
            PMInButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F9ED69")));
            PMOutButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
        });

        // PM Out Button onClickListener
        PMOutButton.setOnClickListener(v -> {
            timelogSession = "timePM_Out";

            // Change button colors
            AMInButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
            AMOutButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
            PMInButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#808080")));
            PMOutButton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#F9ED69")));
        });

        // Submit button onClickListener
        submitButton.setOnClickListener(v -> {
            // Get all Contents
            // Set it all contents to timelog object
            // Call addTimelogChangeRequest method from TimelogController
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
        for (int i = 1; i <= 31; i++) {
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
        String[] timelogIssueType = {"Forgot to Click In", "Forgot to Click Out", "Overbreak", "Off Campus", "Incorrect Time Entry", "Misplaced or Lost Credentials"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timelogIssueType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeOfTimeLogIssue.setAdapter(adapter);
    }

    private void setRequstorDetials() {
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
        correctTimelogDate = findViewById(R.id.correctTimelog_EditText);
        reasonForChange = findViewById(R.id.reasonForChange_EditText);
        submitButton = findViewById(R.id.submitTimelogChangeRequest_Button);
    }
}