package com.ams.campusconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import com.ams.campusconnect.firebase.Read;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class SplashActivity extends AppCompatActivity {

    Read read = new Read();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Check if developer options are enabled


        // Declare thankyou sound
        final MediaPlayer welcome = MediaPlayer.create(this, R.raw.welcometocampusconnect);
        welcome.start();

        // Fetch the Announcement in the Database
        getAnnoucement();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (checkDevOpsOrUnknownSources()) {
                    String message = "Turn Off 'Developer Option' and 'Unknown Source' settings.";
                    Toast.makeText(SplashActivity.this, message, Toast.LENGTH_LONG).show();

                    // Close the app
                    finish();
                    return;
                }

                Intent intent = new Intent(SplashActivity.this, SchoolLogIn.class);
                startActivity(intent);
                finish();
            }
        }, 5000);
    }

    private boolean checkDevOpsOrUnknownSources() {
        int developerOptions = Settings.Secure.getInt(getContentResolver(), Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0);
        int unknownSources;
        try {
            unknownSources = Settings.Secure.getInt(getContentResolver(), Settings.Secure.INSTALL_NON_MARKET_APPS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            unknownSources = 0;
        }
        return developerOptions == 1 || unknownSources == 1;
    }

    private void getAnnoucement() {
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
    }
}