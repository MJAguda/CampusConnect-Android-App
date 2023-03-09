package com.example.campusconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class AdminLogIn extends AppCompatActivity {

    SaveData save = SaveData.getInstance();
    School school = School.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_log_in);

        // Declare Components
        ImageButton back = findViewById(R.id.backButton_ImageButton);
        EditText adminUsername = findViewById(R.id.adminUsername_EditText);
        EditText adminPassword = findViewById(R.id.adminPassword_EditText);
        Button adminLogIn = findViewById(R.id.adminLogIn_Button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (AdminLogIn.this, MainActivity.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (AdminLogIn.this, MainActivity.class);
                startActivity(intent);
            }
        });

        adminLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if admin account is school level or higher admin
                if(adminUsername.getText().toString().equals(save.getAdminUsername()) && adminPassword.getText().toString().equals(save.getAdminPassword())){
                    Toast.makeText(getApplicationContext(), "Welcome System Admin", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminLogIn.this, SystemAdmin.class);
                    startActivity(intent);
                }
                else if (adminUsername.getText().toString().equals(school.getAdminUsername()) && adminPassword.getText().toString().equals(school.getAdminPassword())) {
                    Toast.makeText(getApplicationContext(), "Welcome " + school.getSchoolName() + " Admin", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminLogIn.this, SchoolAdmin.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Wrong Username or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}