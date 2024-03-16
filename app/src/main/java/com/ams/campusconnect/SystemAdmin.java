package com.ams.campusconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ams.campusconnect.controller.SchoolController;
import com.ams.campusconnect.firebase.Create;
import com.ams.campusconnect.firebase.Read;
import com.ams.campusconnect.model.Employee;
import com.ams.campusconnect.model.School;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Calendar;

public class SystemAdmin extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    School school;
    SchoolController schoolController = new SchoolController();
    Employee employee = Employee.getInstance();
    Read read = new Read();

    // Declare all Components

    EditText id;
    EditText firstName;
    EditText lastName;

    TableLayout birthday;
    Spinner monthSpinner;
    Spinner daySpinner;
    Spinner yearSpinner;

    // For submit School
    Spinner schoolIDSpinner;
    EditText schoolID;
    EditText schoolName;
    EditText schoolHead;
    EditText adminUsername;
    EditText adminPassword;
    EditText latitudeBottom;
    EditText latitudeTop;
    EditText longitudeLeft;
    EditText longitudeRight;

    // Buttons
    ImageButton back;
    Button submitSchool;
    Button submitEmployee;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_admin);

        // Initialize progressDialog
        progressDialog = new ProgressDialog(SystemAdmin.this);
        progressDialog.setCancelable(false);

        school = (School) getIntent().getSerializableExtra("school");

        // Declare and hook Hamburger button
        ImageButton hamburger = findViewById(R.id.hamburger_ImageButton);

        TextView prompt = findViewById(R.id.prompt);

        // TODO add search bar for School and Employee

        // For submit employee
        id = findViewById(R.id.id_EditText);
        firstName = findViewById(R.id.firstName_EditText);
        lastName = findViewById(R.id.lastName_EditText);

        //Find the spinner in the layout
        birthday = findViewById(R.id.birthday_TableLayout);
        monthSpinner = findViewById(R.id.month_Spinner);
        daySpinner = findViewById(R.id.day_Spinner);
        yearSpinner = findViewById(R.id.year_Spinner);

        // Create an ArrayList for schoolID
        ArrayList<String> schoolIDList = new ArrayList<>();

        // Fetch all schoolID from the database using schoolController.getSchoolData
        schoolController.getAllSchoolIDs(new SchoolController.OnDataFetchListener() {

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Do not include non integer
                    if (snapshot.getKey().matches("[0-9]+")){
                        schoolIDList.add(snapshot.getKey());
                    }

                    // sort schoolIDList
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        schoolIDList.sort((o1, o2) -> {
                            int n1 = Integer.parseInt(o1);
                            int n2 = Integer.parseInt(o2);
                            return n1 - n2;
                        });
                    }
                }

                // Create an ArrayAdapter for the schoolID spinner
                ArrayAdapter<String> schoolIDAdapter = new ArrayAdapter<>(SystemAdmin.this, android.R.layout.simple_spinner_item, schoolIDList);
                schoolIDAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                schoolIDSpinner.setAdapter(schoolIDAdapter);
            }

            @Override
            public void onFailure(DatabaseError databaseError) {

            }
        });

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
        for (int i = 1; i <= 31; i++) {
            dayList.add(String.valueOf(i));
        }

        // Create an ArrayAdapter for the day spinner
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dayList);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);

        // Create an ArrayList for the year
        ArrayList<String> yearList = new ArrayList<>();
        for (int i = Calendar.getInstance().get(Calendar.YEAR) - 100; i <= Calendar.getInstance().get(Calendar.YEAR); i++) {
            yearList.add(String.valueOf(i));
        }

        // Create an ArrayAdapter for the year spinner
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, yearList);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        // For submit School
        schoolIDSpinner = findViewById(R.id.schoolID_Spinner);
        schoolID = findViewById(R.id.schoolId_EditText);
        schoolName = findViewById(R.id.schoolName_EditText);
        schoolHead = findViewById(R.id.schoolHead_EditText);
        adminUsername = findViewById(R.id.adminUsername_EditText);
        adminPassword = findViewById(R.id.adminPassword_EditText);
        latitudeBottom = findViewById(R.id.latitudeBottom_EditText);
        latitudeTop = findViewById(R.id.latitudeTop_EditText);
        longitudeLeft = findViewById(R.id.longitudeLeft_EditText);
        longitudeRight = findViewById(R.id.longitudeRight_EditText);

        // Buttons
        back = findViewById(R.id.backButton_ImageButton);
        submitSchool = findViewById(R.id.submitSchool_Button);
        submitEmployee = findViewById(R.id.submitEmployee_Button);

        hideAllComponents();

        back.setOnClickListener(view -> {
            changeScreen(AdminLogIn.class);
        });

        hamburger.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(SystemAdmin.this, view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.hamburger_systemadmin_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(SystemAdmin.this);
            popup.show();
        });
    }



    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        TextView prompt = findViewById(R.id.prompt);

        switch (menuItem.getItemId()) {
            case R.id.add_school: {
                // add school button clicked
                hideAllComponents();

                prompt.setText("Register a School");
                submitSchool.setText("Register School");

                // Unhide add school components
                unHideSchoolComponents();


                // if submitSchool is clicked
                submitSchool.setOnClickListener(view -> {

                    progressDialog.setMessage("Registering School...");
                    progressDialog.dismiss();

                    // Check if all field are filled
                    if (schoolID.getText().toString().isEmpty() && schoolName.getText().toString().isEmpty() && schoolHead.getText().toString().isEmpty() && adminUsername.getText().toString().isEmpty() && adminPassword.getText().toString().isEmpty() && latitudeBottom.getText().toString().isEmpty() && latitudeTop.getText().toString().isEmpty() && longitudeLeft.getText().toString().isEmpty() && longitudeRight.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Fill all Fields", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    // Check if latitudeBottom, latitudeTop, longitudeLeft, longitudeRight doesn't contain a .
                    else if (!latitudeBottom.getText().toString().contains(".") ||
                            !latitudeTop.getText().toString().contains(".") ||
                            !longitudeLeft.getText().toString().contains(".") ||
                            !longitudeRight.getText().toString().contains(".")) {
                        Toast.makeText(getApplicationContext(), "Latitude and Longitude must contain a decimal", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    // Check if the . is followed only by 0 example 123.0 but allow if enter is 123.01
                    else if (latitudeBottom.getText().toString().contains(".") &&
                            latitudeBottom.getText().toString().endsWith("0") ||
                            latitudeTop.getText().toString().contains(".") &&
                                    latitudeTop.getText().toString().endsWith("0") ||
                            longitudeLeft.getText().toString().contains(".") &&
                                    longitudeLeft.getText().toString().endsWith("0") ||
                            longitudeRight.getText().toString().contains(".") &&
                                    longitudeRight.getText().toString().endsWith("0")) {
                        Toast.makeText(getApplicationContext(), "Latitude and Longitude must not end with 0", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    // Check if latitudeBottom is greater than latitudeTop
                    else if (Double.parseDouble(latitudeBottom.getText().toString()) >
                            Double.parseDouble(latitudeTop.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Latitude Bottom must be less than Latitude Top", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    // Check if longitudeLeft is greater than longitudeRight
                    else if (Double.parseDouble(longitudeLeft.getText().toString()) >
                            Double.parseDouble(longitudeRight.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Longitude Left must be less than Longitude Right", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    } else {
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
                        school.setLatitudeCenter((school.getLatitudeTop() + school.getLatitudeBottom()) / 2);
                        school.setLongitudeCenter((school.getLongitudeLeft() + school.getLongitudeRight()) / 2);

                        // Check if school Id already exists in the database
                        schoolController.getSchoolData(school.getSchoolID(), new SchoolController.OnDataFetchListener() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    Toast.makeText(getApplicationContext(), "School ID is already Registered", Toast.LENGTH_SHORT).show();

                                    progressDialog.dismiss();

                                } else {

                                    // Create School

                                    schoolController.addSchool(school);

                                    Toast.makeText(getApplicationContext(), "School Successfully Registered", Toast.LENGTH_SHORT).show();

                                    progressDialog.dismiss();

                                    changeScreen(SystemAdmin.class);
                                }
                            }

                            @Override
                            public void onFailure(DatabaseError databaseError) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Read Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                return true;
            }
            case R.id.edit_school: {
                hideAllComponents();

                prompt.setText("Edit a School");
                submitSchool.setText("Edit School");

                // Unhide school components
                unHideSchoolComponents();
                // unHide schoolIDSpinner
                schoolIDSpinner.setVisibility(View.VISIBLE);

                // setEnabled to false so they are not edittable
                schoolID.setEnabled(false);
                schoolName.setEnabled(true);
                schoolHead.setEnabled(true);
                adminUsername.setEnabled(true);
                adminPassword.setEnabled(true);
                latitudeBottom.setEnabled(true);
                latitudeTop.setEnabled(true);
                longitudeLeft.setEnabled(true);
                longitudeRight.setEnabled(true);

                // setOnItemSelecteListener for schoolIDSpinner ()
                schoolIDSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        progressDialog.setMessage("Fetching School data...");
                        progressDialog.show();

                        // Get the selected item from the schoolIDSpinner
                        String selectedItemName = parent.getItemAtPosition(position).toString();

                        // Display a Toast message to show the selected item
                        Toast.makeText(getApplicationContext(), "Selected : " + selectedItemName, Toast.LENGTH_SHORT).show();

                        // Get data from database
                        schoolController.getSchoolData(Integer.parseInt(selectedItemName), new SchoolController.OnDataFetchListener() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                // Get data from database
                                school = dataSnapshot.getValue(School.class);


                                // Set data to EditText
                                schoolID.setText(String.valueOf(school.getSchoolID()));
                                schoolName.setText(school.getSchoolName());
                                schoolHead.setText(school.getSchoolHead());
                                adminUsername.setText(school.getAdminUsername());
                                adminPassword.setText(school.getAdminPassword());
                                latitudeBottom.setText(String.valueOf(school.getLatitudeBottom()));
                                latitudeTop.setText(String.valueOf(school.getLatitudeTop()));
                                longitudeLeft.setText(String.valueOf(school.getLongitudeLeft()));
                                longitudeRight.setText(String.valueOf(school.getLongitudeRight()));

                                progressDialog.dismiss();

                            }

                            @Override
                            public void onFailure(DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(), "Read Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        // Do nothing

                        // Dismiss progressDialog
                        progressDialog.dismiss();
                    }
                });

                // Submit ActionListerner
                submitSchool.setOnClickListener(view -> {

                    progressDialog.setMessage("Updating School...");
                    progressDialog.show();

                    // TODO: Add error handling
                    if(schoolName.getText().toString().isEmpty() || schoolHead.getText().toString().isEmpty() || adminUsername.getText().toString().isEmpty() || adminPassword.getText().toString().isEmpty() || latitudeBottom.getText().toString().isEmpty() || latitudeTop.getText().toString().isEmpty() || longitudeLeft.getText().toString().isEmpty() || longitudeRight.getText().toString().isEmpty()){
                        Toast.makeText(getApplicationContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    else if (!latitudeBottom.getText().toString().contains(".") ||
                            !latitudeTop.getText().toString().contains(".") ||
                            !longitudeLeft.getText().toString().contains(".") ||
                            !longitudeRight.getText().toString().contains(".")) {
                        Toast.makeText(getApplicationContext(), "Latitude and Longitude must contain a decimal", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    // Check if the . is followed only by 0 example 123.0 but allow if enter is 123.01
                    else if (latitudeBottom.getText().toString().contains(".") &&
                            latitudeBottom.getText().toString().endsWith("0") ||
                            latitudeTop.getText().toString().contains(".") &&
                                    latitudeTop.getText().toString().endsWith("0") ||
                            longitudeLeft.getText().toString().contains(".") &&
                                    longitudeLeft.getText().toString().endsWith("0") ||
                            longitudeRight.getText().toString().contains(".") &&
                                    longitudeRight.getText().toString().endsWith("0")) {
                        Toast.makeText(getApplicationContext(), "Latitude and Longitude must not end with 0", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    // Check if latitudeBottom is greater than latitudeTop
                    else if (Double.parseDouble(latitudeBottom.getText().toString()) >
                            Double.parseDouble(latitudeTop.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Latitude Bottom must be less than Latitude Top", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    // Check if longitudeLeft is greater than longitudeRight
                    else if (Double.parseDouble(longitudeLeft.getText().toString()) >
                            Double.parseDouble(longitudeRight.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Longitude Left must be less than Longitude Right", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                    else {
                        // Set school class from the textfield
                        school.setSchoolName(schoolName.getText().toString());
                        school.setSchoolHead(schoolHead.getText().toString());
                        school.setAdminUsername(adminUsername.getText().toString());
                        school.setAdminPassword(adminPassword.getText().toString());
                        school.setLatitudeBottom(Double.parseDouble(latitudeBottom.getText().toString()));
                        school.setLatitudeTop(Double.parseDouble(latitudeTop.getText().toString()));
                        school.setLongitudeLeft(Double.parseDouble(longitudeLeft.getText().toString()));
                        school.setLongitudeRight(Double.parseDouble(longitudeRight.getText().toString()));
                        school.setLatitudeCenter((school.getLatitudeTop() + school.getLatitudeBottom()) / 2);
                        school.setLongitudeCenter((school.getLongitudeLeft() + school.getLongitudeRight()) / 2);

                        // Update school from database
                        schoolController.updateSchool(school);

                        // Toast Message
                        Toast.makeText(getApplicationContext(), "School Updated : " + school.getSchoolID(), Toast.LENGTH_SHORT).show();

                        // Dismiss progressDialog
                        progressDialog.dismiss();

                        // changeScreen (Intent)
                        changeScreen(SystemAdmin.class);
                    }
                });
                return true;
            }
            case R.id.delete_school: {
                hideAllComponents();

                prompt.setText("Delete a School");
                submitSchool.setText("Delete School");

                // Unhide school components
                unHideSchoolComponents();
                // unHide schoolIDSpinner
                schoolIDSpinner.setVisibility(View.VISIBLE);

                // setEnabled to false so they are not edittable
                schoolID.setEnabled(false);
                schoolName.setEnabled(false);
                schoolHead.setEnabled(false);
                adminUsername.setEnabled(false);
                adminPassword.setEnabled(false);
                latitudeBottom.setEnabled(false);
                latitudeTop.setEnabled(false);
                longitudeLeft.setEnabled(false);
                longitudeRight.setEnabled(false);

                submitSchool.setOnClickListener(view -> {

                    progressDialog.setMessage("Deleting School...");
                    progressDialog.show();

                    // Delete school from database
                    schoolController.deleteSchool(school.getSchoolID());

                    // Toast Message
                    Toast.makeText(getApplicationContext(), "School Deleted : " + school.getSchoolID(), Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();

                    // changeScreen (Intent)
                    changeScreen(SystemAdmin.class);
                });

                // setOnItemSelecteListener for schoolIDSpinner ()
                schoolIDSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        progressDialog.setMessage("Fetching School data...");
                        progressDialog.show();

                        // Get the selected item from the schoolIDSpinner
                        String selectedItemName = parent.getItemAtPosition(position).toString();

                        // Display a Toast message to show the selected item
                        Toast.makeText(getApplicationContext(), "Selected : " + selectedItemName, Toast.LENGTH_SHORT).show();

                        // Get data from database
                        schoolController.getSchoolData(Integer.parseInt(selectedItemName), new SchoolController.OnDataFetchListener() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {

                                school = dataSnapshot.getValue(School.class);

                                // Set data to EditText
                                schoolID.setText(String.valueOf(school.getSchoolID()));
                                schoolName.setText(school.getSchoolName());
                                schoolHead.setText(school.getSchoolHead());
                                adminUsername.setText(school.getAdminUsername());
                                adminPassword.setText(school.getAdminPassword());
                                latitudeBottom.setText(String.valueOf(school.getLatitudeBottom()));
                                latitudeTop.setText(String.valueOf(school.getLatitudeTop()));
                                longitudeLeft.setText(String.valueOf(school.getLongitudeLeft()));
                                longitudeRight.setText(String.valueOf(school.getLongitudeRight()));

                                progressDialog.dismiss();

                            }

                            @Override
                            public void onFailure(DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(), "Read Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        // Do nothing
                    }
                });

                // Submit ActionListerner

                return true;
            }
            case R.id.add_employee: {
                // add employee button clicked

                prompt.setText("Register an Employee");
                submitEmployee.setText("Register Employee");

                hideAllComponents();

                unHideEmployeeComponents();

                // if submitEmployee is clicked
                submitEmployee.setOnClickListener(view -> {

                    progressDialog.setMessage("Registering Employee...");
                    progressDialog.show();

                    // Check if views are not empty
                    if (!firstName.getText().toString().isEmpty() || !lastName.getText().toString().isEmpty() || !id.getText().toString().isEmpty()) {
                        // Get String from EditText components
                        employee.setFirstName(firstName.getText().toString());
                        employee.setLastName(lastName.getText().toString());
                        employee.setId(id.getText().toString());
                        employee.setFullName(employee.getLastName() + ", " + employee.getFirstName());

                        employee.setBirthday(monthSpinner.getSelectedItem().toString() + "/" + daySpinner.getSelectedItem().toString() + "/" + yearSpinner.getSelectedItem().toString());

                        employee.setLatitude(0);
                        employee.setLongitude(0);

                        // Check id if exist
                        read.readRecord(school.getSchoolID() + "/employee" + employee.getId(), new Read.OnGetDataListener() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    Toast.makeText(getApplicationContext(), "Employee is already registered", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Successfully registered", Toast.LENGTH_SHORT).show();

                                    // Push data to Firebase Realtime Database
                                    Create create = new Create();
                                    create.createRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/id", employee.getId());
                                    create.createRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/fullname", employee.getFullName());
                                    create.createRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/birthdate", employee.getBirthday());
                                    create.createRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/latitude", employee.getLatitude());
                                    create.createRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/longitude", employee.getLongitude());

                                    firstName.setText("");
                                    lastName.setText("");
                                    id.setText("");

                                    progressDialog.dismiss();

                                    changeScreen(SystemAdmin.class);

                                }
                            }

                            @Override
                            public void onFailure(DatabaseError databaseError) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Read Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
                    }
                });

                return true;
            }
            case R.id.edit_employee: {
                // TODO Edit employee
                Toast.makeText(getApplicationContext(), "On going", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.delete_employee: {
                // TODO delete employee

                Toast.makeText(getApplicationContext(), "On going", Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.transfer_employee: {
                // TODO delete employee
                Toast.makeText(getApplicationContext(), "On going", Toast.LENGTH_SHORT).show();
                return true;
            }
            default:
                return false;
        }
    }
    private void hideAllComponents() {
        //Hide add school components
        schoolIDSpinner.setVisibility(View.GONE);
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
    }
    private void unHideEmployeeComponents() {
        // Unhide add employee components
        id.setVisibility(View.VISIBLE);
        firstName.setVisibility(View.VISIBLE);
        lastName.setVisibility(View.VISIBLE);
        birthday.setVisibility(View.VISIBLE);

        // Unhide submitSchool button
        submitEmployee.setVisibility(View.VISIBLE);
    }

    private void unHideSchoolComponents() {
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

        // Unhide submitSchool button
        submitSchool.setVisibility(View.VISIBLE);
    }

    private void changeScreen(Class<?> toClass) {
        Intent intent = new Intent(SystemAdmin.this, toClass);
        intent = intent.putExtra("school", school);
        startActivity(intent);
    }
}