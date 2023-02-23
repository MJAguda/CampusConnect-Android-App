package com.example.campusconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class MainActivity extends AppCompatActivity {

    SaveData save = SaveData.getInstance();
    School school = School.getInstance();
    Read read = new Read();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Analog Clock
        AnalogClock analogClock = findViewById(R.id.analog_clock);

        // Declare components
        TextView header = findViewById(R.id.headerText_TextView);
        TextView schoolName = findViewById(R.id.schoolName_TextView);

        EditText schoolID = findViewById(R.id.schoolID_EditText);

        Button submit = findViewById(R.id.submit_Button);
        ImageButton home = findViewById(R.id.home_Button);
        ImageButton attendance = findViewById(R.id.attendance_Button);
        ImageView logo = findViewById(R.id.footerlogo_ImageView);
        ImageButton register = findViewById(R.id.register_Button);
        ImageButton generate = findViewById(R.id.generate_Button);
        TextView guide1 = findViewById(R.id.buttonGuide_TextView1);
        TextView guide2 = findViewById(R.id.buttonGuide_TextView2);
        TextView guide3 = findViewById(R.id.buttonGuide_TextView3);
        TextView guide4 = findViewById(R.id.buttonGuide_TextView4);
        TextView guide5 = findViewById(R.id.buttonGuide_TextView5);

        home.setVisibility(View.GONE);
        attendance.setVisibility(View.GONE);
        logo.setVisibility(View.GONE);
        register.setVisibility(View.GONE);
        generate.setVisibility(View.GONE);
        guide1.setVisibility(View.GONE);
        guide2.setVisibility(View.GONE);
        guide3.setVisibility(View.GONE);
        guide4.setVisibility(View.GONE);
        guide5.setVisibility(View.GONE);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if school edittext is not empty
                if(schoolID.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Fill all Fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    // Store edittext to schoolID in the School class
                    school.setSchoolID(Integer.parseInt(schoolID.getText().toString()));

                    // Login as System admin
                    if(school.getSchoolID() == Integer.parseInt(save.getAdminPassword())){
                        home.setVisibility(View.VISIBLE);
                        attendance.setVisibility(View.VISIBLE);
                        logo.setVisibility(View.VISIBLE);
                        register.setVisibility(View.VISIBLE);
                        generate.setVisibility(View.VISIBLE);
                        guide1.setVisibility(View.VISIBLE);
                        guide2.setVisibility(View.VISIBLE);
                        guide3.setVisibility(View.VISIBLE);
                        guide4.setVisibility(View.VISIBLE);
                        guide5.setVisibility(View.VISIBLE);
                    }

                    read.readRecord( school.getSchoolID() + "/", new Read.OnGetDataListener() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.exists()){
                                Toast.makeText(getApplicationContext(), "School ID not found", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                // Fetch data and store it in School Class
                                school.setSchoolID(Integer.parseInt(dataSnapshot.child("schoolID").getValue().toString()));
                                school.setSchoolName(dataSnapshot.child("schoolName").getValue().toString());
                                school.setSchoolHead(dataSnapshot.child("schoolHead").getValue().toString());
                                school.setAdminUsername(dataSnapshot.child("adminUsername").getValue().toString());
                                school.setAdminPassword(dataSnapshot.child("adminPassword").getValue().toString());
                                school.setLatitudeBottom(dataSnapshot.child("latitudeBottom").getValue().toString());
                                school.setLatitudeTop(dataSnapshot.child("latitudeTop").getValue().toString());
                                school.setLongitudeLeft(dataSnapshot.child("longitudeLeft").getValue().toString());
                                school.setLongitudeRight(dataSnapshot.child("longitudeRight").getValue().toString());

                                // Hide components
                                schoolID.setVisibility(View.GONE);
                                submit.setVisibility(View.GONE);

                                // Unhide components
                                home.setVisibility(View.VISIBLE);
                                attendance.setVisibility(View.VISIBLE);
                                logo.setVisibility(View.VISIBLE);
                                register.setVisibility(View.VISIBLE);
                                generate.setVisibility(View.VISIBLE);
                                guide1.setVisibility(View.VISIBLE);
                                guide2.setVisibility(View.VISIBLE);
                                guide3.setVisibility(View.VISIBLE);
                                guide4.setVisibility(View.VISIBLE);
                                guide5.setVisibility(View.VISIBLE);

                                // TODO set school LOGO
                                // Set school name
                                schoolName.setText(school.getSchoolName());
                                header.setText("Campus Connect");

                            }
                        }

                        @Override
                        public void onFailure(DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), "Read Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Attendance.class);
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AdminLogIn.class);
                startActivity(intent);
            }
        });

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Generate.class);
                startActivity(intent);
            }
        });
    }
}