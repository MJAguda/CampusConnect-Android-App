package com.ams.campusconnect;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ams.campusconnect.controller.EmployeeController;
import com.ams.campusconnect.model.EmployeeModel;
import com.ams.campusconnect.model.SaveData;
import com.ams.campusconnect.model.School;

public class AdminLogIn extends AppCompatActivity {

    SaveData save = SaveData.getInstance();
    School school;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_log_in);

        school = (School) getIntent().getSerializableExtra("school");

        // Declare Components
        ImageButton back = findViewById(R.id.backButton_ImageButton);
        EditText adminUsername = findViewById(R.id.adminUsername_EditText);
        EditText adminPassword = findViewById(R.id.adminPassword_EditText);
        Button adminLogIn = findViewById(R.id.adminLogIn_Button);

        back.setOnClickListener(view -> {
            Intent intent = new Intent(AdminLogIn.this, LogbookActivity.class);
            intent = intent.putExtra("school", school);
            startActivity(intent);
        });

        adminLogIn.setOnClickListener(view -> {

            progressDialog = new ProgressDialog(AdminLogIn.this);
            progressDialog.setTitle("Logging In...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            // Error detection
            // if admin username and password is empty
            if (adminUsername.getText().toString().isEmpty() || adminPassword.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }

            // if admin username and password is not equal to the saved admin username and password
            if (!adminUsername.getText().toString().equals(save.getAdminUsername()) && !adminPassword.getText().toString().equals(save.getAdminPassword())) {
                Toast.makeText(getApplicationContext(), "Wrong Username or Password", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }

            // if admin username and password is not equal to the school admin username and password
            if (!adminUsername.getText().toString().equals(school.getAdminUsername()) && !adminPassword.getText().toString().equals(school.getAdminPassword())) {
                Toast.makeText(getApplicationContext(), "Wrong Username or Password", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                return;
            }

            // Check if admin account is school level or higher admin
            if (adminUsername.getText().toString().equals(save.getAdminUsername()) && adminPassword.getText().toString().equals(save.getAdminPassword())) {

                Toast.makeText(getApplicationContext(), "Welcome System Admin", Toast.LENGTH_SHORT).show();

                // Dismiss progress dialog
                progressDialog.dismiss();

                Intent intent = new Intent(AdminLogIn.this, SystemAdmin.class);
                intent = intent.putExtra("school", school);
                startActivity(intent);
            } else if (adminUsername.getText().toString().equals(school.getAdminUsername()) &&
                    adminPassword.getText().toString().equals(school.getAdminPassword())) {

                Toast.makeText(getApplicationContext(), "Welcome " + school.getSchoolName() + " Admin", Toast.LENGTH_SHORT).show();

                // Dismiss progress dialog
                progressDialog.dismiss();

                Intent intent = new Intent(AdminLogIn.this, SchoolAdmin.class);
                intent = intent.putExtra("school", school);
                startActivity(intent);
            } else {

                // Dismiss progress dialog
                progressDialog.dismiss();

                Toast.makeText(getApplicationContext(), "Wrong Username or Password", Toast.LENGTH_SHORT).show();
            }
        });

    }
}