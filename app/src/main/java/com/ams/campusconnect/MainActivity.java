package com.ams.campusconnect;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.ams.campusconnect.firebase.Read;
import com.ams.campusconnect.model.SaveData;
import com.ams.campusconnect.model.School;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    School school = School.getInstance();
    Read read = new Read();
    Timer timer;

    TableLayout table;
    DateUtils dateUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate DateUtils
        dateUtils = new DateUtils(MainActivity.this);

        // Declare components
        ImageButton back = findViewById(R.id.backButton_ImageButton);
        TextView schoolName = findViewById(R.id.schoolName_TextView);

        ImageButton home = findViewById(R.id.home_Button);
        ImageButton attendance = findViewById(R.id.attendance_Button);
        ImageView logo = findViewById(R.id.footerlogo_ImageView);
        ImageButton register = findViewById(R.id.admin_Button);
        ImageButton generate = findViewById(R.id.generate_Button);
        TextView guide1 = findViewById(R.id.buttonGuide_TextView1);
        TextView guide2 = findViewById(R.id.buttonGuide_TextView2);
        TextView guide3 = findViewById(R.id.buttonGuide_TextView3);
        TextView guide4 = findViewById(R.id.buttonGuide_TextView4);
        TextView guide5 = findViewById(R.id.buttonGuide_TextView5);

        TextView dateTimeTextView = findViewById(R.id.dateAndTime_TextView);
        TableLayout previewHeader = findViewById(R.id.previewHeaderdailyLog_TableLayout);
        table = (TableLayout) findViewById(R.id.dailyLog_TableLayout);

        dateUtils.getDateTime(new DateUtils.VolleyCallBack() {
            @Override
            public void onGetDateTime(String month, String day, String year, String currentTimeIn24Hours, String currentTimeIn12Hours) {
                // Display a Display Date and Time
                timer = new Timer();
                TimerTask updateTimeTask = new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                dateTimeTextView.setText(month + "/" + day + "/" + year + " " + currentTimeIn12Hours);
                            }
                        });
                    }
                };

                timer.scheduleAtFixedRate(updateTimeTask, 0, 1000); // TODO update every 1 second

                //Set school Name
                schoolName.setText(school.getSchoolName());

                read.readRecord(school.getSchoolID() + "/employee", new Read.OnGetDataListener() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        table.removeAllViews();

                        // Get all the names from the "employee" node and store them in a list
                        List<String> names = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            String fullName = child.child("fullname").getValue(String.class);
                            if (fullName != null) {
                                names.add(fullName);
                            }
                        }

                        // Sort the names alphabetically
                        Collections.sort(names);

                        // Display the sorted names in the UI
                        for (String fullName : names) {
                            // Create a row for each name
                            TableRow row = new TableRow(MainActivity.this);

                            // Add the name to the row
                            TextView name = new TextView(MainActivity.this);
                            name.setText(fullName);
                            name.setTextSize(10);
                            name.setGravity(Gravity.TOP);
                            name.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                            name.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.cell_shape));

                            // Get the height of the first TextView
                            name.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                            int nameHeight = name.getMeasuredHeight();

                            TableRow.LayoutParams nameParams = new TableRow.LayoutParams(0, nameHeight, 0.32f);
                            name.setLayoutParams(nameParams);
                            row.addView(name);

                            // Add the attendance times to the row
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                String employeeName = child.child("fullname").getValue(String.class);
                                if (employeeName != null && employeeName.equals(fullName)) {
                                    for (DataSnapshot grandChild : child.child("attendance/" + year + "/" + dateUtils.getMonthName(month) + "/" + Integer.parseInt(day)).getChildren()) {
                                        TextView time = new TextView(MainActivity.this);
                                        time.setText(grandChild.getValue().toString());
                                        time.setTextSize(10);
                                        TableRow.LayoutParams timeParams = new TableRow.LayoutParams(0, nameHeight, 0.17f);
                                        time.setLayoutParams(timeParams);
                                        time.setGravity(Gravity.CENTER);
                                        time.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.cell_shape));
                                        row.addView(time);
                                    }
                                }
                            }

                            table.addView(row);
                        }
                    }

                    @Override
                    public void onFailure(DatabaseError databaseError) {
                        Log.d("Read", "Error: " + databaseError.getMessage());
                        Toast.makeText(getApplicationContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


                back.setOnClickListener(view -> {
                    Intent intent = new Intent(MainActivity.this, SchoolLogIn.class);
                    startActivity(intent);
                });

                home.setOnClickListener(view -> {
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                });

                attendance.setOnClickListener(view -> {

                    Intent intent = new Intent(MainActivity.this, Attendance.class);
                    startActivity(intent);
                });
                register.setOnClickListener(view -> {
                    Intent intent = new Intent(MainActivity.this, AdminLogIn.class);
                    startActivity(intent);
                });

                generate.setOnClickListener(view -> {
                    Intent intent = new Intent(MainActivity.this, Generate.class);
                    startActivity(intent);
                });
            }
        });
    }
}