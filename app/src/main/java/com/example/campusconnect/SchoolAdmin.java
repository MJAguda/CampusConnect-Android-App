package com.example.campusconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class SchoolAdmin extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    SaveData save = SaveData.getInstance();
    School school = School.getInstance();
    Employee employee = Employee.getInstance();
    Read read = new Read();
    Create create = new Create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_admin);

        ImageButton hamburger = findViewById(R.id.hamburger_ImageButton);

        // TODO add search bar for Employee

        TextView prompt = findViewById(R.id.prompt);

        // For submit employee
        EditText id = findViewById(R.id.id_EditText);
        EditText firstName = findViewById(R.id.firstName_EditText);
        EditText lastName = findViewById(R.id.lastName_EditText);

        //Find the spinner in the layout
        TableLayout birthday = findViewById(R.id.birthday_TableLayout);
        Spinner monthSpinner = findViewById(R.id.month_Spinner);
        Spinner daySpinner = findViewById(R.id.day_Spinner);
        Spinner yearSpinner = findViewById(R.id.year_Spinner);
        //DatePicker birthday = findViewById(R.id.birthday_DatePicker);

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

        // Create an ArrayList for the day
        ArrayList<String> dayList = new ArrayList<>();
        for (int i = 1 ; i <= 31 ; i++) {
            dayList.add(String.valueOf(i));
        }

        // Create an ArrayAdapter for the day spinner
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dayList);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);

        // Create an ArrayList for the year
        ArrayList<String> yearList = new ArrayList<>();
        for (int i = Calendar.getInstance().get(Calendar.YEAR)-100; i <= Calendar.getInstance().get(Calendar.YEAR); i++) {
            yearList.add(String.valueOf(i));
        }

        // Create an ArrayAdapter for the year spinner
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, yearList);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        // Buttons
        ImageButton back = findViewById(R.id.backButton_ImageButton);
        Button submitEmployee = findViewById(R.id.submitEmployee_Button);

        // Hide add employee components
        id.setVisibility(View.GONE);
        firstName.setVisibility(View.GONE);
        lastName.setVisibility(View.GONE);
        birthday.setVisibility(View.GONE);

        //Hide buttons
        submitEmployee.setVisibility(View.GONE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (SchoolAdmin.this, AdminLogIn.class);
                startActivity(intent);
            }
        });

        hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(SchoolAdmin.this, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.hamburger_schooladmin_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(SchoolAdmin.this);
                popup.show();
            }
        });

        // if submitEmployee is clicked
        submitEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if views are not empty
                if(!firstName.getText().toString().isEmpty() || !lastName.getText().toString().isEmpty() || !id.getText().toString().isEmpty()){
                    // Get String from EditText components
                    employee.setFirstName(firstName.getText().toString());
                    employee.setLastName(lastName.getText().toString());
                    employee.setId(id.getText().toString());
                    employee.setFullName(employee.getLastName() +", "+ employee.getFirstName());

                    employee.setBirthday(monthSpinner.getSelectedItem().toString() + "/" + daySpinner.getSelectedItem().toString() + "/"+ yearSpinner.getSelectedItem().toString());

                    /*// Initialize Firebase Realtime Database
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                final DatabaseReference myRef = database.getReference(school.getSchoolID() + "/employee");
                                 */
                    // Check id if exist
                    read.readRecord(school.getSchoolID() + "/employee" + employee.getId(), new Read.OnGetDataListener() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Toast.makeText(getApplicationContext(), "Employee is already registered", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getApplicationContext(), "Successfully registered", Toast.LENGTH_SHORT).show();

                                // Push data to Firebase Realtime Database
                                Create create = new Create();
                                create.createRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/id", employee.getId());
                                create.createRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/fullname", employee.getFullName());
                                create.createRecord(school.getSchoolID() + "/employee/"+employee.getId()+ "/birthdate", employee.getBirthday());

                                firstName.setText("");
                                lastName.setText("");
                                id.setText("");
                                /*
                                Intent intent = new Intent(SystemAdmin.this, MainActivity.class);
                                startActivity(intent);
                                 */
                            }
                        }

                        @Override
                        public void onFailure(DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), "Read Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        TextView prompt = findViewById(R.id.prompt);
        TextView date = findViewById(R.id.dateAndTime_TextView);

        // For submit employee
        EditText id = findViewById(R.id.id_EditText);
        EditText firstName = findViewById(R.id.firstName_EditText);
        EditText lastName = findViewById(R.id.lastName_EditText);

        //Find the spinner in the layout
        TableLayout birthday = findViewById(R.id.birthday_TableLayout);
        Spinner monthSpinner = findViewById(R.id.month_Spinner);
        Spinner daySpinner = findViewById(R.id.day_Spinner);
        Spinner yearSpinner = findViewById(R.id.year_Spinner);
        //DatePicker birthday = findViewById(R.id.birthday_DatePicker);

        // Buttons
        ImageButton back = findViewById(R.id.backButton_ImageButton);
        Button submitEmployee = findViewById(R.id.submitEmployee_Button);

        switch(menuItem.getItemId()){
            case R.id.add_employee:{
                prompt.setText("Register an Employee");

                // Unhide add employee components
                id.setVisibility(View.VISIBLE);
                firstName.setVisibility(View.VISIBLE);
                lastName.setVisibility(View.VISIBLE);
                birthday.setVisibility(View.VISIBLE);

                // Unhide submitSchool button
                submitEmployee.setVisibility(View.VISIBLE);
                return true;
            }
            case R.id.edit_employee:{

                return true;
            }
            case R.id.delete_employee:{
                return true;
            }
            default:
                return false;
        }
    }
}