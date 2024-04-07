package com.ams.campusconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class TimelogChangeActivity extends AppCompatActivity {

    // Declare components
    EditText requestorId, requestorName, correctTimelogDate, reasonForChange;
    Spinner typeOfTimeLogIssue, month, day, year;
    Button AMInButton, AMOutButton, PMInButton, PMOutButton, submitButton;
    TextView AMIn, AMOut, PMIn, PMOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timelog_change);

        // Initialize components
        requestorId = findViewById(R.id.requestorId_EditText);
        requestorName = findViewById(R.id.requestorName_EditText);
        typeOfTimeLogIssue = findViewById(R.id.typeOfTimelogIssue_Spinner);
        month = findViewById(R.id.month_Spinner);
        day = findViewById(R.id.day_Spinner);
        year = findViewById(R.id.year_Spinner);
        AMInButton = findViewById(R.id.AMIn_Button);
        AMOutButton = findViewById(R.id.AMOut_Button);
        PMInButton = findViewById(R.id.PMIn_Button);
        PMOutButton = findViewById(R.id.PMOut_Button);
        submitButton =  findViewById(R.id.submitTimelogChangeRequest_Button);

        // Set Type of Timelog Issue Spinner
        String[] timelogSessions = { "timeAM_In", "timeAM_Out", "timePM_In", "timePM_Out" };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timelogSessions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeOfTimeLogIssue.setAdapter(adapter);

        // Set Month Spinner
        // Set Day Spinner
        // Set Year Spinner

        // submit button onClickListener
        submitButton.setOnClickListener(v -> {

        });
    }
}