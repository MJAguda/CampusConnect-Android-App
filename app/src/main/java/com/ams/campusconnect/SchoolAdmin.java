package com.ams.campusconnect;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ams.campusconnect.firebase.Create;
import com.ams.campusconnect.firebase.Delete;
import com.ams.campusconnect.firebase.Read;
import com.ams.campusconnect.firebase.Transfer;
import com.ams.campusconnect.firebase.Update;
import com.ams.campusconnect.model.Employee;
import com.ams.campusconnect.model.SaveData;
import com.ams.campusconnect.model.School;
import com.ams.campusconnect.qrcode.DownloadQR;
import com.ams.campusconnect.qrcode.GenerateQR;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class SchoolAdmin extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    SaveData save = SaveData.getInstance();
    School school = School.getInstance();
    Employee employee = Employee.getInstance();
    Read read = new Read();
    Update update = new Update();
    Delete delete = new Delete();

    TableLayout sourceAndDestinationTableLayout;
    Spinner idSpinner;
    EditText idEditText;
    EditText firstNameEditText;
    EditText lastNameEditText;
    Spinner monthSpinner;
    Spinner daySpinner;
    Spinner yearSpinner;
    TableLayout featuresTableLayout;
    ImageView qrCodeImageView;
    LinearLayout dtrLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_admin);

        hideAllComponents();

        ImageButton hamburger = findViewById(R.id.hamburger_ImageButton);
        ImageButton back = findViewById(R.id.backButton_ImageButton);

        back.setOnClickListener(view -> {
            Intent intent = new Intent(SchoolAdmin.this, AdminLogIn.class);
            startActivity(intent);
        });

        hamburger.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(SchoolAdmin.this, view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.hamburger_schooladmin_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(SchoolAdmin.this);
            popup.show();
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
        idNumberSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            Log.d("idNumberSwitch", "idSwitchSwitch checked: " + b);
            school.setIdNumberFeature(b);

            update.updateRecord(String.valueOf(school.getSchoolID()), "idNumberFeature", school.isIdNumberFeature());
        });
        gpsSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            Log.d("gpsSwitch", "gpsSwitch checked: " + b);
            school.setGpsFeature(b);
            update.updateRecord(String.valueOf(school.getSchoolID()), "gpsFeature", school.isGpsFeature());
        });
        timeBasedSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            Log.d("timeBasedSwitch", "timeBasedSwitch checked: " + b);
            school.setTimeBasedFeature(b);
            update.updateRecord(String.valueOf(school.getSchoolID()), "timeBasedFeature", school.isTimeBasedFeature());
        });

        qrSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            Log.d("SchoolAdmin", "qrSwitch checked: " + b);
            school.setQrScannerFeature(b);

            update.updateRecord(String.valueOf(school.getSchoolID()), "qrcodeFeature", school.isQrScannerFeature());
        });

        fingerprintSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            Log.d("SchoolAdmin", "fingerprintSwitch checked: " + b);
            school.setFingerPrintScannerFeature(b);

            update.updateRecord(String.valueOf(school.getSchoolID()), "fingerPrintFeature", school.isFingerPrintScannerFeature());
        });

        facialrecognitionSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            Log.d("SchoolAdmin", "facialrecognitionSwitch checked: " + b);
            school.setFacialRecognitionFeature(b);

            update.updateRecord(String.valueOf(school.getSchoolID()), "facialRecognitionFeature", school.isFacialRecognitionFeature());
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        TextView prompt = findViewById(R.id.prompt);
        monthSpinner = findViewById(R.id.month_Spinner);
        daySpinner = findViewById(R.id.day_Spinner);
        yearSpinner = findViewById(R.id.year_Spinner);
        TextView sourceTextView = findViewById(R.id.source_TextView);
        //Spinner sourceSpinner = findViewById(R.id.source_Spinner);
        Spinner destinationSpinner = findViewById(R.id.destination_Spinner);
        Spinner idSpinner = findViewById(R.id.id_Spinner);

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
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    // check if the key matches the sourceTextView text
                    if (!childSnapshot.getKey().equals(sourceTextView.getText().toString())) {
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
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
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
        for (int i = 1; i <= 31; i++) {
            dayList.add(String.valueOf(i));
        }

        // Create an ArrayAdapter for the day spinner
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dayList);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

        Button submit = findViewById(R.id.submitEmployee_Button);

        DateUtils dateUtils = new DateUtils(SchoolAdmin.this);

        switch (menuItem.getItemId()) {
            case R.id.add_employee: {
                prompt.setText("Register an Employee");

                // Hides all components
                hideAllComponents();

                // Unhide add employee components
                idEditText.setVisibility(View.VISIBLE);
                firstNameEditText.setVisibility(View.VISIBLE);
                lastNameEditText.setVisibility(View.VISIBLE);
                monthSpinner.setVisibility(View.VISIBLE);
                daySpinner.setVisibility(View.VISIBLE);
                yearSpinner.setVisibility(View.VISIBLE);

                // setEnabled to true so they are edittable
                idEditText.setEnabled(true);
                firstNameEditText.setEnabled(true);
                lastNameEditText.setEnabled(true);
                monthSpinner.setEnabled(true);
                daySpinner.setEnabled(true);
                yearSpinner.setEnabled(true);

                // Reset the component
                idEditText.setText("");
                idEditText.setText("");
                firstNameEditText.setText("");
                lastNameEditText.setText("");

                // Unhide submitSchool button
                submit.setVisibility(View.VISIBLE);
                submit.setText("Add an Employee");
                // Submit Button ActionListener
                submit.setOnClickListener(view -> {

                    // Check if one of the TextView are empty
                    if (idEditText.getText().toString().isEmpty() || firstNameEditText.getText().toString().isEmpty() || lastNameEditText.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
                    }
                    // Check if the length of id number is 12
                    else if (idEditText.getText().toString().length() != 12) {
                        Toast.makeText(getApplicationContext(), "Id Number must be equal to 12", Toast.LENGTH_SHORT).show();
                    } else {
                        // Get String from EditText components
                        employee.setFirstName(firstNameEditText.getText().toString());
                        employee.setLastName(lastNameEditText.getText().toString());
                        employee.setId(idEditText.getText().toString());
                        employee.setFullName(employee.getLastName() + ", " + employee.getFirstName());
                        employee.setBirthday(monthSpinner.getSelectedItem().toString() + "/" + daySpinner.getSelectedItem().toString() + "/" + yearSpinner.getSelectedItem().toString());
                        employee.setLatitude(0);
                        employee.setLongitude(0);

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
                                    create.createRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/birthdate", employee.getBirthday());
                                    create.createRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/latitude", employee.getLatitude());
                                    create.createRecord(school.getSchoolID() + "/employee/" + employee.getId() + "/longitude", employee.getLongitude());

                                    firstNameEditText.setText("");
                                    lastNameEditText.setText("");
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
                });
                return true;
            }
            case R.id.edit_employee: {
                prompt.setText("Edit an Employee");

                // Hides all components
                hideAllComponents();

                // Unhide add employee components
                idEditText.setVisibility(View.VISIBLE);
                firstNameEditText.setVisibility(View.VISIBLE);
                lastNameEditText.setVisibility(View.VISIBLE);
                monthSpinner.setVisibility(View.VISIBLE);
                daySpinner.setVisibility(View.VISIBLE);
                yearSpinner.setVisibility(View.VISIBLE);
                idSpinner.setVisibility(View.VISIBLE);

                // setEnabled to true so they are edittable
                idEditText.setEnabled(true);
                firstNameEditText.setEnabled(true);
                lastNameEditText.setEnabled(true);
                monthSpinner.setEnabled(true);
                daySpinner.setEnabled(true);
                yearSpinner.setEnabled(true);

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
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    String fullname = childSnapshot.child("fullname").getValue(String.class);
                                    if (fullname != null && fullname.equals(selectedItemName)) {
                                        parentKey = childSnapshot.getKey();
                                        employee.setId(parentKey);
                                        break;
                                    }
                                }
                                if (parentKey != null) {
                                    // Read Employee data from the selectedItemId
                                    read.readRecord(school.getSchoolID() + "/employee/" + parentKey, new Read.OnGetDataListener() {
                                        @Override
                                        public void onSuccess(DataSnapshot dataSnapshot) {
                                            String id = dataSnapshot.child("id").getValue(String.class);
                                            String fullname = dataSnapshot.child("fullname").getValue(String.class);
                                            String birthday = dataSnapshot.child("birthdate").getValue(String.class);

                                            String[] nameArray = fullname.split(", ");
                                            String[] birthdayArray = birthday.split("/");

                                            employee.setId(id);

                                            // Set the values in the EditText
                                            idEditText.setText(id);
                                            idEditText.setEnabled(false);
                                            lastNameEditText.setText(nameArray[0]);
                                            firstNameEditText.setText(nameArray[1]);
                                            //monthSpinner.setSelection(Integer.parseInt(birthdayArray[0]) - 1) ;
                                            monthSpinner.setSelection(DateUtils.getMonthNumber(birthdayArray[0]));
                                            daySpinner.setSelection(Integer.parseInt(birthdayArray[1]) - 1);

                                            dateUtils.getDateTime((month, day, year, currentTimeIn24Hours, currentTimeIn12Hours) -> yearSpinner.setSelection(Integer.parseInt(birthdayArray[2]) - (Integer.parseInt(year) - 100)));
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
                submit.setOnClickListener(view -> {

                    update.updateRecord(school.getSchoolID() + "/employee/" + employee.getId(), "fullname", lastNameEditText.getText().toString() + ", " + firstNameEditText.getText().toString());
                    update.updateRecord(school.getSchoolID() + "/employee/" + employee.getId(), "birthdate", (DateUtils.getMonthName(String.valueOf(monthSpinner.getSelectedItemPosition() + 1))) + "/" + daySpinner.getSelectedItem().toString() + "/" + yearSpinner.getSelectedItem().toString());

                    Toast.makeText(getApplicationContext(), "Successfully Edited", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SchoolAdmin.this, SchoolAdmin.class);
                    startActivity(intent);
                });
                return true;
            }
            case R.id.delete_employee: {
                prompt.setText("Delete an Employee");

                // Hides all components
                hideAllComponents();

                // Unhide add employee components
                idEditText.setVisibility(View.VISIBLE);
                firstNameEditText.setVisibility(View.VISIBLE);
                lastNameEditText.setVisibility(View.VISIBLE);
                monthSpinner.setVisibility(View.VISIBLE);
                daySpinner.setVisibility(View.VISIBLE);
                yearSpinner.setVisibility(View.VISIBLE);
                idSpinner.setVisibility(View.VISIBLE);

                // setEnabled to true so they are edittable
                idEditText.setEnabled(true);
                firstNameEditText.setEnabled(true);
                lastNameEditText.setEnabled(true);
                monthSpinner.setEnabled(true);
                daySpinner.setEnabled(true);
                yearSpinner.setEnabled(true);

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
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    String fullname = childSnapshot.child("fullname").getValue(String.class);
                                    if (fullname != null && fullname.equals(selectedItemName)) {
                                        parentKey = childSnapshot.getKey();
                                        employee.setId(parentKey);
                                        break;
                                    }
                                }
                                if (parentKey != null) {
                                    // Read Employee data from the selectedItemId
                                    read.readRecord(school.getSchoolID() + "/employee/" + parentKey, new Read.OnGetDataListener() {
                                        @Override
                                        public void onSuccess(DataSnapshot dataSnapshot) {
                                            String id = dataSnapshot.child("id").getValue(String.class);
                                            String fullname = dataSnapshot.child("fullname").getValue(String.class);
                                            String birthday = dataSnapshot.child("birthdate").getValue(String.class);

                                            String[] nameArray = fullname.split(", ");
                                            String[] birthdayArray = birthday.split("/");

                                            employee.setId(id);

                                            // Set the values in the EditText
                                            idEditText.setText(id);
                                            idEditText.setEnabled(false);
                                            lastNameEditText.setText(nameArray[0]);
                                            firstNameEditText.setText(nameArray[1]);
                                            //monthSpinner.setSelection(Integer.parseInt(birthdayArray[0]) - 1) ;
                                            monthSpinner.setSelection(DateUtils.getMonthNumber(birthdayArray[0]));
                                            daySpinner.setSelection(Integer.parseInt(birthdayArray[1]) - 1);

                                            dateUtils.getDateTime((month, day, year, currentTimeIn24Hours, currentTimeIn12Hours) -> yearSpinner.setSelection(Integer.parseInt(birthdayArray[2]) - (Integer.parseInt(year) - 100)));
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
                submit.setOnClickListener(view -> {
                    delete.deleteRecord(school.getSchoolID() + "/employee", String.valueOf(idEditText.getText()));

                    Toast.makeText(getApplicationContext(), "Successfully Deleted", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SchoolAdmin.this, SchoolAdmin.class);
                    startActivity(intent);
                });
                return true;
            }
            case R.id.transfer_employee: {
                prompt.setText("Transfer Employee");

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
                monthSpinner.setVisibility(View.VISIBLE);
                daySpinner.setVisibility(View.VISIBLE);
                yearSpinner.setVisibility(View.VISIBLE);

                // SetEnabled to false making them unedittable
                idEditText.setEnabled(false);
                firstNameEditText.setEnabled(false);
                lastNameEditText.setEnabled(false);
                monthSpinner.setEnabled(false);
                daySpinner.setEnabled(false);
                yearSpinner.setEnabled(false);

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
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    String fullname = childSnapshot.child("fullname").getValue(String.class);
                                    if (fullname != null && fullname.equals(selectedItemName)) {
                                        parentKey = childSnapshot.getKey();
                                        employee.setId(parentKey);
                                        break;
                                    }
                                }
                                if (parentKey != null) {
                                    // Read Employee data from the selectedItemId
                                    read.readRecord(school.getSchoolID() + "/employee/" + parentKey, new Read.OnGetDataListener() {
                                        @Override
                                        public void onSuccess(DataSnapshot dataSnapshot) {
                                            String id = dataSnapshot.child("id").getValue(String.class);
                                            String fullname = dataSnapshot.child("fullname").getValue(String.class);
                                            String birthday = dataSnapshot.child("birthdate").getValue(String.class);

                                            String[] nameArray = fullname.split(", ");
                                            String[] birthdayArray = birthday.split("/");

                                            employee.setId(id);

                                            // Set the values in the EditText
                                            idEditText.setText(id);
                                            idEditText.setEnabled(false);
                                            lastNameEditText.setText(nameArray[0]);
                                            firstNameEditText.setText(nameArray[1]);
                                            //monthSpinner.setSelection(Integer.parseInt(birthdayArray[0]) - 1) ;
                                            monthSpinner.setSelection(DateUtils.getMonthNumber(birthdayArray[0]));
                                            daySpinner.setSelection(Integer.parseInt(birthdayArray[1]) - 1);

                                            dateUtils.getDateTime((month, day, year, currentTimeIn24Hours, currentTimeIn12Hours) -> yearSpinner.setSelection(Integer.parseInt(birthdayArray[2]) - (Integer.parseInt(year) - 100)));
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

                submit.setText("Download All QRs");

                submit.setOnClickListener(view -> {
                    DatabaseReference source = FirebaseDatabase.getInstance().getReference(school.getSchoolID() + "/employee/" + employee.getId());
                    DatabaseReference destination = FirebaseDatabase.getInstance().getReference(destinationSpinner.getSelectedItem().toString() + "/employee/" + employee.getId());

                    // Create an instance of the Transfer class
                    Transfer transfer = new Transfer(source, destination, employee.getId(), getApplicationContext());

                    // Call the copyRecord method to copy the subtree from the source to the destination node
                    transfer.copyRecord(source, destination);

                    Intent intent = new Intent(SchoolAdmin.this, SchoolAdmin.class);
                    startActivity(intent);
                });
                return true;
            }
            case R.id.generateAllQR: {
                prompt.setText("Generate QRs");

                // Hide all components
                hideAllComponents();

                // Unhide QRImageView
                qrCodeImageView.setVisibility(View.VISIBLE);

                read.readRecord(school.getSchoolID() + "/employee", new Read.OnGetDataListener() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {

                            // Set ID and Set Name
                            employee.setId((String) child.child("id").getValue());
                            employee.setFullName((String) child.child("fullname").getValue());

                            // Instance
                            GenerateQR generateQR = new GenerateQR();

                            // Declare ImageView
                            ImageView qr = findViewById(R.id.qrCode_ImageView);

                            // call generateQRCode method from GenerateQR class
                            qr.setImageBitmap(generateQR.generateQRCode(child.getKey()));


                            // Download qr if a ImageView
                            DownloadQR imageDownloader = new DownloadQR(qr);
                            imageDownloader.downloadImage();

                        }
                    }

                    @Override
                    public void onFailure(DatabaseError databaseError) {
                        Log.d("Read", "Error: " + databaseError.getMessage());
                        Toast.makeText(getApplicationContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
            }
            // Download All DTR
            case R.id.generateAllDTR: {
                prompt.setText("Generate DTRs");

                // Hide all components
                hideAllComponents();

                // Unhide needed Component
                monthSpinner.setVisibility(View.VISIBLE);
                yearSpinner.setVisibility(View.VISIBLE);
                dtrLinearLayout.setVisibility(View.VISIBLE);

                // Declare components
                TextView name = findViewById(R.id.name_TextView);
                TextView date = findViewById(R.id.monthyear_TextView);
                TextView schoolHead = findViewById(R.id.schoolHead_TextView);
                TableLayout table = (TableLayout) findViewById(R.id.dtr_TableLayout);

                submit.setText("Download All DTRs");

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String month = monthSpinner.getSelectedItem().toString();
                        String year = yearSpinner.getSelectedItem().toString();
                        int day = DateUtils.getNumberOfDays(month, year);

                        save.setMonth(month);
                        save.setYear(year);

                        read.readRecord(school.getSchoolID() + "/employee", new Read.OnGetDataListener() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                processChildData(dataSnapshot.getChildren().iterator(), month, day, year);
                            }

                            @Override
                            public void onFailure(DatabaseError databaseError) {
                                Log.d("Read", "Error: " + databaseError.getMessage());
                                Toast.makeText(getApplicationContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    private void processChildData(Iterator<DataSnapshot> iterator, String month, int day, String year) {
                        if (iterator.hasNext()) {
                            DataSnapshot child = iterator.next();
                            employee.setId((String) child.child("id").getValue());
                            Log.d("ID:", employee.getId());

                            DTR.generateDTR(employee.getId(), month, day, year, name, date, schoolHead, table, SchoolAdmin.this, () -> {
                                DTR.downloadDTR(findViewById(R.id.dtr_LinearLayout), SchoolAdmin.this);
                                processChildData(iterator, month, day, year); // Recursive call to process the next child
                            });


                        } else {
                            // All children have been processed
                            Log.d("Status:", "All DTR generated");
                        }
                    }
                });


                return true;
            }
            case R.id.settings: {
                prompt.setText("Settings");

                // Hide all components
                hideAllComponents();
                submit.setVisibility(View.GONE);

                // Unhide features_table layout
                featuresTableLayout.setVisibility(View.VISIBLE);
                return true;
            }
            default: {
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
        monthSpinner = findViewById(R.id.month_Spinner);
        daySpinner = findViewById(R.id.day_Spinner);
        yearSpinner = findViewById(R.id.year_Spinner);
        qrCodeImageView = findViewById(R.id.qrCode_ImageView);
        dtrLinearLayout = findViewById(R.id.dtr_LinearLayout);
        featuresTableLayout = findViewById(R.id.features_TableLayout);

        // Hide all components
        sourceAndDestinationTableLayout.setVisibility(View.GONE);
        idSpinner.setVisibility(View.GONE);
        idEditText.setVisibility(View.GONE);
        firstNameEditText.setVisibility(View.GONE);
        lastNameEditText.setVisibility(View.GONE);
        monthSpinner.setVisibility(View.GONE);
        daySpinner.setVisibility(View.GONE);
        yearSpinner.setVisibility(View.GONE);
        qrCodeImageView.setVisibility(View.GONE);
        dtrLinearLayout.setVisibility(View.GONE);
        featuresTableLayout.setVisibility(View.GONE);
    }
}