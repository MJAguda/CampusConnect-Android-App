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
import android.widget.AdapterView;
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
    Create create = new Create();
    Read read = new Read();
    Update update = new Update();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_admin);

        ImageButton hamburger = findViewById(R.id.hamburger_ImageButton);
        ImageButton back = findViewById(R.id.backButton_ImageButton);

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
        Spinner idSpinner = findViewById(R.id.id_Spinner);
        Button submit = findViewById(R.id.submitEmployee_Button);

        // Hide add employee components
        id.setVisibility(View.GONE);
        firstName.setVisibility(View.GONE);
        lastName.setVisibility(View.GONE);
        birthday.setVisibility(View.GONE);
        idSpinner.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);
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
        Spinner idSpinner = findViewById(R.id.id_Spinner);

        // Create an ArrayList for the id employee
        ArrayList<String> idList = new ArrayList<>();
        read.readRecord(school.getSchoolID() + "/employee", new Read.OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                    idList.add(childSnapshot.getKey());
                }

                // Create an ArrayAdapter for the idSpinner
                ArrayAdapter<String> idAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, idList);
                idAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                idSpinner.setAdapter(idAdapter);
            }

            @Override
            public void onFailure(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Read Error", Toast.LENGTH_SHORT).show();
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

        switch(menuItem.getItemId()){
            case R.id.add_employee:{
                prompt.setText("Register an Employee");

                // Unhide add employee components
                idEditText.setVisibility(View.VISIBLE);
                firstName.setVisibility(View.VISIBLE);
                lastName.setVisibility(View.VISIBLE);
                birthday.setVisibility(View.VISIBLE);

                // Unhide submitSchool button
                submit.setVisibility(View.VISIBLE);
                submit.setText("Add Employee");
                // Submit Button ActionListener
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Check if views are not empty
                        if(!firstName.getText().toString().isEmpty() || !lastName.getText().toString().isEmpty() || !idEditText.getText().toString().isEmpty()){
                            // Get String from EditText components
                            employee.setFirstName(firstName.getText().toString());
                            employee.setLastName(lastName.getText().toString());
                            employee.setId(idEditText.getText().toString());
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
                                        idEditText.setText("");

                                        Intent intent = new Intent(SchoolAdmin.this, SchoolAdmin.class);
                                        startActivity(intent);

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
                return true;
            }
            case R.id.edit_employee:{
                prompt.setText("Edit an Employee");

                // Unhide add employee components
                idEditText.setVisibility(View.VISIBLE);
                firstName.setVisibility(View.VISIBLE);
                lastName.setVisibility(View.VISIBLE);
                birthday.setVisibility(View.VISIBLE);
                idSpinner.setVisibility(View.VISIBLE);

                // ActionListener for the selected Item in the idSpinner
                idSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // Get the selected item from idSpinner
                        String selectedItemId = parent.getItemAtPosition(position).toString();

                        // Display a Toast message with the selected item
                        Toast.makeText(getApplicationContext(), "Selected Item: " + selectedItemId, Toast.LENGTH_SHORT).show();

                        // Read Employee data from the selectedItemId
                        read.readRecord(school.getSchoolID() + "/employee/" + selectedItemId, new Read.OnGetDataListener() {
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
                                monthSpinner.setSelection(Integer.parseInt(birthdayArray[0]) - 1);
                                daySpinner.setSelection(Integer.parseInt(birthdayArray[1]) - 1);
                                yearSpinner.setSelection(Integer.parseInt(birthdayArray[2]) - (Integer.parseInt(DateUtils.getCurrentYear()) - 100));
                            }

                            @Override
                            public void onFailure(DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(), "Read Error", Toast.LENGTH_SHORT).show();
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
                submit.setText("Edit Employee");
                // Submit Button ActionListener
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        update.updateRecord(school.getSchoolID() + "/employee/" + employee.getId() , "fullname" , lastName.getText().toString() + ", " + firstName.getText().toString());
                        update.updateRecord(school.getSchoolID() + "/employee/" + employee.getId() , "birthdate" , (monthSpinner.getSelectedItemPosition() + 1) + "/" + daySpinner.getSelectedItem().toString() + "/" + yearSpinner.getSelectedItem().toString());

                        Intent intent = new Intent(SchoolAdmin.this, SchoolAdmin.class);
                        startActivity(intent);

                    }
                });
                return true;
            }
            case R.id.delete_employee:{
                // TODO deleter Employee
                return true;
            }
            case R.id.transfer_employee:{
                // TODO transfer Employee
                return true;
            }
            default:
                return false;
        }
    }
}