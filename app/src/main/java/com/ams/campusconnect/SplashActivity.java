package com.ams.campusconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.ams.campusconnect.firebase.Read;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class SplashActivity extends AppCompatActivity {

    Read read = new Read();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Declare thankyou sound
        final MediaPlayer welcome = MediaPlayer.create(this, R.raw.welcometocampusconnect);
        welcome.start();

        // Fetch the Announcement in the Database
        read.readRecord("Announcement", new Read.OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                // Set the text in the announcementTextView
                TextView announcementTextView = findViewById(R.id.announcement_TextView);
                announcementTextView.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onFailure(DatabaseError databaseError) {
                // handle error here
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, SchoolLogIn.class);
                startActivity(intent);
                finish();
            }
        }, 5000);
    }
}