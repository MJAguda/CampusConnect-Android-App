package com.example.campusconnect;

import static android.content.ContentValues.TAG;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class GenerateDTR extends AppCompatActivity {

    SaveData save = SaveData.getInstance();
    School school = School.getInstance();
    Read read = new Read();

    private static final int PERMISSION_REQUEST_CODE = 10;
    //private static final String TAG
    int pdfHeight = 1080;
    int pdfWidth = 720;
    private PdfDocument document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_dtr);

        // Find button in the Layout
        Button generate = findViewById(R.id.generateDTR_Button);

        //Find the spinner in the layout
        Spinner monthSpinner = findViewById(R.id.month_Spinner);
        Spinner yearSpinner = findViewById(R.id.year_Spinner);

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

        // Create an ArrayList for the year
        ArrayList<String> yearList = new ArrayList<>();
        for (int i = Calendar.getInstance().get(Calendar.YEAR)-5; i <= Calendar.getInstance().get(Calendar.YEAR) + 10; i++) {
            yearList.add(String.valueOf(i));
        }

        // Create an ArrayAdapter for the year spinner
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, yearList);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        // Declare components
        TextView name = findViewById(R.id.name_TextView);
        TextView date = findViewById(R.id.monthyear_TextView);
        TextView schoolHead = findViewById(R.id.schoolHead_TextView);

        // TODO Generate DTR
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String month = monthSpinner.getSelectedItem().toString();
                String year = yearSpinner.getSelectedItem().toString();

                save.setMonth(month);
                save.setYear(year);

                int day = DateUtils.getNumberOfDays(save.getMonth(), save.getYear());

                read.readRecord(school.getSchoolID() + "/employee/" + save.getId(), new Read.OnGetDataListener() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        name.setText(dataSnapshot.child("fullname").getValue().toString());
                        date.setText(month + " " + year);

                        read.readRecord(school.getSchoolID() + "/employee/" + save.getId() + "/attendance/" + save.getYear() + "/" + save.getMonth(), new Read.OnGetDataListener() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {

                                TableLayout table = (TableLayout) findViewById(R.id.dtr_TableLayout);
                                table.removeAllViews();

                                for(int i = 1 ; i <= day ; i++){

                                    DataSnapshot child = dataSnapshot.child(String.valueOf(i));

                                    // Instance of the row
                                    TableRow row = new TableRow(GenerateDTR.this);

                                    // Add day to the row
                                    TextView day = new TextView(GenerateDTR.this);
                                    day.setText(child.getKey());
                                    day.setTextColor(Color.BLACK);
                                    day.setTextSize(12);
                                    day.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                    TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f);
                                    day.setLayoutParams(params);
                                    day.setBackground(ContextCompat.getDrawable(GenerateDTR.this, R.drawable.table_border));

                                    row.addView(day);
                                    // Add time TextView to the row
                                    for(DataSnapshot grandChild : child.getChildren()){
                                        Log.d("Time", grandChild.getKey() + " : " + grandChild.getValue());

                                        // Add time to the row
                                        TextView time = new TextView(GenerateDTR.this);

                                        time.setText(grandChild.getValue().toString());
                                        time.setTextSize(12);
                                        time.setTextColor(Color.BLACK);
                                        time.setLayoutParams(params);
                                        time.setGravity(Gravity.CENTER);
                                        time.setBackground(ContextCompat.getDrawable(GenerateDTR.this, R.drawable.table_border));

                                        row.addView(time);
                                    }
                                    table.addView(row);
                                }
                            }

                            @Override
                            public void onFailure(DatabaseError databaseError) {
                                // handle error here
                            }
                        });


                        // Set School head
                        read.readRecord(school.getSchoolID() + "/", new Read.OnGetDataListener() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                schoolHead.setText(dataSnapshot.child("schoolHead").getValue().toString());
                            }

                            @Override
                            public void onFailure(DatabaseError databaseError) {
                                // handle error here
                            }
                        });

                    }

                    @Override
                    public void onFailure(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Read Error", Toast.LENGTH_SHORT).show();
                    }
                });



                /*
                // display all data from month parent node try to store data to a 2d array first
                // Initialize Firebase Realtime Database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                // Define the reference to the desired node
                DatabaseReference ref = database.getReference(school.getSchoolID() + "/employee/" + save.getId() + "/attendance/" + save.getYear() + "/" + save.getMonth());

                // Attach a listener to the reference
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        TableLayout table = (TableLayout) findViewById(R.id.dtr_TableLayout);
                        table.removeAllViews();

                        // Iterate through all child nodes
                        for (int i = 1 ; i <= day ; i++) {
                            DataSnapshot child = dataSnapshot.child(String.valueOf(i));
                            Log.d("Time", "KEY : " + child.getKey() + " : " + "Value : " + child.getValue());

                            // Instance of the row
                            TableRow row = new TableRow(GenerateDTR.this);

                            // Add day to the row
                            TextView day = new TextView(GenerateDTR.this);
                            day.setText(child.getKey());
                            day.setTextSize(12);

                            //day.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f);
                            day.setLayoutParams(params);

                            //day.setBackgroundColor(Color.WHITE);
                            //day.setPadding(5,5,5,5);
                            //day.setGravity(Gravity.CENTER);
                            //day.setTextColor(Color.BLACK);
                            //day.setBackground(ContextCompat.getDrawable(Attendance.this, R.drawable.cell_shape));

                            row.addView(day);

                            // Add time TextView to the row
                            for(DataSnapshot grandChild : child.getChildren()){
                                Log.d("Time", grandChild.getKey() + " : " + grandChild.getValue());

                                // Add time to the row
                                TextView time = new TextView(GenerateDTR.this);
                                time.setText(grandChild.getValue().toString());
                                time.setTextSize(12);
                                time.setLayoutParams(params);
                                time.setGravity(Gravity.CENTER);

                                //time.setBackgroundColor(Color.WHITE);
                                //time.setPadding(5,5,5,5);
                                //time.setGravity(Gravity.CENTER);
                                //time.setTextColor(Color.BLACK);
                                //time.setBackground(ContextCompat.getDrawable(Attendance.this, R.drawable.cell_shape));

                                row.addView(time);
                            }
                            table.addView(row);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("Error reading data: " + databaseError.getMessage());
                    }
                });

                */
            }
        });
    }
}