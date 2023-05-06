package com.example.campusconnect;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    Create create = new Create();
    Read read = new Read();
    Update update = new Update();
    Delete delete = new Delete();

    TableLayout sourceAndDestinationTableLayout;
    Spinner idSpinner;
    EditText idEditText;
    EditText firstNameEditText;
    EditText lastNameEditText;
    TableLayout birthdayTableLayout;
    TableLayout featuresTableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_admin);

        DateUtils dateUtils = new DateUtils(SchoolAdmin.this);

        ImageButton hamburger = findViewById(R.id.hamburger_ImageButton);
        ImageButton back = findViewById(R.id.backButton_ImageButton);

        /*TextView prompt = findViewById(R.id.prompt);

        // For submit employee
        EditText id = findViewById(R.id.id_EditText);
        EditText firstName = findViewById(R.id.firstName_EditText);
        EditText lastName = findViewById(R.id.lastName_EditText);

        //Find the spinner in the layout
        TableLayout birthday = findViewById(R.id.birthday_TableLayout);
        Spinner monthSpinner = findViewById(R.id.month_Spinner);
        Spinner daySpinner = findViewById(R.id.day_Spinner);
        Spinner yearSpinner = findViewById(R.id.year_Spinner);
        Spinner idSpinner = findViewById(R.id.id_Spinner);
        TableLayout features = findViewById(R.id.features_TableLayout);
        Button submit = findViewById(R.id.submitEmployee_Button);
         */

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

        // Toggle Switches
        // Hook the toggle switches
        Switch idNumberSwitch = findViewById(R.id.idNumber_Switch);
        Switch gpsSwitch = findViewById(R.id.gpsFeature_Switch);
        Switch timeBasedSwitch = findViewById(R.id.timeBased_Switch);
        Switch qrSwitch = findViewById(R.id.qrScannerFeature_Switch);
        Switch fingerprintSwitch = findViewById(R.id.biometricFeature_Switch);
        Switch facialrecognitionSwitch = findViewById(R.id.facialRecognitionFeature_Switch);

        // Set the value for the ToggleSwitch
        idNumberSwitch.setChecked(school.isIdNumberFeature());
        gpsSwitch.setChecked(school.isGpsFeature());
        timeBasedSwitch.setChecked(school.isTimeBasedFeature());
        qrSwitch.setChecked(school.isQrScannerFeature());
        fingerprintSwitch.setChecked(school.isFingerPrintScannerFeature());
        facialrecognitionSwitch.setChecked(school.isFacialRecognitionFeature());

        // Set an OnCheckedChangedListener to listen for changes in the toggle switch
        idNumberSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("idNumberSwitch", "idSwitchSwitch checked: " + b);
                school.setIdNumberFeature(b);

                update.updateRecord(school.getSchoolID() + "", "idNumberFeature", school.isIdNumberFeature());
            }
        });
        gpsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("gpsSwitch", "gpsSwitch checked: " + b);
                school.setGpsFeature(b);
                update.updateRecord(String.valueOf(school.getSchoolID()), "gpsFeature", school.isGpsFeature());
            }
        });
        timeBasedSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("timeBasedSwitch", "timeBasedSwitch checked: " + b);
                school.setTimeBasedFeature(b);
                update.updateRecord(String.valueOf(school.getSchoolID()), "timeBasedFeature", school.isTimeBasedFeature());
            }
        });

        qrSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("SchoolAdmin", "qrSwitch checked: " + b);
                school.setQrScannerFeature(b);

                update.updateRecord(school.getSchoolID() + "", "qrcodeFeature", school.isQrScannerFeature());
            }
        });

        fingerprintSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("SchoolAdmin", "fingerprintSwitch checked: " + b);
                school.setFingerPrintScannerFeature(b);

                update.updateRecord(school.getSchoolID() + "", "fingerPrintFeature", school.isFingerPrintScannerFeature());
            }
        });

        facialrecognitionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("SchoolAdmin", "facialrecognitionSwitch checked: " + b);
                school.setFacialRecognitionFeature(b);

                update.updateRecord(school.getSchoolID() + "", "facialRecognitionFeature", school.isFacialRecognitionFeature());
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        TextView prompt = findViewById(R.id.prompt);

        // For submit employee
        EditText idEditText = findViewById(R.id.id_EditText);
        EditText firstName = findViewById(R.id.firstName_EditText);
        EditText lastName = findViewById(R.id.lastName_EditText);

        //Find the spinner in the layout
        TableLayout birthday = findViewById(R.id.birthday_TableLayout);
        Spinner monthSpinner = findViewById(R.id.month_Spinner);
        Spinner daySpinner = findViewById(R.id.day_Spinner);
        Spinner yearSpinner = findViewById(R.id.year_Spinner);
        TextView sourceTextView = findViewById(R.id.source_TextView);
        //Spinner sourceSpinner = findViewById(R.id.source_Spinner);
        Spinner destinationSpinner = findViewById(R.id.destination_Spinner);
        Spinner idSpinner = findViewById(R.id.id_Spinner);
        TableLayout features = findViewById(R.id.features_TableLayout);

        // Create an ArrayList for the source schoolID
        //ArrayList<String> sourceList = new ArrayList<>();

        // Create an ArrayList for the destination schoolID
        ArrayList<String> destinationList = new ArrayList<>();

        // Create an ArrayList for the id employee
        ArrayList<String> idList = new ArrayList<>();

        // Read all school ID
        read.readRecord("/", new Read.OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                    // check if the key matches the sourceTextView text
                    if(!childSnapshot.getKey().equals(sourceTextView.getText().toString())){
                        destinationList.add(childSnapshot.getKey());
                    }
                }
                // Create an ArrayAdapter for the destinationList
                ArrayAdapter<String> destinationAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, destinationList);
                destinationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                destinationSpinner.setAdapter(destinationAdapter);
            }

            @Override
            public void onFailure(DatabaseError databaseError) {
                Log.d("Read", "Error: " + databaseError.getMessage());
                Toast.makeText(getApplicationContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

            // Read all employee and add it in the idList
        read.readRecord(school.getSchoolID() + "/employee", new Read.OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                    idList.add(childSnapshot.child("fullname").getValue(String.class));
                }

                // Create an ArrayAdapter for the idSpinner
                ArrayAdapter<String> idAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, idList);
                idAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                idSpinner.setAdapter(idAdapter);
            }

            @Override
            public void onFailure(DatabaseError databaseError) {
                Log.d("Read", "Error: " + databaseError.getMessage());
                Toast.makeText(getApplicationContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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

        Button submit = findViewById(R.id.submitEmployee_Button);

        DateUtils dateUtils = new DateUtils(SchoolAdmin.this);

        switch(menuItem.getItemId()){
            case R.id.add_employee:{
                prompt.setText("Register an Employee");

                // Hides all components
                hideAllComponents();

                // Unhide add employee components
                idEditText.setVisibility(View.VISIBLE);
                firstName.setVisibility(View.VISIBLE);
                lastName.setVisibility(View.VISIBLE);
                birthday.setVisibility(View.VISIBLE);

                // setEnabled to true so they are edittable
                idEditText.setEnabled(true);
                firstNameEditText.setEnabled(true);
                lastNameEditText.setEnabled(true);
                birthday.setEnabled(true);

                // Reset the component
                idEditText.setText("");
                idEditText.setText("");
                firstName.setText("");
                lastName.setText("");

                // Unhide submitSchool button
                submit.setVisibility(View.VISIBLE);
                submit.setText("Add an Employee");
                // Submit Button ActionListener
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // Check if one of the TextView are empty
                        if (idEditText.getText().toString().isEmpty() || firstName.getText().toString().isEmpty() || lastName.getText().toString().isEmpty()){
                            Toast.makeText(getApplicationContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
                        }
                        // Check if the length of id number is 12
                        else if(idEditText.getText().toString().length() != 12){
                            Toast.makeText(getApplicationContext(), "Id Number must be equal to 12", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            // Get String from EditText components
                            employee.setFirstName(firstName.getText().toString());
                            employee.setLastName(lastName.getText().toString());
                            employee.setId(idEditText.getText().toString());
                            employee.setFullName(employee.getLastName() +", "+ employee.getFirstName());

                            employee.setBirthday(monthSpinner.getSelectedItem().toString() + "/" + daySpinner.getSelectedItem().toString() + "/"+ yearSpinner.getSelectedItem().toString());

                            // Check id if exist
                            read.readRecord(school.getSchoolID() + "/employee/" + employee.getId(), new Read.OnGetDataListener() {
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
                                        create.createRecord(school.getSchoolID() + "/employee/"+ employee.getId()+ "/birthdate", employee.getBirthday());

                                        firstName.setText("");
                                        lastName.setText("");
                                        idEditText.setText("");

                                        Intent intent = new Intent(SchoolAdmin.this, SchoolAdmin.class);
                                        startActivity(intent);

                                    }
                                }

                                @Override
                                public void onFailure(DatabaseError databaseError) {
                                    Log.d("Read", "Error: " + databaseError.getMessage());
                                    Toast.makeText(getApplicationContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
                return true;
            }
            case R.id.edit_employee:{
                prompt.setText("Edit an Employee");

                // Hides all components
                hideAllComponents();

                // Unhide add employee components
                idEditText.setVisibility(View.VISIBLE);
                firstName.setVisibility(View.VISIBLE);
                lastName.setVisibility(View.VISIBLE);
                birthday.setVisibility(View.VISIBLE);
                idSpinner.setVisibility(View.VISIBLE);

                // setEnabled to true so they are edittable
                idEditText.setEnabled(true);
                firstNameEditText.setEnabled(true);
                lastNameEditText.setEnabled(true);
                birthday.setEnabled(true);

                // ActionListener for the selected Item in the idSpinner
                idSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // Get the selected item from idSpinner
                        String selectedItemName = parent.getItemAtPosition(position).toString();

                        // Display a Toast message with the selected item
                        Toast.makeText(getApplicationContext(), "Selected Item: " + selectedItemName, Toast.LENGTH_SHORT).show();

                        // Read the parent node of the selected fullname
                        read.readRecord(school.getSchoolID() + "/employee", new Read.OnGetDataListener() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                String parentKey = null;
                                for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                                    String fullname = childSnapshot.child("fullname").getValue(String.class);
                                    if (fullname != null && fullname.equals(selectedItemName)) {
                                        parentKey = childSnapshot.getKey();
                                        employee.setId(parentKey);
                                        break;
                                    }
                                }
                                if(parentKey != null){
                                    // Read Employee data from the selectedItemId
                                    read.readRecord(school.getSchoolID() + "/employee/" + parentKey, new Read.OnGetDataListener() {
                                        @Override
                                        public void onSuccess(DataSnapshot dataSnapshot) {
                                            String id = dataSnapshot.child("id").getValue(String.class);
                                            String fullname = dataSnapshot.child("fullname").getValue(String.class);
                                            String birthday = dataSnapshot.child("birthdate").getValue(String.class);

                                            String [] nameArray = fullname.split(", ");
                                            String [] birthdayArray = birthday.split("/");

                                            employee.setId(id);

                                            // Set the values in the EditText
                                            idEditText.setText(id);
                                            idEditText.setEnabled(false);
                                            lastName.setText(nameArray[0]);
                                            firstName.setText(nameArray[1]);
                                            //monthSpinner.setSelection(Integer.parseInt(birthdayArray[0]) - 1) ;
                                            monthSpinner.setSelection(DateUtils.getMonthNumber(birthdayArray[0]));
                                            daySpinner.setSelection(Integer.parseInt(birthdayArray[1]) - 1);

                                            dateUtils.getDateTime(new DateUtils.VolleyCallBack() {
                                                @Override
                                                public void onGetDateTime(String month, String day, String year, String currentTimeIn24Hours, String currentTimeIn12Hours) {
                                                    yearSpinner.setSelection(Integer.parseInt(birthdayArray[2]) - (Integer.parseInt(year) - 100));
                                                }
                                            });
                                        }

                                        @Override
                                        public void onFailure(DatabaseError databaseError) {
                                            Log.d("Read", "Error: " + databaseError.getMessage());
                                            Toast.makeText(getApplicationContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                }
                            }

                            @Override
                            public void onFailure(DatabaseError databaseError) {
                                Log.d("Read", "Error: " + databaseError.getMessage());
                                Toast.makeText(getApplicationContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Do nothing
                    }
                });

                // Unhide submitSchool button
                submit.setVisibility(View.VISIBLE);
                submit.setText("Edit an Employee");
                // Submit Button ActionListener
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // TODO make idNumber editable
                        //update.updateRecord(school.getSchoolID() + "/employee/", employee.getId() , idEditText.getText().toString());
                        //update.updateRecord(school.getSchoolID() + "/employee/" + employee.getId() , "id" , idEditText.getText().toString());
                        update.updateRecord(school.getSchoolID() + "/employee/" + employee.getId() , "fullname" , lastName.getText().toString() + ", " + firstName.getText().toString());
                        update.updateRecord(school.getSchoolID() + "/employee/" + employee.getId() , "birthdate" , (DateUtils.getMonthName(String.valueOf(monthSpinner.getSelectedItemPosition() + 1))) + "/" + daySpinner.getSelectedItem().toString() + "/" + yearSpinner.getSelectedItem().toString());

                        Toast.makeText(getApplicationContext(), "Successfully Edited", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SchoolAdmin.this, SchoolAdmin.class);
                        startActivity(intent);
                    }
                });
                return true;
            }
            case R.id.delete_employee:{
                prompt.setText("Delete an Employee");

                // Hides all components
                hideAllComponents();

                // Unhide add employee components
                idEditText.setVisibility(View.VISIBLE);
                firstName.setVisibility(View.VISIBLE);
                lastName.setVisibility(View.VISIBLE);
                birthday.setVisibility(View.VISIBLE);
                idSpinner.setVisibility(View.VISIBLE);

                // setEnabled to true so they are edittable
                idEditText.setEnabled(true);
                firstNameEditText.setEnabled(true);
                lastNameEditText.setEnabled(true);
                birthday.setEnabled(true);

                // ActionListener for the selected Item in the idSpinner
                idSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // Get the selected item from idSpinner
                        String selectedItemName = parent.getItemAtPosition(position).toString();

                        // Display a Toast message with the selected item
                        Toast.makeText(getApplicationContext(), "Selected Item: " + selectedItemName, Toast.LENGTH_SHORT).show();

                        // Read the parent node of the selected fullname
                        read.readRecord(school.getSchoolID() + "/employee", new Read.OnGetDataListener() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                String parentKey = null;
                                for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                                    String fullname = childSnapshot.child("fullname").getValue(String.class);
                                    if (fullname != null && fullname.equals(selectedItemName)) {
                                        parentKey = childSnapshot.getKey();
                                        employee.setId(parentKey);
                                        break;
                                    }
                                }
                                if(parentKey != null){
                                    // Read Employee data from the selectedItemId
                                    read.readRecord(school.getSchoolID() + "/employee/" + parentKey, new Read.OnGetDataListener() {
                                        @Override
                                        public void onSuccess(DataSnapshot dataSnapshot) {
                                            String id = dataSnapshot.child("id").getValue(String.class);
                                            String fullname = dataSnapshot.child("fullname").getValue(String.class);
                                            String birthday = dataSnapshot.child("birthdate").getValue(String.class);

                                            String [] nameArray = fullname.split(", ");
                                            String [] birthdayArray = birthday.split("/");

                                            employee.setId(id);

                                            // Set the values in the EditText
                                            idEditText.setText(id);
                                            idEditText.setEnabled(false);
                                            lastName.setText(nameArray[0]);
                                            firstName.setText(nameArray[1]);
                                            //monthSpinner.setSelection(Integer.parseInt(birthdayArray[0]) - 1) ;
                                            monthSpinner.setSelection(DateUtils.getMonthNumber(birthdayArray[0]));
                                            daySpinner.setSelection(Integer.parseInt(birthdayArray[1]) - 1);

                                            dateUtils.getDateTime(new DateUtils.VolleyCallBack() {
                                                @Override
                                                public void onGetDateTime(String month, String day, String year, String currentTimeIn24Hours, String currentTimeIn12Hours) {
                                                    yearSpinner.setSelection(Integer.parseInt(birthdayArray[2]) - (Integer.parseInt(year) - 100));
                                                }
                                            });
                                        }

                                        @Override
                                        public void onFailure(DatabaseError databaseError) {
                                            Log.d("Read", "Error: " + databaseError.getMessage());
                                            Toast.makeText(getApplicationContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                }
                            }

                            @Override
                            public void onFailure(DatabaseError databaseError) {
                                Log.d("Read", "Error: " + databaseError.getMessage());
                                Toast.makeText(getApplicationContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Do nothing
                    }
                });
                // Unhide submitSchool button
                submit.setVisibility(View.VISIBLE);
                submit.setText("Delete an Employee");
                // Submit Button ActionListener
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        delete.deleteRecord(school.getSchoolID() + "/employee", String.valueOf(idEditText.getText()));

                        Toast.makeText(getApplicationContext(), "Successfully Deleted", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(SchoolAdmin.this, SchoolAdmin.class);
                        startActivity(intent);
                    }
                });
                return true;
            }
            case R.id.transfer_employee:{

                // Hide components
                hideAllComponents();
                submit.setVisibility(View.GONE);

                sourceTextView.setText(school.getSchoolID() + "");

                // Unhide components needed in transfering employee
                sourceAndDestinationTableLayout.setVisibility(View.VISIBLE);
                idSpinner.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);
                submit.setText("Transfer an Employee");
                idEditText.setVisibility(View.VISIBLE);
                firstNameEditText.setVisibility(View.VISIBLE);
                lastNameEditText.setVisibility(View.VISIBLE);
                birthday.setVisibility(View.VISIBLE);

                // SetEnabled to false making them unedittable
                idEditText.setEnabled(false);
                firstNameEditText.setEnabled(false);
                lastNameEditText.setEnabled(false);
                birthday.setEnabled(false);

                idSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // Get the selected item from idSpinner
                        String selectedItemName = parent.getItemAtPosition(position).toString();

                        // Display a Toast message with the selected item
                        Toast.makeText(getApplicationContext(), "Selected Item: " + selectedItemName, Toast.LENGTH_SHORT).show();

                        // Read the parent node of the selected fullname
                        read.readRecord(school.getSchoolID() + "/employee", new Read.OnGetDataListener() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                String parentKey = null;
                                for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                                    String fullname = childSnapshot.child("fullname").getValue(String.class);
                                    if (fullname != null && fullname.equals(selectedItemName)) {
                                        parentKey = childSnapshot.getKey();
                                        employee.setId(parentKey);
                                        break;
                                    }
                                }
                                if(parentKey != null){
                                    // Read Employee data from the selectedItemId
                                    read.readRecord(school.getSchoolID() + "/employee/" + parentKey, new Read.OnGetDataListener() {
                                        @Override
                                        public void onSuccess(DataSnapshot dataSnapshot) {
                                            String id = dataSnapshot.child("id").getValue(String.class);
                                            String fullname = dataSnapshot.child("fullname").getValue(String.class);
                                            String birthday = dataSnapshot.child("birthdate").getValue(String.class);

                                            String [] nameArray = fullname.split(", ");
                                            String [] birthdayArray = birthday.split("/");

                                            employee.setId(id);

                                            // Set the values in the EditText
                                            idEditText.setText(id);
                                            idEditText.setEnabled(false);
                                            lastName.setText(nameArray[0]);
                                            firstName.setText(nameArray[1]);
                                            //monthSpinner.setSelection(Integer.parseInt(birthdayArray[0]) - 1) ;
                                            monthSpinner.setSelection(DateUtils.getMonthNumber(birthdayArray[0]));
                                            daySpinner.setSelection(Integer.parseInt(birthdayArray[1]) - 1);

                                            dateUtils.getDateTime(new DateUtils.VolleyCallBack() {
                                                @Override
                                                public void onGetDateTime(String month, String day, String year, String currentTimeIn24Hours, String currentTimeIn12Hours) {
                                                    yearSpinner.setSelection(Integer.parseInt(birthdayArray[2]) - (Integer.parseInt(year) - 100));
                                                }
                                            });
                                        }

                                        @Override
                                        public void onFailure(DatabaseError databaseError) {
                                            Log.d("Read", "Error: " + databaseError.getMessage());
                                            Toast.makeText(getApplicationContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                }
                            }

                            @Override
                            public void onFailure(DatabaseError databaseError) {
                                Log.d("Read", "Error: " + databaseError.getMessage());
                                Toast.makeText(getApplicationContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Do nothing
                    }
                });

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseReference source = FirebaseDatabase.getInstance().getReference(school.getSchoolID() + "/employee/" + employee.getId());
                        DatabaseReference destination = FirebaseDatabase.getInstance().getReference(destinationSpinner.getSelectedItem().toString() + "/employee/" + employee.getId());

                        // Create an instance of the Transfer class
                        Transfer transfer = new Transfer(source, destination, employee.getId(), getApplicationContext());

                        // Call the copyRecord method to copy the subtree from the source to the destination node
                        transfer.copyRecord(source, destination);

                        Intent intent = new Intent(SchoolAdmin.this, SchoolAdmin.class);
                        startActivity(intent);
                    }
                });
                return true;
            }
            case R.id.settings:{
                prompt.setText("Settings");

                // Hide all components
                hideAllComponents();
                submit.setVisibility(View.GONE);

                // Unhide features_table layout
                features.setVisibility(View.VISIBLE);
                return true;
            }
            // TODO add case Download All QR
            // TODO add case Download All DTR
            default:{
                return false;
            }
        }
    }

    private void hideAllComponents() {
        // Hook all components
        sourceAndDestinationTableLayout = findViewById(R.id.sourceAndDestination_TableLayout);
        idSpinner = findViewById(R.id.id_Spinner);
        idEditText = findViewById(R.id.id_EditText);
        firstNameEditText = findViewById(R.id.firstName_EditText);
        lastNameEditText = findViewById(R.id.lastName_EditText);
        birthdayTableLayout = findViewById(R.id.birthday_TableLayout);
        featuresTableLayout = findViewById(R.id.features_TableLayout);

        // Hide all components
        sourceAndDestinationTableLayout.setVisibility(View.GONE);
        idSpinner.setVisibility(View.GONE);
        idEditText.setVisibility(View.GONE);
        firstNameEditText.setVisibility(View.GONE);
        lastNameEditText.setVisibility(View.GONE);
        birthdayTableLayout.setVisibility(View.GONE);
        featuresTableLayout.setVisibility(View.GONE);
    }
}