package com.ams.campusconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ams.campusconnect.firebase.Create;
import com.ams.campusconnect.firebase.Read;
import com.ams.campusconnect.model.Employee;
import com.ams.campusconnect.model.School;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Calendar;

public class SystemAdmin extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    School school = School.getInstance();
    Employee employee = Employee.getInstance();
    Read read = new Read();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_admin);

        // Declare and hook Hamburger button
        ImageButton hamburger = findViewById(R.id.hamburger_ImageButton);

        TextView prompt = findViewById(R.id.prompt);

        // TODO add search bar for School and Employee

        // For submit employee
        EditText id = findViewById(R.id.id_EditText);
        EditText firstName = findViewById(R.id.firstName_EditText);
        EditText lastName = findViewById(R.id.lastName_EditText);

        //Find the spinner in the layout
        TableLayout birthday = findViewById(R.id.birthday_TableLayout);
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

        // For submit School
        EditText schoolID = findViewById(R.id.schoolId_EditText);
        EditText schoolName = findViewById(R.id.schoolName_EditText);
        EditText schoolHead = findViewById(R.id.schoolHead_EditText);
        EditText adminUsername = findViewById(R.id.adminUsername_EditText);
        EditText adminPassword = findViewById(R.id.adminPassword_EditText);
        EditText latitudeBottom = findViewById(R.id.latitudeBottom_EditText);
        EditText latitudeTop = findViewById(R.id.latitudeTop_EditText);
        EditText longitudeLeft = findViewById(R.id.longitudeLeft_EditText);
        EditText longitudeRight = findViewById(R.id.longitudeRight_EditText);

        // Buttons
        ImageButton back = findViewById(R.id.backButton_ImageButton);
        Button submitSchool = findViewById(R.id.submitSchool_Button);
        Button submitEmployee = findViewById(R.id.submitEmployee_Button);

        //Hide add school components
        schoolID.setVisibility(View.GONE);
        schoolName.setVisibility(View.GONE);
        schoolHead.setVisibility(View.GONE);
        adminUsername.setVisibility(View.GONE);
        adminPassword.setVisibility(View.GONE);
        latitudeBottom.setVisibility(View.GONE);
        latitudeTop.setVisibility(View.GONE);
        longitudeLeft.setVisibility(View.GONE);
        longitudeRight.setVisibility(View.GONE);

        // Hide add employee components
        id.setVisibility(View.GONE);
        firstName.setVisibility(View.GONE);
        lastName.setVisibility(View.GONE);
        birthday.setVisibility(View.GONE);

        //Hide buttons
        submitSchool.setVisibility(View.GONE);
        submitEmployee.setVisibility(View.GONE);

        back.setOnClickListener(view -> {
            Intent intent = new Intent (SystemAdmin.this, AdminLogIn.class);
            startActivity(intent);
        });

        hamburger.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(SystemAdmin.this, view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.hamburger_systemadmin_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(SystemAdmin.this);
            popup.show();
        });

        // if submitSchool is clicked
        submitSchool.setOnClickListener(view -> {
            // Check if all field are filled
            if(schoolID.getText().toString().isEmpty() && schoolName.getText().toString().isEmpty() && schoolHead.getText().toString().isEmpty() && adminUsername.getText().toString().isEmpty() && adminPassword.getText().toString().isEmpty() && latitudeBottom.getText().toString().isEmpty() && latitudeTop.getText().toString().isEmpty() && longitudeLeft.getText().toString().isEmpty() && longitudeRight.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(), "Fill all Fields", Toast.LENGTH_SHORT).show();
            }
            else{
                // Get data from EditText and store it in School class

                school.setSchoolID(Integer.parseInt(schoolID.getText().toString()));
                school.setSchoolName(schoolName.getText().toString());
                school.setSchoolHead(schoolHead.getText().toString());
                school.setAdminUsername(adminUsername.getText().toString());
                school.setAdminPassword(adminPassword.getText().toString());
                school.setLatitudeBottom(Double.parseDouble(latitudeBottom.getText().toString()));
                school.setLatitudeTop(Double.parseDouble(latitudeTop.getText().toString()));
                school.setLongitudeLeft(Double.parseDouble(longitudeLeft.getText().toString()));
                school.setLongitudeRight(Double.parseDouble(longitudeRight.getText().toString()));

                // Check if school Id already exists in the databse
                read.readRecord( school.getSchoolID() + "/", new Read.OnGetDataListener() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Toast.makeText(getApplicationContext(), "School ID is already Registered", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            // Create School
                            Create create = new Create();
                            create.createRecord(school.getSchoolID() + "/schoolID", school.getSchoolID());
                            create.createRecord(school.getSchoolID() + "/schoolName", school.getSchoolName());
                            create.createRecord(school.getSchoolID() + "/schoolHead", school.getSchoolHead());
                            create.createRecord(school.getSchoolID() + "/adminUsername", school.getAdminUsername());
                            create.createRecord(school.getSchoolID() + "/adminPassword", school.getAdminPassword());
                            create.createRecord(school.getSchoolID() + "/idNumberFeature", true);
                            create.createRecord(school.getSchoolID() + "/gpsFeature", true);
                            create.createRecord(school.getSchoolID() + "/qrcodeFeature", true);
                            create.createRecord(school.getSchoolID() + "/timeBasedFeature", true);
                            create.createRecord(school.getSchoolID() + "/fingerPrintFeature", true);
                            create.createRecord(school.getSchoolID() + "/facialRecognitionFeature", true);
                            create.createRecord(school.getSchoolID() + "/latitudeBottom", school.getLatitudeBottom());
                            create.createRecord(school.getSchoolID() + "/latitudeTop", school.getLatitudeTop());
                            create.createRecord(school.getSchoolID() + "/longitudeLeft", school.getLongitudeLeft());
                            create.createRecord(school.getSchoolID() + "/longitudeRight", school.getLongitudeRight());
                            create.createRecord(school.getSchoolID() + "/latitudeCenter", (double) (0.0)); // TODO add EditText for latitudeCenter
                            create.createRecord(school.getSchoolID() + "/longitudeCenter", (double) (0.0)); // TODO add EditText for longitudeCenter

                            Toast.makeText(getApplicationContext(), "School Successfully Registered", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SystemAdmin.this, LogbookActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Read Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // if submitEmployee is clicked
        submitEmployee.setOnClickListener(view -> {
            // Check if views are not empty
            if(!firstName.getText().toString().isEmpty() || !lastName.getText().toString().isEmpty() || !id.getText().toString().isEmpty()){
                // Get String from EditText components
                employee.setFirstName(firstName.getText().toString());
                employee.setLastName(lastName.getText().toString());
                employee.setId(id.getText().toString());
                employee.setFullName(employee.getLastName() +", "+ employee.getFirstName());

                employee.setBirthday(monthSpinner.getSelectedItem().toString() + "/" + daySpinner.getSelectedItem().toString() + "/"+ yearSpinner.getSelectedItem().toString());

                employee.setLatitude(0);
                employee.setLongitude(0);

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
                            create.createRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/latitude", employee.getLatitude());
                            create.createRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/longitude", employee.getLongitude());

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
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

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

        // For submit School
        EditText schoolID = findViewById(R.id.schoolId_EditText);
        EditText schoolName = findViewById(R.id.schoolName_EditText);
        EditText schoolHead = findViewById(R.id.schoolHead_EditText);
        EditText adminUsername = findViewById(R.id.adminUsername_EditText);
        EditText adminPassword = findViewById(R.id.adminPassword_EditText);
        EditText latitudeBottom = findViewById(R.id.latitudeBottom_EditText);
        EditText latitudeTop = findViewById(R.id.latitudeTop_EditText);
        EditText longitudeLeft = findViewById(R.id.longitudeLeft_EditText);
        EditText longitudeRight = findViewById(R.id.longitudeRight_EditText);

        // Buttons
        ImageButton back = findViewById(R.id.backButton_ImageButton);
        Button submitSchool = findViewById(R.id.submitSchool_Button);
        Button submitEmployee = findViewById(R.id.submitEmployee_Button);

        switch (menuItem.getItemId()){
            case R.id.add_school:{
                // add school button clicked

                prompt.setText("Register a School");

                // Unhide add school components
                schoolID.setVisibility(View.VISIBLE);
                schoolName.setVisibility(View.VISIBLE);
                schoolHead.setVisibility(View.VISIBLE);
                adminUsername.setVisibility(View.VISIBLE);
                adminPassword.setVisibility(View.VISIBLE);
                latitudeBottom.setVisibility(View.VISIBLE);
                latitudeTop.setVisibility(View.VISIBLE);
                longitudeLeft.setVisibility(View.VISIBLE);
                longitudeRight.setVisibility(View.VISIBLE);

                // Hide add employee components
                id.setVisibility(View.GONE);
                firstName.setVisibility(View.GONE);
                lastName.setVisibility(View.GONE);
                birthday.setVisibility(View.GONE);

                // Unhide submitSchool button
                submitSchool.setVisibility(View.VISIBLE);
                submitEmployee.setVisibility(View.GONE); 
                return true;
            }
            case R.id.edit_school:{
                // TODO edit school
                Toast.makeText(getApplicationContext(), "On going", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.delete_school:{
                // TODO delete school
                Toast.makeText(getApplicationContext(), "On going", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.add_employee:{
                // add employee button clicked

                prompt.setText("Register an Employee");

                // Hide add school components
                schoolID.setVisibility(View.GONE);
                schoolName.setVisibility(View.GONE);
                schoolHead.setVisibility(View.GONE);
                adminUsername.setVisibility(View.GONE);
                adminPassword.setVisibility(View.GONE);
                latitudeBottom.setVisibility(View.GONE);
                latitudeTop.setVisibility(View.GONE);
                longitudeLeft.setVisibility(View.GONE);
                longitudeRight.setVisibility(View.GONE);

                // Unhide add employee components
                id.setVisibility(View.VISIBLE);
                firstName.setVisibility(View.VISIBLE);
                lastName.setVisibility(View.VISIBLE);
                birthday.setVisibility(View.VISIBLE);

                // Unhide submitSchool button
                submitSchool.setVisibility(View.GONE);
                submitEmployee.setVisibility(View.VISIBLE);
                return true;
            }
            case R.id.edit_employee:{
                // TODO Edit employee
                Toast.makeText(getApplicationContext(), "On going", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.delete_employee:{
                // TODO delete employee
                Toast.makeText(getApplicationContext(), "On going", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.transfer_employee:{
                // TODO delete employee
                Toast.makeText(getApplicationContext(), "On going", Toast.LENGTH_SHORT).show();
                return true;
            }
            default:
                return false;
        }
    }
}