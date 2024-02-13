package com.ams.campusconnect;

import android.content.Intent;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;

import com.ams.campusconnect.biometric.BiometricUtils;
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

        // Check the state of Biometric
        BiometricUtils biometricManagerHelper = new BiometricUtils(this, createBiometricCallback());
        biometricManagerHelper.checkBiometricSupported();

        // Fetch the Announcement in the Database
        getAnnoucement();

        Handler handler = new Handler();
        handler.postDelayed(() -> {

            if (checkDeveloperOptions()) {
                // Developer options are enabled, open settings
                finish();
                openDeveloperOptionsSettings();
            }
//                else if (checkUnknownSources()) {
//                    finish();
//                    openUnknownSourcesSettings();
//                }
            else if (!checkGPS()) {
                // GPS is disabled, close the app and open settings
                finish();
                openGPSSettings();
            }
            else {
                Intent intent = new Intent(SplashActivity.this, SchoolLogIn.class);
                startActivity(intent);
                finish();
            }
        }, 5000);
    }

    private boolean checkDeveloperOptions() {
        int developerOptions = Settings.Secure.getInt(getContentResolver(), Settings.Global.DEVELOPMENT_SETTINGS_ENABLED , 0);
        return developerOptions == 1;
    }

//    private boolean checkUnknownSources() {
//        int unknownSources = Settings.Secure.getInt(getContentResolver(), Settings.Secure.INSTALL_NON_MARKET_APPS, 0);
//        return unknownSources == 1;
//    }

    private boolean checkGPS() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void openDeveloperOptionsSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
        startActivity(intent);
        // Display a message informing the user
        Toast.makeText(SplashActivity.this, "Please turn off Developer Options", Toast.LENGTH_LONG).show();
    }

//    private void openUnknownSourcesSettings() {
//        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
//        startActivity(intent);
//        // Display a message informing the user
//        Toast.makeText(SplashActivity.this, "Please turn off Unknown Sources", Toast.LENGTH_LONG).show();
//    }

    private void openGPSSettings() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        // Display a message informing the user
        Toast.makeText(SplashActivity.this, "Please turn on GPS", Toast.LENGTH_LONG).show();
    }

    private BiometricPrompt.AuthenticationCallback createBiometricCallback() {
        return new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(SplashActivity.this, "Auth error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(SplashActivity.this, "Auth succeeded", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(SplashActivity.this, "Auth failed", Toast.LENGTH_SHORT).show();
            }
        };
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