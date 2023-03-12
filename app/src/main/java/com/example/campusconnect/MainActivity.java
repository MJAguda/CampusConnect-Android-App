package com.example.campusconnect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    SaveData save = SaveData.getInstance();
    School school = School.getInstance();
    Read read = new Read();
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Declare components
        ImageButton back = findViewById(R.id.backButton_ImageButton);
        TextView schoolName = findViewById(R.id.schoolName_TextView);

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

        TextView date = findViewById(R.id.dateAndTime_TextView);
        TableLayout previewHeader = findViewById(R.id.previewHeaderdailyLog_TableLayout);
        TableLayout table = (TableLayout) findViewById(R.id.dailyLog_TableLayout);

        // Display a Display Date and Time
        timer = new Timer();
        TimerTask updateTimeTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        date.setText(DateUtils.getCurrentDate());
                    }
                });
            }
        };

        timer.scheduleAtFixedRate(updateTimeTask, 0, 1000); // update every 1 second

        //Set school Name
        schoolName.setText(school.getSchoolName());

        //Read all Personnel's Time Log for the day
        save.setMonth(String.valueOf(Integer.parseInt(DateUtils.getCurrentMonth())));
        save.setDay(String.valueOf(Integer.parseInt(DateUtils.getCurrentDay())));
        save.setYear(String.valueOf(Integer.parseInt(DateUtils.getCurrentYear())));
        read.readRecord(school.getSchoolID() + "/employee", new Read.OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                table.removeAllViews();

                for(DataSnapshot child : dataSnapshot.getChildren()){
                    String fullName = child.child("fullname").getValue(String.class);
                    if (fullName != null) {
                        Log.d("TAG", fullName);
                        // Instance of the row
                        TableRow row = new TableRow(MainActivity.this);


                        // Add day to the row
                        TextView name = new TextView(MainActivity.this);
                        name.setText(fullName);
                        name.setTextSize(12);
                        name.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.33333333333333333333333333333334f);
                        name.setLayoutParams(params);
                        name.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.cell_shape));

                        row.addView(name);

                        for(DataSnapshot grandChild : child.child("attendance/" + DateUtils.getCurrentYear() + "/" + DateUtils.getMonthName(DateUtils.getCurrentMonth()) + "/" + Integer.parseInt(DateUtils.getCurrentDay())).getChildren()){
                            Log.d("Time", grandChild.getKey() + " : " + grandChild.getValue());

                            // Add time to the row
                            TextView time = new TextView(MainActivity.this);

                            time.setText(grandChild.getValue().toString());
                            time.setTextSize(12);
                            TableRow.LayoutParams timeparams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT , 0.16666666666666666666666666666667f);
                            time.setLayoutParams(timeparams);
                            time.setGravity(Gravity.CENTER);
                            time.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.cell_shape));

                            row.addView(time);

                        }

                        table.addView(row);
                    }
                }
            }

            @Override
            public void onFailure(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Read Error", Toast.LENGTH_SHORT).show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SchoolLogIn.class);
                startActivity(intent);
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