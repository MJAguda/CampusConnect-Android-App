package com.example.campusconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DTRLayout extends AppCompatActivity {

    SaveData save = SaveData.getInstance();
    School school = School.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dtrlayout);

        int day = DateUtils.getNumberOfDays(save.getYear(), save.getMonth());

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
                    TableRow row = new TableRow(DTRLayout.this);

                    // Add day to the row
                    TextView day = new TextView(DTRLayout.this);
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
                        TextView time = new TextView(DTRLayout.this);
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



    }
}