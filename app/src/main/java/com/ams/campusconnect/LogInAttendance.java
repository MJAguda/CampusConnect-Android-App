package com.ams.campusconnect;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ams.campusconnect.controller.EmployeeController;
import com.ams.campusconnect.firebase.Create;
import com.ams.campusconnect.firebase.Read;
import com.ams.campusconnect.model.EmployeeModel;
import com.ams.campusconnect.model.School;
import com.ams.campusconnect.qrcode.ScanQR;
import com.ams.campusconnect.repository.EmployeeRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class LogInAttendance extends AppCompatActivity {

    School school;
    EmployeeModel employeeModel;
    EmployeeController employeeController;
    DateUtils dateUtils;
    Create create = new Create();
    Read read = new Read();

    ImageButton back;
    EditText idNumber;
    Button Submit;
    MediaPlayer thankyou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_attendance);

        // Initialize Components
        back = findViewById(R.id.backButton_ImageButton);
        idNumber = findViewById(R.id.id_EditText);
        Submit = findViewById(R.id.submit_Button);

        thankyou = MediaPlayer.create(this, R.raw.thankyou);

        school = (School) getIntent().getSerializableExtra("school");
        employeeModel = (EmployeeModel) getIntent().getSerializableExtra("employee");
        employeeController = new EmployeeController(this, school);

        dateUtils = new DateUtils(LogInAttendance.this);

        // Back Button ActionListener
        back.setOnClickListener(v -> {
            Intent intent = new Intent(LogInAttendance.this, LogbookActivity.class);
            intent = intent.putExtra("school", school);
            intent = intent.putExtra("employee", employeeModel);
            startActivity(intent);
        });

        // Submit Button ActionListener
        Submit.setOnClickListener(v -> {
            // Get ID Number
            String id = idNumber.getText().toString();

            // Verify if employee id exists
            employeeController.getEmployee(school, id, new EmployeeRepository.OnDataFetchListener() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        Toast.makeText(getApplicationContext(), "ID Not Found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Get employee fields from firebase realtime database
                    employeeModel = dataSnapshot.getValue(EmployeeModel.class);

                    dateUtils.getDateTime((month, day, year, currentTimeIn24Hours, currentTimeIn12Hours) -> {
                        read.readRecord(school.getSchoolID() + "/employee/" + employeeModel.getId() + "/attendance/" + year + "/" + DateUtils.getMonthName(month), new Read.OnGetDataListener() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    for (int i = 1; i <= DateUtils.getNumberOfDays(DateUtils.getMonthName(month), year); i++) {
                                        if (!dataSnapshot.hasChild(String.valueOf(i))) {
                                            create.createRecord(school.getSchoolID() + "/employee/" + employeeModel.getId() + "/attendance/" + year + "/" + DateUtils.getMonthName(month) + "/" + i + "/timeAM_In", "");
                                            create.createRecord(school.getSchoolID() + "/employee/" + employeeModel.getId() + "/attendance/" + year + "/" + DateUtils.getMonthName(month) + "/" + i + "/timeAM_Out", "");
                                            create.createRecord(school.getSchoolID() + "/employee/" + employeeModel.getId() + "/attendance/" + year + "/" + DateUtils.getMonthName(month) + "/" + i + "/timePM_In", "");
                                            create.createRecord(school.getSchoolID() + "/employee/" + employeeModel.getId() + "/attendance/" + year + "/" + DateUtils.getMonthName(month) + "/" + i + "/timePM_Out", "");
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

                    thankyou.start();

                    Intent intent = new Intent(LogInAttendance.this, Attendance.class);
                    intent = intent.putExtra("school", school);
                    intent = intent.putExtra("employee", employeeModel);
                    startActivity(intent);
                }

                @Override
                public void onFailure(DatabaseError databaseError) {
                    // handle error here
                    Log.d("EmployeeController", "Error: " + databaseError.getMessage());
                }
            });
        });

    }
}
